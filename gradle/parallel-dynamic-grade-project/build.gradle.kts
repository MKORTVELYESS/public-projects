//call to achieve dynamic flagging of commands like
// ./gradlew sitePreparation -PsitePreparationFlags="sitePreparationComplete" -PlandSurveyFlags="-n 10 127.0.0.1 >nul" -PsiteClearanceFlags="-n 12 127.0.0.1>nul"

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.fasterxml.jackson.core:jackson-databind:2.18.0")
        classpath("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")
        classpath("org.jetbrains.kotlin:kotlin-script-runtime:2.0.20")
        classpath("org.jetbrains.kotlin:kotlin-scripting-jvm:2.0.20")
        classpath("org.jetbrains.kotlin:kotlin-scripting-jvm-host:2.0.20")
    }
}

fun executeShellCommand(command: String) {
    val isWindows = System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")

    val process = if (isWindows) {
        ProcessBuilder("cmd.exe", "/c", command)
    } else {
        ProcessBuilder("sh", "-c", command)
    }.redirectErrorStream(true).start()

    val result = process.inputStream.bufferedReader().use { it.readText() }
    println(result)
}


// Define a task to use jacksonObjectMapper within the task action

val jsonFile = file("tasks.json")

val mapper = jacksonObjectMapper()

val list: TaskList = mapper.readValue(jsonFile)
list.tasks.forEach { task ->
    tasks.register(task.name, FlaggedTask::class) {
        task.dependsOn.forEach { dependency ->
            dependsOn(dependency)
        }

        doLast {
            executeShellCommand("${task.action} ${flagsProvider.getOrElse("")}")
        }
    }
}

abstract class FlaggedTask : DefaultTask() {
    @Input // Shared property for all tasks to use, needs to marked as input https://docs.gradle.org/8.10/userguide/validation_problems.html#missing_annotation
    val flagsProvider: Provider<String> = project.providers.gradleProperty("${name}Flags")
}

data class Task(
        val name: String,
        val taskType: String,
        val dependsOn: List<String>,
        val action: String
)

data class TaskList(
        val tasks: List<Task>
)



