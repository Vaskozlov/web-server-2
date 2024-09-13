plugins {
    id("java")
    id("application")
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(files("src/main/resources/libs/fastcgi-lib.jar"))
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.example.Main")
    applicationDefaultJvmArgs = listOf("-DFCGI_PORT=24226")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}