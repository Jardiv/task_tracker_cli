plugins {
    application
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.example.Main") // ← change this
}

//tasks.jar {
//    manifest {
//        attributes["Main-Class"] = "org.example.Main"
//    }

//    // Include dependencies in the JAR (fat JAR / uber JAR)
//    from({
//        configurations.runtimeClasspath.get().filter { it.exists() }.map {
//            if (it.isDirectory) it else zipTree(it)
//        }
//    })
//}