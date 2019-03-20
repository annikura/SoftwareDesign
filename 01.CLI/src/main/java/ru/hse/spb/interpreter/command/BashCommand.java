package ru.hse.spb.interpreter.command;


import org.apache.commons.cli.ParseException;
import ru.hse.spb.interpreter.model.BashCommandResult;

import javax.annotation.Nonnull;
import java.util.Map;


public interface BashCommand {
    boolean isFits(final String inputString);

    @Nonnull
    BashCommandResult apply(final String inputString, BashCommandContext context);

    @Nonnull
    default BashCommandResult apply(final String inputString, BashCommandContext context, final BashCommandResult predResult) {
        return apply(inputString, context);
    }


}
