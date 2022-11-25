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
    }
    
    var body: some Scene {
        WindowGroup {
            ForecastView(viewModel: .init(getForecast: sdk.getForecast, selectPlace: sdk.selectPlace))
        }
    }
}
