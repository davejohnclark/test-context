package com.dc.test.context;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;

import com.dc.test.context.junit.ContextRule;

public class ContextRuleTest {

    @Rule
    public ContextRule context = new ContextRule(this);

    Object a;
    Object b;

    @BeforeContext("a is not null")
    public void aIsNotNullSetUp() {
        a = new Object();
    }

    @BeforeContext("b is not null")
    public void bIsNotNullSetUp() {
        b = new Object();
    }

    @Test
    @Context(when = "a is not null")
    public void shouldOnlyExecuteAIsNotNullBeforeTheTest() {

        assertNotNull(a);
        assertNull(b);
    }

    @Test
    @Context(when = "b is not null")
    public void shouldOnlyExecuteBIsNotNullBeforeTheTest() {

        assertNotNull(b);
        assertNull(a);
    }

    @Test
    @Context(whenEachOf = { "a is not null", "b is not null" })
    public void shouldExecuteTheBeforeContextsForBothAAndB() {

        assertNotNull(a);
        assertNotNull(b);
    }
}
