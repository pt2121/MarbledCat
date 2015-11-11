MarbledCat
============

Rx Marble diagram in your logcat.

###Install
* Add marbledcat-plugin in your build script dependencies.
  ```
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
  ```
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
  ```
  @RxLog // Add annotation
  public class AwesomeActivity extends AppCompatActivity {
  ```
