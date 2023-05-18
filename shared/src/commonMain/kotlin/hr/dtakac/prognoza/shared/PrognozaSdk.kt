package hr.dtakac.prognoza.shared

import hr.dtakac.prognoza.shared.usecase.*

interface PrognozaSdk {
    val getOverview: GetOverview
    val getAllMeasurementSystems: GetAllMeasurementSystems
    val getSelectedMeasurementSystem: GetSelectedMeasurementSystem
    val getSavedPlaces: GetSavedPlaces
    val getSelectedPlace: GetSelectedPlace
    val searchPlaces: SearchPlaces
    val selectPlace: SelectPlace
    val deleteSavedPlace: DeleteSavedPlace
}