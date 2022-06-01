description = "Represents a convenience module which merely bundles dependencies."

val niokVersion: String by rootProject.extra

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(prefixedProject("verbs"))
                api(prefixedProject("api-infix-en_GB"))
                api(prefixedProject("translations-en_GB"))
            }
        }
    }
}

val jacocoAdditional by extra(
    listOf(
        prefixedProject("verbs"),
        prefixedProject("translations-en_GB"),
        prefixedProject("logic"),
        prefixedProject("core")
    )
)