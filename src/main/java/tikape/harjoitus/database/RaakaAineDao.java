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
    public RaakaAine haeYksi(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }

        Integer id = res.getInt("id");
        String nimi = res.getString("nimi");

        RaakaAine r = new RaakaAine(id, nimi);

        res.close();
        stmt.close();
        conn.close();

        return r;
    }

    public RaakaAine findOneByName(String nimi) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        stmt.setString(1, nimi);

        ResultSet res = stmt.executeQuery();
        if (!res.next()) {
            return null;
        }

        Integer id = res.getInt("id");
        RaakaAine r = new RaakaAine(id, nimi);

        res.close();
        stmt.close();
        conn.close();

        return r;
    }
    
    @Override
    public List<RaakaAine> haeKaikki() throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi");

        ResultSet res = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (res.next()) {
            Integer id = res.getInt("id");
            String nimi = res.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        res.close();
        stmt.close();
        conn.close();

        return raakaAineet;
    }

    public List<RaakaAine> findAllByAnnos(Integer annos_id) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine " +
                "JOIN AnnosRaakaAine ON RaakaAine.id = AnnosRaakaAine.raaka_aine_id " +
                "AND AnnosRaakaAine.annos_id = ? ORDER BY AnnosRaakaAine.jarjestys");
        stmt.setInt(1, annos_id);

        ResultSet res = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (res.next()) {
            Integer id = res.getInt("id");
            String nimi = res.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        res.close();
        stmt.close();
        conn.close();

        return raakaAineet;
    }
    
    @Override
    public void poista(Integer key) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
        stmt.setInt(1, key);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    @Override
    public void tallenna(RaakaAine ra) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
        stmt.setString(1, ra.getNimi());
        stmt.execute();
        stmt.close();
        conn.close();
    }


}
