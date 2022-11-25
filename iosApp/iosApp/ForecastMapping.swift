//
//  ForecastMapping.swift
//  iosApp
//
//  Created by David on 23.11.2022..
//

import Foundation
import shared

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

func getTemperature(
    temperature: Temperature,
    unit: TemperatureUnit
) -> String {
    let temperatureValue: Double
    
    switch unit {
    case TemperatureUnit.degreeCelsius:
        temperatureValue = temperature.celsius
    case TemperatureUnit.degreeFahrenheit:
        temperatureValue = temperature.fahrenheit
    default:
        temperatureValue = 0.0
    }
    
    return String(
        format: NSLocalizedString("template_temperature", comment: "Temperature degrees template"),
        arguments: [formatNumber(number: temperatureValue, decimalPlaces: 0)]
    )
}
