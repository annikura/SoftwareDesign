package ru.hse.spb.interpreter.command.impl;

import org.junit.Test;
import ru.hse.spb.interpreter.command.BashCommandContext;
import ru.hse.spb.interpreter.model.BashCommandResult;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CdTest {
    @Test
    public void testIsFits() {
        Cd cd = new Cd();
        assertTrue(cd.isFits("  cd /foo/    "));
        assertTrue(cd.isFits("cd"));
        assertTrue(cd.isFits("  cd  "));
        assertTrue(cd.isFits("cd fjkdjfsl fdlj sdfkjfldskjf sa"));
        assertFalse(cd.isFits("fjkdjfsl cd fdlj sdfkjfldskjf sa"));
        assertFalse(cd.isFits(""));
    }

    @Test
    public void testIgnoresPassed() {
        Cd cd = new Cd();
        BashCommandContext context = new BashCommandContext();
        cd.apply("cd", context, new BashCommandResult("foo"));
        assertEquals(System.getProperty("user.home"), context.getPath(""));
    }

    @Test
    public void testCdToHome() {
        Cd cd = new Cd();
        BashCommandContext context = new BashCommandContext();
        cd.apply("cd", context);
        assertEquals(System.getProperty("user.home"), context.getPath(""));
    }

    @Test
    public void testCdToSrc() {
        Cd cd = new Cd();
        BashCommandContext context = new BashCommandContext();
        String oldPath = context.getPath("");
        cd.apply("cd src", context);
        assertEquals(Paths.get(oldPath, "src").toString(), context.getPath(""));
    }
}
