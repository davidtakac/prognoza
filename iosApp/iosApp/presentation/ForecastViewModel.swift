//
//  ForecastViewModel.swift
//  iosApp
//
//  Created by David on 01.12.2022..
//

import Foundation
import shared

extension ForecastView {
    class ViewModel: ObservableObject {
        private(set) var mapper: ForecastUiMapper
        private(set) var getForecast: GetForecast
        
        @Published var state: ForecastState = ForecastState()
        
        init(
            forecastUiMapper: ForecastUiMapper,
            getForecast: GetForecast
        ) {
            self.mapper = forecastUiMapper
            self.getForecast = getForecast
            
            var stateCopy = self.state
            stateCopy.isLoading = true
            self.state = stateCopy
            
            self.getForecast.invoke { result, error in
                DispatchQueue.main.async {
                    if let success = result as? GetForecastResultSuccess {
                        stateCopy.error = nil
                        stateCopy.forecast = self.mapper.mapToForecastUi(
                            placeName: success.placeName,
                            current: success.forecast.current,
                            today: success.forecast.today,
                            temperatureUnit: success.temperatureUnit,
                            windUnit: success.windUnit,
                            precipitationUnit: success.precipitationUnit
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
