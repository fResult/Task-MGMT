# In the root directory, run `docker build --tag app-task-mgmt .`

# FROM gradle:8-jdk21 AS build
# COPY --chown=gradle:gradle . /home/gradle/src
# WORKDIR /home/gradle/src
# RUN gradle build --no-daemon

# FROM eclipse-temurin:21-jammy

# WORKDIR /app
# EXPOSE 8088
# COPY --from=build /home/gradle/src/build/libs/task-mgmt-0.0.1.jar /app/
# RUN bash -c 'touch /app/task-mgmt-0.0.1.jar'
# ENTRYPOINT ["java", "-jar", "/app/task-mgmt-0.0.1.jar"]
#ENTRYPOINT [
#  "java",
#  "-XX:+UnlockExperimentalVMOptions",
#  "-XX:+UseCGroupMemoryLimitForHeap",
#  "-Djava.security.egd=file:/dev/./urandom",
#  "-jar",
#  "/app/task-mgmt-0.0.1.jar"
#]

#####

FROM eclipse-temurin:21-jammy
# Set the working directory inside the container
WORKDIR /app

# Copy only the necessary Gradle files to leverage caching
COPY build.gradle.kts settings.gradle.kts gradlew /app/
COPY gradle /app/gradle

# Copy the entire project (except files listed in .dockerignore)
COPY . /app

# Build the application
RUN gradle build --no-daemon

# Expose the port on which the application will run
EXPOSE 8088

# Run the application
CMD ["java", "-jar", "build/libs/task-mgmt-0.0.1.jar"]
