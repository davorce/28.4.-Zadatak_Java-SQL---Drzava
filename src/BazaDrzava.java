

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class BazaDrzava {

    public static void main(String[] args) {

        DataSource dataSource = createDataSource();
        Scanner scan = new Scanner(System.in);

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspjesno ste spojeni na bazu podataka!");
            while (true) {
                System.out.println("\nIzbornik:");
                System.out.println("1. Unos nove drzave");
                System.out.println("2. Izmjena postojece drzave");
                System.out.println("3. Brisanje postojece drzave");
                System.out.println("4. Prikaz svih drzava (A-Ž)");
                System.out.println("5. Kraj programa");

                System.out.print("\nOdaberi opciju: ");
                int opcija = scan.nextInt();

                switch (opcija) {
                    case 1:
                        novaDrzava(connection);
                        break;
                    case 2:
                        izmjenaDrzave(connection);
                        break;
                    case 3:
                        brisanjeDrzave(connection);
                        break;
                    case 4:
                        ispisDrzava(connection);
                        break;
                    case 5:
                        System.out.println("\nHvala, bok!");
                        return;
                    default:
                        System.out.println("\nOdaberi opciju od 1 do 5!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Greska prilikom spajanja na bazu podataka!");
            e.printStackTrace();
        }
    }

    private static void novaDrzava(Connection connection) throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Unesi naziv nove drzave: ");
        String naziv = scan.nextLine();

        String sql = "INSERT INTO Drzava (Naziv) VALUES(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, naziv);
            stmt.executeUpdate();
            System.out.println("Drzava je uspjesno dodana.");
        }
    }

    private static void izmjenaDrzave(Connection connection) throws SQLException {
        System.out.println("Unesi ID drzave koju zelis promijeniti (ID > 3): ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        if (id < 3) {
            System.out.println("Nije dopusteno promijeniti naziv odabrane drzave!");
            return;
        }
        System.out.println("Unesi novi naziv drzave: ");
        Scanner scan1 = new Scanner(System.in);
        String noviNaziv = scan1.nextLine();

        String sql = "UPDATE Drzava SET Naziv = ? WHERE IDDrzava = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, noviNaziv);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Naziv drzave je uspjesno izmijenjen!");
            } else {
                System.out.println("Drzava s tim IDom ne postoji!");
            }
        }
    }

    private static void brisanjeDrzave(Connection connection) throws SQLException {
        System.out.println("Unesi ID drzave koju zelis promijeniti (ID > 3): ");
        Scanner scan = new Scanner(System.in);
        int id = scan.nextInt();

        if (id < 3) {
            System.out.println("Nije dopusteno obrisati odabranu drzavu!");
            return;
        }
        String sql = "DELETE FROM Drzava WHERE IDDrzava = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Drzava je izbrisana!");
            } else {
                System.out.println("Drzava s tim IDom ne postoji!");
            }
        }
    }

    private static void ispisDrzava(Connection connection) throws SQLException {
        String sql = "SELECT IDDrzava, Naziv FROM Drzava ORDER BY Naziv";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n*** Popis drzava ***");
            while (rs.next()) {
                int id = rs.getInt("IDDrzava");
                String naziv = rs.getString("Naziv");
                System.out.printf("%d %s\n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
            }
        }

    }


    // ********** METODA ZA SPAJANJE NA BAZU **********
    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        //ds.setPortNumber(1433);
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }

}



