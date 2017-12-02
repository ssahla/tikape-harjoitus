package tikape.harjoitus.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tietokanta {

    private String databaseAddress;

    public Tietokanta(String tiedostonimi) throws ClassNotFoundException {
        this.databaseAddress = tiedostonimi;
    }

    public Connection yhteys() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void alusta() {
        List<String> lauseet = sqliteLauseet();

        try (Connection conn = yhteys()) {
            Statement st = conn.createStatement();

            for (String lause : lauseet) {
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // luodaan tarvittavat taulut, jos niitä ei jo ole
        lista.add("CREATE TABLE IF NOT EXISTS Annos (" +
                "id integer PRIMARY KEY, " +
                "nimi varchar(32))");
        lista.add("CREATE TABLE IF NOT EXISTS RaakaAine (" +
                "id integer PRIMARY KEY, " +
                "nimi varchar(32))");
        lista.add("CREATE TABLE IF NOT EXISTS AnnosRaakaAine (" +
                "raaka_aine_id integer, " +
                "annos_id integer, " +
                "jarjestys integer, " +
                "maara varchar(32), " +
                "ohje varchar(128), " +
                "FOREIGN KEY (raaka_aine_id) REFERENCES RaakaAine(id)," +
                "FOREIGN KEY (annos_id) REFERENCES Annos(id))");

        return lista;
    }
}
