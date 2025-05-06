import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/*KOD ZA BAZU:
      CREATE PROCEDURE brisiNoveDrzave @idDrzava int
                AS
       BEGIN
       DELETE FROM Drzava WHERE IDDrzava >= @idDrzava
                END
        GO*/

public class DodajBrisiDrzave {


    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }

    public static void main(String[] args) {

         DataSource dataSource = createDataSource();

        Scanner sc = new Scanner(System.in);
        int unos;

        while (true) {
            System.out.println("\nOdaberite akciju:");
            System.out.println("1. Unesi države");
            System.out.println("2. Izbriši države");
            System.out.println("3. Izlaz");
            System.out.print("Vaš odabir: ");

            if (sc.hasNextInt()) {
                unos = sc.nextInt();

                if (unos == 3) {
                    System.out.println("Izlaz iz aplikacije.");
                    break;
                }


                try (Connection connection = dataSource.getConnection()) {
                    System.out.println("Uspješno ste spojeni na bazu.");

                    switch (unos) {
                        case 1:
                            unesiDrzavu(connection);
                            printajDrzave(connection);
                            break;
                        case 2:
                            System.out.print("Unesite ID od kojeg se žele izbrisati države: ");
                            if (sc.hasNextInt()) {
                                int idToDelete = sc.nextInt();
                                brisanjeDrzava(connection, idToDelete);
                                printajDrzave(connection);
                            } else {
                                System.out.println("Neispravan unos za ID.");
                                sc.next();
                            }
                            break;
                        default:
                            System.out.println("Neispravan unos.");
                    }
                } catch (SQLException e) {
                    System.err.println("Greška prilikom spajanja na bazu podataka.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Neispravan unos. Molimo unesite broj.");
                sc.next();
            }
        }

        sc.close();
    }
//UNOS DRZAVA
    private static void unesiDrzavu(Connection connection) {

        ArrayList<String> drzave = new ArrayList<>();
        drzave.add("Argentina");
        drzave.add("Brazil");
        drzave.add("Čile");
        drzave.add("Australija");
        drzave.add("Novi Zeland");
        drzave.add("Japan");
        drzave.add("Sjeverna Koreja");
        drzave.add("Južna Koreja");
        drzave.add("Finska");
        drzave.add("Danska");

        System.out.println("Početak dodavanja država...");
        String insertSql = "INSERT INTO Drzava (Naziv) VALUES(?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            for (String s : drzave) {
                ps.setString(1, s);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Greška prilikom unosa država.");
            e.printStackTrace();
        }
        System.out.println("Uspješno dodavanje država.");



    }
//BRISANJE DRZAVA
    private static void brisanjeDrzava(Connection connection, int id) {

        System.out.println("Početak brisanja država pomoću pohranjene procedure...");

        String storedProcedureCall = "{call BrisanjeDrzava(?)}";
        try (CallableStatement cs = connection.prepareCall(storedProcedureCall)) {
            cs.setInt(1, id);

            cs.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Greška prilikom pozivanja pohranjene procedure za brisanje.");
            e.printStackTrace();
        }
        System.out.println("Uspješno pozvana pohranjena procedura za brisanje država sa ID >= " + id);
    }

//ISPISI DRZAVE
    public static void printajDrzave(Connection connection) throws SQLException {



            try (PreparedStatement stmt = connection.prepareStatement("SELECT IDDrzava, Naziv FROM Drzava");
                 ResultSet rs = stmt.executeQuery()) {

                System.out.println("Države iz baze podataka:");
                while (rs.next()) {
                    int id = rs.getInt("IDDrzava");
                    String name = rs.getString("Naziv");
                    System.out.printf("%d, %s\n", id, name);
                }
            }


    }
}