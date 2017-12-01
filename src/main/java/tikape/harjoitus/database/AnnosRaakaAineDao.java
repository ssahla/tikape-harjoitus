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
import tikape.harjoitus.domain.AnnosRaakaAine;
import tikape.harjoitus.domain.RaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Tietokanta tk;

    public AnnosRaakaAineDao(Tietokanta tk) {
        this.tk = tk;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        return null;
    }

    public Boolean raakaAineOnAnnoksessa(Integer aId, Integer rId) throws SQLException {
        Connection connection = tk.yhteys();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine "
                + "WHERE raaka_aine_id = ? AND annos_id = ?");
        stmt.setInt(1, rId);
        stmt.setInt(2, aId);
        ResultSet rs = stmt.executeQuery();
        Boolean result = rs.next();
        rs.close();
        stmt.close();
        connection.close();
        return result;
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {

        Connection connection = tk.yhteys();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer raakaAine_id = rs.getInt("raaka_aine_id");
            Integer annos_id = rs.getInt("annos_id");
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");

            AnnosRaakaAineet.add(new AnnosRaakaAine(raakaAine_id, annos_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        connection.close();

        return AnnosRaakaAineet;
    }

    public List<AnnosRaakaAine> findAllByAnnos(Integer annos_id) throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE annos_id = ? ORDER BY jarjestys");
        stmt.setInt(1, annos_id);

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer raakaAine_id = rs.getInt("raaka_aine_id");
//            Integer annos_id = rs.getInt("annos_id"); 
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");
//            System.out.println("'" + raakaAine_id); tulee oikein
            

            AnnosRaakaAineet.add(new AnnosRaakaAine(annos_id, raakaAine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        conn.close();

        return AnnosRaakaAineet;
    }

    public List<AnnosRaakaAine> findAllByRaakaAine(Integer raakaAine_id) throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE raaka_aine_id = ?");
        stmt.setInt(1, raakaAine_id);

        ResultSet rs = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (rs.next()) {
            Integer annos_id = rs.getInt("annos_id");
            Integer jarjestys = rs.getInt("jarjestys");
            String maara = rs.getString("maara");
            String ohje = rs.getString("ohje");

            AnnosRaakaAineet.add(new AnnosRaakaAine(annos_id, raakaAine_id, jarjestys, maara, ohje));
        }

        rs.close();
        stmt.close();
        conn.close();

        return AnnosRaakaAineet;
    }

    public Integer latestOrderNumberByAnnos(Integer annos_id) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT MAX(jarjestys) AS suurin FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setInt(1, annos_id);
        ResultSet res = stmt.executeQuery();
        Integer suurin = 0;
        if (res.next()) {
            suurin = res.getInt("suurin");
        }
        res.close();
        stmt.close();
        conn.close();
        return suurin;
        
}
    
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteAllByAnnos(Integer annos_id) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setInt(1, annos_id);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    public void deleteByAnnosAndRaakaAine(Integer annos_id, Integer raakaAine_id) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ? "
                        + "AND raaka_aine_id = ?");
        stmt.setInt(1, annos_id);
        stmt.setInt(2, raakaAine_id);
        stmt.execute();
        stmt.close();
        conn.close();
    }

    @Override
    public void save(AnnosRaakaAine ar) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine "
                + "(raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, ar.getRaakaAine_id());
        stmt.setInt(2, ar.getAnnos_id());
        stmt.setInt(3, ar.getJarjestys());
        stmt.setString(4, ar.getMaara());
        stmt.setString(5, ar.getOhje());

        stmt.execute();
        stmt.close();
        conn.close();
    }

}
