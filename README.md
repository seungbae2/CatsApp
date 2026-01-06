# CatsApp

CatsApp is an Android application that displays and manages a collection of cat images.  
It focuses on clean architecture, modularization, and a resilient paging strategy that provides a
smooth user experience even under unstable network conditions.

## Table of Contents

- [Project Overview](#project-overview)
- [Development & Runtime Environment](#development--runtime-environment)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Module Dependencies](#module-dependencies)
- [Layer Responsibilities & Modules](#layer-responsibilities--modules)
- [Data Flow](#data-flow)
- [Why PagingSource Instead of RemoteMediator](#why-pagingsource-instead-of-remotemediator)

---

## Project Overview

CatsApp displays cat images using a paginated grid layout.  
Users can scroll infinitely through cat images and select a specific image to view detailed
information.

The app monitors network connectivity and optimizes the user experience by gracefully falling back
to locally cached data when offline.

---

## Development & Runtime Environment

- **Operating Systems**: macOS, Windows, Linux
- **Android Studio**: Meerkat | 2024.3.1 Patch 2
- **Kotlin**: 2.1.10
- **Java**: 17
- **Min SDK**: 26
- **Target SDK**: 35
- **Gradle**: 8.9.0

---

## Key Features

### 1. Cat Image List

- Infinite scrolling with Paging 3
- Grid-based image layout

### 2. Cat Detail View

- Full-screen image display
- Detailed information for each cat image

### 3. Network State Handling

- Offline state detection
- Automatic retry when network connectivity is restored

### 4. Orientation Optimization

- Supports both portrait and landscape modes
- 3-column grid layout in landscape mode

---

## Tech Stack

| Category             | Technology               |
|----------------------|--------------------------|
| Language             | Kotlin                   |
| UI                   | Jetpack Compose          |
| Architecture         | MVVM, Clean Architecture |
| Async                | Coroutines, Flow         |
| Networking           | Retrofit                 |
| Image Loading        | Coil                     |
| Dependency Injection | Hilt                     |
| Paging               | Paging 3                 |
| Navigation           | Navigation Compose       |
| Logging              | Timber                   |

---

## Project Structure

```
CatsApp/
├── app/
├── build-logic/
├── core/
│   ├── common/
│   ├── data/
│   ├── data-api/
│   ├── database/
│   ├── domain/
│   ├── model/
│   ├── network/
│   └── ui/
└── feature/
    └── cats/
```

---

## Module Dependencies

```
core:network ─┐
              ├─> core:data ──> core:data-api <── core:domain <── feature:cats
core:database ─┘
core:common ────────────────────────────────────────────────┘
app (Shell)
```

---

## Layer Responsibilities & Modules

### Presentation Layer

- **app**: Application shell and navigation
- **feature:cats**: Cat list & detail UI
- **core:ui**: Shared UI components

### Domain Layer

- **core:model**: Domain models
- **core:domain**: Business logic & use cases

### Data Layer

- **core:data**: Repository implementations
- **core:data-api**: Repository interfaces
- **core:network**: Remote API
- **core:database**: Local DB

### Common Layer

- **core:common**: Network monitoring utilities

---

## Data Flow

Uses PagingSource with network-first strategy and local DB fallback.

---

## Why PagingSource Instead of RemoteMediator

PagingSource allows centralized control of:

- Network → DB caching
- Offline fallback
- Retry behavior on network recovery

This results in simpler flow control and better handling of mid-session network changes compared to
RemoteMediator.
