package ru.hse.spb.interpreter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import ru.hse.spb.interpreter.Interpreter;
import ru.hse.spb.interpreter.Preprocessor;
import ru.hse.spb.interpreter.Tokenizer;
import ru.hse.spb.interpreter.command.Assignment;
import ru.hse.spb.interpreter.command.BashCommand;
import ru.hse.spb.interpreter.model.BashCommandResult;
import ru.hse.spb.interpreter.model.PipeSplitCommand;
import ru.hse.spb.interpreter.model.Token;


import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Component("Interpreter")
public class InterpreterImpl implements Interpreter {
    private final Map<String, String> envVariables;
    private final Assignment assignment;
    private final Tokenizer tokenizer;
    private final Preprocessor preprocessor;
    private final List<BashCommand> commands;
    private final PrintStream out;
    private final Scanner in;

    @Inject
    public InterpreterImpl(final List<BashCommand> commands,
                           final Assignment assignment,
                           @Qualifier("envVariables") final Map<String, String> envVariables,
                           final PrintStream out,
                           final Scanner in,
                           final Preprocessor preprocessor,
                           final Tokenizer tokenizer) {
        this.commands = commands;
        this.assignment = assignment;
        this.envVariables = envVariables;
        this.out = out;
        this.in = in;
        this.preprocessor = preprocessor;
        this.tokenizer = tokenizer;
    }

    @Override
    public void run() {
        while (in.hasNextLine()) {
            envVariables.putAll(System.getenv());
            final String input = in.nextLine();
            final List<Token> tokens = tokenizer.getTokens(input);
            for (Token token : tokens) {
                BashCommandResult prevResult= new BashCommandResult(null);
                for (PipeSplitCommand inputCommand : token.getcommands()) {
                    if (assignment.updateVariables(inputCommand.getEntities())) {
                        continue;
                    }
                    final String commandWithReplacement = preprocessor.run(inputCommand.getEntities());
                    final Optional<BashCommandResult>  response = findCommand(commandWithReplacement, prevResult);
                    if (!response.isPresent()) {
                        out.println("command not found: " + inputCommand);
                    }
                    prevResult = response.orElseGet(() -> new BashCommandResult(""));
                }
                out.println(prevResult.getResultOrDefault(""));
            }
        }
    }

    private Optional<BashCommandResult> findCommand(final String token, final BashCommandResult prevResult) {
        for (BashCommand command : commands) {
            if (command.isFits(token)) {
                return Optional.of(command.apply(token, prevResult));
            }
        }
        return Optional.empty();
    }
}
