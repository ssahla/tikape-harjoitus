/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.harjoitus.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.harjoitus.domain.Annos;

public class AnnosDao implements Dao<Annos, Integer> {

    private Tietokanta tk;

    public AnnosDao(Tietokanta tk) {
        this.tk = tk;
    }

    @Override
    public Annos haeYksi(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }

        Integer id = res.getInt("id");
        String nimi = res.getString("nimi");

        Annos a = new Annos(id, nimi);

        res.close();
        stmt.close();
        conn.close();

        return a;
    }

    public Annos haeYksiNimella(String nimi) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }

        Integer id = res.getInt("id");
        Annos a = new Annos(id, nimi);

        res.close();
        stmt.close();
        conn.close();

        return a;
    }

    @Override
    public List<Annos> haeKaikki() throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos ORDER BY nimi");

        ResultSet res = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (res.next()) {
            Integer id = res.getInt("id");
            String nimi = res.getString("nimi");

            annokset.add(new Annos(id, nimi));
        }

        res.close();
        stmt.close();
        conn.close();

        return annokset;
    }

    @Override
    public void poista(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    @Override
    public void tallenna(Annos annos) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        stmt.setString(1, annos.getNimi());
        stmt.execute();
        stmt.close();
        conn.close();
    }

}
