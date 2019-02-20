package ru.hse.spb.interpreter.command.impl;

import org.junit.Test;
import ru.hse.spb.interpreter.command.BashCommandContext;
import ru.hse.spb.interpreter.model.BashCommandResult;

import static org.junit.Assert.*;

public class LsTest {
    @Test
    public void testIsFits() {
        Ls ls = new Ls();
        assertTrue(ls.isFits("  ls /foo/    "));
        assertTrue(ls.isFits("ls"));
        assertTrue(ls.isFits("  ls  "));
        assertTrue(ls.isFits("ls fjkdjfsl fdlj sdfkjfldskjf sa"));
        assertFalse(ls.isFits("fjkdjfsl ls fdlj sdfkjfldskjf sa"));
        assertFalse(ls.isFits(""));
    }

    @Test
    public void testIgnoresPassed() {
        Ls ls = new Ls();
        BashCommandContext context = new BashCommandContext();
        assertEquals("readme.md\n" +
                "Hw01.jpg\n" +
                "1.iml\n" +
                ".idea\n" +
                "src\n" +
                "pom.xml\n" +
                "target", ls.apply("ls", context, new BashCommandResult("foo")).getResult());
    }

    @Test
    public void testSimpleLs() {
        Ls ls = new Ls();
        BashCommandContext context = new BashCommandContext();
        assertEquals("readme.md\n" +
                "Hw01.jpg\n" +
                "1.iml\n" +
                ".idea\n" +
                "src\n" +
                "pom.xml\n" +
                "target", ls.apply("ls", context).getResult());
    }

    @Test
    public void testLsInSRC() {
        Ls ls = new Ls();
        BashCommandContext context = new BashCommandContext();
        assertEquals("main\n" +
                "test", ls.apply("ls src", context).getResult());
    }
}
