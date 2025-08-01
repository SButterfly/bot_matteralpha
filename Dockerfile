# Multistage Dockerfile for building and running the Bot application
# using multistage docker build ref: https://docs.docker.com/build/building/multi-stage/

# Base image for building the application
# syntax=docker/dockerfile:1.4
FROM --platform=$BUILDPLATFORM gradle:8.14.3-jdk21-graal-jammy AS builder

# Set the working directory
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

# Copy the Gradle config, source code, and static analysis config into the build container
COPY --chown=gradle:gradle build.gradle.kts gradle.properties settings.gradle.kts $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

# Build the application
RUN gradle nativeCompile --no-daemon

# Final image to run the application
FROM ubuntu:jammy
ENV DATA_FOLDER=/app/data
COPY --from=builder /usr/app/build/native/nativeCompile/bot_matteralpha /bot_matteralpha
CMD ["/bot_matteralpha"]
