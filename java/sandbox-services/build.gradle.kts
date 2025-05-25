import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.kotlin.dsl.internal.sharedruntime.codegen.pluginEntriesFrom

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
    alias(libs.plugins.spotbugs)
}

val javaVersion: String by project
val spotbugsToolVersion: String by project
val mockitoAgent = configurations.create("mockitoAgent")

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
}

spotless {
    java {
        googleJavaFormat()
    }
}

spotbugs {
    toolVersion.set(spotbugsToolVersion)
    ignoreFailures.set(false)
    showProgress.set(true)
    effort.set(Effort.MAX)
    reportLevel.set(Confidence.LOW)

}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    mockitoAgent("org.mockito:mockito-core") {
        isTransitive = false
    }

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")

    runtimeOnly("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = (jvmArgs ?: listOf()) + "-javaagent:${mockitoAgent.asPath}"
}

tasks.withType<SpotBugsTask>().configureEach{
    reports.create("html"){
        required = true
    }
}
