# **Moodify**

## Table of Contents

1. [App Overview](#App-Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Build Notes](#Build-Notes)

## App Overview

### Description 

"Moodify" is an Android app programmed in Kotlin where users can generate a list of songs based on their mood according to different tags from an API called last.fm. Different options for moods include "party," "sad," workout," "chill," and "study." The app utilizes a recycler view which enables users to scroll down for more songs in the generated playlist. This app is perfect for users who want to create a playlist according to their mood, but they do not have the time to do it manually.

### App Evaluation

<!-- Evaluation of your app across the following attributes -->

- Category: Music
- Mobile: Convenient for users to create a playlist while on the go
- Story: Instead of having to create a whole new playlist for your mood, you can just search for one.
- Market: Anyone who listens to music
- Habit: Could be used daily for new playlists.
- Scope: Use Last.fm API to find tracks that match mood and try to incorporate an API that allows for playback if time permits

## Product Spec

### 1. User Features (Required and Optional)

Required Features:

- At least 5 mood options
- Allow user to search for a specific mood
- List of tracks with artist and album art using recylcer view

Stretch Features:

- More mood options
- In-app music player

### 2. Chosen API(s)

Last.fm API, Spotify API

### 3. User Interaction

Required Features

- Mood Buttons
  - User can select mood of the music they want to find

- Search Bar
    - User can search for songs by a specific mood

- Display of tracks
  - Users can scroll through list of tracks which match the chosen mood


## Wireframes
### [BONUS] Digital Wireframes & Mockups

<!-- Add picture of your hand sketched wireframes in this section -->
<iframe style="border: 1px solid rgba(0, 0, 0, 0.1);" width="800" height="450" src="https://embed.figma.com/design/PHm1Ml9gHi2jXrKmBFdbv9/AND-101-Pod-4-App-Prototype?node-id=0-1&embed-host=share" allowfullscreen></iframe> 


### [BONUS] Interactive Prototype

## Build Notes

App creation process taught essential skills such as product brainstorming, wireframing/prototyping via Figma, Android developement, API calls, handling recycler views, etc.     

For Milestone 2, include **2+ Videos/GIFs** of the build process here!

User presses buttons to search for songs matching pre-selected moods
<img src='capstonedemo1.gif' title='Video Demo 1' width='50%' alt='Video Demo 1' />

User manually searches for songs using the search bar
<img src='capstonedemo2.gif' title='Video Demo 2' width='50%' alt='Video Demo 2' />


## License

Copyright 2025 Juhi Trivedi, Sophia Lu, Luniva Joshi, Kasish Jain

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
