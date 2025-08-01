Matter alpha bot

A simple telegram bot that fetches the latest news from the https://www.matteralpha.com/ and sends it to a Telegram channel https://t.me/matteralpha

---
The bot is designed to run on a Raspberry Pi or any other low-resource device,
making it an ideal solution for those who want to keep up with the latest news without consuming too much power or resources.

Stack:
- kotlin
- spring boot (web)
- graalvm native image

# How to

# Prerequisites

1. Copy `src/main/resources/application-secrets.properties_example` to `src/main/resources/application-secrets.properties` and fill in the required values
2. Download graalvm 21+ and set it as your JAVA_HOME

## How to run locally

```bash
./gradlew bootRun
./gradlew nativeRun
```

## How to nativeCompile and build the docker image

```bash
docker buildx build --platform linux/arm64 -t bot_matteralpha . # nativeCompile & build the docker image
docker run --rm -v $(pwd)/data:/app/data -e DATA_FOLDER=/app/data bot_matteralpha:latest # to run the container with SQLite volume
```

## How to push the docker image to the raspberry pi

```bash
docker save bot_matteralpha:latest | ssh -C raspberrypi.local sudo docker load
ssh raspberrypi.local 'mkdir -p ~/bot_matteralpha_data && sudo docker run --rm -v ~/bot_matteralpha_data:/app/data -e DATA_FOLDER=/app/data bot_matteralpha:latest' # to run the container on the raspberry pi with persistent storage
```
