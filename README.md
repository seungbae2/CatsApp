# CatsApp

An Android application that displays and manages a collection of cat images. Built with Clean
Architecture, multi-module structure, and a resilient paging strategy that provides a smooth user
experience even under unstable network conditions.

## Key Features

- **Infinite Scrolling** — Paginated cat image grid using Paging 3
- **Detail View** — Full-screen image display with metadata
- **Offline Support** — Automatic fallback to cached data when network is unavailable, with
  auto-retry on reconnection
- **Orientation Aware** — Responsive grid layout (2-column portrait, 3-column landscape)

## Tech Stack

| Category      | Technology               |
|---------------|--------------------------|
| Language      | Kotlin                   |
| UI            | Jetpack Compose          |
| Architecture  | MVVM, Clean Architecture |
| Async         | Coroutines, Flow         |
| Networking    | Retrofit                 |
| Image Loading | Coil                     |
| DI            | Hilt                     |
| Pagination    | Paging 3                 |
| Navigation    | Navigation Compose       |
| Logging       | Timber                   |

## Architecture

The app follows Clean Architecture with multi-module separation:

```
app/                  → Application shell
build-logic/          → Convention plugins for Gradle
core/
├── common/           → Shared utilities
├── data/             → Repository implementations
├── data-api/         → Repository interfaces
├── database/         → Room local database
├── domain/           → Use cases & business logic
├── model/            → Domain models
├── network/          → Retrofit API clients
└── ui/               → Shared UI components
feature/
└── cats/             → Cat list & detail screens
```

### Data Flow

Uses **PagingSource** (not RemoteMediator) to centralize control over network-to-database caching,
offline fallback, and retry logic. This approach simplifies flow management and handles mid-session
connectivity changes gracefully.

```
UI (Compose) → ViewModel → UseCase → Repository → PagingSource
                                                    ├── Network (API)
                                                    └── Local (Room DB)
```

## How to Build

### Requirements

- Android Studio Meerkat | 2024.3.1 Patch 2+
- JDK 17
- Min SDK 26 / Target SDK 35

### Steps

```bash
git clone https://github.com/seungbae2/CatsApp.git
cd CatsApp
```

Open in Android Studio and sync Gradle. Run on emulator or device (API 26+).

## Design Decisions

- **PagingSource over RemoteMediator**: Provides simpler control flow for offline-first pagination
  with network recovery
- **Multi-module structure**: Enforces dependency rules at compile time — feature modules cannot
  access each other directly
- **Convention Plugins**: Centralized build configuration via `build-logic` module reduces
  duplication across modules
