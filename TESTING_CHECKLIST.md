# Testing Checklist

Use this checklist to verify your microservices system is working correctly.

## ✅ Pre-Start Checklist

- [ ] Java 17 installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] Node.js installed (`node -version`)
- [ ] npm installed (`npm -version`)
- [ ] All ports available (8761, 8080, 8081, 8082, 8083, 3000)

## ✅ Service Startup Checklist

### 1. Eureka Server
- [ ] Started successfully on port 8761
- [ ] Can access http://localhost:8761
- [ ] Dashboard shows "Instances currently registered with Eureka"
- [ ] No errors in console

### 2. User Service
- [ ] Started successfully on port 8082
- [ ] Registered with Eureka (check dashboard)
- [ ] Shows "USER-SERVICE" in Eureka dashboard
- [ ] No errors in console

### 3. Book Service
- [ ] Started successfully on port 8081
- [ ] Registered with Eureka (check dashboard)
- [ ] Shows "BOOK-SERVICE" in Eureka dashboard
- [ ] No errors in console

### 4. Order Service
- [ ] Started successfully on port 8083
- [ ] Registered with Eureka (check dashboard)
- [ ] Shows "ORDER-SERVICE" in Eureka dashboard
- [ ] No errors in console

### 5. API Gateway
- [ ] Started successfully on port 8080
- [ ] Registered with Eureka (check dashboard)
- [ ] Shows "API-GATEWAY" in Eureka dashboard
- [ ] No errors in console

### 6. Frontend
- [ ] Started successfully on port 3000
- [ ] Browser opens automatically
- [ ] Shows login page
- [ ] No errors in browser console

## ✅ Functional Testing Checklist

### User Management Tests

#### Test 1: Seller Registration
- [ ] Click "Register" on login page
- [ ] Fill in name: "Test Seller"
- [ ] Fill in email: "seller@test.com"
- [ ] Fill in password: "test123"
- [ ] Select role: "Seller"
- [ ] Click Register button
- [ ] Successfully redirected to Seller Dashboard
- [ ] See welcome message with username

#### Test 2: Seller Login
- [ ] Logout from dashboard
- [ ] Click "Login"
- [ ] Enter email: "seller@test.com"
- [ ] Enter password: "test123"
- [ ] Click Login button
- [ ] Successfully logged in to Seller Dashboard

#### Test 3: Buyer Registration
- [ ] Logout if logged in
- [ ] Click "Register"
- [ ] Fill in name: "Test Buyer"
- [ ] Fill in email: "buyer@test.com"
- [ ] Fill in password: "test123"
- [ ] Select role: "Buyer"
- [ ] Click Register button
- [ ] Successfully redirected to Buyer Dashboard

#### Test 4: Buyer Login
- [ ] Logout from dashboard
- [ ] Click "Login"
- [ ] Enter email: "buyer@test.com"
- [ ] Enter password: "test123"
- [ ] Click Login button
- [ ] Successfully logged in to Buyer Dashboard

### Book Management Tests (Seller)

#### Test 5: Add Book
- [ ] Login as Seller
- [ ] Fill in book form:
  - Title: "Spring Boot Guide"
  - Author: "John Doe"
  - ISBN: "978-1234567890"
  - Price: 29.99
  - Quantity: 10
- [ ] Click "Add Book" button
- [ ] Book appears in "My Books" section
- [ ] Shows correct price and quantity
- [ ] Status shows "AVAILABLE"

#### Test 6: Edit Book
- [ ] Click "Edit" on a book
- [ ] Form populates with book data
- [ ] Change price to 34.99
- [ ] Change quantity to 15
- [ ] Click "Update" button
- [ ] Book updates in list
- [ ] Shows new price and quantity

#### Test 7: Delete Book
- [ ] Click "Delete" on a book
- [ ] Confirm deletion in popup
- [ ] Book removed from list

#### Test 8: Add Multiple Books
- [ ] Add 3 different books
- [ ] All books appear in "My Books" section
- [ ] Count shows correct number

### Purchase Tests (Buyer)

#### Test 9: Browse Books
- [ ] Login as Buyer
- [ ] See "Available Books" section
- [ ] Books listed by seller are visible
- [ ] Each book shows title, author, price, quantity

#### Test 10: Add to Cart
- [ ] Click "Add to Cart" on a book
- [ ] Cart controls appear (-, Qty, +, Buy)
- [ ] Quantity shows 1

#### Test 11: Adjust Quantity
- [ ] Click "+" button
- [ ] Quantity increases to 2
- [ ] Total price updates (price × 2)
- [ ] Click "-" button
- [ ] Quantity decreases to 1

#### Test 12: Purchase Book
- [ ] Set quantity to 2
- [ ] Click "Buy" button
- [ ] Success message appears
- [ ] Book removed from cart
- [ ] Order appears in "My Orders" section
- [ ] Order shows correct quantity and total

#### Test 13: Verify Stock Update
- [ ] Note book quantity before purchase
- [ ] Purchase 2 copies
- [ ] Refresh page
- [ ] Book quantity decreased by 2
- [ ] If quantity reaches 0, status changes to "SOLD_OUT"

#### Test 14: View Order History
- [ ] Check "My Orders" section
- [ ] All purchases listed
- [ ] Each order shows:
  - Order ID
  - Book ID
  - Quantity
  - Total price
  - Status (COMPLETED)
  - Order date

### Seller Sales Tests

#### Test 15: View Sales
- [ ] Login as Seller
- [ ] Check "Sales" section
- [ ] See orders from buyers
- [ ] Each sale shows:
  - Order ID
  - Book ID
  - Quantity sold
  - Total amount
  - Status
  - Date

#### Test 16: Multiple Sales
- [ ] Have buyer purchase multiple books
- [ ] All sales appear in seller's dashboard
- [ ] Count is correct

### Integration Tests

#### Test 17: Multi-User Scenario
- [ ] Seller A lists Book X
- [ ] Seller B lists Book Y
- [ ] Buyer C purchases Book X
- [ ] Buyer D purchases Book Y
- [ ] Seller A sees only sale of Book X
- [ ] Seller B sees only sale of Book Y
- [ ] Buyer C sees only purchase of Book X
- [ ] Buyer D sees only purchase of Book Y

#### Test 18: Stock Depletion
- [ ] Seller lists book with quantity 2
- [ ] Buyer 1 purchases 1 copy
- [ ] Verify quantity is now 1
- [ ] Buyer 2 purchases 1 copy
- [ ] Verify quantity is now 0
- [ ] Book status changes to "SOLD_OUT"
- [ ] Book no longer appears in available books

#### Test 19: Insufficient Stock
- [ ] Seller lists book with quantity 3
- [ ] Buyer tries to purchase 5 copies
- [ ] Error message appears
- [ ] Purchase fails
- [ ] Stock remains unchanged

### Session Management Tests

#### Test 20: Logout
- [ ] Login as any user
- [ ] Click "Logout" button
- [ ] Redirected to login page
- [ ] Cannot access dashboard without login

#### Test 21: Session Persistence
- [ ] Login as user
- [ ] Refresh page
- [ ] Still logged in
- [ ] Dashboard loads correctly

#### Test 22: Role-Based Access
- [ ] Login as Seller
- [ ] See Seller Dashboard (not Buyer Dashboard)
- [ ] Logout and login as Buyer
- [ ] See Buyer Dashboard (not Seller Dashboard)

## ✅ API Testing Checklist (Optional - Using Postman/cURL)

### Test 23: Direct API Calls
- [ ] POST /api/users/register - Returns token
- [ ] POST /api/users/login - Returns token
- [ ] GET /api/books - Returns book list
- [ ] POST /api/books - Creates book
- [ ] GET /api/books/available - Returns available books
- [ ] POST /api/orders/purchase - Creates order
- [ ] GET /api/orders/buyer/{id} - Returns buyer orders

## ✅ Error Handling Tests

#### Test 24: Invalid Login
- [ ] Try login with wrong password
- [ ] Error message appears
- [ ] Not logged in

#### Test 25: Duplicate Email
- [ ] Try registering with existing email
- [ ] Error message appears
- [ ] Registration fails

#### Test 26: Empty Form Submission
- [ ] Try submitting empty book form
- [ ] Browser validation prevents submission
- [ ] Required fields highlighted

## ✅ Performance Tests

#### Test 27: Multiple Concurrent Users
- [ ] Open 3 browser windows
- [ ] Login as different users in each
- [ ] Perform actions simultaneously
- [ ] All actions complete successfully
- [ ] No conflicts or errors

#### Test 28: Large Inventory
- [ ] Seller adds 20+ books
- [ ] All books load correctly
- [ ] No performance issues
- [ ] Buyer can browse all books

## ✅ Service Communication Tests

#### Test 29: Inter-Service Communication
- [ ] Order Service successfully calls Book Service
- [ ] Stock updates propagate correctly
- [ ] No timeout errors
- [ ] Check service logs for successful calls

#### Test 30: Service Discovery
- [ ] All services registered in Eureka
- [ ] Gateway routes to correct services
- [ ] No 503 Service Unavailable errors

## 📊 Test Results Summary

Total Tests: 30

Passed: _____ / 30
Failed: _____ / 30

## 🐛 Issues Found

| Test # | Issue Description | Severity | Status |
|--------|------------------|----------|--------|
|        |                  |          |        |
|        |                  |          |        |
|        |                  |          |        |

## ✅ Final Verification

- [ ] All services running without errors
- [ ] All functional tests passed
- [ ] No console errors in frontend
- [ ] No exceptions in service logs
- [ ] System ready for demonstration

## 📝 Notes

Add any additional observations or issues here:

---

**Tested By:** _______________
**Date:** _______________
**Environment:** Development
**Status:** ⬜ Pass | ⬜ Fail | ⬜ Partial
