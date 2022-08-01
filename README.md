# agent-java-testNG
A TestNG reporter that uploads the results to a adaklabs.

> **DISCLAIMER**: We use Google Analytics for sending anonymous usage information such as agent's and client's names, and their versions
> after a successful launch start. This information might help us to improve both adaklabs backend and client sides. It is used by the
> adaklabs team only and is not supposed for sharing with 3rd parties.

---
- [Objects interrelation TestNG - adaklabs](https://github.com/reportportal/agent-java-testNG#objects-interrelation-testng---reportportal)
- [Dependencies](https://github.com/reportportal/agent-java-testNG#dependencies)
- [Install listener](https://github.com/reportportal/agent-java-testNG#install-listener)
  - [Listener class](https://github.com/reportportal/agent-java-testNG#listener-class)
  - [Listener parameters](https://github.com/reportportal/agent-java-testNG/#listener-parameters)
  - [Maven Surefire plugin](https://github.com/reportportal/agent-java-testNG#maven-surefire-plugin)
  - [Specify listener in testng.xml](https://github.com/reportportal/agent-java-testNG#specify-listener-in-testngxml)
  - [Custom runner](https://github.com/reportportal/agent-java-testNG#custom-runner)
  - [Using command line](https://github.com/reportportal/agent-java-testNG#using-command-line)
  - [Using \@Listeners annotation](https://github.com/reportportal/agent-java-testNG#using-listeners-annotation)
  - [Using ServiceLoader](https://github.com/reportportal/agent-java-testNG#using-serviceloader)
- [Code example How to overload params in run-time](https://github.com/reportportal/agent-java-testNG#code-example-how-to-overload-params-in-run-time)
- [Example and step-by-step instruction with logback](https://github.com/reportportal/examples-java/tree/master/example-testng-logback)
- [Example and step-by-step instruction with Log4j](https://github.com/reportportal/examples-java/tree/master/example-testng-log4j)
---

**[TestNG](http://testng.org)** provides support for attaching custom listeners, reporters, annotation transformers and method interceptors to your tests.
Handling events

TestNG agent can handle next events:

-   Start launch
-   Finish launch
-   Start suite
-   Finish suite
-   Start test
-   Finish test
-   Start test step
-   Successful finish of test step
-   Fail of test step
-   Skip of test step
-   Start configuration (All «before» and «after» methods)
-   Fail of configuration
-   Successful finish of configuration
-   Skip configuration

## Objects interrelation TestNG - adaklabs

| **TestNG object**    | **adaklabs object**       |
|----------------------|-------------------------------|
| LAUNCH               |LAUNCH                         |
| BEFORE_SUITE         |TestItem (type = BEFORE_SUITE) |
| BEFORE_GROUPS        |TestItem (type = BEFORE_GROUPS)|
| SUITE                |TestItem (type = SUITE)        |
| BEFORE_TEST          |TestItem (type = BEFORE_TEST)  |
| TEST                 |TestItem (type = TEST)         |
| BEFORE_CLASS         |TestItem (type = BEFORE_CLASS) |
| CLASS                |Not in structure. Avoid it     |
| BEFORE_METHOD        |TestItem (type = BEFORE_METHOD)|
| METHOD               |TestItem (type = STEP)         |
| AFTER_METHOD         |TestItem (type = AFTER_METHOD) |
| AFTER_CLASS          |TestItem (type = AFTER_CLASS)  |
| AFTER_TEST           |TestItem (type = AFTER_TEST)   |
| AFTER_SUITE          |TestItem (type = AFTER_SUITE)  |
| AFTER_GROUPS         |TestItem (type = AFTER_GROUPS) |

TestItem – adaklabs specified object for representing:  suite, test, method objects in different test systems. Used as tree structure and can be recursively placed inside himself.

## Dependencies
> Minimum supported TestNG version: [7.1.0](https://search.maven.org/artifact/org.testng/testng/7.1.0/jar)


Add to `POM.xml`

**dependency**

```xml
<dependency>
  <groupId>com.epam.reportportal</groupId>
  <artifactId>agent-java-testng</artifactId>
  <version>5.1.2</version>
</dependency>
<!-- TODO Leave only one dependency, depends on what logger you use: -->
<dependency>
  <groupId>com.epam.reportportal</groupId>
  <artifactId>logger-java-logback</artifactId>
  <version>5.1.1</version>
</dependency>
<dependency>
  <groupId>com.epam.reportportal</groupId>
  <artifactId>logger-java-log4j</artifactId>
  <version>5.1.4</version>
</dependency>
```

## Install listener

Download package [here](<https://search.maven.org/search?q=g:%22com.epam.reportportal%22%20AND%20a:%22agent-java-testng%22>).
Choose latest version.

By default, TestNG attaches a few basic listeners to generate HTML and XML
reports. For reporting TestNG test events (ie start of test, successful finish
of test, test fail) to adaklabs user should add adaklabs TestNg
listener to run and configure input parameters. 

### Listener parameters

Which are common for all **JVM based** agents.

### Listener class:
`com.epam.reportportal.testng.ReportPortalTestNGListener`

There are several ways how to install listener:

- [Maven Surefire plugin](https://github.com/reportportal/agent-java-testNG#maven-surefire-plugin)
- [Specify listener in testng.xml](https://github.com/reportportal/agent-java-testNG#specify-listener-in-testngxml)
- [Custom runner](https://github.com/reportportal/agent-java-testNG#custom-runner)
- [Using command line](https://github.com/reportportal/agent-java-testNG#using-command-line)
- [Using \@Listeners annotation](https://github.com/reportportal/agent-java-testNG#using-listeners-annotation)
- [Using ServiceLoader](https://github.com/reportportal/agent-java-testNG#using-serviceloader)

> Please note, that listener must be configured in a single place only.
> Configuring multiple listeners will lead to incorrect application behavior.

### Maven Surefire plugin

Maven surefire plugin allows configuring multiple custom listeners. For logging
to Report Portal user should add Report Portal listener to “Listener” property.

```xml
<plugins>
    [...]
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.15</version>
        <configuration>
          <properties>
            <property>
              <name>usedefaultlisteners</name>
              <value>false</value> <!-- disabling default listeners is optional -->
            </property>
            <property>
              <name>listener</name>
              <value>com.epam.reportportal.testng.ReportPortalTestNGListener</value>
            </property>
          </properties>
        </configuration>
      </plugin>
    [...]
</plugins>
```

>   If you use maven surefire plugin set report portal listener only in
>   "listener" property in pom.xml.

### Specify listener in testng.xml

Here is how you can define report portal listener in your testng.xml file.

```xml
<suite>
   
  <listeners>
    <listener class-name="com.example.MyListener" />
    <listener class-name="com.epam.reportportal.testng.ReportPortalTestNGListener" />
  </listeners>
.....
