# Xero Degrees

## Overview

To use the app install the provided .apk. There are 3 main screens:
 
- Main Forecast screen
- Detail screen (tap a forecast to see more details)
- Settings screen (found in the menu at the top right)


## Features

The app provides a 5 day weather forecast for a selected city (can be customised in the settings page). The 5 day forecast is divided into 3 hourly entries.

I used the [Open Weather Map 5 day / 3 hour forecast](https://openweathermap.org/forecast5) rather than the example API provided in the brief ([16 day / daily](https://openweathermap.org/forecast16)) because the suggested API is paid. The 5 day forecast was sufficient for the data which I wished to provide.

The app uses a Master-Detail interface to show additional weather data in a detail activity.

Preferences for selected city and metric/imperial measurements.

I largely used this project as an opportunity to develop with Android Architecture components which I had not used before. The app uses Room for persistence, as well as ViewModel and LiveData for lifecycle aware updates. 

### Architecture

As a rough overview of the project, there are 4 main packages:

- data.database: Contains required classes for Room database, dao and WeatherEntry table
- data.network: Classes for requesting and parsing data from OWM. Runs an IntentService to fetch the data. There is an additional Repository class in the data package which provides a link between the database and network functionality
- ui: All Activities and relevant ViewModels (and their Factories)
- utils: Weather, Date and Injector utility classes

### Libraries

- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [Preference](https://developer.android.com/reference/androidx/preference/package-summary.html)


## Improvements

Though I'm happy with the final product, there are a few major changes I would make if I wasn't limited in time:

- Currently the project has no tests. The first priority if I had more time would be to cover with junit tests.
- Get current location with permissions. Would be a simple update to add this, and more useful for the majority of users. I do like the ability to select a city in the settings as this gives you the option of searching for weather in a place you're travelling to. A best implementation would support both.
- The 5 day / 3 hourly API provided sufficient data but if I had access to the paid APIs I would use a combination of the daily and hourly APIs. I think it's more useful to see daily weather in the forecast list, and weather by hour in the detail view.


## Note

I reused some code from the 'Developing Android Apps' course by Google on Udacity. This was a course that I completed a couple of years ago, and guides you through creating a weather app which also fetches data from Open Weather Maps. In completing each of the lessons you write the app yourself but there is some amount of code provided already, namely utilities and assets.  Though some of the weather project code was outdated, it made sense to reuse utils and icons which were still applicable to Open Weather Map data. 
