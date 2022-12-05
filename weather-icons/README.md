# Icons

MET Norway provides weather symbols [here](https://github.com/nrkno/yr-weather-symbols), but there 
are a couple of issues if you wish to use them for mobile apps: 
- They are not compatible with Android vector drawables, which means you can't import them as-is
- They're too dark to be legible in dark mode

These issues are addressed here: 
- From the default symbols (located in `light`) I generated dark symbols (located in `dark`) by 
manually changing the colors of the moon, rain, lightning and snow parts to be the same as the 
actual yr.no application. I used Inkscape for this. I haven't modified the cloud and sun colors 
as they seem to be legible enough in both modes
- Each dir contains a `convert` script which generates ldpi, mdpi, hdpi, xhdpi, xxhdpi and xxxhdpi 
PNGs from the provided SVGs and directly moves them to the Android project

## Instructions
On MacOS or Linux, just install Inkscape and run the `convert` scripts. 