plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.scrapertweeter3"
version = "0.1.1"

gradlePlugin {
    website = "https://poketdev.com"
    vcsUrl = "https://github.com/scrapertweeter3-prog/gradle-flatratemath"
    plugins {
        create("flatratemath") {
            id = "io.github.scrapertweeter3.flatratemath"
            implementationClass = "io.github.scrapertweeter3.flatratemath.FlatratemathPlugin"
            displayName = "FlatrateMath"
            description = "Flat-rate vs hourly math for build budgeting: cost-per-decade, fee compounding, and day-rate comparison functions usable from any Gradle script."
            tags = listOf("math", "cost", "budgeting", "utilities")
        }
    }
}

repositories {
    mavenCentral()
}
