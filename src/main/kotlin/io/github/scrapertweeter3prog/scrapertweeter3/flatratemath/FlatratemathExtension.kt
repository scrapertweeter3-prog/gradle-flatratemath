package io.github.scrapertweeter3.flatratemath

/**
 * Flat-rate vs hourly math helpers. All functions are pure and side-effect free.
 */
open class FlatratemathExtension {
    /** Flat monthly cost of the subscription under evaluation (USD). */
    var monthlyCost: Double = 0.0

    /** Optional hourly rate to compare against (USD/h); 0 disables the comparison. */
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
     * per month, same escalation applied to the rate.
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

    /** Break-even hours per month: hours at which hourly billing equals the flat fee. */
    fun breakEvenHoursPerMonth(): Double {
        if (hourlyRate <= 0.0) return Double.NaN
        return monthlyCost / hourlyRate
    }

    /**
     * Effective day rate of the flat fee, given [workingDays] per month.
     * This is the number a procurement team compares against contractor quotes.
     */
    fun effectiveDayRate(workingDays: Double = 21.0): Double {
        if (workingDays <= 0.0) return Double.NaN
        return monthlyCost / workingDays
    }
}
