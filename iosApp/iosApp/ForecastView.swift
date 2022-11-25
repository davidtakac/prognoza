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
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text(self.viewModel.state.forecast?.current.temperature ?? "NA")
        }
        .padding()
    }
}

extension ForecastView {
    class ViewModel: ObservableObject {
        private(set) var getForecast: GetForecast
        private(set) var selectPlace: SelectPlace
        
        @Published var state: ForecastState = ForecastState()
        
        init(getForecast: GetForecast, selectPlace: SelectPlace) {
            self.getForecast = getForecast
            self.selectPlace = selectPlace
            
            self.state.isLoading = true
            
            let place = Place(
                name: "Osijek",
                details: "osjecko baranjska",
                latitude: 45.0,
                longitude: 18.0
            )
            
            self.selectPlace.invoke(place: place) { _ in
                DispatchQueue.main.async {
                    self.getForecast.invoke { result, error in
                        DispatchQueue.main.async {
                            var stateCopy = self.state
                            if let success = result as? GetForecastResultSuccess {
                                stateCopy.error = nil
                                stateCopy.forecast?.current = CurrentUi(temperature: getTemperature(
                                    temperature: success.forecast.current.temperature,
                                    unit: success.temperatureUnit)
                                )
                            } else {
                                stateCopy.error = "Error"
                            }
                            stateCopy.isLoading = false
                            self.state = stateCopy
                        }
                    }
                }
            }
        }
    }
}

struct ForecastState {
    var isLoading: Bool = false
    var error: String? = nil
    var forecast: ForecastUi? = nil
}

struct ForecastUi {
    var current: CurrentUi
}

struct CurrentUi {
    var temperature: String
}
