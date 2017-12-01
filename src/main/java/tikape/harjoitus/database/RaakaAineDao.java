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
import tikape.harjoitus.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Tietokanta tk;

    public RaakaAineDao(Tietokanta tk) {
        this.tk = tk;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");

        RaakaAine r = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        conn.close();

        return r;
    }

    public RaakaAine findOneByName(String nimi) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }

        Integer id = rs.getInt("id");
        RaakaAine r = new RaakaAine(id, nimi);

        rs.close();
        stmt.close();
        conn.close();

        return r;
    }
    
    @Override
    public List<RaakaAine> findAll() throws SQLException {

        Connection connection = tk.yhteys();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi");

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }

    public List<RaakaAine> findAllByAnnos(Integer annos_id) throws SQLException {
        Connection connection = tk.yhteys();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine " +
                "JOIN AnnosRaakaAine ON RaakaAine.id = AnnosRaakaAine.raaka_aine_id " +
                "AND AnnosRaakaAine.annos_id = ? ORDER BY AnnosRaakaAine.jarjestys");
        stmt.setInt(1, annos_id);

        ResultSet rs = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }
    
    @Override
    public void delete(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    @Override
    public void save(RaakaAine ra) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
        stmt.setString(1, ra.getNimi());
        stmt.execute();
        stmt.close();
        conn.close();
    }


}
