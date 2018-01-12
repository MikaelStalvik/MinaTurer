# Mina Turer

Find the next departure for a bus, tram or train within the Västra Götalands regions public transportation.
The full application can be downloaded from:
https://play.google.com/store/apps/details?id=com.imploded.minaturer

This application is an Android application written in Kotlin.

## Getting Started

Grab the source code and open it in Android Studio 3 or higher. 
Make sure that you have the Android SDK up-to-date (version 27 is used)

### Prerequisites

Just do a Gradle sync and all dependencies shall be downloaded.

An API account for accessing Västtrafiks API services is required.
You can register your account at:
https://developer.vasttrafik.se/portal/#/

After this step is performed you will need to create a class named WebApiKeys in the utils package.

The class should look this:
```
object WebApiKeys {
    val clientId ="<insert your client id here>"
    val clientSecret = "<insert your client secret here>"
}
```

## Running the tests

To run the tests, simply select the com.imploded.minaturer (test) package and click Ctrl+Shift+F10

## Built with

* [Android studio](https://developer.android.com/studio/index.html) - IDE
* [Gson](https://github.com/google/gson) - JSON Serialization library
* [Fuel](https://github.com/kittinunf/Fuel) - Kotlin http networking library
* [Dagger2](https://github.com/google/dagger) - Dependency injection library
* [Mockito](https://github.com/mockito/mockito) - Mocking framework

