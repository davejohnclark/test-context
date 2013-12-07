test-context
============

A simple way of adding context to java unit tests to allow different set up and tear down steps to run - currently aimed at junit tests.  Inspired by RSpec.

Often when testing a class there are certain tests that require the same set up steps before the test can be exectuted.  These tests share the same context within which they should run.  Often these steps are not important in terms of the detail of the test, but provide the scope within which the test case holds true.

This often manifests in one of two ways:

1. Shared setUp method that executes before *every* test in the class:

  ```java
  @Before
  public void setUp(){
    setUpA();
    setUpB();
  }
  
  @Test
  public void shouldDoSomethingWhenA(){
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingElseWhenA(){
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingWhenB(){
    // do test that requires B
  }
  
  @Test
  public void shouldDoSomethingElseWhenB(){
    // do test that requires B
  }
  ```
  
  Which means B is being set up for tests that require A, and A is being set up for tests that require B.  This is often deemed *ok*, but falls down when the different tests require conficting set up.  When this is the case, your only real remaining option is:

2. Repeated steps within the tests themselves:
  
  ```java
  @Test
  public void shouldDoSomethingWhenA(){
    setUpA();
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingElseWhenA(){
    setUpA();
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingWhenB(){
    setUpB();
    // do test that requires B
  }

  @Test
  public void shouldDoSomethingElseWhenB(){
    setUpB();
    // do test that requires B
  }
  ```
  
  Also often deemed *ok*, but can end up with a lot of repetition between tests within the test method itself.  Ideally the test method would contain only the detail of this specific test case, anything else is really just noise to be sifted through to get to the meat of the test.
  

