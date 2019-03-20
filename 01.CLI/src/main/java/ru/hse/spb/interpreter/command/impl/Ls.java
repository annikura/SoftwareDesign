package ru.hse.spb.interpreter.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hse.spb.interpreter.command.BashCommand;
import ru.hse.spb.interpreter.command.BashCommandContext;
import ru.hse.spb.interpreter.command.NoSuchDirectoryException;
import ru.hse.spb.interpreter.model.BashCommandResult;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Ls implements BashCommand {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(\\s)*ls(\\s+|$)");
    private static final Logger LOG = LoggerFactory.getLogger(Ls.class);

    @Override
    public boolean isFits(final String inputString) {
        return getData(inputString).isPresent();
    }


    @Nonnull
    @Override
    public BashCommandResult apply(String inputString, BashCommandContext context) {
        Optional<List<String>> data = getData(inputString);
        final BashCommandResult result = new BashCommandResult("");
        if (!data.isPresent()) {
            LOG.warn("unable to apply command ls to " + inputString);
            return result;
        }
        String dir = context.getPath("");
        List<String> dataList = data.get();
        switch (dataList.size()) {
            case 0:
                break;
            case 1:
                dir = context.getPath(dataList.get(0));
                break;
            default:
                LOG.warn("Too many arguments in command: " + inputString);
                return result;
        }

        File dirFile = new File(dir);
        File[] listOfFiles = dirFile.listFiles();
        if (!dirFile.isDirectory() || listOfFiles == null) {
            LOG.warn("Not a directory: " + dir);
            return result;
        }

        return new BashCommandResult(Arrays.stream(listOfFiles).map(File::getName).collect(Collectors.joining("\n")));
    }

    @Nonnull
    private Optional<List<String>> getData(final String inputString) {
        final Matcher matcher = COMMAND_PATTERN.matcher(inputString);
        if (!matcher.find()) {
            return Optional.empty();
        }
        final String fileNames = matcher.replaceFirst("");
        return Optional.of(Arrays.stream(fileNames.split("\\s+")).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
    }
}
