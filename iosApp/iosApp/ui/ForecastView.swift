//
//  ContentView.swift
//  iosApp
//
//  Created by David on 23.11.2022..
//

import SwiftUI
import shared

struct ForecastView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    var body: some View {
        VStack {
            let state = self.viewModel.state
            
            if state.isLoading {
                Text("Loading...")
            }
            
            if let current = self.viewModel.state.forecast?.current as? CurrentUi {
                Text(current.place)
                Text(current.date)
                Text(current.temperature)
                HStack {
                    Text(current.description)
                    Image(current.iconKey).resizable().frame(width: 32, height: 32)
                    Text(current.precipitation)
                }
                HStack {
                    Text(current.wind)
                    Text(current.feelsLike)
                }
            }
            
            if let today = self.viewModel.state.forecast?.today as? TodayUi {
                HStack {
                    Text(NSLocalizedString("hourly", comment: "Hourly header label"))
                    Text(today.lowHighTemperature)
                }
                ForEach(today.hourly) { hour in
                    HStack {
                        Text(hour.time)
                        Text(hour.description)
                        Text(hour.precipitation)
                        Text(hour.temperature)
                        Image(hour.weatherIconKey).resizable().frame(width: 32, height: 32)
                    }
                }
            }
        }
        .frame(
            maxWidth: .infinity,
            maxHeight: .infinity,
            alignment: .topLeading
        )
        .padding()
    }
}
