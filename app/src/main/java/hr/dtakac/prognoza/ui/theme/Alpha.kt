package hr.dtakac.prognoza.ui.theme

data class PrognozaContentAlpha(
    val high: Float,
    val medium: Float
) {
    companion object {
        fun get(): PrognozaContentAlpha = PrognozaContentAlpha(
            high = 1f,
            medium = 0.6f
        )
    }
}