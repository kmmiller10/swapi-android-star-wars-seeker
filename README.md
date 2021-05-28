# swapi-android-star-wars-seeker

## This project contains the following:
- 100% Kotlin, with extensive use of Kotlin Extensions to decouple the local database from ViewModels as well as maintaining single-responsibility
- Kotlin Coroutines for network calls; Concurrency design pattern - [Kotlin Coroutine Documentation](https://kotlinlang.org/docs/coroutines-basics.html) | [Developer Guide](https://developer.android.com/kotlin/coroutines)
- MVVM Architectural design pattern
- LiveData, lifecycle-aware data holding observers - [Developer Guide](https://developer.android.com/topic/libraries/architecture/livedata)
- GSON for deserialization of JSON
- Realm for the local database and Realm models to represent the DTOs
- Koin dependency injection framework - [Koin documentation/guides](https://insert-koin.io)
- Retrofit + OkHttp + RxAndroid for networking
- Android Jetpack Navigation Component - [Developer Guide](https://developer.android.com/guide/navigation/navigation-getting-started)

## This project does not contain (but could make use of):
- Kotlin Flows - [Kotlin documentation](https://kotlinlang.org/docs/flow.html) | [Developer Guide](https://developer.android.com/kotlin/flow)
- Tranformations, used to Transform LiveData and return it from a function - [ProAndroidDev article](https://proandroiddev.com/making-network-calls-with-livedata-transformations-77c47272afcf) | [Corresponding Github repo](https://github.com/KunalChaubal/LiveDataTransformExample)
- PagingSource for paging data - [Developer Guide](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) | [Documentation](https://developer.android.com/reference/kotlin/androidx/paging/PagingSource)

_____________________________
## Project Overview:
Access the Star Wars API ([SWAPI](https://swapi.dev/documentation)) to search for Star Wars characters using the search endpoint: "https://swapi.dev/api/people/?search=".
Display the list of characters in a Master/Detail flow (Split View), and when a character is clicked on display some characteristics about the person in the detail pane. As an
extension of this project, I also added the retrieval of a character's home world and display it in the detail pane; when the home world planet is tapped, I show characteristics
about that planet in the detail pane. Additionally, I cache results using Realm for quick loading. On initial app launch, empty screens display instructions for the user.

_____________________________
## Code Implementation Details:

### app package
- Contains the Application class as well as the Koin modules that tell Koin how to create objects when injecting them

_____________________________
### remote package
- #### api subpackage
       - API definitions telling Retrofit which URLs to access and the return types
       - Protocol pattern using interfaces to define the repository functions
       - Interface implementations which implement the interfaces and perform the network calls using coroutines. Wraps the response types in a Resource class, described below
- #### base subpackage
       - App-recognized exception types
       - Exception interface with default implementation to parse HTTP errors and coroutine exceptions into the app-recognized exceptions
       - Resource wrapper class that provides the data of the API call on success, app-recognized exceptions on error, and an empty state
       - Sealed class which defines the network call statuses. The advantage over enums is that each type can provide different parameter types, which is useful for passing along an exception on error state
- #### models subpackage
       - List response DTO containing the list of items and details about a list GET (would be useful if paging was implemented)
       - Realm models representing the DTO
       - Realm Extensions which perform Realm queries and writing models to Realm (decouples it from the ViewModels)
       - Interface defining a ViewModel which represents a Realm model DTO, and thus must implement a method to map the DTO into a ViewModel

_____________________________
### ui package
- Main activity, hosts the frags and the navigation component
- UI extensions - handles errors, shows alerts, formats strings
- #### detail subpackage
       - Detail pane screens
- #### list subpackage
       - Master pane screen (list)
       - Adapter and ViewHolder for binding data to list items
- #### utils subpackage
       - DiffUtil for the list adapter (animates changes and handles list updates seamlessly)
       - Click listener for list items
       - Search View query debouncer; shorter delay for local queries, longer delay for submitting search term to web. Uses coroutine function to delay emissions

_____________________________
### viewmodels package
- Represents the view model layer in MVVM. The view model layer is the piece between the view and the DTO/repository, and must handle network calls as well as local Realm queries
- List view model for the character search query list retrieval and binding data to the list
- Individual view models representing each DTO object and the network calls for those individual objects (e.g. full GET or in this case, getting the home world of the character, given the home world URL)
- Extensions for sorting models and presenting the data on screen
