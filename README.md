# AddressBook.Android

## Overview
This repository contains **Address Book** application for Android that shows design & coding practices followed by **[Differenz System]([http://www.differenzsystem.com/](http://www.differenzsystem.com?tab=readme-ov-file&utm_source=github&utm_medium=AddressBook.Android))**.

The app does the following:
1. **Login:** User can login email/password. 
2. **Home:** It will list all the save contacts, having the option to add a new contact on the top right.
3. **Create new contact:** User can add a new contact to his address book by filling details here.

## What's New
1. **Updates and Upgrades:** 
	- Butter Knife Replaced with View Binding
2. **OnClick Method Enhancements:**
	- Refactored and upgraded the onClick methods
3. **Screen Orientation**
	- Upgraded screen orientation in the manifest to fullSensor for better flexibility.
4. **Toasty Library Dependency Updated**
	- Upgraded the Toasty library version
5. **Android Gradle Plugin (AGP) Upgrade**
	- Upgraded AGP dependency
6. **Gradle Version Upgrade**
    - Updated the Gradle version
7. **SDP and SSP Dependency Updates**
	- Upgraded sdp and ssp dependencies
8. **RoomDB Implementation**
    - Migrated the project from GreenDao to RoomDB for a more modern database management approach.
9. **Target SDK & Compile SDK Upgrade**
    - Updated the target SDK and compile SDK from 33 to 34 for improved performance and new Android features.
10 **Android Studio Version Upgrade**
    - Upgraded the Android Studio version from Chipmunk | 2021.2.1 to Koala | 2024.1.1 for better development tools and environment.

## Pre-requisites
- Android device or emulator running API 23 (6.0 - Marshmallow) or above
- [Android Studio Koala | 2024.1.1](https://developer.android.com/studio/index.html)

## Getting Started
1. [Install Android Studio](https://developer.android.com/studio/index.html)
2. Clone this sample repository
3. Import the sample project into Android Studio
	- File -> New -> Import Project
	- Browse to <path_to_project>/build.gradle
	- Click "OK"
4. Click Run -> Run, choose the sample you wish to run

## Key Tools & Technologies
- **Database:** Room Database (previously GreenDao)
- **Authentication:** Email/Password login
- **API/Service calls:** LoopJ 
- **IDE:** Android studio (Koala | 2024.1.1)
- **Framework:** MVC

## Screenshots
<img src="https://github.com/differenz-system/AddressBook.Android/blob/master/ScreenShots/login.png" width="280"> <img src="https://github.com/differenz-system/AddressBook.Android/blob/master/ScreenShots/list.png" width="280"> <img src="https://github.com/differenz-system/AddressBook.Android/blob/master/ScreenShots/detail.png" width="280">

## Support
If you've found an error in this sample, please [report an issue](https://github.com/differenz-system/AddressBook.Android/issues/new). You can also send your feedback and suggestions at info@differenzsystem.com

Happy coding!
