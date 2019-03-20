package ru.hse.spb.interpreter.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hse.spb.interpreter.command.BashCommand;
import ru.hse.spb.interpreter.command.BashCommandContext;
import ru.hse.spb.interpreter.command.NoSuchDirectoryException;
import ru.hse.spb.interpreter.model.BashCommandResult;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cd implements BashCommand {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^(\\s)*cd(\\s+|$)");
    private static final Logger LOG = LoggerFactory.getLogger(Cd.class);

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
            LOG.warn("unable to apply command cd to " + inputString);
            return result;
        }
        List<String> dataList = data.get();
        switch (dataList.size()) {
            case 0:
                try {
                    context.cd();
                } catch (NoSuchDirectoryException e) {
                    LOG.warn(e.getMessage());
                }
                break;
            case 1:
                try {
                    context.cd(dataList.get(0));
                } catch (NoSuchDirectoryException e) {
                    LOG.warn(e.getMessage());
                }
                break;
            default:
                LOG.warn("Too many arguments in command: " + inputString);
        }
        return result;
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
