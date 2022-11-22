# Changelog
All notable changes to the Android app will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Backlog]
### Fixed
- Widget rounded corners on <= Android 11
- Widget text reacts to system dark/light theme on <= Android 11
### Added
- iOS support
- OpenWeatherMap as forecast provider

## [Unreleased]
### Changed
- Make app mood a business rule
- Allow external feels like and mood
- Measure pressure in millibar and inches of mercury
- Feels like calculated by wind chill
- Refactored to Kotlin Multiplatform Mobile
- Coming item expands to card
- Add dark mode icons
- Better weather icon
### Fixed
- Hourly and coming data cells wider than they need to be
- Current hour pairs too close together
- Inconsistent spacing between hour and coming data columns
- Forecast not scrolling in horizontal orientation
- Device default theme changes to one from settings on first start
- Wind speed not hiding when 0
- Feels like temperature always same as air temperature
- Long time format (like 2 Uhr PM in German)
### Added
- Unit conversion tests

## [2.2.2] - 2022-11-06
### Fixed
- Forecast not updating to show the current hour when offline

## [2.2.1] - 2022-11-5
### Added
- This changelog :)
### Changed
- Fixed forecast not updating after some time

## [2.2.0] - 2022-11-4
### Note
- Uninstall the app before installing this update. This release migrated to 
an entirely new way of storing data.
### Changed
- Main description and precipitation now have same text style
- Unify space-dot-space format of descriptive text with a value for all languages
- Refactor settings UI code to use a list of settings items
- Replace Retrofit with ktor-client
- Replace Room with SQLDelight
- Make all modules other than app pure Kotlin/JVM modules
- Fix temperatures being displayed as -0
- Fix precipitation being displayed as 0 in or 0 cm, it now gets omitted if 0
- When no precipitation, expand description fully
- Update libraries
- Round knots to nearest integer
- Reduce widget padding