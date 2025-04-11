// Database Setup (MySQL)

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    roll_number VARCHAR(50),
    subject VARCHAR(100),
    attendance_date DATE,
    status VARCHAR(20)
);
// attendance.jsp – JSP Page (Student Portal Form)

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Attendance Portal</title>
</head>
<body>
    <h2>Mark Attendance</h2>
    <form action="AttendanceServlet" method="post">
        Name: <input type="text" name="student_name" required><br><br>
        Roll No: <input type="text" name="roll_number" required><br><br>
        Subject: <input type="text" name="subject" required><br><br>
        Date: <input type="date" name="attendance_date" required><br><br>
        Status:
        <select name="status">
            <option value="Present">Present</option>
            <option value="Absent">Absent</option>
        </select><br><br>
        <input type="submit" value="Submit Attendance">
    </form>
</body>
</html>
// AttendanceServlet.java – Backend Handler Servlet

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("student_name");
        String roll = request.getParameter("roll_number");
        String subject = request.getParameter("subject");
        String date = request.getParameter("attendance_date");
        String status = request.getParameter("status");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/your_database", "root", "your_password");

            String sql = "INSERT INTO attendance (student_name, roll_number, subject, attendance_date, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, roll);
            ps.setString(3, subject);
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.setString(5, status);

            int result = ps.executeUpdate();

            out.println("<html><body>");
            if (result > 0) {
                out.println("<h3>Attendance marked successfully!</h3>");
            } else {
                out.println("<h3>Failed to record attendance. Try again.</h3>");
            }
            out.println("</body></html>");

            conn.close();
        } catch (Exception e) {
            out.println("<html><body><h3>Error: " + e.getMessage() + "</h3></body></html>");
        }
    }
}
//web.xml Servlet Mapping

<web-app>
    <servlet>
        <servlet-name>AttendanceServlet</servlet-name>
        <servlet-class>AttendanceServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>AttendanceServlet</servlet-name>
        <url-pattern>/AttendanceServlet</url-pattern>
    </servlet-mapping>
</web-app>
