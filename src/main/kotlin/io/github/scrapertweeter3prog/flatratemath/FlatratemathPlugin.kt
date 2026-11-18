package io.github.scrapertweeter3prog.flatratemath

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * FlatrateMath: flat-rate vs hourly arithmetic for build budgeting.
 *
 * Registers the `flatratemath` extension plus a `flatratemathReport` task.
 * Set values on the extension in any build script, then run the report task
 * to print the decade totals, break-even point, and effective day/hourly
 * rates for the configured scenario.
 */
class FlatratemathPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("flatratemath", FlatratemathExtension::class.java)

        project.tasks.register("flatratemathReport") {
            group = "help"
            description = "Prints the flat-rate vs hourly comparison for the flatratemath extension values."
            doLast {
                if (ext.monthlyCost <= 0.0) {
                    println("flatratemath: set flatratemath { monthlyCost = ... } first.")
                    return@doLast
                }
                println("flatratemath report (monthly flat fee: %.2f)".format(ext.monthlyCost))
                println("  effective day rate (21 days):   %.2f".format(ext.effectiveDayRate()))
                println("  effective hourly rate (40 h):   %.2f".format(ext.effectiveHourlyRate()))
                if (ext.hourlyRate > 0.0) {
                    println("  break-even hours per month:     %.2f".format(ext.breakEvenHoursPerMonth()))
                    val flat = ext.costPerDecade()
                    val hourly = ext.hourlyCostPerDecade()
                    if (hourly > 0.0) {
                        val winner = if (flat <= hourly) "flat rate" else "hourly"
                        println("  10-year flat spend:             %.2f".format(flat))
                        println("  10-year hourly spend:           %.2f".format(hourly))
                        println("  cheaper over 10 years:          %s".format(winner))
                    }
                }
            }
        }
    }
}
