//
//  ForecastUiMapper.swift
//  iosApp
//
//  Created by David on 01.12.2022..
//

import Foundation
import shared

class ForecastUiMapper {
    func mapToForecastUi(
        placeName: String,
        current: Current,
        today: Today?,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ) -> ForecastUi {
        var todayUi: TodayUi? = nil
        if let today = today {
            todayUi = self.getToday(
                today: today,
                temperatureUnit: temperatureUnit,
                precipitationUnit: precipitationUnit
            )
        }
        
        return ForecastUi(
            current: self.getCurrent(
                placeName: placeName,
                current: current,
                temperatureUnit: temperatureUnit,
                windUnit: windUnit,
                precipitationUnit: precipitationUnit
            ),
            today: todayUi
        )
    }
    
    private func getCurrent(
        placeName: String,
        current: Current,
        temperatureUnit: TemperatureUnit,
        windUnit: SpeedUnit,
        precipitationUnit: LengthUnit
    ) -> CurrentUi {
        return CurrentUi(
            place: placeName,
            date: getDate(epochMillis: current.epochMillis),
            temperature: getTemperature(temperature: current.temperature, unit: temperatureUnit),
            description: getDescription(description: current.description_),
            wind: getWind(wind: current.wind, unit: windUnit),
            feelsLike: getFeelsLike(feelsLike: current.feelsLike, unit: temperatureUnit),
            precipitation: getPrecipitation(precipitation: current.precipitation, unit: precipitationUnit),
            iconKey: getWeatherIconKey(description: current.description_)
        )
    }
    
    private func getToday(
        today: Today,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ) -> TodayUi {
        return TodayUi(
            lowHighTemperature: getLowHighTemperature(
                low: today.lowTemperature,
                high: today.highTemperature,
                unit: temperatureUnit
            ),
            hourly: today.hourly.map {
                getDayHourUi(
                    datum: $0,
                    temperatureUnit: temperatureUnit,
                    precipitationUnit: precipitationUnit
                )
            }
        )
    }
    
    private func formatNumber(
        number: Double,
        decimalPlaces: Int
    ) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.maximumFractionDigits = decimalPlaces
        formatter.locale = Locale.current
        return formatter.string(from: NSNumber(value: number))!
    }

    private func getTemperature(
        temperature: Temperature,
        unit: TemperatureUnit
    ) -> String {
        let temperatureValue: Double
        
        switch unit {
        case TemperatureUnit.degreeFahrenheit:
            temperatureValue = temperature.fahrenheit
        default:
            temperatureValue = temperature.celsius
        }
        
        return String(
            format: NSLocalizedString("template_temperature_degrees", comment: "Air temperature"),
            arguments: [formatNumber(number: temperatureValue, decimalPlaces: 0)]
        )
    }

    private func getFeelsLike(
        feelsLike: Temperature,
        unit: TemperatureUnit
    ) -> String {
        return String(
            format: NSLocalizedString("template_feels_like", comment: "Feels like temperature"),
            arguments: [getTemperature(temperature: feelsLike, unit: unit)]
        )
    }
    
    private func getLowHighTemperature(
        low: Temperature,
        high: Temperature,
        unit: TemperatureUnit
    ) -> String {
        return String(
            format: NSLocalizedString("template_high_low_temperature", comment: "High and low temperature"),
            getTemperature(temperature: high, unit: unit),
            getTemperature(temperature: low, unit: unit)
        )
    }
    
    private func getDayHourUi(
        datum: HourlyDatum,
        temperatureUnit: TemperatureUnit,
        precipitationUnit: LengthUnit
    ) -> DayHourUi {
        return DayHourUi(
            time: getTime(epochMillis: datum.epochMillis),
            temperature: getTemperature(temperature: datum.temperature, unit: temperatureUnit),
            precipitation: getPrecipitation(precipitation: datum.precipitation, unit: precipitationUnit),
            description: getDescription(description: datum.description_),
            weatherIconKey: getWeatherIconKey(description: datum.description_)
        )
    }

    private func getPrecipitation(
        precipitation: Length,
        unit: LengthUnit
    ) -> String {
        let decimalPlaces = 1
        let key: String
        let value: Double
        switch unit {
        case .centimetre:
            key = "template_precipitation_cm"
            value = precipitation.centimetre
        case .inch:
            key = "template_precipitation_in"
            value = precipitation.inch
        default:
            key = "template_precipitation_mm"
            value = precipitation.millimetre
        }
        
        let valueDecimal = Decimal(string: String(format: "%.\(decimalPlaces)f", value))
        if valueDecimal == Decimal.zero {
            return ""
        } else {
            return String(
                format: NSLocalizedString(key, comment: "Precipitation amount"),
                arguments: [formatNumber(number: value, decimalPlaces: decimalPlaces)]
            )
        }
    }

    private func getDate(
        epochMillis: Int64
    ) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .long
        dateFormatter.timeStyle = .none
        dateFormatter.locale = Locale.current
        return dateFormatter.string(from: Date(timeIntervalSince1970: TimeInterval(epochMillis) / 1000))
    }
    
    private func getTime(
        epochMillis: Int64
    ) -> String {
        let timeFormatter = DateFormatter()
        timeFormatter.dateStyle = .none
        timeFormatter.timeStyle = .short
        timeFormatter.locale = Locale.current
        return timeFormatter.string(from: Date(timeIntervalSince1970: TimeInterval(epochMillis) / 1000))
    }

    private func getDescription(
        description: Description
    ) -> String {
        let key: String
        switch description {
        case .clearSkyDay, .clearSkyNight, .clearSkyPolarTwilight:
            key = "description_clear_sky"
        case .cloudy:
            key = "description_cloudy"
        case .fairDay, .fairNight, .fairPolarTwilight:
            key = "description_fair"
        case .heavyRain:
            key = "description_heavy_rain"
        case .heavyRainShowersAndThunderDay, .heavyRainShowersAndThunderNight, .heavyRainShowersAndThunderPolarTwilight:
            key = "description_heavy_rain_showers_and_thunder"
        case .heavyRainShowersDay, .heavyRainShowersNight, .heavyRainShowersPolarTwilight:
            key = "description_heavy_rain_showers"
        case .heavySleetAndThunder:
            key = "description_heavy_sleet_and_thunder"
        case .heavySleet:
            key = "description_heavy_sleet"
        case .heavySleetShowersAndThunderDay, .heavySleetShowersAndThunderNight, .heavySleetShowersAndThunderPolarTwilight:
            key = "description_heavy_sleet_showers_and_thunder"
        case .heavySleetShowersDay, .heavySleetShowersNight, .heavySleetShowersPolarTwilight:
            key = "description_heavy_sleet_showers"
        case .heavySnowAndThunder:
            key = "description_heavy_snow_and_thunder"
        case .heavySnow:
            key = "description_heavy_snow"
        case .heavySnowShowersAndThunderDay, .heavySnowShowersAndThunderNight, .heavySnowShowersAndThunderPolarTwilight:
            key = "description_heavy_snow_showers_and_thunder"
        case .heavySnowShowersDay, .heavySnowShowersNight, .heavySnowShowersPolarTwilight:
            key = "description_heavy_snow_showers"
        case .lightRainAndThunder:
            key = "description_light_rain_and_thunder"
        case .lightRain:
            key = "description_light_rain"
        case .lightRainShowersAndThunderDay, .lightRainShowersAndThunderNight, .lightRainShowersAndThunderPolarTwilight:
            key = "description_light_rain_showers_and_thunder"
        case .lightRainShowersDay, .lightRainShowersNight, .lightRainShowersPolarTwilight:
            key = "description_light_rain_showers"
        case .lightSleetAndThunder:
            key = "description_light_sleet_and_thunder"
        case .lightSleet:
            key = "description_light_sleet"
        case .lightSleetShowersDay, .lightSleetShowersNight, .lightSleetShowersPolarTwilight:
            key = "description_light_sleet_showers"
        case .lightSnowAndThunder:
            key = "description_light_snow_and_thunder"
        case .lightSnow:
            key = "description_light_snow"
        case .lightSnowShowersDay, .lightSnowShowersNight, .lightSnowShowersPolarTwilight:
            key = "description_light_snow_showers"
        case .lightSleetShowersAndThunderDay, .lightSleetShowersAndThunderNight, .lightSleetShowersAndThunderPolarTwilight:
            key = "description_light_sleet_showers_and_thunder"
        case .lightSnowShowersAndThunderDay, .lightSnowShowersAndThunderNight, .lightSnowShowersAndThunderPolarTwilight:
            key = "description_light_snow_showers_and_thunder"
        case .partlyCloudyDay, .partlyCloudyNight, .partlyCloudyPolarTwilight:
            key = "description_partly_cloudy"
        case .rainAndThunder:
            key = "description_rain_and_thunder"
        case .rain:
            key = "description_rain"
        case .rainShowersAndThunderDay, .rainShowersAndThunderNight, .rainShowersAndThunderPolarTwilight:
            key = "description_rain_showers_and_thunder"
        case .rainShowersDay, .rainShowersNight, .rainShowersPolarTwilight:
            key = "description_rain_showers"
        case .sleetAndThunder:
            key = "description_sleet_and_thunder"
        case .sleet:
            key = "description_sleet"
        case .sleetShowersAndThunderDay, .sleetShowersAndThunderNight, .sleetShowersAndThunderPolarTwilight:
            key = "description_sleet_showers_and_thunder"
        case .sleetShowersDay, .sleetShowersNight, .sleetShowersPolarTwilight:
            key = "description_sleet_showers"
        case .snowAndThunder:
            key = "description_snow_and_thunder"
        case .snow:
            key = "description_snow"
        case .snowShowersAndThunderDay, .snowShowersAndThunderNight, .snowShowersAndThunderPolarTwilight:
            key = "description_snow_showers_and_thunder"
        case .snowShowersDay, .snowShowersNight, .snowShowersPolarTwilight:
            key = "description_snow_showers"
        default:
            key = "description_unknown"
        }
        return NSLocalizedString(key, comment: "Weather description")
    }

    private func getWindSpeed(
        windSpeed: Speed,
        unit: SpeedUnit
    ) -> String {
        let key: String
        let value: Double
        switch unit {
        case .kilometrePerHour:
            key = "template_wind_kmh"
            value = windSpeed.kilometrePerHour
        case .milePerHour:
            key = "template_wind_mph"
            value = windSpeed.milePerHour
        case .knot:
            key = "template_wind_knots"
            value = windSpeed.knot
        default:
            key = "template_wind_mps"
            value = windSpeed.metrePerSecond
        }
        return String(
            format: NSLocalizedString(key, comment: "Wind speed"),
            arguments: [formatNumber(number: value, decimalPlaces: 0)]
        )
    }

    private func getBeaufortString(
        beaufort: BeaufortScale
    ) -> String {
        let key: String
        switch beaufort {
        case .calm:
            key = "wind_calm"
        case .lightAir:
            key = "wind_light_air"
        case .lightBreeze:
            key = "wind_light_breeze"
        case .gentleBreeze:
            key = "wind_gentle_breeze"
        case .moderateBreeze:
            key = "wind_moderate_breeze"
        case .freshBreeze:
            key = "wind_fresh_breeze"
        case .strongBreeze:
            key = "wind_strong_breeze"
        case .nearGale:
            key = "wind_near_gale"
        case .gale:
            key = "wind_gale"
        case .severeGale:
            key = "wind_severe_gale"
        case .storm:
            key = "wind_storm"
        case .violentStorm:
            key = "wind_violent_storm"
        default:
            key = "wind_hurricane"
        }
        return NSLocalizedString(key, comment: "Wind speed description")
    }

    private func getWind(
        wind: Wind,
        unit: SpeedUnit
    ) -> String {
        return String(
            format: NSLocalizedString("template_wind", comment: "Wind description and speed"),
            arguments: [
                getBeaufortString(beaufort: wind.speed.beaufortScale),
                getWindSpeed(windSpeed: wind.speed, unit: unit)
            ]
        )
    }

    private func getWeatherIconKey(
        description: Description
    ) -> String {
        let key: String
        switch description {
        case .clearSkyDay: key = "01d";
        case .clearSkyNight: key = "01n";
        case .clearSkyPolarTwilight: key = "01m";
        case .cloudy: key = "04";
        case .fairDay: key = "02d";
        case .fairNight: key = "02n";
        case .fairPolarTwilight: key = "02m";
        case .fog: key = "15";
        case .heavyRainAndThunder: key = "11";
        case .heavyRain: key = "10";
        case .heavyRainShowersAndThunderDay: key = "25d";
        case .heavyRainShowersAndThunderNight: key = "25n";
        case .heavyRainShowersAndThunderPolarTwilight: key = "25m";
        case .heavyRainShowersDay: key = "41d";
        case .heavyRainShowersNight: key = "41n";
        case .heavyRainShowersPolarTwilight: key = "41m";
        case .heavySleetAndThunder: key = "32";
        case .heavySleet: key = "48";
        case .heavySleetShowersAndThunderDay: key = "27d";
        case .heavySleetShowersAndThunderNight: key = "27n";
        case .heavySleetShowersAndThunderPolarTwilight: key = "27m";
        case .heavySleetShowersDay: key = "43d";
        case .heavySleetShowersNight: key = "43n";
        case .heavySleetShowersPolarTwilight: key = "43m";
        case .heavySnowAndThunder: key = "34";
        case .heavySnow: key = "50";
        case .heavySnowShowersAndThunderDay: key = "29d";
        case .heavySnowShowersAndThunderNight: key = "29n";
        case .heavySnowShowersAndThunderPolarTwilight: key = "29m";
        case .heavySnowShowersDay: key = "45d";
        case .heavySnowShowersNight: key = "45n";
        case .heavySnowShowersPolarTwilight: key = "45m";
        case .lightRainAndThunder: key = "30";
        case .lightRain: key = "46";
        case .lightRainShowersAndThunderDay: key = "24d";
        case .lightRainShowersAndThunderNight: key = "24n";
        case .lightRainShowersAndThunderPolarTwilight: key = "24m";
        case .lightRainShowersDay: key = "40d";
        case .lightRainShowersNight: key = "40n";
        case .lightRainShowersPolarTwilight: key = "40m";
        case .lightSleetAndThunder: key = "31";
        case .lightSleet: key = "47";
        case .lightSleetShowersDay: key = "42d";
        case .lightSleetShowersNight: key = "42n";
        case .lightSleetShowersPolarTwilight: key = "42m";
        case .lightSnowAndThunder: key = "33";
        case .lightSnow: key = "49";
        case .lightSnowShowersDay: key = "44d";
        case .lightSnowShowersNight: key = "44n";
        case .lightSnowShowersPolarTwilight: key = "44m";
        case .lightSleetShowersAndThunderDay: key = "26d";
        case .lightSleetShowersAndThunderNight: key = "26n";
        case .lightSleetShowersAndThunderPolarTwilight: key = "26m";
        case .lightSnowShowersAndThunderDay: key = "28d";
        case .lightSnowShowersAndThunderNight: key = "28n";
        case .lightSnowShowersAndThunderPolarTwilight: key = "28m";
        case .partlyCloudyDay: key = "03d";
        case .partlyCloudyNight: key = "03n";
        case .partlyCloudyPolarTwilight: key = "03m";
        case .rainAndThunder: key = "11";
        case .rain: key = "09";
        case .rainShowersAndThunderDay: key = "06d";
        case .rainShowersAndThunderNight: key = "06n";
        case .rainShowersAndThunderPolarTwilight: key = "06m";
        case .rainShowersDay: key = "05d";
        case .rainShowersNight: key = "05n";
        case .rainShowersPolarTwilight: key = "05m";
        case .sleetAndThunder: key = "23";
        case .sleet: key = "12";
        case .sleetShowersAndThunderDay: key = "20d";
        case .sleetShowersAndThunderNight: key = "20n";
        case .sleetShowersAndThunderPolarTwilight: key = "20m";
        case .sleetShowersDay: key = "07d";
        case .sleetShowersNight: key = "07n";
        case .sleetShowersPolarTwilight: key = "07m";
        case .snowAndThunder: key = "14";
        case .snow: key = "13";
        case .snowShowersAndThunderDay: key = "21d";
        case .snowShowersAndThunderNight: key = "21n";
        case .snowShowersAndThunderPolarTwilight: key = "21m";
        case .snowShowersDay: key = "08d";
        case .snowShowersNight: key = "08n";
        case .snowShowersPolarTwilight: key = "08m";
        default: key = "questionmark.circle";
        }
        return key
    }
}
