package server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.model.Profesor;
import server.model.Student;

public class DBBroker {
    private static DBBroker instance;
    private String url = "jdbc:mysql://localhost:3306/test";
    private String username = "root";
    private String password = "";
    private Connection connection;

    private DBBroker() { }

    public static DBBroker getIsntance() {
        if (instance == null)
            instance = new DBBroker();
        return instance;
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public boolean closeConnection() {
    if (connection != null) {
        try {
            connection.close();
            connection = null;
            System.out.println("Konekcija sa bazom zatvorena.");
            return true;
        } catch (SQLException ex) {
            System.out.println("Greška pri zatvaranju konekcije: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    return false;
}

    public boolean updateStudent(Student student) {
        connect();
        String query = "UPDATE student SET ime = ?, prezime = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, student.getIme());
            ps.setString(2, student.getPrezime());
            ps.setInt(3, student.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    public boolean deleteStudent(int id) {
        connect();
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return false;
    }

    public List<Student> findStudent(String value) {
        connect();
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student WHERE ime LIKE ? OR prezime LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + value + "%");
            ps.setString(2, "%" + value + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                students.add(new Student(rs.getInt("id"), rs.getString("ime"),
                        rs.getString("prezime"), rs.getDate("datum_rodjenja"),
                        rs.getInt("godina_upisa"), rs.getInt("esp_bodovi"),
                        rs.getDouble("prosek")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return students;
    }
}
