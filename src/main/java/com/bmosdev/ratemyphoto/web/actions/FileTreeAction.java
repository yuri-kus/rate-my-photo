package com.bmosdev.ratemyphoto.web.actions;

import com.bmosdev.ratemyphoto.web.Action;
import com.bmosdev.ratemyphoto.web.JacksonBean;
import lime.util.Strings;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class FileTreeAction {

    private static final Logger log = LogManager.getLogger();

    public String rootDir;

    @Action(path = "/dirs")
	public JacksonBean[] getDirectories() throws IOException {
        log.debug("rootDir: {}", rootDir);
        List<FileTreeNode> nodes = new ArrayList<>();
        if (Strings.isBlank(this.rootDir)) {
            FileSystemView fileSystemView = FileSystemView.getFileSystemView();
            for(File path : File.listRoots()) {
                nodes.add(new FileTreeNode(fileSystemView.getSystemDisplayName(path), path.getAbsolutePath(), true));
            }
        } else {
            File rootDir = new File(URLDecoder.decode(this.rootDir, "UTF-8"));
            for (File directory : rootDir.listFiles((file) -> file.isDirectory())) {
                nodes.add(new FileTreeNode(directory.getName(), directory.getAbsolutePath(), true));
            }
        }
        return nodes.toArray(new JacksonBean[nodes.size()]);
	}

	private static class FileTreeNode implements JacksonBean {

        private final String title;
        private final String key;
        private final boolean folder;
        private final boolean lazy = true;
//        private final List<FileTreeNode> children;

        private FileTreeNode(String title, String key, boolean folder) {
            this.title = title;
            this.key = key;
            this.folder = folder;
        }

        public String getTitle() {
            return title;
        }

        public String getKey() {
            return key;
        }

        public boolean isFolder() {
            return folder;
        }

        public boolean isLazy() {
            return lazy;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
