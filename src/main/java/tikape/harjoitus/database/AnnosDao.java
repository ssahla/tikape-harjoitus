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
    public Annos findOne(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        Annos a = new Annos(id, nimi);

        rs.close();
        stmt.close();
        conn.close();

        return a;
    }

    public Annos findOneByName(String nimi) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer id = rs.getInt("id");
        Annos a = new Annos(id, nimi);

        rs.close();
        stmt.close();
        conn.close();

        return a;
    }

    @Override
    public List<Annos> findAll() throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Annos ORDER BY nimi");

        ResultSet rs = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            annokset.add(new Annos(id, nimi));
        }

        rs.close();
        stmt.close();
        conn.close();

        return annokset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    @Override
    public void save(Annos annos) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
        stmt.setString(1, annos.getNimi());
        stmt.execute();
        stmt.close();
        conn.close();
    }

}
