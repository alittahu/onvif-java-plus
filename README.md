# onvif-java-plus

[![Latest Release](https://img.shields.io/github/v/release/alittahu/onvif-java-plus)](https://github.com/alittahu/onvif-java-plus/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

<p align="center">
  <img src="https://botw-pd.s3.amazonaws.com/styles/logo-thumbnail/s3/112012/onvif-converted.png?itok=yqR6_a6G" alt="ONVIF logo">
</p>

**onvif-java-plus** is a fork of the original [ONVIF-Java](https://github.com/RootSoft/ONVIF-Java) library, adapted for personal use and extended with features such as PTZ control, event handling, and replay.

---

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Using Maven Central](#using-maven-central)
    - [Using JitPack](#using-jitpack)
    - [Gradle Setup](#gradle-setup)
- [Quick Start](#quick-start)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## Features

- Discover and manage ONVIF devices
- Fetch media profiles and stream URIs
- PTZ (pan-tilt-zoom) continuous move and stop commands
- Pull-Point event subscriptions and message retrieval
- Replay and recording support
- Automatic video source configuration parsing (rotation, mirror, flip)

## Getting Started

### Using Maven Central

If you prefer to pull from Maven Central (when published, so its the original library with different interface), add:

```xml
<dependency>
  <groupId>be.teletask.onvif</groupId>
  <artifactId>onvif-java-plus</artifactId>
  <version>1.0.11</version>
</dependency>
```

### Using JitPack

Add JitPack as a repository and then include the dependency by Git tag or commit SHA.

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.alittahu</groupId>
  <artifactId>onvif-java-plus</artifactId>
  <version>v1.0.11</version> <!-- or master-SNAPSHOT -->
</dependency>
```

### Gradle Setup

#### Groovy DSL (`build.gradle`)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.alittahu:onvif-java-plus:v1.0.11'
}
```

#### Kotlin DSL (`build.gradle.kts`)

```kotlin
tools {
    java
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.alittahu:onvif-java-plus:v1.0.11")
}
```

## Quick Start

```java
import be.teletask.onvif.OnvifManager;
import be.teletask.onvif.models.OnvifDevice;

public class Example {
  public static void main(String[] args) throws Exception {
    OnvifDevice cam = new OnvifDevice(
      "192.168.1.100:80",
      "username",
      "password"
    );

    OnvifManager onvif = new OnvifManager();
    onvif.getServices(cam, (device, services) -> {
      // servicesPath, deviceInfoPath, etc. are now configured
      System.out.println("Services loaded: " + services.getServicesPath());
    });
  }
}
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/XYZ`)
3. Commit your changes (`git commit -m 'Add XYZ'`)
4. Push to the branch (`git push origin feature/XYZ`)
5. Open a Pull Request

Please ensure all tests pass and adhere to the existing code style.

