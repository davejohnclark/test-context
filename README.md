test-context
============

A simple way of adding context to java unit tests to allow different set up and tear down steps to run - currently aimed at junit tests.  Inspired by RSpec.

Often when testing a class there are certain tests that require the same set up steps before the test can be exectuted.  These tests share the same context within which they should run.  Often these steps are not important in terms of the detail of the test, but provide the scope within which the test case holds true.

This often manifests in one of two ways:

1. Shared set up method that executes before *every* test in the class:

  ```java
  @Before
  public void setUp(){
    setUpFoo();
    setUpBar();
  }
  
  @Test
  public void shouldDoSomethingWhenFoo(){
    // do test that requires foo
  }
  
  @Test
  public void shouldDoSomethingElseWhenFoo(){
    // do test that requires foo
  }
  
  @Test
  public void shouldDoSomethingWhenBar(){
    // do test that requires bar
  }
  
  @Test
  public void shouldDoSomethingElseWhenBar(){
    // do test that requires bar
  }
  ```
  
  Which means `bar` is being set up for tests that require `foo`, and `foo` is being set up for tests that require `bar`.  This is often deemed *ok*, but falls down when the different tests require conficting set up.  When this is the case, the only real remaining option is:

2. Repeated steps within the tests themselves:
  
  ```java
  @Test
  public void shouldDoSomethingWhenFoo(){
    setUpFoo();
    // do test that requires foo
  }
  
  @Test
  public void shouldDoSomethingElseWhenFoo(){
    setUpFoo();
    // do test that requires foo
  }
  
  @Test
  public void shouldDoSomethingWhenBar(){
    setUpBar();
    // do test that requires bar
  }

  @Test
  public void shouldDoSomethingElseWhenBar(){
    setUpBar();
    // do test that requires bar
  }
  ```
  
  Also often deemed *ok*, but can end up with a lot of repetition between tests within the test method itself.  Ideally the test method would contain only the detail of this specific test case, anything else is really just noise to be sifted through to get to the meat of the test.
  
Enter **contexts**.  By supplying a context to the set up methods, and scoping each test to a context, the set up methods can be executed only for the tests that require them.
  
  The above scenarios can then be written as:
  
  ```java
  @BeforeContext("foo is set up")
  public void setUpFoo(){
    // do set up of foo
  }
  
  @Test
  @Context(when = "foo is set up")
  public void shouldDoSomethingWhenFoo(){
    // do test that requires foo
  }
  
  @Test
  @Context(when = "foo is set up")
  public void shouldDoSomethingElseWhenFoo(){
    // do test that requires Foo
  }
  
  @BeforeContext("bar is set up")
  public void setUpBar(){
    // do set up of bar
  }
  
  @Test
  @Context(when = "bar is set up")
  public void shouldDoSomethingWhenBar(){
    // do test that requires bar
  }
  
  @Test
  @Context(when = "bar is set up")
  public void shouldDoSomethingElseWhenBar(){
    // do test that requires bar
  }
  ```
  
Each test now declares its context outside the detail of the test case, and only the set up required for the test is run.  Furthermore, the different set up methods can now perform conflicting tasks as they run exclusively.
  
In order to work the annotations need to be processed by the test framework at the appropriate times.   Junit provides a very nice way to add custom before/after events using `TestRule`, which requires the rule to be declared within the test class:
  
  ```java
  @Rule
  public ContextRule context = new ContextRule(this);
  ```

And that's all that is required.  Junit processes this rule before each test `statement`, allowing the rule to wrap the execution of the statement and run custom before and after events.  The same could be achieved with a custom test runner, and any other test framework that supports something similar could be adapted to understand these annotations.

The full feature set currently implemented is:

  * `@BeforeContext("context name")`: runs before any test with the context `context name`.
  * `@AfterContext("context name")`: runs after any test with the context `context name`.
  * `@Context(when = "context name")`: marks a test as belonging within the context `context name`.
  * `@Context(whenEachOf = { "context name", "other context name" } )`: marks a test as belonging within both contexts `context name`, and `other context name`.

