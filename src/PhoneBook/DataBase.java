package PhoneBook;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase {
//<editor-fold defaultstate="collapsed" desc="Class variables">
    private final String URL = "jdbc:derby:PhoneBookDataBase;create=true";
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
//</editor-fold>
    public DataBase() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        if (conn != null) {
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println(ex);
              }
        }
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        try {
            ResultSet rs = dbmd.getTables(null, "APP", "CONTACTS", null);
            if (!rs.next()) {
                createStatement.execute("CREATE TABLE Contacts (ID INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), FirstName varchar(20), LastName varchar(20), Email varchar(40))");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
    public int createPerson(Person person) {
        int id = 0;
        try {
            String sql = "INSERT INTO Contacts (FirstName, LastName, Email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, person.getFirstName());
            pstmt.setString(2, person.getLastName());
            pstmt.setString(3, person.getEmail());
            pstmt.execute();
            ResultSet rs = createStatement.executeQuery("SELECT MAX(ID) FROM Contacts");
            id = rs.next() ? rs.getInt(1) : id;
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        System.out.println("Registered with ID: " + id);
        return id;
    }
    public ArrayList<Person> readPersons() {
        String sql = "SELECT * FROM Contacts";
        ArrayList<Person> contacts = null;
        try {
            contacts = new ArrayList<>();
            ResultSet rs = createStatement.executeQuery(sql);
            while (rs.next()) {
                contacts.add(new Person(rs.getInt("ID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Email")));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
          }
        return contacts;
    }
    public void updatePerson(Person person) {
        try {
            String sql = "UPDATE Contacts SET FirstName = ?, LastName = ?, Email = ? WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, person.getFirstName());
            pstmt.setString(2, person.getLastName());
            pstmt.setString(3, person.getEmail());
            pstmt.setInt(4, Integer.parseInt(person.getID()));
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
    public void deletePerson(Person person) {
        try {
            String sql = "DELETE FROM Contacts WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(person.getID()));
            pstmt.execute();
        } catch (SQLException ex) {
            System.out.println(ex);
          }
    }
}