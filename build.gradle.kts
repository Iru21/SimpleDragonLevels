import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "me.iru"
val mcversion = "1.19"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:${mcversion}-R0.1-SNAPSHOT")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}