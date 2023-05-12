package hr.dtakac.prognoza.shared

import hr.dtakac.prognoza.shared.domain.*

interface PrognozaSdk {
    val getOverview: GetOverview

    val getAvailableLengthUnits: GetAvailableLengthUnits
    val getAvailablePressureUnits: GetAvailablePressureUnits
    val getAvailableTemperatureUnits: GetAvailableTemperatureUnits
    val getAvailableWindSpeedUnits: GetAvailableWindSpeedUnits

    val selectLengthUnit: SelectLengthUnit
    val selectPressureUnit: SelectPressureUnit
    val selectTemperatureUnit: SelectTemperatureUnit
    val selectWindSpeedUnit: SelectWindSpeedUnit

    val getSelectedLengthUnit: GetSelectedLengthUnit
    val getSelectedPressureUnit: GetSelectedPressureUnit
    val getSelectedTemperatureUnit: GetSelectedTemperatureUnit
    val getSelectedWindSpeedUnit: GetSelectedWindSpeedUnit

    val getSavedPlaces: GetSavedPlaces
    val getSelectedPlace: GetSelectedPlace
    val searchPlaces: SearchPlaces
    val selectPlace: SelectPlace
    val deleteSavedPlace: DeleteSavedPlace
}