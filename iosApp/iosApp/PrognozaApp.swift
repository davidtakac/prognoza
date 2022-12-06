//
//  iosAppApp.swift
//  iosApp
//
//  Created by David on 23.11.2022..
//

import SwiftUI
import shared

@main
struct PrognozaApp: App {
    private(set) var sdk: PrognozaSdk
    
    init() {
        let userAgent = "Prognoza-iOS/0.0.1 github.com/davidtakac/prognoza developer.takac@gmail.com"
        sdk = IosPrognozaSdkFactory(userAgent: userAgent).create()
        
        // Uncomment this when reinstalling the app to initialize a selected place for testing
        // initPlace()
    }
    
    var body: some Scene {
        WindowGroup {
            ForecastView(
                viewModel: .init(
                    forecastUiMapper: ForecastUiMapper(),
                    getForecast: sdk.getForecast
                )
            )
        }
    }
    
    private func initPlace() {
        let place = Place(
            name: "Osijek",
            details: "osjecko baranjska",
            latitude: 45.0,
            longitude: 18.0
        )
        self.sdk.selectPlace.invoke(place: place) { _ in /*no-op*/ }
    }
}
