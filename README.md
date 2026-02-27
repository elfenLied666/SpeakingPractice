# Speaking Practice - Paid Demo TaskThis repository contains the Android prototype for the Speaking Practice progress screens, developed as a paid demo task for the C3 hiring assessment. The application is built to closely match the provided Figma design, focusing on clean architecture, modern development practices, and UI accuracy.

## Architecture Summary

The project follows a modern **MVVM (Model-View-ViewModel)** architecture using **XML for UI** and **Android Jetpack components**. Data persistence is handled by **DataStore** with KotlinX Serialization, ensuring a reactive and lifecycle-aware data flow.

## What Was Implemented

### Core Features
- **UI Implementation**: Two main screens (`My Progress` and `Speaking Practice`) were implemented using XML layouts and `MaterialCardView` to match the Figma design.
- **Navigation**: Navigation between screens is handled using the Jetpack Navigation Component.
- **Data Persistence**: `Proto DataStore` is used to store, edit, and delete user's practice history (words and sentences).
- **Reactive UI**: The UI updates automatically in response to data changes using `StateFlow` from the ViewModel, collected in a lifecycle-aware manner within the Fragments.
- **Dynamic Content**:
    - The number of words and sentences in the filter chips is dynamically calculated and displayed.
    - The list of practiced words/sentences is loaded from DataStore and displayed in a `RecyclerView`.

### UI/UX & Interactions
- **Filter Chips**: The "All", "Words", and "Sentences" chips are fully functional, filtering the list displayed below.
- **Multi-Selection Mode**: Users can enter an "edit mode" to select multiple items in the list. The selected items are highlighted with a border.
- **Delete Functionality**:
    - Individual items can be deleted via a dedicated button in the list.
    - Multiple selected items can be deleted in edit mode.
    - A confirmation dialog is shown before any deletion to prevent accidental data loss.
- **Animations**: A frame-by-frame PNG animation (`AnimationDrawable`) is implemented for the speaker icon, which plays when an item is selected.

### Localization
- The application includes string resources for both **English (default)** and **Spanish (`values-es`)**.

## Setup Steps

1. Clone the repository:
   bash git clone <your-repository-url>
2. Open the project in the latest stable version of Android Studio.
3. Let Gradle sync the project dependencies.
4. Run the application on an emulator or a physical Android device.

## Assumptions & Tradeoffs

- **XML over Jetpack Compose**: Although Compose was recommended, XML was chosen to demonstrate proficiency in modern XML-based development, including `ConstraintLayout`, `MaterialCardView`, and `ViewBinding`.
- **PNG Animation**: For the speaker icon animation, a frame-by-frame PNG animation (`AnimationDrawable`) was used instead of a more complex `AnimatedVectorDrawable` or property animation. This was a practical tradeoff for speed of implementation, as ready-made PNG frames were available. The SVG editing route was deemed too time-consuming for this task.
- **Tablet Layout**: A basic tablet layout (`values-sw720dp`) was created by increasing font sizes and margins to improve readability, but a full redesign with multi-pane layouts was considered out of scope for this demo.



