// Database Setup (MySQL)

CREATE TABLE employees (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    department VARCHAR(100)
);


INSERT INTO employees VALUES
(101, 'Aarav Sharma', 'aarav@company.com', 'Engineering'),
(102, 'Isha Mehta', 'isha@company.com', 'HR'),
(103, 'Dev Patel', 'dev@company.com', 'Finance');
// HTML Form (employee.html)

<!DOCTYPE html>
<html>
<head>
    <title>Employee Portal</title>
</head>
<body>
    <h2>Search Employee by ID</h2>
    <form action="EmployeeServlet" method="get">
        Enter Employee ID: <input type="text" name="empId">
        <input type="submit" value="Search">
    </form>

    <hr><br>

    <form action="EmployeeServlet" method="get">
        <input type="hidden" name="action" value="all">
        <input type="submit" value="Show All Employees">
    </form>
</body>
</html>
// Servlet Code (EmployeeServlet.java)

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    private Connection conn;

    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/your_database", "root", "your_password");
        } catch (Exception e) {
            throw new ServletException("DB Connection Error: " + e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String empId = request.getParameter("empId");
        String action = request.getParameter("action");

        try {
            if ("all".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees");
                ResultSet rs = ps.executeQuery();

                out.println("<h2>All Employees</h2>");
                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" +
                        rs.getString("name") + "</td><td>" +
                        rs.getString("email") + "</td><td>" +
                        rs.getString("department") + "</td></tr>");
                }
                out.println("</table>");
            } else if (empId != null && !empId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
                ps.setInt(1, Integer.parseInt(empId));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<h2>Employee Found:</h2>");
                    out.println("ID: " + rs.getInt("id") + "<br>");
                    out.println("Name: " + rs.getString("name") + "<br>");
                    out.println("Email: " + rs.getString("email") + "<br>");
                    out.println("Department: " + rs.getString("department") + "<br>");
                } else {
                    out.println("<h3>No employee found with ID: " + empId + "</h3>");
                }
            } else {
                out.println("<h3>Please enter an ID or click 'Show All'</h3>");
            }
        } catch (SQLException e) {
            out.println("<h3>Database error: " + e.getMessage() + "</h3>");
        }
    }

    public void destroy() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
// web.xml Configuration

<web-app>
    <servlet>
        <servlet-name>EmployeeServlet</servlet-name>
        <servlet-class>EmployeeServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>EmployeeServlet</servlet-name>
        <url-pattern>/EmployeeServlet</url-pattern>
    </servlet-mapping>
</web-app>
