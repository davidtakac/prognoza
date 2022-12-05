# weather-icons

MET Norway provides light-mode weather icons [here](https://github.com/nrkno/yr-weather-symbols), 
but they're too dark to be used in dark mode. 

This issue is fixed here: 
- The `light` SVGs were modified into `dark` SVGs by manually changing the colors of the moon, 
rain, lightning and snow paths to be like the official yr.no application using Inkscape. The 
cloud and sun colors weren't changed as they seem to be legible enough in both modes. See 
`colors.txt` for the exact colors used
- The `convert` script generates ldpi, mdpi, hdpi, xhdpi, xxhdpi and xxxhdpi PNGs from the SVGs and 
imports them into the correct Android drawable directories

## Instructions
On MacOS or Linux, just install Inkscape and run the `convert` script. 