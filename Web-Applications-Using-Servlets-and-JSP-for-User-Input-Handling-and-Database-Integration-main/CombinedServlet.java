import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = {"/login", "/employee", "/attendance"})
public class CombinedServlet extends HttpServlet {

    // Database details
    String EMP_DB_URL = "jdbc:mysql://localhost:3306/company";
    String ATT_DB_URL = "jdbc:mysql://localhost:3306/school";
    String DB_USER = "root";
    String DB_PASS = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String path = request.getServletPath();

        if (path.equals("/employee")) {
            handleEmployee(out, request);
        } else {
            out.println("<html><body><h2>Invalid GET request!</h2></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String path = request.getServletPath();

        if (path.equals("/login")) {
            handleLogin(out, request);
        } else if (path.equals("/attendance")) {
            handleAttendance(out, request);
        } else {
            out.println("<html><body><h2>Invalid POST request!</h2></body></html>");
        }
    }

    // ========== LOGIN SECTION ==========
    void handleLogin(PrintWriter out, HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        out.println("<html><body>");
        if ("admin".equals(username) && "pass123".equals(password)) {
            out.println("<h2>Welcome, " + username + "!</h2>");
            out.println("<p>Login successful.</p>");
        } else {
            out.println("<h2>Login Failed</h2>");
            out.println("<p>Invalid username or password.</p>");
            out.println("<a href='login.html'>Try Again</a>");
        }
        out.println("</body></html>");
    }

    // ========== EMPLOYEE SECTION ==========
    void handleEmployee(PrintWriter out, HttpServletRequest request) {
        String empId = request.getParameter("empId");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(EMP_DB_URL, DB_USER, DB_PASS);

            out.println("<html><body>");
            out.println("<h2>Employee Records</h2>");
            out.println("<form method='get'>");
            out.println("Search by ID: <input type='text' name='empId'>");
            out.println("<input type='submit' value='Search'></form><hr>");

            String query;
            if (empId != null && !empId.isEmpty()) {
                query = "SELECT * FROM Employee WHERE EmpID = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(empId));
                ResultSet rs = ps.executeQuery();
                displayTable(out, rs);
            } else {
                query = "SELECT * FROM Employee";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                displayTable(out, rs);
            }

            conn.close();
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    void displayTable(PrintWriter out, ResultSet rs) throws SQLException {
        out.println("<table border='1'>");
        out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getInt("EmpID") + "</td>");
            out.println("<td>" + rs.getString("Name") + "</td>");
            out.println("<td>" + rs.getDouble("Salary") + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }

    // ========== ATTENDANCE SECTION ==========
    void handleAttendance(PrintWriter out, HttpServletRequest request) {
        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(ATT_DB_URL, DB_USER, DB_PASS);

            String query = "INSERT INTO Attendance (StudentID, Date, Status) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(studentId));
            ps.setString(2, date);
            ps.setString(3, status);

            int rows = ps.executeUpdate();

            out.println("<html><body>");
            if (rows > 0) {
                out.println("<h2>Success!</h2>");
                out.println("<p>Attendance marked for Student ID: " + studentId + "</p>");
                out.println("<p>Date: " + date + "</p>");
                out.println("<p>Status: " + status + "</p>");
            } else {
                out.println("<h2>Failed to mark attendance</h2>");
            }
            out.println("<br><a href='attendance.jsp'>Mark Another</a>");
            out.println("</body></html>");

            conn.close();

        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h2>Error</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</body></html>");
        }
    }
}
