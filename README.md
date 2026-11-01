<h1 align="center">
  <img src="docs/title.png" alt="OpenPhobos" width="560">
</h1>

## Build and run

Gradle 9.4.1 comes with the wrapper and needs JDK 17 or newer to start. The project compiles with a Java 21 toolchain that Gradle downloads on its own, so no JDK path has to be configured. Network access to Maven Central and `maven.fabricmc.net` is required for Minecraft, Yarn, Fabric and the test dependencies.

```
./gradlew build       # build/libs/OpenPhobos-1.6.6.jar (remapped, for Fabric Loader), build/devlibs/OpenPhobos-1.6.6-dev.jar (Yarn names)
./gradlew test
./gradlew runClient   # starts the client with the working directory run/
```

On Windows use `gradlew.bat`. The bytecode target is Java 21 (`--release 21`).

IntelliJ IDEA: `File > Open` and select this directory, import it as a Gradle project and let Loom download and map Minecraft on the first import (slow). The **Minecraft Client** run configuration in `.idea/runConfigurations` starts the client through the `runClient` Gradle task; use it instead of launching the Knot main class directly, which fails when the project path contains non-ASCII characters.

[![QQ Group 1078719379](https://img.shields.io/badge/QQ%20Group-1078719379-12B7F5?logo=tencentqq&logoColor=white)](https://qm.qq.com/q/OnrCkjX7Y4)
