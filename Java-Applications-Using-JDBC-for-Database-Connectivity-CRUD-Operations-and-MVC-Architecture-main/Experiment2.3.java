import java.sql.*;
import java.util.Scanner;

// ============================================================================
// PART A: Simple Employee Data Fetcher
// ============================================================================
class PartA_EmployeeFetcher {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    public static void execute() {
        System.out.println("\n### PART A: FETCHING EMPLOYEE DATA ###\n");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT EmpID, Name, Salary FROM Employee")) {
            
            System.out.println("=".repeat(60));
            System.out.printf("%-10s %-30s %-15s%n", "EmpID", "Name", "Salary");
            System.out.println("=".repeat(60));
            
            while (rs.next()) {
                System.out.printf("%-10d %-30s $%-14.2f%n", 
                    rs.getInt("EmpID"),
                    rs.getString("Name"),
                    rs.getDouble("Salary"));
            }
            System.out.println("=".repeat(60));
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

// ============================================================================
// PART B: Product CRUD with Transaction Management
// ============================================================================
class PartB_ProductCRUD {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    private Connection conn;
    private Scanner sc;
    
    public PartB_ProductCRUD() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false);
        sc = new Scanner(System.in);
    }
    
    public void showMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("    PART B: PRODUCT MANAGEMENT SYSTEM");
            System.out.println("=".repeat(50));
            System.out.println("1. Create - Add Product");
            System.out.println("2. Read - View All Products");
            System.out.println("3. Update - Modify Product");
            System.out.println("4. Delete - Remove Product");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(50));
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            try {
                switch (choice) {
                    case 1: create(); break;
                    case 2: read(); break;
                    case 3: update(); break;
                    case 4: delete(); break;
                    case 5: return;
                    default: System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void create() throws SQLException {
        System.out.print("Product ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Product Name: ");
        String name = sc.nextLine();
        System.out.print("Price: ");
        double price = sc.nextDouble();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();
        
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Product VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);
            ps.executeUpdate();
            conn.commit();
            System.out.println("✓ Product added!");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
    
    private void read() throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {
            
            System.out.println("\n" + "=".repeat(75));
            System.out.printf("%-10s %-25s %-15s %-10s%n", 
                "ID", "Name", "Price", "Quantity");
            System.out.println("=".repeat(75));
            
            while (rs.next()) {
                System.out.printf("%-10d %-25s $%-14.2f %-10d%n",
                    rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4));
            }
            System.out.println("=".repeat(75));
        }
    }
    
    private void update() throws SQLException {
        System.out.print("Product ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Price: ");
        double price = sc.nextDouble();
        System.out.print("New Quantity: ");
        int qty = sc.nextInt();
        
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?")) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.setInt(4, id);
            
            int rows = ps.executeUpdate();
            conn.commit();
            
            if (rows > 0) System.out.println("✓ Product updated!");
            else System.out.println("✗ Product not found!");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
    
    private void delete() throws SQLException {
        System.out.print("Product ID to delete: ");
        int id = sc.nextInt();
        
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Product WHERE ProductID=?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            conn.commit();
            
            if (rows > 0) System.out.println("✓ Product deleted!");
            else System.out.println("✗ Product not found!");
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
    
    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

// ============================================================================
// PART C: Student Management with MVC Architecture
// ============================================================================

// MODEL
class Student {
    private int studentId;
    private String name;
    private String department;
    private double marks;
    
    public Student(int studentId, String name, String department, double marks) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
    
    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
    
    public boolean isValid() {
        return studentId > 0 && name != null && !name.trim().isEmpty() &&
               department != null && marks >= 0 && marks <= 100;
    }
}

// CONTROLLER
class StudentController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/school_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    private Connection conn;
    
    public StudentController() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public boolean addStudent(Student s) {
        if (!s.isValid()) return false;
        
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Student VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setDouble(4, s.getMarks());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public void displayAllStudents() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Student ORDER BY StudentID")) {
            
            System.out.println("\n" + "=".repeat(80));
            System.out.printf("%-10s %-25s %-20s %-10s%n", 
                "ID", "Name", "Department", "Marks");
            System.out.println("=".repeat(80));
            
            while (rs.next()) {
                System.out.printf("%-10d %-25s %-20s %-10.2f%n",
                    rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4));
            }
            System.out.println("=".repeat(80));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public boolean updateStudent(int id, String name, String dept, double marks) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?")) {
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, marks);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteStudent(int id) {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Student WHERE StudentID=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

// VIEW
class StudentView {
    private StudentController controller;
    private Scanner sc;
    
    public StudentView() throws SQLException {
        controller = new StudentController();
        sc = new Scanner(System.in);
    }
    
    public void showMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("  PART C: STUDENT MANAGEMENT - MVC");
            System.out.println("=".repeat(50));
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(50));
            System.out.print("Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1: addStudent(); break;
                case 2: controller.displayAllStudents(); break;
                case 3: updateStudent(); break;
                case 4: deleteStudent(); break;
                case 5: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    private void addStudent() {
        System.out.print("Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Marks: ");
        double marks = sc.nextDouble();
        
        Student s = new Student(id, name, dept, marks);
        if (controller.addStudent(s)) {
            System.out.println("✓ Student added!");
        } else {
            System.out.println("✗ Failed to add student!");
        }
    }
    
    private void updateStudent() {
        System.out.print("Student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Department: ");
        String dept = sc.nextLine();
        System.out.print("New Marks: ");
        double marks = sc.nextDouble();
        
        if (controller.updateStudent(id, name, dept, marks)) {
            System.out.println("✓ Student updated!");
        } else {
            System.out.println("✗ Student not found!");
        }
    }
    
    private void deleteStudent() {
        System.out.print("Student ID to delete: ");
        int id = sc.nextInt();
        
        if (controller.deleteStudent(id)) {
            System.out.println("✓ Student deleted!");
        } else {
            System.out.println("✗ Student not found!");
        }
    }
    
    public void close() throws SQLException {
        controller.close();
    }
}

// ============================================================================
// MAIN CLASS
// ============================================================================
public class JDBCCompleteApplication {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("       JDBC COMPLETE APPLICATION - ALL PARTS");
            System.out.println("=".repeat(60));
            System.out.println("Part A: Fetch Employee Data");
            System.out.println("Part B: Product CRUD with Transactions");
            System.out.println("Part C: Student Management with MVC");
            System.out.println("Exit: Quit Application");
            System.out.println("=".repeat(60));
            System.out.print("Enter Part (A/B/C) or Exit: ");
            
            String choice = sc.nextLine().trim().toUpperCase();
            
            try {
                switch (choice) {
                    case "A":
                        PartA_EmployeeFetcher.execute();
                        break;
                        
                    case "B":
                        PartB_ProductCRUD partB = new PartB_ProductCRUD();
                        partB.showMenu();
                        partB.close();
                        break;
                        
                    case "C":
                        StudentView partC = new StudentView();
                        partC.showMenu();
                        partC.close();
                        break;
                        
                    case "EXIT":
                        System.out.println("\nThank you for using the application!");
                        sc.close();
                        return;
                        
                    default:
                        System.out.println("Invalid choice! Enter A, B, C, or Exit");
                }
            } catch (SQLException e) {
                System.err.println("\nDatabase Error: " + e.getMessage());
                System.err.println("Please check:");
                System.err.println("1. MySQL server is running");
                System.err.println("2. Databases and tables are created");
                System.err.println("3. Database credentials are correct");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}

/*
=============================================================================
SQL SETUP SCRIPTS - Run these before executing the Java program
=============================================================================

-- PART A: Employee Table
CREATE DATABASE IF NOT EXISTS company_db;
USE company_db;

CREATE TABLE Employee (
    EmpID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Salary DECIMAL(10, 2) NOT NULL
);

INSERT INTO Employee (Name, Salary) VALUES
('John Smith', 55000.00),
('Sarah Johnson', 62000.00),
('Michael Brown', 58000.00),
('Emily Davis', 71000.00),
('David Wilson', 49000.00);

-- PART B: Product Table
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

CREATE TABLE Product (
    ProductID INT PRIMARY KEY,
    ProductName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Quantity INT NOT NULL
);

INSERT INTO Product VALUES
(101, 'Laptop', 899.99, 15),
(102, 'Mouse', 25.50, 50),
(103, 'Keyboard', 45.00, 30),
(104, 'Monitor', 299.99, 20),
(105, 'USB Cable', 10.99, 100);

-- PART C: Student Table
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

CREATE TABLE Student (
    StudentID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Department VARCHAR(50) NOT NULL,
    Marks DECIMAL(5, 2) NOT NULL CHECK (Marks >= 0 AND Marks <= 100)
);

INSERT INTO Student VALUES
(1001, 'Alice Johnson', 'Computer Science', 85.50),
(1002, 'Bob Smith', 'Mechanical', 78.00),
(1003, 'Charlie Brown', 'Computer Science', 92.25),
(1004, 'Diana Prince', 'Electrical', 88.75),
(1005, 'Ethan Hunt', 'Civil', 76.50);

=============================================================================
IMPORTANT NOTES:
=============================================================================
1. Update DB_URL, DB_USER, DB_PASSWORD in each class as per your setup
2. Add MySQL JDBC Driver to classpath: mysql-connector-java-8.x.x.jar
3. For online compilers like JDoodle or OnlineGDB:
   - They may not support MySQL connections
   - You'll need a MySQL server accessible from the internet
   - Consider using db4free.net for testing (free MySQL hosting)
   
4. Alternative for online testing without MySQL:
   - Use H2 Database (embedded Java database)
   - Or SQLite with JDBC
   
5. To run locally:
   javac JDBCCompleteApplication.java
   java -cp .:mysql-connector-java-8.0.33.jar JDBCCompleteApplication
=============================================================================
*/