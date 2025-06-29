plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "org.cyanlch.slackaiagent"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.slack.api:bolt:1.45.3")
    implementation("com.slack.api:bolt-ktor:1.45.3")
    implementation("com.slack.api:bolt-servlet:1.45.3")

    implementation("io.ktor:ktor-server-core:2.3.12")
    implementation("io.ktor:ktor-server-netty:2.3.12")
    implementation("io.ktor:ktor-server-websockets:2.3.12")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("com.cyanlch.slackaiagent.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}