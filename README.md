test-context
============

A simple way of adding context to java unit tests to allow different set up and tear down steps to run - currently aimed at junit tests.  Inspired by RSpec.

Often when testing a class there are certain tests that require the same set up steps before the test can be exectuted.  These tests share the same context within which they should run.  Often these steps are not important in terms of the detail of the test, but provide the scope within which the test case holds true.

This often manifests in one of two ways:

1. Shared setUp method that executes before *every* test in the class:

  ```java
  @Before
  public void setUp(){
    setupA();
    setupB();
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

2. Repeated steps within the tests that add noise to the detail of the test case:
  
  ```java
  @Test
  public void shouldDoSomethingWhenA(){
    setupA();
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingElseWhenA(){
    setupA();
    // do test that requires A
  }
  
  @Test
  public void shouldDoSomethingWhenB(){
    setupB();
    // do test that requires B
  }

  @Test
  public void shouldDoSomethingElseWhenB(){
    setupB();
    // do test that requires B
  }
  ```
