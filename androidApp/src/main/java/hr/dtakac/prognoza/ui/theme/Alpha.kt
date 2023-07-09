package hr.dtakac.prognoza.ui.theme

data class ContentAlpha(
  val high: Float,
  val medium: Float,
  val disabled: Float
) {
  companion object {
    fun get(): ContentAlpha = ContentAlpha(
      high = 0.87f,
      medium = 0.6f,
      disabled = 0.4f
    )
  }
}