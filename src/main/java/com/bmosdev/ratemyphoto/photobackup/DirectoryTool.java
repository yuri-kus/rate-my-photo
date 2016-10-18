package com.bmosdev.ratemyphoto.photobackup;

import com.bmosdev.ratemyphoto.photobackup.SyncCommand.Action;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

public class DirectoryTool {
    
    public DirectoryInfo explore(File rootDirectory, final List<String> allowedExtensions) {
        if (!rootDirectory.isDirectory()) {
            throw new IllegalArgumentException(rootDirectory + " should be directory");
        }
        DirectoryInfo directoryInfo = new DirectoryInfo(rootDirectory);
        
        File[] files;
        if (allowedExtensions != null) {
            FileFilter filter = file -> {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String name = file.getName().toLowerCase();
                    for (String extension : allowedExtensions) {
                        if (name.endsWith(extension.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            files = rootDirectory.listFiles(filter);
        } else {
            files = rootDirectory.listFiles();
        }

        if (files == null) {
            System.out.println("error processing [" + rootDirectory + "]. skipping");
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    directoryInfo.addFile(new FileInfo(file));
                } else if (file.isDirectory()) {
                    DirectoryInfo directory = explore(file, allowedExtensions);
                    directoryInfo.addSubDir(directory);
                }
            }
        }
        
        return directoryInfo;
    }

    public DirectoryComparison compare(DirectoryInfo actual, DirectoryInfo backup) {
        DirectoryComparison directoryComparison = new DirectoryComparison(actual, backup, ComparisonResult.MODIFIED);
        
        compareFiles(actual, backup, directoryComparison);
        
        SortedMap<String, DirectoryInfo> actualSubDirs = new TreeMap<String, DirectoryInfo>();
        for (DirectoryInfo subDir : actual.getSubDirs()) {
            actualSubDirs.put(subDir.getName(), subDir);
        }
        SortedMap<String, DirectoryInfo> backupSubDirs = new TreeMap<String, DirectoryInfo>();
        for (DirectoryInfo subDir : backup.getSubDirs()) {
            backupSubDirs.put(subDir.getName(), subDir);
        }
        
        for (Map.Entry<String, DirectoryInfo> entry : actualSubDirs.entrySet()) {
            DirectoryInfo actualSubdir = entry.getValue();
            DirectoryInfo backupSubdir = backupSubDirs.remove(entry.getKey());
            if (backupSubdir == null) {
                directoryComparison.addSubdir(dirToComparison(actualSubdir, ComparisonResult.ADDED));
            } else {
                if (!backupSubdir.equals(actualSubdir)) {
                    directoryComparison.addSubdir(compare(actualSubdir, backupSubdir));
                }
            }
        }
        
        for (DirectoryInfo subdir : backupSubDirs.values()) {
            directoryComparison.addSubdir(dirToComparison(subdir, ComparisonResult.REMOVED));
        }
        
        if (isChanged(directoryComparison)) {
            return directoryComparison;
        } else {
            return new DirectoryComparison(actual, backup, ComparisonResult.UNCHANGED);
        }
//        if (directoryComparation.getFiles().isEmpty() && directoryComparation.getSubDirs().isEmpty()) {
//            return new DirectoryComparation(actual, backup, ComparationResult.UNCHANGED);
//        } else {
//            return directoryComparation;
//        }
    }
    
    private boolean isChanged(DirectoryComparison comparation) {
        if (!comparation.getFiles().isEmpty()) {
            return true;
        }
        for (DirectoryComparison subdirComparation : comparation.getSubdirs()) {
            if (subdirComparation.getComparisonResult() == ComparisonResult.ADDED
                    || subdirComparation.getComparisonResult() == ComparisonResult.REMOVED || isChanged(subdirComparation)) {
                return true;
            }
        }
        return false;
    }
    
    public List<SyncCommand> compareAndBuildSyncCommandList(DirectoryInfo actual, DirectoryInfo backup) {
        DirectoryComparison comparison = compare(actual, backup);
        List<SyncCommand> commands = new ArrayList<SyncCommand>();
        fillSyncCommandList(comparison, commands, actual, backup);
        return commands;
    }
    
    public List<SyncCommand> compareAndBuildSyncCommandList(DirectoryComparison comparation, DirectoryInfo actual, DirectoryInfo backup) {
        List<SyncCommand> commands = new ArrayList<SyncCommand>();
        fillSyncCommandList(comparation, commands, actual, backup);
        return commands;
    }
    
    public void executeCommands(List<SyncCommand> commands) throws IOException {
        int total = commands.size();
        int count = 0;
        for (SyncCommand command : commands) {
            count++;
            System.out.println("executings (" + count + "/" + total + "): " + command);
            switch (command.getAction()) {
                case MAKE_DIR:
                    if (!command.getTarget().mkdir()) {
                        throw new IOException("Error creating directory " + command.getTarget());
                    }
                    break;
                case REMOVE_DIR:
                    if (!command.getTarget().delete()) {
                        throw new IOException("Error deleting directory " + command.getTarget());
                    }
                    break;
                case REMOVE_FILE:
                    if (!command.getTarget().delete()) {
                        throw new IOException("Error deleting file " + command.getTarget());
                    }
                    break;
                case COPY_FILE:
                    FileUtils.copyFile(command.getSource(), command.getTarget());
                    break;
                default: 
                    throw new IllegalArgumentException("Unsupported type: " + command.getAction());
            }
        }
    }
    
    private void fillSyncCommandList(DirectoryComparison directoryComparison, List<SyncCommand> commands, DirectoryInfo actualRoot, DirectoryInfo backupRoot) {
        if (directoryComparison.getComparisonResult() == ComparisonResult.ADDED) {
            File destinationPath = createEquivalentPathForAnotherRoot(directoryComparison.getActual().getPath(), actualRoot, backupRoot);
            commands.add(new SyncCommand(Action.MAKE_DIR, destinationPath));
        }
        
        for (FileComparison fileComparison : directoryComparison.getFiles()) {
            switch (fileComparison.getComparisonResult()) {
                case ADDED:
                    File destinationPath = createEquivalentPathForAnotherRoot(fileComparison.getActual().getPath(), actualRoot, backupRoot);
                    commands.add(new SyncCommand(Action.COPY_FILE, fileComparison.getActual().getPath(), destinationPath));
                    break;
                case REMOVED:
                    commands.add(new SyncCommand(Action.REMOVE_FILE, fileComparison.getPrevious().getPath()));
                    break;
                case MODIFIED:
                    commands.add(new SyncCommand(Action.REMOVE_FILE, fileComparison.getPrevious().getPath()));
                    commands.add(new SyncCommand(Action.COPY_FILE, fileComparison.getActual().getPath(), fileComparison.getPrevious().getPath()));
                    break;
                case UNCHANGED:
                    break;
                default: 
                    throw new IllegalArgumentException("Unsupported type: " + fileComparison.getComparisonResult());
            }
        }
        for (DirectoryComparison subDirComparison : directoryComparison.getSubdirs()) {
            fillSyncCommandList(subDirComparison, commands, actualRoot, backupRoot);
        }
        switch (directoryComparison.getComparisonResult()) {
            case REMOVED:
                commands.add(new SyncCommand(Action.REMOVE_DIR, directoryComparison.getPrevious().getPath()));
                break;
            case ADDED:
            case MODIFIED:
            case UNCHANGED:
                break;
            default: 
                throw new IllegalArgumentException("Unsupported type: " + directoryComparison.getComparisonResult());
        }
    }
    
    private void compareFiles(DirectoryInfo actual, DirectoryInfo backup, DirectoryComparison directoryComparison) {
        SortedMap<String, FileInfo> actualFiles = new TreeMap<>();
        for (FileInfo file : actual.getFiles()) {
            actualFiles.put(file.getName(), file);
        }
        SortedMap<String, FileInfo> backupFiles = new TreeMap<>();
        for (FileInfo file : backup.getFiles()) {
            backupFiles.put(file.getName(), file);
        }
        for (Map.Entry<String, FileInfo> entry : actualFiles.entrySet()) {
            FileInfo actualFile = entry.getValue();
            FileInfo backupFile = backupFiles.remove(entry.getKey());
            if (backupFile == null) {
                directoryComparison.addFile(new FileComparison(actualFile, null, ComparisonResult.ADDED));
            } else {
                if (!filesEquals(backupFile, actualFile)) {
                    directoryComparison.addFile(new FileComparison(actualFile, backupFile, ComparisonResult.MODIFIED));
                }
            }
        }
        for (FileInfo removedFile : backupFiles.values()) {
            directoryComparison.addFile(new FileComparison(null, removedFile, ComparisonResult.REMOVED));
        }
    }
    
    private File createEquivalentPathForAnotherRoot(File sourcePath, DirectoryInfo sourceRoot, DirectoryInfo targetRoot) {
        String relative = sourceRoot.getPath().toURI().relativize(sourcePath.toURI()).getPath();
        return new File(targetRoot.getPath(), relative);
    }
    
    private boolean filesEquals(FileInfo first, FileInfo second) {
        if (first == null && second == null) {
            return true;
        } else if (first == null && second != null || first != null && second == null) {
            return false;
        } else {
            return new EqualsBuilder()
                .append(first.getName(), second.getName())
                .append(first.getSize(), second.getSize())
                .isEquals();
        }
    }
    
    private DirectoryComparison dirToComparison(DirectoryInfo dir, ComparisonResult comparisonResult) {
        DirectoryInfo actual = null;
        DirectoryInfo backup = null;
        switch (comparisonResult) {
            case ADDED:
                actual = dir;
                break;
            case REMOVED:
                backup = dir;
                break;
            default:
                throw new IllegalArgumentException("Unexpected comparisonResult: " + comparisonResult);
        }
        
        DirectoryComparison resultComparison = new DirectoryComparison(actual, backup, comparisonResult);
        for (FileInfo file : dir.getFiles()) {
            resultComparison.addFile(new FileComparison(actual != null ? file : null, backup != null ? file : null, comparisonResult));
        }
        for (DirectoryInfo subDir : dir.getSubDirs()) {
            resultComparison.addSubdir(dirToComparison(subDir, comparisonResult));
        }
        return resultComparison;
    }
    
    public ComparisonStat calculateStat(DirectoryComparison comparison) {
        ComparisonStat stat = new ComparisonStat();
        innerCalculateStat(comparison, stat);
        
        return stat;
    }

    private void innerCalculateStat(DirectoryComparison comparison, ComparisonStat stat) {
        for (FileComparison file : comparison.getFiles()) {
            switch (file.getComparisonResult()) {
                case ADDED:
                    stat.fileAdded(((FileInfo) file.getActual()).getSize());
                    break;
                case REMOVED:
                    stat.fileRemoved(((FileInfo) file.getPrevious()).getSize());
                    break;
                case MODIFIED:
                    stat.fileModified(((FileInfo) file.getPrevious()).getSize(), ((FileInfo) file.getActual()).getSize());
                    break;
            }
        }
        for (DirectoryComparison subdir : comparison.getSubdirs()) {
            switch (subdir.getComparisonResult()) {
                case ADDED:
                    stat.setDirsAdded(stat.getDirsAdded() + 1);
                    break;
                case REMOVED:
                    stat.setDirsRemoved(stat.getDirsRemoved() + 1);
                    break;
            }
            innerCalculateStat(subdir, stat);
        }
    }
}
