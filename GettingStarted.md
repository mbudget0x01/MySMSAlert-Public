# Getting Started
## API Key
### Use Google Maps integration
In Case you want to use the Google Maps Integration you have to add an API key. First
register for a key according to Googles Guideline. Then add the following to files, with the
provided Template.
Check also the AndroidManifest.xml for the Geocoder.

Files:
 * src/debug/res/values/google_maps_api.xml
 * src/release/res/values/google_maps_api.xml
Template:
```xml
<resources> 
    <!--
    TODO: Before you run your application, you need a Google Maps API key.


    Follow the directions here:
    https://developers.google.com/maps/documentation/android/start#get-key

    Once you have your key (it starts with "AIza"), replace the "google_maps_key"
    string in this file.
    -->
    <string name="google_maps_key" translatable="false">YourAPIKEyHere</string>
</resources>
```
### dont use Google Maps Integration
 * Turn off google Maps Integration in the Settings
## build
 * Use gradle to build the necessary files are provided.

## done?
Great now you are ready to test, customize and distribute the App.

## what to do next
 * Implement your custom Address parser via the IAddressParser Interface.
    * First, Implement the Interface.
    * Second, Add it to the Parser Factory and to the AdressParser.xml File.
 * Customize the GUI for your needs.
 * Extend the app in any way for your needs.
 
## Questions?
You can leave them here on Github an i'll provide help.
