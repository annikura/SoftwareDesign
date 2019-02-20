package ru.hse.spb.interpreter.command;

import java.io.File;
import java.nio.file.Paths;

public class BashCommandContext {
    private String processPath;

    public BashCommandContext() {
        processPath = Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public String getPath(String path) {
        if (Paths.get(path).isAbsolute()) {
            return Paths.get(path).normalize().toString();
        }
        return Paths.get(processPath, path).normalize().toString();
    }

    public String cd() throws NoSuchDirectoryException {
        processPath = System.getProperty("user.home");
        if (!checkPathIsDir(processPath)) {
            throw new NoSuchDirectoryException("Not a directory: " + processPath);
        }
        return processPath;
    }

    public String cd(String path) throws NoSuchDirectoryException {
        processPath = getPath(path);
        if (!checkPathIsDir(processPath)){
            throw new NoSuchDirectoryException("Not a directory: " + processPath);
        }
        return processPath;
    }

    private boolean checkPathIsDir(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }
}


