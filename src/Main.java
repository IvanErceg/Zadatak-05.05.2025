import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        ArrayList<String> drzava = new ArrayList<>();
        drzava.add("Bjelorusija");
        drzava.add("Japan");
        drzava.add("Škotska");
        drzava.add("Italija");
        drzava.add("Španjolska");
        drzava.add("Ujedinjeno Kraljevstvo");
        drzava.add("Nizozemska");
        drzava.add("Belgija");
        drzava.add("Švicarska");
        drzava.add("Austrija");

        DataSource dataSource = createDataSource();

        //unos drzave
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno ste spojeni na bazu podataka za umetanje.");

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Drzava (Naziv) VALUES (?)");
            for (String imeDrzave : drzava) {
                stmt.setString(1, imeDrzave);
                stmt.executeUpdate();
            }
            System.out.println("Podaci o državama su uspješno umetnuti.");

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja ili umetanja podataka.");
            e.printStackTrace();
        }

        // printanje
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("\nUspješno ste spojeni na bazu podataka za čitanje.");


            try (PreparedStatement stmt = connection.prepareStatement("SELECT IDDrzava, Naziv FROM Drzava");
                 ResultSet rs = stmt.executeQuery()) {

                System.out.println("Države iz baze podataka:");
                while (rs.next()) {
                    int id = rs.getInt("IDDrzava");
                    String name = rs.getString("Naziv");
                    System.out.printf("%d, %s\n", id, name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja ili čitanja podataka.");
            e.printStackTrace();
        }
    }


    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        //ds.setPortNumber(1433); //
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false); //

        return ds;
    }
}