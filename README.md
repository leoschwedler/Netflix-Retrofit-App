# Netflix Retrofit App 🎬

## 🌟 Overview
This Android app project fetches and displays popular and recent movies from The Movie Database (TMDb) using the Retrofit library for network requests. The app features a sleek, user-friendly interface with a RecyclerView for listing movies, and a detailed movie screen for more information about each film. The app is designed with a clean architecture, utilizing coroutines for asynchronous operations and Picasso for image loading.

## 🛠️ Project Structure
The project is structured to efficiently fetch and display movie data from TMDb:

### Key Components

#### `MainActivity`
- **Handles the initialization and main UI of the app.**
- Fetches and displays a list of popular movies using a `RecyclerView` and a `GridLayoutManager`.
- Supports infinite scrolling to load more movies as the user reaches the bottom of the list.
- Shows the most recent movie in a prominent banner.

#### `DetailActivity`
- **Displays detailed information about a selected movie.**
- Fetches the movie details passed from `MainActivity` and loads the movie poster and title.

#### `FilmeAdapter`
- **An adapter for the `RecyclerView` to manage the movie items.**
- Uses `Picasso` to load movie posters into the list items.
- Handles item clicks to navigate to `DetailActivity` for more information.

#### `RetrofitService`
- **Manages network communication with TMDb API using Retrofit.**
- Configures `OkHttpClient` with timeout settings and an authentication interceptor.
- Defines the `TheMovieDBAPI` interface for making API requests.

#### `TheMovieDBAPI`
- **Interface that defines the API endpoints for TMDb.**
- Provides methods for fetching popular movies and the most recent movie.

## 🚀 Implemented Features
- Fetching and displaying popular movies from TMDb.
- Fetching and displaying the most recent movie on app startup.
- Infinite scrolling to load more movies.
- Detailed movie information screen.
- Image loading and caching using Picasso.

## 📷 Screenshots
<img src="https://github.com/user-attachments/assets/d0306cc8-bce0-4ca5-9b0b-9de5514a4d95" width="260"/>
<img src="https://github.com/user-attachments/assets/509bb7b7-8ee1-44a9-8888-4ae16a1db50a" width="260"/>

## 🛠️ Dependencies
- `com.squareup.retrofit2:retrofit:2.9.0`: For network requests and API communication.
- `com.squareup.retrofit2:converter-gson:2.9.0`: For converting JSON responses to Kotlin objects.
- `com.squareup.okhttp3:okhttp:4.9.3`: For HTTP client configuration and network requests.
- `com.squareup.picasso:picasso:2.71828`: For image loading and caching.

## 📌 License
This project is open-source under the MIT License.
