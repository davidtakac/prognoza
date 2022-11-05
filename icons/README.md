# Icons

The weather icon SVGs provided by MET Norway are not compatible with Android vector drawables.
They also provide PNGs which are, but at only 200x200, they are too small for most Android devices. 

The `convert` script generates ldpi, mdpi, hdpi, xhdpi, xxhdpi and xxxhdpi PNGs from the provided 
SVGs that can be directly imported into the project.

## Instructions
1. Install Inkscape
2. Download the [icons archive](https://api.met.no/weatherapi/weathericon/2.0/data)
3. Extract the archive
4. Navigate to its `svg` directory
5. Copy the `convert` script into it and execute it
6. Copy the generated drawable directories into `app/src/main/res`

Example on Linux: 
```sh
# 1 - Install inkscape (on Arch)
pacman -Syu inkscape
# 2 - Download archive
cd ~/Downloads
curl https://api.met.no/weatherapi/weathericon/2.0/data --output weathericon.tgz
# 3 - Extract archive
mkdir weathericons
tar -xf weathericon.tgz -C weathericons
# 4 - Navigate to svg
cd weathericons/svg
# 5 - Copy and execute convert script
cp /path/to/prognoza/icons/convert ./convert
chmod +x convert
./convert
# 6 - Copy the generated drawable dirs into the project
cp -r drawable* /path/to/prognoza/app/src/main/res/
```