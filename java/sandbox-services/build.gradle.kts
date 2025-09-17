import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.kotlin.dsl.internal.sharedruntime.codegen.pluginEntriesFrom

plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
    alias(libs.plugins.spotbugs)
}

val javaVersion: String by project
val spotbugsToolVersion: String by project
val jacocoToolVersion: String by project
val lombokVersion: String by project
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
jacoco {
    toolVersion = jacocoToolVersion
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

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testImplementation("ch.qos.logback:logback-classic")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
}

tasks.spotlessCheck{
    dependsOn(tasks.spotlessApply)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = (jvmArgs ?: listOf()) + "-javaagent:${mockitoAgent.asPath}"
    finalizedBy(tasks.jacocoTestReport,tasks.jacocoTestCoverageVerification)
}

tasks.withType<SpotBugsTask>().configureEach{
    reports.create("html"){
        required = true
    }
}

tasks.jacocoTestReport {

    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}