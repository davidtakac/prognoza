# weather-icons

MET Norway provides light-mode weather icons [here](https://github.com/nrkno/yr-weather-symbols), 
but they're too dark to be used in dark mode. 

This issue is fixed here: 
- The `light` SVGs were modified into `dark` SVGs by manually changing the colors of the moon, 
rain, lightning and snow paths to be like the official yr.no application using Inkscape. The 
cloud and sun colors weren't changed as they seem to be legible enough in both modes. See 
below for the exact colors used
- Android conversion scripts (`android-light`, `android-dark`) generate ldpi, mdpi, hdpi, 
xhdpi, xxhdpi and xxxhdpi PNGs from the SVGs and import them into the correct Android drawable 
directories
- iOS conversion scripts (`ios-light`, `ios-dark`) generate 1x, 2x and 3x PNGs from the SVGs
and import them into the correct iOS imagesets.

## Instructions
On MacOS and Linux, install Inkscape and run the `convert` script. 

## Changed colors
- moon dark: #e9d9b1 (approx. from official yr.no)
- rain dark: #4fc3f7 (Material light blue 300)
- snow dark: #e1f5fe (Material light blue 50)
- lightning dark: #fedc15 (from official yr.no)