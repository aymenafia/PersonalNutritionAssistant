# PersonalNutritionAssistant (Still Ongoing)

A **Kotlin Multiplatform** project showcasing a simple nutrition-tracking application with an iOS SwiftUI client and (optionally) an Android app. This sample demonstrates:

- **Shared business logic** in Kotlin (`MealRepository`, etc.).
- **Dependency injection** with [Kotlin-Inject](https://github.com/evant/kotlin-inject).
- **Networking** (Ktor) and **Local Database** (SQLDelight).
- A **SwiftUI** interface calling shared Kotlin code.

---

## Contents

1. [Features](#features)  
2. [Tech Stack](#tech-stack)  
3. [Project Structure](#project-structure)  
4. [Getting Started](#getting-started)  
5. [Running on iOS](#running-on-ios)  
6. [Usage in SwiftUI](#usage-in-swiftui)  

---

## Features

1. **Add & Fetch Meals**: Demonstrates fetching mock meal data (or real endpoints if configured).
2. **Local Storage**: Persists meals using SQLDelight in Kotlin (shared code).
3. **SwiftUI** (iOS side) for minimal UI display.
4. **Kotlin-Inject** for a minimal DI example (`@Inject`) (still ongoing).

---

## Tech Stack

- **Kotlin Multiplatform**: Shared code in the `shared/` module  
  - Targets: iOS (`iosArm64`, `iosSimulatorArm64`) and optional Android.  
- **Ktor** (Networking)
- **SQLDelight** (Database)
- **Kotlin-Inject** (Dependency Injection)
- **SwiftUI** for the iOS UI layer

---


### Notable Shared Classes

- **MealRepositoryImpl**: Core logic for fetching meals (mock or real API) and storing them in SQLDelight.
- **DriverFactory**: Creates a SQLDelight `SqlDriver` for each platform (Android/iOS).
- **(Optional) AppComponent**: If using Kotlin-Inject’s `@Component` for DI (on going).

---

## Getting Started

1. **Clone** the repository:
   ```bash
   git clone ...
   cd PersonalNutritionAssistant

2. **Import** Import the project into Android Studio or IntelliJ
3. **Sync** Sync Gradle to download dependencies.
4. **Build** Build the shared module
To build the **Kotlin Multiplatform** shared module, run:

- **On macOS/Linux**:
  ```bash
  ./gradlew :shared:assemble


### Running on iOS

- Open the folder in Xcode
- Select a simulator or device
- Run. SwiftUI code calls into the shared framework
  
### Usage in SwiftUI
  ```bash

import SwiftUI
import shared

struct ContentView: View {
    @State private var meals: [Meal] = []
    @State private var errorMessage: String = ""

    var body: some View {
        VStack {
            Button("Fetch Meals") {
                Task {
                    do {
                        
                        let driver = DriverFactory().createDriver()
                        
                       
                        let repo = MealRepositoryImpl(driver: driver)
                        
                        // Call the suspend function to fetch mock/real meals
                        let result = try await repo.fetchMeals()
                        
                        
                        guard let fetchedMeals = result as? [Meal] else {
                            self.errorMessage = "Failed to parse meals from Kotlin."
                            return
                        }
                        
                       
                        self.meals = fetchedMeals
                        self.errorMessage = ""
                        
                    } catch {
                        self.errorMessage = error.localizedDescription
                    }
                }
            }

            if !errorMessage.isEmpty {
                Text("Error: \(errorMessage)")
                    .foregroundColor(.red)
            }

            List(meals, id: \.id) { meal in
                VStack(alignment: .leading) {
                    Text(meal.name)
                        .font(.headline)
                    Text("Calories: \(meal.calories)")
                }
            }
        }
        .padding()
    }
}


