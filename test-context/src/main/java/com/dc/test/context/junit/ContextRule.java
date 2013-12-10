package com.dc.test.context.junit;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.dc.test.context.AfterContext;
import com.dc.test.context.BeforeContext;
import com.dc.test.context.Context;

/**
 * Junit Rule that invokes any methods annotated as "Before" the context of the test being run.
 * 
 * @author dave
 * 
 */
public class ContextRule implements TestRule {

    private Object testInstance;
    private Map<String, List<Method>> beforeContexts = new HashMap<String, List<Method>>();
    private Map<String, List<Method>> afterContexts = new HashMap<String, List<Method>>();

    public ContextRule(Object testInstance) {
        this.testInstance = testInstance;
        for (Method method : testInstance.getClass().getMethods()) {
            storeBeforeContext(method);
            storeAfterContext(method);
        }
    }

    private void storeBeforeContext(Method method) {
        BeforeContext beforeContext = method.getAnnotation(BeforeContext.class);
        if (beforeContext != null) {
            addToStore(beforeContext.value(), method, beforeContexts);
        }
    }

    private void storeAfterContext(Method method) {
        AfterContext afterContext = method.getAnnotation(AfterContext.class);
        if (afterContext != null) {
            addToStore(afterContext.value(), method, afterContexts);
        }
    }

    private void addToStore(String contextName, Method method, Map<String, List<Method>> contextStore) {
        List<Method> methods = contextStore.get(contextName);
        if (methods == null) {
            methods = new ArrayList<Method>();
            contextStore.put(contextName, methods);
        }
        methods.add(method);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                List<String> contextNames = getContextNames(description);
                executeBeforeContexts(contextNames);
                try {
                    base.evaluate();
                } finally {
                    executeAfterContexts(contextNames);
                }
            }
        };
    }

    private List<String> getContextNames(Description description) {
        Context context = description.getAnnotation(Context.class);
        if (context == null) {
            return emptyList();
        }
        if (context.whenEachOf().length >= 1) {
            return asList(context.whenEachOf());
        }
        return asList(context.when());
    }

    private void executeBeforeContexts(List<String> contextNames) {
        executeContexts(contextNames, beforeContexts);
    }

    private void executeAfterContexts(List<String> contextNames) {
        executeContexts(contextNames, afterContexts);
    }

    private void executeContexts(List<String> contextNames, Map<String, List<Method>> contextStore) {
        for (String name : contextNames) {
            List<Method> methods = contextStore.get(name);
            if (methods == null) {
                continue;
            }
            invokeEachMethod(methods);
        }
    }

    private void invokeEachMethod(List<Method> methods) {
        for (Method method : methods) {
            try {
                invoke(method);
            } catch (Exception e) {
                throw new ContextRuleException(e);
            }
        }
    }

    private void invoke(Method method) throws IllegalAccessException, InvocationTargetException {
        method.invoke(testInstance, (Object[]) null);
    }

    /**
     * Indicates an exception was thrown during the execution of the ContextRule.
     * 
     * @author dave
     * 
     */
    private class ContextRuleException extends RuntimeException {

        private static final long serialVersionUID = 5690097822353534540L;

        public ContextRuleException(Exception e) {
            super(e);
        }

    }
}
