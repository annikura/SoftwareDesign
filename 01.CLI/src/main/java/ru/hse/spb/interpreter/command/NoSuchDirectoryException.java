package ru.hse.spb.interpreter.command;

public class NoSuchDirectoryException extends Exception {
    public NoSuchDirectoryException(String message) {
        super(message);
    }
}
