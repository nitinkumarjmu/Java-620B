# JDBC Complete Application

**JDBC Complete Application** is a Java console program designed to demonstrate the power and flexibility of **Java Database Connectivity (JDBC)** with **MySQL databases**. It integrates fundamental database operations with proper software design principles and provides a hands-on experience for learning Java database programming.

---

## Key Objectives

### 1. Database Connectivity
- Establish a connection between Java and MySQL using JDBC.
- Learn to use `DriverManager`, `Connection`, `Statement`, and `ResultSet`.

### 2. CRUD Operations
- Perform **Create, Read, Update, Delete** operations on multiple tables (`Employee`, `Product`, and `Student`).
- Implement **transaction management** with `commit()` and `rollback()` for safe database updates.
- Use **PreparedStatement** for secure and parameterized SQL queries to prevent SQL injection.

### 3. MVC Architecture (Part C – Student Management)
- Apply the **Model-View-Controller (MVC) pattern** to structure Java programs.
  - **Model:** Defines the data structure (`Student` class).
  - **View:** Provides a console-based menu interface for user interaction.
  - **Controller:** Handles database operations and business logic securely.
- Ensures **separation of concerns**, making the program more maintainable and scalable.

### 4. Practical Learning Outcomes
- Understand how to integrate Java applications with MySQL.
- Handle exceptions and ensure **data integrity** using JDBC transaction control.
- Gain experience in designing **menu-driven console applications**.
- Explore **object-oriented programming concepts** combined with database operations.

### 5. Additional Highlights
- Fully **menu-driven interface** for all parts (`Employee`, `Product`, `Student`).
- **Transaction-safe operations** in Product and Student management.
- Can be **easily extended** to include more tables or features, like reporting or analytics.
- Prepares learners for **real-world database-driven Java applications**.

  ## Sample Output

============================================================
       JDBC COMPLETE APPLICATION - ALL PARTS
============================================================
Part A: Fetch Employee Data
Part B: Product CRUD with Transactions
Part C: Student Management with MVC
Exit: Quit Application
============================================================
Enter Part (A/B/C) or Exit: A

### PART A: FETCHING EMPLOYEE DATA ###

============================================================
EmpID      Name                           Salary         
============================================================
1          John Smith                     $55000.00      
2          Sarah Johnson                  $62000.00      
3          Michael Brown                  $58000.00      
4          Emily Davis                    $71000.00      
5          David Wilson                   $49000.00      
============================================================

============================================================
       JDBC COMPLETE APPLICATION - ALL PARTS
============================================================
Part A: Fetch Employee Data
Part B: Product CRUD with Transactions
Part C: Student Management with MVC
Exit: Quit Application
============================================================
Enter Part (A/B/C) or Exit: B

==================================================
    PART B: PRODUCT MANAGEMENT SYSTEM
==================================================
1. Create - Add Product
2. Read - View All Products
3. Update - Modify Product
4. Delete - Remove Product
5. Back to Main Menu
==================================================
Choice: 2

===========================================================================
ID         Name                      Price           Quantity  
===========================================================================
101        Laptop                    $899.99         15        
102        Mouse                     $25.50          50        
103        Keyboard                  $45.00          30        
104        Monitor                   $299.99         20        
105        USB Cable                 $10.99          100       
===========================================================================

Choice: 1
Product ID: 106
Product Name: Webcam
Price: 49.99
Quantity: 25
✓ Product added!

Choice: 2

===========================================================================
ID         Name                      Price           Quantity  
===========================================================================
101        Laptop                    $899.99         15        
102        Mouse                     $25.50          50        
103        Keyboard                  $45.00          30        
104        Monitor                   $299.99         20        
105        USB Cable                 $10.99          100       
106        Webcam                    $49.99          25        
===========================================================================

Choice: 3
Product ID to update: 102
New Name: Wireless Mouse
New Price: 30.00
New Quantity: 60
✓ Product updated!

Choice: 2

===========================================================================
ID         Name                      Price           Quantity  
===========================================================================
101        Laptop                    $899.99         15        
102        Wireless Mouse            $30.00          60        
103        Keyboard                  $45.00          30        
104        Monitor                   $299.99         20        
105        USB Cable                 $10.99          100       
106        Webcam                    $49.99          25        
===========================================================================

Choice: 4
Product ID to delete: 105
✓ Product deleted!

Choice: 2

===========================================================================
ID         Name                      Price           Quantity  
===========================================================================
101        Laptop                    $899.99         15        
102        Wireless Mouse            $30.00          60        
103        Keyboard                  $45.00          30        
104        Monitor                   $299.99         20        
106        Webcam                    $49.99          25        
===========================================================================

Choice: 5
Back to Main Menu

============================================================
       JDBC COMPLETE APPLICATION - ALL PARTS
============================================================
Enter Part (A/B/C) or Exit: C

==================================================
  PART C: STUDENT MANAGEMENT - MVC
==================================================
1. Add Student
2. View All Students
3. Update Student
4. Delete Student
5. Back to Main Menu
==================================================
Choice: 2

================================================================================
ID         Name                      Department           Marks     
================================================================================
1001       Alice Johnson             Computer Science     85.50     
1002       Bob Smith                 Mechanical           78.00     
1003       Charlie Brown             Computer Science     92.25     
1004       Diana Prince              Electrical           88.75     
1005       Ethan Hunt                Civil                76.50     
================================================================================

Choice: 1
Student ID: 1006
Name: Frank Castle
Department: Computer Science
Marks: 90.00
✓ Student added!

Choice: 2

================================================================================
ID         Name                      Department           Marks     
================================================================================
1001       Alice Johnson             Computer Science     85.50     
1002       Bob Smith                 Mechanical           78.00     
1003       Charlie Brown             Computer Science     92.25     
1004       Diana Prince              Electrical           88.75     
1005       Ethan Hunt                Civil                76.50     
1006       Frank Castle              Computer Science     90.00     
================================================================================

Choice: 3
Student ID to update: 1002
New Name: Robert Smith
New Department: Mechanical
New Marks: 80.00
✓ Student updated!

Choice: 2

================================================================================
ID         Name                      Department           Marks     
================================================================================
1001       Alice Johnson             Computer Science     85.50     
1002       Robert Smith              Mechanical           80.00     
1003       Charlie Brown             Computer Science     92.25     
1004       Diana Prince              Electrical           88.75     
1005       Ethan Hunt                Civil                76.50     
1006       Frank Castle              Computer Science     90.00     
================================================================================

Choice: 4
Student ID to delete: 1005
✓ Student deleted!

Choice: 2

================================================================================
ID         Name                      Department           Marks     
================================================================================
1001       Alice Johnson             Computer Science     85.50     
1002       Robert Smith              Mechanical           80.00     
1003       Charlie Brown             Computer Science     92.25     
1004       Diana Prince              Electrical           88.75     
1006       Frank Castle              Computer Science     90.00     
================================================================================

Choice: 5
Back to Main Menu

============================================================
       JDBC COMPLETE APPLICATION - ALL PARTS
============================================================
Enter Part (A/B/C) or Exit: Exit

Thank you for using the application!

