import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Brisanje {
    public static void main(String[] args) {

        DataSource dataSource = createDataSource();

        //brisanje
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno ste spojeni na bazu podataka za brisanje.");

            String deleteSql = "DELETE FROM Drzava WHERE IDDrzava >= 10";
            try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
                int rowsAffected = stmt.executeUpdate();
                System.out.printf("%d redova je obrisano iz tabele Drzava (IDDrzava >= 10).\n", rowsAffected);
            }

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja ili brisanja podataka.");
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
        //ds.setPortNumber(1433); // Uncomment and set if your SQL Server is on a non-default port
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);

        return ds;
    }
}