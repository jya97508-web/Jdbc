package com.example.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.Pattern;

@WebServlet("/api/students")
public class StudentServlet extends HttpServlet {

    private static final Pattern INTEGER = Pattern.compile("\\d+");

    private static String jsonEscape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace(""", "\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    private static void jsonHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        jsonHeaders(response);

        String sql = "SELECT id, name, course, marks FROM student ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             PrintWriter out = response.getWriter()) {

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                    .append(""id":").append(rs.getInt("id")).append(",")
                    .append(""name":"").append(jsonEscape(rs.getString("name"))).append("",")
                    .append(""course":"").append(jsonEscape(rs.getString("course"))).append("",")
                    .append(""marks":").append(rs.getInt("marks"))
                    .append("}");
            }

            json.append("]");
            out.print(json);
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().print("{"error":"Database error"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        jsonHeaders(response);

        String name = request.getParameter("name");
        String course = request.getParameter("course");
        String marksText = request.getParameter("marks");

        if (name == null || name.isBlank() ||
            course == null || course.isBlank() ||
            marksText == null || !INTEGER.matcher(marksText).matches()) {
            response.setStatus(400);
            response.getWriter().print("{"error":"Enter valid name, course and marks"}");
            return;
        }

        int marks = Integer.parseInt(marksText);

        String sql = "INSERT INTO student(name, course, marks) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, course.trim());
            ps.setInt(3, marks);
            ps.executeUpdate();

            response.setStatus(201);
            response.getWriter().print("{"message":"Student added successfully"}");
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().print("{"error":"Could not add student"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        jsonHeaders(response);

        String idText = request.getParameter("id");
        String name = request.getParameter("name");
        String course = request.getParameter("course");
        String marksText = request.getParameter("marks");

        if (idText == null || !INTEGER.matcher(idText).matches() ||
            name == null || name.isBlank() ||
            course == null || course.isBlank() ||
            marksText == null || !INTEGER.matcher(marksText).matches()) {
            response.setStatus(400);
            response.getWriter().print("{"error":"Invalid data"}");
            return;
        }

        int id = Integer.parseInt(idText);
        int marks = Integer.parseInt(marksText);

        String sql = "UPDATE student SET name=?, course=?, marks=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, course.trim());
            ps.setInt(3, marks);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                response.setStatus(404);
                response.getWriter().print("{"error":"Student not found"}");
            } else {
                response.getWriter().print("{"message":"Student updated successfully"}");
            }
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().print("{"error":"Could not update student"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        jsonHeaders(response);

        String idText = request.getParameter("id");

        if (idText == null || !INTEGER.matcher(idText).matches()) {
            response.setStatus(400);
            response.getWriter().print("{"error":"Invalid student ID"}");
            return;
        }

        int id = Integer.parseInt(idText);
        String sql = "DELETE FROM student WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                response.setStatus(404);
                response.getWriter().print("{"error":"Student not found"}");
            } else {
                response.getWriter().print("{"message":"Student deleted successfully"}");
            }
        } catch (SQLException e) {
            response.setStatus(500);
            response.getWriter().print("{"error":"Could not delete student"}");
            e.printStackTrace();
        }
    }
}
