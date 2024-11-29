# ConsoleCardGame

ConsoleCardGame is a JavaFX-based card game application. This document provides instructions on how to set up, build, and run the application.

---

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK)**
    - Download and install the latest JDK from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).
    - Ensure the `JAVA_HOME` environment variable is set, and the `java` and `javac` commands are accessible from the terminal.

2. **Apache Maven**
    - Download and install Maven from [Maven's official website](https://maven.apache.org/download.cgi).
    - Ensure Maven is correctly set up by running:
      ```bash
      mvn -version
      ```

3. **JavaFX SDK**
    - Download the JavaFX SDK matching your system from [OpenJFX](https://gluonhq.com/products/javafx/).
    - Extract the downloaded ZIP file to a location on your system.

---

## Project Setup

Follow these steps to set up the project:

### 1. Clone the Repository

```bash
git clone https://github.com/Ivan825/AnimeAttax.git
cd ConsoleCardGame
```

### Intellij setup:
1. Setup a javaFX project in intellij ,select com.example architecture.
2. Head to File>Project Structure>Libraries>add library> and add path to lib folder of your javafx sdk
3. Go to edit Run configuration of the main file in the java folder add VM options:
````bash
--module-path
/path-tojavafx-sdk/javafx-sdk-23/lib
--add-modules
javafx.controls,javafx.fxml,javafx.media
--add-opens
javafx.graphics/com.sun.glass.utils=ALL-UNNAMED
--add-opens
javafx.media/com.sun.media.jfxmediaimpl=ALL-UNNAMED
-Djava.library.path=src/main/java/com/example/consolecardgame/JNIFiles
````
4. Add a before launch build task in edit configuration itself.
5. Delete any module-info.java if  generated
6. Run the run configuration.
7. 