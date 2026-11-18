plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.scrapertweeter3-prog"
version = "0.1.2"

gradlePlugin {
    website = "https://poketdev.com"
    vcsUrl = "https://github.com/scrapertweeter3-prog/gradle-flatratemath"
    plugins {
        create("flatratemath") {
            id = "io.github.scrapertweeter3-prog.flatratemath"
            implementationClass = "io.github.scrapertweeter3prog.flatratemath.FlatratemathPlugin"
            displayName = "FlatrateMath"
            description = "Flat-rate vs hourly procurement math for any Gradle build: decade totals with escalation, break-even hours, effective day and hourly rates, and a ready-made flatratemathReport task."
            tags = listOf("math", "cost", "budgeting", "procurement", "utilities")
        }
    }
}

repositories {
    mavenCentral()
}
