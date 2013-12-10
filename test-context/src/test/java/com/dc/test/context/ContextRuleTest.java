package com.dc.test.context;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;

import com.dc.test.context.junit.ContextRule;

public class ContextRuleTest {

    @Rule
    public ContextRule context = new ContextRule(this);

    Object foo;
    Object bar;

    @BeforeContext("foo is not null")
    public void fooIsNotNullSetUp() {
        foo = new Object();
    }

    @BeforeContext("bar is not null")
    public void barIsNotNullSetUp() {
        bar = new Object();
    }

    @Test
    @Context(when = "foo is not null")
    public void shouldOnlyExecuteFooIsNotNullBeforeTheTest() {

        assertNotNull(foo);
        assertNull(bar);
    }

    @Test
    @Context(when = "bar is not null")
    public void shouldOnlyExecuteBarIsNotNullBeforeTheTest() {

        assertNotNull(bar);
        assertNull(foo);
    }

    @Test
    @Context(whenEachOf = { "foo is not null", "bar is not null" })
    public void shouldExecuteTheBeforeContextsForBothFooAndBar() {

        assertNotNull(foo);
        assertNotNull(bar);
    }

    @Test
    public void shouldNotRunAnyBeforeContextsWhenTheTestDeclaresNoContext() {

        assertNull(foo);
        assertNull(bar);
    }
}
