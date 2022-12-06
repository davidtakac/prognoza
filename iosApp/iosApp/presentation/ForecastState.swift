//
//  ForecastState.swift
//  iosApp
//
//  Created by David on 01.12.2022..
//

import Foundation

struct ForecastState {
    var isLoading: Bool = false
    var error: String? = nil
    var forecast: ForecastUi? = nil
}

struct ForecastUi {
    var current: CurrentUi
    var today: TodayUi?
}

struct CurrentUi {
    var place: String
    var date: String
    var temperature: String
    var description: String
    var wind: String
    var feelsLike: String
    var precipitation: String
    var iconKey: String
}

struct TodayUi {
    var lowHighTemperature: String
    var hourly: [DayHourUi]
}

struct DayHourUi : Identifiable {
    var id: String { time }
    var time: String
    var temperature: String
    var precipitation: String
    var description: String
    var weatherIconKey: String
}
