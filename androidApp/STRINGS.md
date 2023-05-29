# Importing strings from Crowdin

Because Crowdin does not seem to support changing the output format of the exported directories, they need to be manually renamed before they're imported into the Android app:

```sh
unzip 'Prognoza (translations).zip' -d prognoza-translations
cd prognoza-translations
# pt-BR -> values-pt-rBR
rename -- - -r * && rename '' values- * && rename values-en values values-en
cp -r * /path/to/Prognoza/androidApp/src/main/res/
```