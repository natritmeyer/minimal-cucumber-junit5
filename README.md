# Minimal Cucumber & JUnit5 project

Since version 5.0.0-RC2 Cucumber-JVM has supported JUnit 5. This example repo
contains the minimal set of moving pieces required for a junit5-based cucumber
project.

## Quirks

The setup isn't 100% intuitive if you're used to using cucumber with JUnit4;
there are a few surprises you'll find along the way if you try to work this
out yourself. Here are those quirks:

### 0. Required dependencies

The absolute minimum dependencies are (at time of writing):

* `org.junit.jupiter:junit-jupiter`
* `org.junit.vintage:junit-vintage-engine`
* `io.cucumber:cucumber-java8` (or `cucumber-java` if you're so inclined)
* `io.cucumber:cucumber-junit-platform-engine`

### 1. Dependency versions

Whether you're using `surefire` or `failsafe` to run your tests, the _maximum_
version that works is `2.22.2`. Have a read of [SUREFIRE-1724](https://issues.apache.org/jira/browse/SUREFIRE-1724)
to understand why you can't use Surefire `3.0.0-M1` and above (yet).

### 2. Test Runner classes

Back in Junit4 land, you'd create a test runner class that looks something
like this:

```java
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", plugin = "pretty")
public class RunCucumberTests {
}
```

You'd use the `@CucumberOption`'s `features` parameter to inform cucumber
where to look for your feature files; and, so long as you set that parameter,
the location of the test runner class itself was irrelevant - it could be
in any package.

In JUnit5 land that's changed. The concept of a test runner class has been
watered down somewhat. Here's what one looks like now:

```java
package my.features;

import io.cucumber.junit.platform.engine.Cucumber;

@Cucumber
public class RunCucumberTests {
}
```

There is no facility (yet) to instruct cucumber where to look for feature
files. The feature files must be in a directory that matches the test runner
class' package.

E.g. if the test runner class' package is `my.features` (as it is in the example
above), then your feature files must be located in `src/test/resources/my/features`.
If they're not in a directory structure matching the test runner's package,
cucumber won't know about them.

### 3. Cucumber options

Back in JUnit4 land you'd configure the test runner with the `@CucumberOptions`
annotation. These options must now come from a `junit-platform.properties` file
found in your resources (i.e. `src/test/resources`), or passed through surefire's
configuration in the `pom.xml`. E.g.

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-surefire-plugin</artifactId>
  <version>${maven.surefire.plugin.version}</version>
  <configuration>
    <properties>
      <configurationParameters>
        cucumber.execution.parallel.enabled = true
        cucumber.plugin = pretty, json:target/results.json
      </configurationParameters>
    </properties>
  </configuration>
</plugin>
```

The full list of cucumber options you can supply is documented [here](https://github.com/cucumber/cucumber-jvm/tree/master/junit-platform-engine#configuration-options).

