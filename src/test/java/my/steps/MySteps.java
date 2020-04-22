package my.steps;

import io.cucumber.java8.En;

public class MySteps implements En {
  private int i;

  public MySteps() {
    Given("some precondition", () -> {
      i = 0;
    });

    When("I do something", () -> {
      i++;
    });

    Then("something happens", () -> {
      assert i == 1;
    });
  }
}
