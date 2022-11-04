# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.2.1] - 2022-11-4
### Added
- This changelog :)

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