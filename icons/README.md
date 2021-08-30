# Weather icon SVG to PNG converter

The weather icon SVG provided at [weathericon/2.0](https://api.met.no/weatherapi/weathericon/2.0/documentation) are not compatible with Android vector drawables. The provided PNG's are compatible, but at only 200x200, they are too small for some Android devices. 

This script generates ldpi, mdpi, hdpi, xhdpi, xxhdpi and xxxhdpi drawables from the provided SVG's that can be directly imported into the project. 

## Instructions
1. Install Inkscape
2. Navigate to the directory with containing weather icons SVG's (there should be 83 items)
3. Copy the `convert` script into the directory and give it execute permissions
4. Run the script
5. Copy and paste the generated drawable folders into the project's `res` directory. 