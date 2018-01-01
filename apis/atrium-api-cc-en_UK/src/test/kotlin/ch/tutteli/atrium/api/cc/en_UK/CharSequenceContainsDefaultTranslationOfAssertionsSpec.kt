package ch.tutteli.atrium.api.cc.en_UK

import ch.tutteli.atrium.AssertionVerbFactory
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.reporting.translating.Translatable

class CharSequenceContainsDefaultTranslationOfAssertionsSpec : ch.tutteli.atrium.spec.assertions.CharSequenceContainsDefaultTranslationAssertionSpec(
    AssertionVerbFactory,
    getNameContainsDefaultTranslationOf(),
    getAtLeastTriple(),
    getAtMostTriple(),
    getAtMostIgnoringCaseTriple()
) {

    companion object : CharSequenceContainsSpecBase() {

        private fun getNameContainsDefaultTranslationOf() = "$contains with search mode $defaultTranslationOf"

        private fun getAtLeastTriple() = Triple(
            "$contains.$atLeast.$defaultTranslationOf",
            { what: String, times: String -> "$contains $what $atLeast $times" },
            Companion::containsAtLeast
        )

        private fun containsAtLeast(plant: AssertionPlant<CharSequence>, atLeast: Int, a: Translatable, aX: Array<out Translatable>)
            = plant.contains.atLeast(atLeast).defaultTranslationOf(a, *aX)

        private fun getAtMostTriple() = Triple(
            "$contains.$atMost.$defaultTranslationOf",
            { what: String, times: String -> "$contains $what $atMost $times" },
            Companion::containsAtMost
        )

        private fun containsAtMost(plant: AssertionPlant<CharSequence>, atMost: Int, a: Translatable, aX: Array<out Translatable>)
            = plant.contains.atMost(atMost).defaultTranslationOf(a, *aX)

        private fun getAtMostIgnoringCaseTriple() = Triple(
            "$contains.$ignoringCase.$atMost.$defaultTranslationOf",
            { what: String, times: String -> "$contains $ignoringCase $what $atMost $times" },
            Companion::containsAtMostIgnoringCase
        )

        private fun containsAtMostIgnoringCase(plant: AssertionPlant<CharSequence>, atMost: Int, a: Translatable, aX: Array<out Translatable>)
            = plant.contains.ignoringCase.atMost(atMost).defaultTranslationOf(a, *aX)
    }
}