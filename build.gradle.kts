plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "dev.arnaldo"
version = "1.0.0"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/nms/")
    maven("https://maven.pkg.github.com/srblecaute01/inventoryhelper")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    implementation("br.com.blecaute:inventory-helper:1.5.1")
    implementation("com.github.Revxrsal.Lamp:common:3.1.9")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.1.9")
    implementation("com.github.Jaoow:sql-provider:1.9.1")
    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")
}

bukkit {
    name = "SunriseColecao"
    description = "The collection plugin"
    website = "arnaldo.dev"
    authors = listOf("SrBlecaute")
    main = "${rootProject.group}.mission.Main"
    version = rootProject.version.toString()
    apiVersion = "1.13"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}