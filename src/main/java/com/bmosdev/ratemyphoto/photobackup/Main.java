package com.bmosdev.ratemyphoto.photobackup;

public class Main {
    
//    private static final String PROPERTIES_FILE = "photobackup.properties";
//
//    private static final String PROP_ACTUAL_PATH = "actualPath";
//    private static final String PROP_BACKUP_PATH = "backupPath";
//
//    private static final String CMD_ACTUAL_PATH = "a";
//    private static final String CMD_BACKUP_PATH = "b";
//
//    private static Options options;
//
//    public static void main(String[] args) throws Exception {
//        Config config = loadConfig(args);
//
//        DirectoryTool directoryTool = new DirectoryTool();
//        File actualDir = new File(config.actualPath);
//        log("exploring '", actualDir, "'\n");
//        DirectoryInfo actual = directoryTool.explore(actualDir, null);
//        File backupDir = new File(config.backupPath);
//        log("exploring '", backupDir, "'\n");
//        DirectoryInfo backup = directoryTool.explore(backupDir, null);
//        log("comparing '", actual.getPath(), "' and '", backup.getPath(), "'\n");
//        DirectoryComparation comparation = directoryTool.compare(actual, backup);
//        log(comparation.toFormattedString());
//        log(directoryTool.calculateStat(comparation).toFormattedString());
//        List<SyncCommand> commands = directoryTool.compareAndBuildSyncCommandList(comparation, actual, backup);
//        if (commands.isEmpty()) {
//            log("Nothing to synchronize");
//        } else {
//            log("commands to execute: ", commands.size());
//            log("Continue(y/n)?");
//            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//            String str = bufferRead.readLine();
//            if ("y".equals(str)) {
//                directoryTool.executeCommands(commands);
//            }
//        }
//    }
//
//    private static Config loadConfig(String[] args) throws IOException, ParseException {
//        Config config = new Config();
//
//        File path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
//        if (path.isFile()) {
//            path = path.getParentFile();
//        }
//        File file = new File(path, PROPERTIES_FILE);
//        if (!file.exists()) {
//            log("Properties file '", PROPERTIES_FILE, "' not found, skipping\n");
//        } else {
//            Properties properties = new Properties();
//            properties.load(new FileInputStream(file));
//            config.actualPath = properties.getProperty(PROP_ACTUAL_PATH);
//            config.backupPath = properties.getProperty(PROP_BACKUP_PATH);
//        }
//
//        CommandLine cmd = parseCommandLine(args);
//        if (cmd.hasOption(CMD_ACTUAL_PATH)) {
//            config.actualPath = cmd.getOptionValue(CMD_ACTUAL_PATH);
//        }
//        if (cmd.hasOption(CMD_BACKUP_PATH)) {
//            config.backupPath = cmd.getOptionValue(CMD_BACKUP_PATH);
//        }
//
//        checkConfig(config);
//
//        log(config.toFormattedString(), "\n");
//
//        return config;
//    }
//
//    private static void checkConfig(Config config) {
//        boolean valid = true;
//        if (config.actualPath == null) {
//            log("Actual path not specified");
//            valid = false;
//        }
//        if (config.backupPath == null) {
//            log("Backup path not specified");
//            valid = false;
//        }
//        if (!valid) {
//            showUsage();
//            System.exit(1);
//        }
//    }
//
//    private static CommandLine parseCommandLine(String[] args) throws ParseException {
//        createOptions();
//
//        CommandLineParser parser = new PosixParser();
//        return parser.parse(options, args);
//    }
//
//    private static void createOptions() {
//        options = new Options();
//        Option actualPath = new Option(CMD_ACTUAL_PATH, "actualPath", true, "Actual path");
//        actualPath.setArgName("actual path");
//        options.addOption(actualPath);
//        Option backupPath = new Option(CMD_BACKUP_PATH, "backupPath", true, "Backup path");
//        backupPath.setArgName("backup path");
//        options.addOption(backupPath);
//    }
//
//    private static void log(Object... parts) {
//        StringBuilder sb = new StringBuilder();
//        for (Object part : parts) {
//            sb.append(part);
//        }
//        System.out.println(sb.toString());
//    }
//
//    private static void showUsage() {
//        HelpFormatter formatter = new HelpFormatter();
//        formatter.printHelp("java -jar photo-backup.jar", options);
//    }
//
//    private static class Config {
//        String actualPath;
//        String backupPath;
//
//        @Override
//        public String toString() {
//            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
//        }
//
//        public String toFormattedString() {
//            return new StrBuilder()
//                    .appendln("config: ")
//                    .append("actualPath: ").appendln(actualPath)
//                    .append("backupPath: ").append(backupPath)
//                    .toString();
//        }
//    }
}
    