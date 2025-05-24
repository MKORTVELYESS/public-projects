plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

val javaVersion: String by project
val mockitoAgent = configurations.create("mockitoAgent")

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = (jvmArgs ?: listOf()) + "-javaagent:${mockitoAgent.asPath}"
}