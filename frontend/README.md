# Book Management Frontend

A React frontend for the Book Management microservices system.

## Features

### 👥 User Management
- Create new users (Consumer/BookKeeper)
- View all users with their roles
- User type badges for easy identification

### 📚 Book Management  
- Add new books with title, author, ISBN
- View all books with availability status
- Delete books from inventory
- Visual indicators for available/borrowed books

### 📋 Rental Management
- Borrow books by selecting user and book
- Set rental duration (1-30 days)
- View active rentals by user
- Return books with one click
- Real-time availability updates

## Setup & Run

### Prerequisites
- Node.js 16+ installed
- Backend microservices running

### Installation
```bash
cd frontend
npm install
```

### Development
```bash
npm start
```
Runs on http://localhost:3000

### Production Build
```bash
npm run build
```

## API Integration
- Connects to API Gateway at `http://localhost:8080`
- Uses proxy configuration for development
- Axios for HTTP requests
- Error handling for all API calls

## Components
- **App.js** - Main app with navigation
- **UserManagement.js** - User CRUD operations
- **BookManagement.js** - Book inventory management  
- **RentalManagement.js** - Borrowing/returning system

## Styling
- Clean, responsive design
- Color-coded badges and status indicators
- Mobile-friendly interface
- Professional business application look