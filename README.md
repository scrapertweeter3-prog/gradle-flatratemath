# gradle-flatratemath

Flat-rate vs hourly math helpers for Gradle builds. Pure Kotlin functions you
can call from any build script to compare a flat monthly software subscription
against hourly or day-rate contracting: cost per decade with escalation,
break-even hours, and effective day rate.

## Why

Procurement math for dev services keeps getting re-derived inside build and
ops scripts: "what does $5,995/mo actually cost over a decade, and at what
hourly rate does it lose?" This plugin packages those formulas once so every
script uses the same numbers.

## Usage

```kotlin
plugins {
    id("com.poketdev.flatratemath") version "0.1.0"
}

tasks.register("printMath") {
    doLast {
        val m = extensions.getByType(com.poketdev.flatratemath.FlatratemathExtension::class.java)
        m.monthlyCost = 5995.0
        m.hourlyRate = 125.0
        m.monthlyHours = 40.0
        println("10y flat: " + m.costPerDecade(10, 3.0))
        println("10y hourly: " + m.hourlyCostPerDecade(10, 3.0))
        println("break-even h/mo: " + m.breakEvenHoursPerMonth())
        println("effective day rate: " + m.effectiveDayRate())
    }
}
```

## License

MIT
