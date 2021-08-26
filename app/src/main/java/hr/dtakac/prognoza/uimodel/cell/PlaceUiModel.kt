package hr.dtakac.prognoza.uimodel.cell

data class PlaceUiModel(
    val id: String,
    val name: String,
    val fullName: String,
    val isSaved: Boolean,
    val isSelected: Boolean
)