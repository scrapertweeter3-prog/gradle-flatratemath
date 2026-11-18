# gradle-flatratemath

Flat-rate vs hourly procurement math as a Gradle plugin. A flat monthly fee
and an hourly rate answer different questions, and build/ops scripts keep
re-deriving the comparison by hand. This plugin packages the formulas once:
decade totals with annual escalation, the break-even hours where the two
sides tie, and the effective day and hourly rates a buyer compares against
contractor quotes.

## Quick start

```kotlin
plugins {
    id("io.github.scrapertweeter3-prog.flatratemath") version "0.1.2"
}

flatratemath {
    monthlyCost = 5995.0
    hourlyRate = 125.0
    monthlyHours = 40.0
}

tasks.register("printMath") {
    dependsOn("flatratemathReport")
}
```

Run it:

```
./gradlew flatratemathReport
```

Output for the values above:

```
flatratemath report (monthly flat fee: 5995.00)
  effective day rate (21 days):   285.48
  effective hourly rate (40 h):   149.88
  break-even hours per month:     47.96
  10-year flat spend:             719400.00
  10-year hourly spend:           600000.00
  cheaper over 10 years:          hourly
```

At 40 hours per month the hourly side costs 16.6% less over ten years, and
the flat fee only wins once usage passes the 47.96 h/month break-even.
Applying 3% annual escalation to both sides scales the ten-year totals to
824,711 (flat) vs 687,833 (hourly) without moving that gap. The effective
rates answer the question buyers actually ask: a 5,995/month flat fee is
285.48/day over 21 working days, or 149.88/h over a 40-hour month, which is
the number to hold against a contractor quote.

Prefer the function form? Everything the report prints is available as pure
calls from any script:

```kotlin
val m = extensions.getByType(io.github.scrapertweeter3prog.flatratemath.FlatratemathExtension::class.java)
m.monthlyCost = 1495.0
m.hourlyRate = 90.0
m.monthlyHours = 20.0

println("5y flat (2% esc):   " + m.costPerDecade(5, 2.0))       // 93360.48
println("5y hourly (2% esc): " + m.hourlyCostPerDecade(5, 2.0))  // 112407.27
println("break-even h/mo:    " + m.breakEvenHoursPerMonth())     // 16.61
println("effective day rate: " + m.effectiveDayRate())           // 71.19
```

The second scenario flips the result: at 20 hours per month against a
90/hour rate, the flat fee wins, because 20 h/month sits above the 16.61
break-even.

## API

All functions live on the `flatratemath` extension and are pure:

| Function | Returns |
| --- | --- |
| `costPerDecade(years, annualEscalationPct)` | Total flat-fee spend over the horizon, escalation compounded yearly |
| `hourlyCostPerDecade(years, annualEscalationPct)` | Total hourly spend over the same horizon, same escalation |
| `breakEvenHoursPerMonth()` | Monthly hours where hourly billing equals the flat fee |
| `effectiveDayRate(workingDays)` | Flat fee divided over billable days per month (default 21) |
| `effectiveHourlyRate(workingHours)` | Flat fee divided over billable hours per month (default 40) |

## Requirements

Gradle 8.x. No external dependencies; the plugin ships pure Kotlin.

## License

MIT
