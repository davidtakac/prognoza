package hr.dtakac.prognoza.shared.entity

class Visibility(val distance: Length) {
    // From ChatGPT 3.5 for the prompt
    // "Can you map meteorological visibility in kilometres to qualitative measurements?"
    val description: VisibilityDescription = when {
        distance.kilometres >= 10 -> VisibilityDescription.Excellent
        distance.kilometres >= 5 -> VisibilityDescription.VeryGood
        distance.kilometres >= 3 -> VisibilityDescription.Good
        distance.kilometres >= 1 -> VisibilityDescription.Moderate
        distance.metres >= 500 -> VisibilityDescription.Poor
        distance.metres >= 200 -> VisibilityDescription.VeryPoor
        else -> VisibilityDescription.ExtremelyPoor
    }
}

enum class VisibilityDescription {
    Excellent,
    VeryGood,
    Good,
    Moderate,
    Poor,
    VeryPoor,
    ExtremelyPoor
}