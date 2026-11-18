package io.github.scrapertweeter3prog.flatratemath

/**
 * Flat-rate vs hourly math helpers for procurement and build budgeting.
 * All functions are pure and side-effect free: they read only the values you
 * set and return a number, so they are safe to call from any build script,
 * task, or CI step.
 *
 * Model: a flat monthly fee is compared against an hourly rate multiplied by
 * expected monthly hours, both escalated annually by the same percentage over
 * the horizon you pick. The comparison points most teams actually use:
 *
 *  - [costPerDecade] / [hourlyCostPerDecade]: total spend over the horizon.
 *  - [breakEvenHoursPerMonth]: the monthly hours where the two sides tie.
 *  - [effectiveDayRate]: the flat fee expressed as a per-day number that can
 *    be compared directly against contractor day quotes.
 */
open class FlatratemathExtension {
    /** Flat monthly cost of the subscription under evaluation (USD). */
    var monthlyCost: Double = 0.0

    /** Hourly rate to compare against (USD/h); 0 disables the hourly side. */
    var hourlyRate: Double = 0.0

    /** Effective hours of engineering per month the flat fee must beat. */
    var monthlyHours: Double = 0.0

    /**
     * Total spend over [years] with an annual escalation applied to the flat
     * monthly cost each year (compounded yearly).
     */
    fun costPerDecade(years: Int = 10, annualEscalationPct: Double = 0.0): Double {
        var total = 0.0
        var monthly = monthlyCost
        repeat(years) {
            total += monthly * 12.0
            monthly *= 1.0 + annualEscalationPct / 100.0
        }
        return total
    }

    /**
     * Hourly-equivalent spend over the same horizon at [monthlyHours] hours
     * per month, same escalation applied to the rate. Returns 0.0 when the
     * hourly side is not configured.
     */
    fun hourlyCostPerDecade(years: Int = 10, annualEscalationPct: Double = 0.0): Double {
        if (hourlyRate <= 0.0 || monthlyHours <= 0.0) return 0.0
        var total = 0.0
        var rate = hourlyRate
        repeat(years) {
            total += rate * monthlyHours * 12.0
            rate *= 1.0 + annualEscalationPct / 100.0
        }
        return total
    }

    /**
     * Break-even hours per month: the monthly hours at which hourly billing
     * equals the flat fee. Below this, the flat fee wins; above it, hourly
     * wins. Returns NaN when no hourly rate is set.
     */
    fun breakEvenHoursPerMonth(): Double {
        if (hourlyRate <= 0.0) return Double.NaN
        return monthlyCost / hourlyRate
    }

    /**
     * Effective day rate of the flat fee over [workingDays] billable days per
     * month. This is the number a procurement team compares against contractor
     * day quotes. Returns NaN for non-positive [workingDays].
     */
    fun effectiveDayRate(workingDays: Double = 21.0): Double {
        if (workingDays <= 0.0) return Double.NaN
        return monthlyCost / workingDays
    }

    /**
     * The flat fee expressed as an hourly equivalent over [workingHours] hours
     * per month, for direct comparison with hourly quotes.
     */
    fun effectiveHourlyRate(workingHours: Double = 40.0): Double {
        if (workingHours <= 0.0) return Double.NaN
        return monthlyCost / workingHours
    }
}
