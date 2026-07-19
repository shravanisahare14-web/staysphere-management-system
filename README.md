# Hotel Grand Palace - Java + React Booking System

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.java.com)
[![React](https://img.shields.io/badge/React-18-61DAFB)](https://reactjs.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

> A premium Hotel Room Booking System with **Java Backend** and **React Frontend**.
> Built for Java course projects, GitHub portfolios, and placement interviews.

![Dashboard Preview](screenshots/dashboard-preview.png)

## Architecture

```
+------------------+         REST API (JSON)         +------------------+
|   React Frontend |  <--------------------------->  |  Java Backend    |
|   (Port 3000)    |                               |  (Port 8080)     |
+------------------+                               +------------------+
        |                                                    |
        | React Router                                       | HttpServer (built-in)
        | Lucide Icons                                       | HotelService (Singleton)
        | CSS3 Dark Theme                                    | FileManager (Serialization)
        v                                                    v
   Dashboard Page                                      Room, Guest, Booking
   Rooms Page                                          models (OOP)
   Bookings Page                                       
   New Booking Page                                    
```

## Features

### Backend (Java)
- Pure Java with built-in `HttpServer` - **zero external dependencies!**
- REST API with CORS support
- Full OOP design: Room, Guest, Booking classes
- Singleton HotelService for business logic
- Java Serialization for data persistence
- GST calculation (18%) on all bills
- 12 rooms across 4 categories with Indian pricing

### Frontend (React)
- Modern React 18 with Hooks
- React Router for SPA navigation
- Dark theme with gradient cards
- Lucide React icons
- Responsive sidebar layout
- Real-time data from Java API
- Toast notifications
- Form validation

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Built-in HttpServer |
| Frontend | React 18, React Router, Lucide Icons |
| Data | Java Serialization (.dat files) |
| Styling | CSS3 with custom properties |

## How to Run

### Prerequisites
- Java JDK 17 or higher
- Node.js 16+ and npm

### Step 1: Start the Java Backend

```bash
cd backend
mkdir data
javac -encoding UTF-8 src/api/*.java src/model/*.java src/service/*.java src/repository/*.java src/utility/*.java
java -cp . api.HotelApiServer
```

You should see:
```
========================================
  Hotel API Server Started!
  URL: http://localhost:8080
========================================
```

### Step 2: Start the React Frontend (New Terminal)

```bash
cd frontend
npm install
npm start
```

The browser will open at `http://localhost:3000`

### Step 3: Use the Application

1. **Dashboard** - View statistics, occupancy rates, recent bookings
2. **Rooms** - Browse all rooms with images, filter by type
3. **Bookings** - View all bookings, check-in, check-out, cancel
4. **New Booking** - Create bookings with guest details

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/rooms | All rooms |
| GET | /api/rooms/available | Available rooms |
| GET | /api/bookings | All bookings |
| POST | /api/bookings/create | Create booking |
| POST | /api/bookings/cancel | Cancel booking |
| POST | /api/bookings/checkin | Check in guest |
| POST | /api/bookings/checkout | Check out guest |
| GET | /api/stats | Dashboard statistics |

## Project Structure

```
Hotel-Booking-Java-React/
├── backend/
│   └── src/
│       ├── api/
│       │   └── HotelApiServer.java      # REST API server
│       ├── model/
│       │   ├── Room.java                # Room entity
│       │   ├── Guest.java               # Guest entity
│       │   └── Booking.java             # Booking entity
│       ├── service/
│       │   └── HotelService.java        # Business logic
│       ├── repository/
│       │   └── FileManager.java         # File persistence
│       └── utility/
│           └── BookingIdGenerator.java  # ID generation
│
├── frontend/
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/
│   │   │   ├── Layout.js                # Sidebar + Header
│   │   │   └── Layout.css
│   │   ├── pages/
│   │   │   ├── Dashboard.js             # Stats dashboard
│   │   │   ├── Rooms.js                 # Room listing
│   │   │   ├── Bookings.js              # Booking management
│   │   │   └── NewBooking.js            # Booking form
│   │   ├── styles/
│   │   │   ├── index.css
│   │   │   └── App.css
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
│
├── data/                                 # Runtime data files
├── screenshots/                          # Demo images
└── README.md
```

## Room Pricing (Indian Rupees)

| Type | Price/Night | Capacity | Rooms |
|------|-------------|----------|-------|
| Single | Rs 1,500 - 1,800 | 1 | 3 |
| Double | Rs 2,800 - 3,200 | 2 | 4 |
| Deluxe | Rs 4,500 - 5,000 | 3 | 3 |
| Suite | Rs 8,000 - 12,000 | 4-6 | 3 |

## Why This Architecture?

| Aspect | Benefit |
|--------|---------|
| **Pure Java Backend** | Shows deep OOP understanding, no framework magic |
| **Built-in HttpServer** | Zero dependencies, easy to run anywhere |
| **React Frontend** | Modern UI that impresses recruiters |
| **File Persistence** | Demonstrates serialization, no DB setup needed |
| **Dark Theme** | Professional, modern appearance |

## Learning Outcomes

- **Java OOP**: Classes, inheritance, encapsulation, singleton
- **REST API Design**: HTTP methods, JSON, CORS
- **React**: Hooks, Router, component architecture
- **Full-Stack Integration**: Connecting frontend to backend
- **Data Persistence**: Java Serialization

## Author

**[Your Name]**
- Computer Science Student
- Aspiring Java / Full-Stack Developer

---

⭐ Star this repository if you found it helpful!
