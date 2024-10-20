plugins {
    id("java")
    id("war")
    kotlin("jvm")
}

group = "org.vaskozlov.web2"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.servlet:jstl:1.2")
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("javax.servlet.jsp:javax.servlet.jsp-api:2.3.3")
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Exec>("compileTypeScript") {
    doFirst {
        file("src/main/webapp/resources/JS").mkdirs()
    }
    
    commandLine("npx", "tsc", "--project", "tsconfig.json")
}

tasks.war {
    dependsOn("compileTypeScript")
    from("src/main/webapp")
}

