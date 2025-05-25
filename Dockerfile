# Stage 1: Build application using JDK
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /src/wallet
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar

# Stage 2: Create runtime environment using JRE
FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

# Define user and group
ARG USER_NAME=wallet
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

# Create user and group
RUN addgroup -g ${USER_GID} ${USER_NAME} \
  && adduser -h /opt/wallet -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

# Switch to non-root user
USER ${USER_NAME}
WORKDIR /opt/wallet

# Copy built JAR from builder stage
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/wallet/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run application
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]