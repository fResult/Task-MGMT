# In the root directory, run `docker build --tag app-task-mgmt .

FROM eclipse-temurin:21-jammy AS base
# Set the working directory inside the container
WORKDIR /app

# Copy only the necessary Gradle files to leverage caching
#COPY build.gradle.kts ./settings.gradle.kts ./gradlew /app/
COPY build.gradle.kts ./settings.gradle.kts ./gradlew /app/
COPY gradle /app/gradle

# Copy the Gradle Wrapper files
COPY gradlew /app/
COPY gradlew.bat /app/

# Copy the entire project (except files listed in .dockerignore)
#COPY .. /app
COPY . /app

# Build the application
RUN ./gradlew build --no-daemon

# Expose the port on which the application will run
EXPOSE 8088:8080

# Run the application
FROM base AS development
#CMD ["java", "-jar", "build/libs/task-mgmt-0.0.1.jar"]
CMD ["./gradlew", "bootRun", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]


#CMD [
#  "./gradlew", "bootRun", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"
#]

FROM base AS build
RUN ./gradlew build

FROM eclipse-temurin:21-jammy AS production
EXPOSE 8080:8080
#COPY --from=build /app/target/task-mgmt-*.jar /task-mgmt.jar
COPY --from=build /app/build/libs/task-mgmt-*.jar /task-mgmt.jar
#CMD ["java", "-jar", "build/libs/task-mgmt.jar"]
CMD ["java", "-jar", "/task-mgmt.jar"]
