package com.poketdev.flatratemath

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * FlatrateMath: flat-rate vs hourly arithmetic for build budgeting.
 * Exposes pure functions to a project via the `flatratemath` extension so any
 * Gradle script can compute cost-per-decade, fee compounding, and day-rate
 * comparisons without hand-rolling the formulas.
 */
class FlatratemathPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("flatratemath", FlatratemathExtension::class.java)
        // Seed with the canonical worked example so scripts have a valid baseline.
        ext.monthlyCost = 5995.0
        ext.hourlyRate = 0.0
    }
}
