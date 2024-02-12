import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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
    mavenLocal()

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

    implementation("com.jaoow:sql-provider:1.9.1")
    implementation("com.jaoow:connector:1.9.1")
    implementation("com.jaoow:executor:1.9.1")

    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")

    compileOnly("net.kyori:adventure-api:4.15.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.2")
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

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    mergeServiceFiles()

    dependencies {
        exclude("META-INF/**", "**/*.html", "module-info.*")
        exclude(dependency("org.slf4j:slf4j-api"))
        exclude(dependency("org.xerial:sqlite-jdbc"))
        exclude(dependency("mysql:mysql-connector-java"))
        exclude(dependency("com.mysql:mysql-connector-j"))
        exclude(dependency("com.google.protobuf:protobuf-java"))
        exclude(dependency("org.mariadb.jdbc:mariadb-java-client"))
        exclude(dependency("com.github.waffle:waffle-jna"))
        exclude(dependency("net.java.dev.jna:.*"))
    }

    relocate("com.zaxxer.hikari", "dev.arnaldo.mission.libs.hikari")
    relocate("br.com.blecaute.inventory", "dev.arnaldo.mission.libs.helper")
    relocate("com.github.benmanes.caffeine", "dev.arnaldo.mission.libs.caffeine")
    relocate("com.google.errorprone.annotations", "dev.arnaldo.mission.libs.errorprone")
    relocate("org.apache.commons.logging", "dev.arnaldo.mission.libs.apache")
    relocate("org.checkerframework", "dev.arnaldo.mission.libs.checkerframework")
    relocate("revxrsal.commands", "dev.arnaldo.mission.libs.lamp")

}