MarbledCat
============

####Rx Marble diagram in logcat.

MarbledCat uses Aspect-oriented programming techniques to inject logging code into RxJava method calls.
It allows developers to visualize Rx streams in Android logcat.

![screen record](https://raw.githubusercontent.com/prt2121/MarbledCat/master/assets/vid.gif)

####Install

* Add marbledcat-plugin in your build script dependencies.

  ```groovy
  buildscript {
    repositories {
      jcenter()
      mavenCentral() // Add mavenCentral
    }
    dependencies {
      classpath 'com.android.tools.build:gradle:1.3.0'
      classpath 'com.github.prt2121:marbledcat-plugin:0.4.0' // Add plugin
    }
  }
  ```

* In our app build.gradle, Apply the plugin and add marbledcat-annotation dependency.

  ```groovy
  apply plugin: 'com.android.application'
  apply plugin: 'com.prt2121.marbledcat' // Apply plugin

  dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    ...
    compile 'com.github.prt2121:marbledcat-annotation:0.4.0' // add marbledcat-annotation dependency
  }
  ```

* In the class that you need to log add RxLog annotation.

  ```java
  @RxLog // Add annotation
  public class AwesomeActivity extends AppCompatActivity {
  ```

![screenshot](https://raw.githubusercontent.com/prt2121/MarbledCat/master/assets/screenshot.png)

#### Thanks/Credits/Inspiration

* [rxvision](https://github.com/jaredly/rxvision)
* [Hugo](https://github.com/JakeWharton/hugo)
* [Frodo](https://github.com/android10/frodo)

[License](LICENSE)
