# NearbyVenues
## Overview
Example android app for fetching nearby Venues using Foursquare API, CleanArchitecture and popular Android libraries
## Local setup
In order to run this on your local machine you must provide a Foursquare configuration
### 1. Create Foursquare developer account and app (https://developer.foursquare.com/)
### 2. Create an empty `secret.gradle` file in project root directory
### 3. Add variable in `secret.gradle`

```
ext{
    foursquareClientApi = "<CLIENT_ID>"
    foursquareClientSecret = "<CLIENT_SECRET>"
}
```

## Libraries
 - RxJava, RxAndroid
 - FastAdapter
 - Mockito
 - Retrofit
 - Moshi
 - Glide
