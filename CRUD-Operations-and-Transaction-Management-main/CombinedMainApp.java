// ============================================================
// PART A: SPRING DEPENDENCY INJECTION - JAVA-BASED CONFIG
// ============================================================

// Course.java
package com.example.combined;

public class Course {
    private String courseName;
    private String courseCode;
    
    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }
    
    public String getCourseDetails() {
        return "Course: " + courseName + " (Code: " + courseCode + ")";
    }
}

// StudentBean.java (renamed to avoid conflict with entity)
package com.example.combined;

public class StudentBean {
    private String name;
    private int rollNumber;
    private Course course;
    
    public StudentBean(String name, int rollNumber, Course course) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.course = course;
    }
    
    public void displayStudentInfo() {
        System.out.println("Student Name: " + name);
        System.out.println("Roll Number: " + rollNumber);
        System.out.println(course.getCourseDetails());
    }
}

// SpringDIConfig.java
package com.example.combined;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDIConfig {
    
    @Bean
    public Course course() {
        return new Course("Computer Science", "CS101");
    }
    
    @Bean
    public StudentBean studentBean() {
        return new StudentBean("John Doe", 12345, course());
    }
}

// ============================================================
// PART B: HIBERNATE CRUD OPERATIONS
// ============================================================

// Student.java - Hibernate Entity
package com.example.combined;

import javax.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int id;
    
    @Column(name = "student_name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "age")
    private int age;
    
    @Column(name = "course")
    private String course;
    
    public Student() {}
    
    public Student(String name, String email, int age, String course) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.course = course;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    
    @Override
    public String toString() {
        return "Student [ID=" + id + ", Name=" + name + ", Email=" + email + 
               ", Age=" + age + ", Course=" + course + "]";
    }
}

// HibernateUtil.java
package com.example.combined;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Transaction.class)
                .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

// StudentDAO.java
package com.example.combined;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class StudentDAO {
    
    public void saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            System.out.println("Student saved successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public Student getStudent(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(student);
            transaction.commit();
            System.out.println("Student updated successfully!");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
    
    public void deleteStudent(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                session.delete(student);
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found!");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}

// ============================================================
// PART C: SPRING + HIBERNATE TRANSACTION MANAGEMENT (BANKING)
// ============================================================

// Account.java - Entity
package com.example.combined;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;
    
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    
    @Column(name = "holder_name", nullable = false)
    private String holderName;
    
    @Column(name = "balance", nullable = false)
    private double balance;
    
    public Account() {}
    
    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }
    
    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    @Override
    public String toString() {
        return "Account [ID=" + accountId + ", Number=" + accountNumber + 
               ", Holder=" + holderName + ", Balance=$" + balance + "]";
    }
}

// Transaction.java - Entity
package com.example.combined;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;
    
    @Column(name = "from_account", nullable = false)
    private String fromAccount;
    
    @Column(name = "to_account", nullable = false)
    private String toAccount;
    
    @Column(name = "amount", nullable = false)
    private double amount;
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(name = "status")
    private String status;
    
    public Transaction() {}
    
    public Transaction(String fromAccount, String toAccount, double amount, 
                      LocalDateTime transactionDate, String status) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }
    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Transaction [ID=" + transactionId + ", From=" + fromAccount + 
               ", To=" + toAccount + ", Amount=$" + amount + 
               ", Date=" + transactionDate + ", Status=" + status + "]";
    }
}

// AccountDAO.java
package com.example.combined;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public Account findByAccountNumber(String accountNumber) {
        Session session = sessionFactory.getCurrentSession();
        Query<Account> query = session.createQuery(
            "FROM Account WHERE accountNumber = :accNum", Account.class);
        query.setParameter("accNum", accountNumber);
        return query.uniqueResult();
    }
    
    public void updateAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.update(account);
    }
    
    public void saveAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.save(account);
    }
}

// TransactionDAO.java
package com.example.combined;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    public void saveTransaction(Transaction transaction) {
        Session session = sessionFactory.getCurrentSession();
        session.save(transaction);
    }
}

// BankingService.java - Service with Transaction Management
package com.example.combined;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class BankingService {
    
    @Autowired
    private AccountDAO accountDAO;
    
    @Autowired
    private TransactionDAO transactionDAO;
    
    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, 
                             double amount) throws Exception {
        
        System.out.println("\n=== Starting Money Transfer ===");
        System.out.println("From: " + fromAccountNumber + " To: " + toAccountNumber + 
                          " Amount: $" + amount);
        
        // Find accounts
        Account fromAccount = accountDAO.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountDAO.findByAccountNumber(toAccountNumber);
        
        if (fromAccount == null) {
            throw new Exception("Source account not found: " + fromAccountNumber);
        }
        
        if (toAccount == null) {
            throw new Exception("Destination account not found: " + toAccountNumber);
        }
        
        // Check sufficient balance
        if (fromAccount.getBalance() < amount) {
            throw new Exception("Insufficient balance in account: " + fromAccountNumber);
        }
        
        // Deduct from source account
        System.out.println("Deducting $" + amount + " from " + fromAccountNumber);
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountDAO.updateAccount(fromAccount);
        
        // Add to destination account
        System.out.println("Adding $" + amount + " to " + toAccountNumber);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountDAO.updateAccount(toAccount);
        
        // Record transaction
        Transaction transaction = new Transaction(
            fromAccountNumber, toAccountNumber, amount,
            LocalDateTime.now(), "SUCCESS"
        );
        transactionDAO.saveTransaction(transaction);
        
        System.out.println("Transaction completed successfully!");
        System.out.println("New balance of " + fromAccountNumber + ": $" + 
                          fromAccount.getBalance());
        System.out.println("New balance of " + toAccountNumber + ": $" + 
                          toAccount.getBalance());
    }
    
    @Transactional
    public void createAccount(Account account) {
        accountDAO.saveAccount(account);
        System.out.println("Account created: " + account);
    }
    
    @Transactional(readOnly = true)
    public Account getAccount(String accountNumber) {
        return accountDAO.findByAccountNumber(accountNumber);
    }
}

// SpringHibernateConfig.java - Spring Configuration
package com.example.combined;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.example.combined")
public class SpringHibernateConfig {
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/combined_db");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.example.combined");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        return properties;
    }
    
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}

// ============================================================
// MAIN APPLICATION - RUNS ALL THREE PARTS
// ============================================================

// CombinedMainApp.java
package com.example.combined;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;

public class CombinedMainApp {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  SPRING & HIBERNATE COMPREHENSIVE DEMONSTRATION        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        
        // PART A: Spring Dependency Injection Demo
        runPartA();
        
        // PART B: Hibernate CRUD Operations Demo
        runPartB();
        
        // PART C: Spring + Hibernate Transaction Management Demo
        runPartC();
        
        // Cleanup
        HibernateUtil.shutdown();
    }
    
    private static void runPartA() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PART A: SPRING DEPENDENCY INJECTION (JAVA-BASED CONFIG)");
        System.out.println("=".repeat(60));
        
        ApplicationContext context = 
            new AnnotationConfigApplicationContext(SpringDIConfig.class);
        
        StudentBean student = context.getBean(StudentBean.class);
        student.displayStudentInfo();
        
        ((AnnotationConfigApplicationContext) context).close();
    }
    
    private static void runPartB() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PART B: HIBERNATE CRUD OPERATIONS");
        System.out.println("=".repeat(60));
        
        StudentDAO dao = new StudentDAO();
        
        // CREATE
        System.out.println("\n1. CREATE - Adding Students...");
        Student s1 = new Student("Alice Johnson", "alice@example.com", 20, "Computer Science");
        Student s2 = new Student("Bob Smith", "bob@example.com", 22, "Mechanical Engineering");
        dao.saveStudent(s1);
        dao.saveStudent(s2);
        
        // READ ALL
        System.out.println("\n2. READ - Fetching All Students...");
        List<Student> students = dao.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }
        
        // UPDATE
        System.out.println("\n3. UPDATE - Modifying Student...");
        if (!students.isEmpty()) {
            Student studentToUpdate = students.get(0);
            studentToUpdate.setAge(21);
            studentToUpdate.setCourse("Software Engineering");
            dao.updateStudent(studentToUpdate);
        }
        
        // READ SINGLE
        System.out.println("\n4. READ - Fetching Single Student...");
        Student student = dao.getStudent(1);
        if (student != null) {
            System.out.println(student);
        }
        
        // DELETE
        System.out.println("\n5. DELETE - Removing Student...");
        if (students.size() > 1) {
            dao.deleteStudent(students.get(1).getId());
        }
        
        // FINAL LIST
        System.out.println("\n6. Final Student List:");
        students = dao.getAllStudents();
        for (Student s : students) {
            System.out.println(s);
        }
    }
    
    private static void runPartC() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PART C: SPRING + HIBERNATE TRANSACTION MANAGEMENT");
        System.out.println("=".repeat(60));
        
        ApplicationContext context = 
            new AnnotationConfigApplicationContext(SpringHibernateConfig.class);
        
        BankingService bankingService = context.getBean(BankingService.class);
        
        try {
            // Create accounts
            System.out.println("\n1. Creating Bank Accounts...");
            Account acc1 = new Account("ACC001", "John Doe", 5000.0);
            Account acc2 = new Account("ACC002", "Jane Smith", 3000.0);
            bankingService.createAccount(acc1);
            bankingService.createAccount(acc2);
            
            // Display initial balances
            System.out.println("\n2. Initial Account Balances:");
            Account john = bankingService.getAccount("ACC001");
            Account jane = bankingService.getAccount("ACC002");
            System.out.println(john);
            System.out.println(jane);
            
            // Successful transfer
            System.out.println("\n3. Performing Successful Transfer...");
            bankingService.transferMoney("ACC001", "ACC002", 500.0);
            
            // Display final balances
            System.out.println("\n4. Final Account Balances:");
            john = bankingService.getAccount("ACC001");
            jane = bankingService.getAccount("ACC002");
            System.out.println(john);
            System.out.println(jane);
            
            // Test insufficient balance (rollback scenario)
            System.out.println("\n5. Testing Transaction Rollback (Insufficient Balance)...");
            try {
                bankingService.transferMoney("ACC001", "ACC002", 10000.0);
            } catch (Exception e) {
                System.out.println("❌ Transaction Failed: " + e.getMessage());
                System.out.println("✓ All changes have been rolled back!");
            }
            
            // Verify balances unchanged after failed transaction
            System.out.println("\n6. Balances After Failed Transaction (Should be unchanged):");
            john = bankingService.getAccount("ACC001");
            jane = bankingService.getAccount("ACC002");
            System.out.println(john);
            System.out.println(jane);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((AnnotationConfigApplicationContext) context).close();
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL DEMONSTRATIONS COMPLETED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
    }
}

/*
 * ============================================================
 * CONFIGURATION FILES NEEDED
 * ============================================================
 * 
 * 1. hibernate.cfg.xml (place in src/main/resources):
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE hibernate-configuration PUBLIC
 *     "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
 *     "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
 * <hibernate-configuration>
 *     <session-factory>
 *         <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
 *         <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/combined_db</property>
 *         <property name="hibernate.connection.username">root</property>
 *         <property name="hibernate.connection.password">password</property>
 *         <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
 *         <property name="hibernate.hbm2ddl.auto">update</property>
 *         <property name="hibernate.show_sql">true</property>
 *         <property name="hibernate.format_sql">true</property>
 *     </session-factory>
 * </hibernate-configuration>
 * 
 * ============================================================
 * 2. pom.xml dependencies:
 * ============================================================
 * 
 * <dependencies>
 *     <!-- Spring Context -->
 *     <dependency>
 *         <groupId>org.springframework</groupId>
 *         <artifactId>spring-context</artifactId>
 *         <version>5.3.20</version>
 *     </dependency>
 *     
 *     <!-- Spring ORM -->
 *     <dependency>
 *         <groupId>org.springframework</groupId>
 *         <artifactId>spring-orm</artifactId>
 *         <version>5.3.20</version>
 *     </dependency>
 *     
 *     <!-- Spring JDBC -->
 *     <dependency>
 *         <groupId>org.springframework</groupId>
 *         <artifactId>spring-jdbc</artifactId>
 *         <version>5.3.20</version>
 *     </dependency>
 *     
 *     <!-- Hibernate Core -->
 *     <dependency>
 *         <groupId>org.hibernate</groupId>
 *         <artifactId>hibernate-core</artifactId>
 *         <version>5.6.10.Final</version>
 *     </dependency>
 *     
 *     <!-- MySQL Connector -->
 *     <dependency>
 *         <groupId>mysql</groupId>
 *         <artifactId>mysql-connector-java</artifactId>
 *         <version>8.0.29</version>
 *     </dependency>
 * </dependencies>
 * 
 * ============================================================
 * 3. Database Setup (MySQL):
 * ============================================================
 * 
 * CREATE DATABASE combined_db;
 * USE combined_db;
 * 
 * -- Tables will be auto-created by Hibernate (hbm2ddl.auto=update)
 * 
 * ============================================================
 * PROJECT STRUCTURE:
 * ============================================================
 * 
 * src/main/java/com/example/combined/
 * ├── Course.java
 * ├── StudentBean.java
 * ├── SpringDIConfig.java
 * ├── Student.java (Hibernate Entity)
 * ├── HibernateUtil.java
 * ├── StudentDAO.java
 * ├── Account.java
 * ├── Transaction.java
 * ├── AccountDAO.java
 * ├── TransactionDAO.java
 * ├── BankingService.java
 * ├── SpringHibernateConfig.java
 * └── CombinedMainApp.java (MAIN CLASS - RUN THIS)
 * 
 * src/main/resources/
 * └── hibernate.cfg.xml
 * 
 * ============================================================
 * HOW TO RUN:
 * ============================================================
 * 
 * 1. Create MySQL database: combined_db
 * 2. Update database credentials in:
 *    - hibernate.cfg.xml
 *    - SpringHibernateConfig.java
 * 3. Add Maven dependencies to pom.xml
 * 4. Run: CombinedMainApp.java
 * 
 * The application will execute all three parts sequentially:
 * - Part A: Spring DI demonstration
 * - Part B: Hibernate CRUD operations
 * - Part C: Banking system with transaction management
 */