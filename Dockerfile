FROM eclipse-temurin:21-jdk

WORKDIR /app

# Install JavaFX native dependencies (GTK, OpenGL, sound)
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libgl1 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libnss3 \
    libasound2t64 \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download JavaFX SDK for Linux (glibc)
RUN wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip -O javafx.zip \
    && unzip javafx.zip -d /opt \
    && rm javafx.zip

COPY target/*.jar app.jar

ENTRYPOINT ["java","--module-path=/opt/javafx-sdk-21.0.1/lib","--add-modules=javafx.controls,javafx.fxml","-jar", "app.jar"]