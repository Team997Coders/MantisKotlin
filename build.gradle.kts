import edu.wpi.first.deployutils.deploy.artifact.FileTreeArtifact
import edu.wpi.first.gradlerio.GradleRIOPlugin
import edu.wpi.first.gradlerio.deploy.roborio.FRCJavaArtifact
import edu.wpi.first.gradlerio.deploy.roborio.RoboRIO
import edu.wpi.first.toolchain.NativePlatforms
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.8.10"
    id("edu.wpi.first.GradleRIO") version "2023.4.2"
}

group = "org.chsrobotics.robot"
version = "2023"
val mainClass = "org.chsrobotics.robot.MainKt"

val includeDesktopSupport = true

JavaVersion.VERSION_17.let {javaVersion ->
    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.majorVersion
        }
    }
}

// Define targets (RoboRIO) and artifacts (deployable files)
deploy {
    targets {
        register<RoboRIO>("roborio") {
            team = project.frc.teamNumber
            debug.set(project.frc.getDebugOrDefault(false))

            directory = "/home/lvuser/deploy"
            this.artifacts {

                register<FRCJavaArtifact>("frcJava") {
                    dependsOn(tasks.jar.get())
                    setJarTask(tasks.jar.get())
                }

                register<FileTreeArtifact>("frcStaticFileDeploy") {
                    files(project.fileTree("src/main/resources"))
                }
            }
        }
    }
}

wpi {
    // Simulation configuration (e.g. environment variables).
    with(sim) {
        addGui().defaultEnabled.set(true)
        addDriverstation()
    }

    with(java) {
        // Configure jar and deploy tasks
        configureExecutableTasks(tasks.jar.get())
        configureTestTasks(tasks.test.get())
        // Set to true to use debug for JNI.
        debugJni.set(false)
    }
}

dependencies {
    // Kotlin Standard Library
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    // Additional Libraries
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("com.fazecast:jSerialComm:2.9.3")

    // WPILib and Vendors
    with(wpi.java) {
        deps.wpilib().forEach { implementation(it.get()) }
        vendor.java().forEach { implementation(it.get()) }

        deps.wpilibJniDebug(NativePlatforms.roborio).forEach { "roborioDebug"(it) }
        vendor.jniDebug(NativePlatforms.roborio).forEach { "roborioDebug"(it) }

        deps.wpilibJniRelease(NativePlatforms.roborio).forEach { "roborioRelease"(it) }
        vendor.jniRelease(NativePlatforms.roborio).forEach { "roborioRelease"(it) }

        deps.wpilibJniDebug(NativePlatforms.desktop).forEach { nativeDebug(it) }
        vendor.jniDebug(NativePlatforms.desktop).forEach { nativeDebug(it) }

        deps.wpilibJniRelease(NativePlatforms.desktop).forEach { nativeRelease(it) }
        vendor.jniRelease(NativePlatforms.desktop).forEach { nativeRelease(it) }
    }
    // Simulation
    wpi.sim.enableRelease().forEach { simulationRelease(it) }
    wpi.sim.enableDebug().forEach { simulationDebug(it) }
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    jar {
        group = "build"
        description = """
            Setting up my Jar File. In this case, adding all libraries into the main jar ('fat jar')
            in order to make them all available at runtime. Also adding the manifest so WPILib
            knows where to look for our Robot Class.
        """.trimIndent()
        dependsOn(configurations.runtimeClasspath)
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        manifest {
            attributes["Main-Class"] = mainClass
        }
        doFirst {
            from(
                configurations
                    .runtimeClasspath
                    .get()
                    .map { if (it.isDirectory) it else zipTree(it) }
            )
        }
    }
}
