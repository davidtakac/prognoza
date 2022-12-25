package hr.dtakac.prognoza.shared

import hr.dtakac.prognoza.shared.domain.*

interface PrognozaSdk {
    val getAllPrecipitationUnits: GetAllPrecipitationUnits
    val getAllPressureUnits: GetAllPressureUnits
    val getAllTemperatureUnits: GetAllTemperatureUnits
    val getAllWindUnits: GetAllWindUnits
    val getForecast: GetForecast
    val getForecastFrugal: GetForecast
    val getPrecipitationUnit: GetPrecipitationUnit
    val getPressureUnit: GetPressureUnit
    val getSavedPlaces: GetSavedPlaces
    val getSelectedPlace: GetSelectedPlace
    val getTemperatureUnit: GetTemperatureUnit
    val getWindUnit: GetWindUnit
    val searchPlaces: SearchPlaces
    val selectPlace: SelectPlace
    val deleteSavedPlace: DeleteSavedPlace
    val setPrecipitationUnit: SetPrecipitationUnit
    val setPressureUnit: SetPressureUnit
    val setTemperatureUnit: SetTemperatureUnit
    val setWindUnit: SetWindUnit
    val setForecastProvider: SetForecastProvider
    val getForecastProvider: GetForecastProvider
    val getAllForecastProviders: GetAllForecastProviders
}