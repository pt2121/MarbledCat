MarbledCat
============

####Rx Marble diagram in logcat.

[![Build Status](https://travis-ci.org/prt2121/MarbledCat.svg?branch=master)](https://travis-ci.org/prt2121/MarbledCat)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.prt2121/marbledcat-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.prt2121/marbledcat-plugin)

MarbledCat allows developers to visualize Rx streams in Android logcat by applying Aspect-oriented programming techniques to inject logging code into RxJava method calls.

![screenshot](https://raw.githubusercontent.com/prt2121/MarbledCat/master/assets/screenshot.png)

![screen record](https://raw.githubusercontent.com/prt2121/MarbledCat/master/assets/vid.gif)

####Install

* Add marbledcat-plugin in your build script dependencies:

  ```groovy
  buildscript {
    repositories {
      jcenter()
      mavenCentral() // Add mavenCentral
    }
    dependencies {
      classpath 'com.android.tools.build:gradle:1.3.0'
      classpath 'com.github.prt2121:marbledcat-plugin:0.4.1' // Add plugin
    }
  }
  ```

* In our app build.gradle, apply the plugin and add marbledcat-annotation dependency:

  ```groovy
  apply plugin: 'com.android.application'
  apply plugin: 'com.prt2121.marbledcat' // Apply plugin

  dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    ...
    compile 'com.github.prt2121:marbledcat-annotation:0.4.1' // add marbledcat-annotation dependency
  }
  ```

* In the class that you need to log, add RxLog annotation:

  ```java
  @RxLog // Add annotation
  public class AwesomeActivity extends AppCompatActivity {
  ```

#### [Release page](https://github.com/prt2121/MarbledCat/releases)

#### Limitation
* The log printed correctly only when one class is annotated.

#### Thanks/Credits/Inspiration

* [rxvision](https://github.com/jaredly/rxvision)
* [Hugo](https://github.com/JakeWharton/hugo)
* [Frodo](https://github.com/android10/frodo)

[License](LICENSE)
