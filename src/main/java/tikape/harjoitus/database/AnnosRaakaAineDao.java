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
    public AnnosRaakaAine haeYksi(Integer key) throws SQLException {
        // ei toteutettu
        return null;
    }

    public Boolean raakaAineOnAnnoksessa(Integer aId, Integer rId) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine "
                + "WHERE raaka_aine_id = ? AND annos_id = ?");
        stmt.setInt(1, rId);
        stmt.setInt(2, aId);
        ResultSet res = stmt.executeQuery();
        Boolean result = res.next();
        res.close();
        stmt.close();
        conn.close();
        return result;
    }

    @Override
    public List<AnnosRaakaAine> haeKaikki() throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet res = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (res.next()) {
            Integer raakaAine_id = res.getInt("raaka_aine_id");
            Integer annos_id = res.getInt("annos_id");
            Integer jarjestys = res.getInt("jarjestys");
            String maara = res.getString("maara");
            String ohje = res.getString("ohje");
            AnnosRaakaAineet.add(new AnnosRaakaAine(raakaAine_id, annos_id, jarjestys, maara, ohje, ""));
        }

        res.close();
        stmt.close();
        conn.close();

        return AnnosRaakaAineet;
    }

    public List<AnnosRaakaAine> haeKaikkiAnnoksella(Integer annos_id) throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE annos_id = ? ORDER BY jarjestys");
        stmt.setInt(1, annos_id);

        ResultSet res = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (res.next()) {
            Integer raakaAine_id = res.getInt("raaka_aine_id");
            Integer jarjestys = res.getInt("jarjestys");
            String maara = res.getString("maara");
            String ohje = res.getString("ohje");
            AnnosRaakaAineet.add(new AnnosRaakaAine(annos_id, raakaAine_id, jarjestys, maara, ohje, ""));
        }

        res.close();
        stmt.close();
        conn.close();

        return AnnosRaakaAineet;
    }

    public List<AnnosRaakaAine> haeKaikkiRaakaAineella(Integer raakaAine_id) throws SQLException {

        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AnnosRaakaAine"
                + " WHERE raaka_aine_id = ?");
        stmt.setInt(1, raakaAine_id);

        ResultSet res = stmt.executeQuery();
        List<AnnosRaakaAine> AnnosRaakaAineet = new ArrayList<>();
        while (res.next()) {
            Integer annos_id = res.getInt("annos_id");
            Integer jarjestys = res.getInt("jarjestys");
            String maara = res.getString("maara");
            String ohje = res.getString("ohje");
            AnnosRaakaAineet.add(new AnnosRaakaAine(annos_id, raakaAine_id, jarjestys, maara, ohje, ""));
        }

        res.close();
        stmt.close();
        conn.close();

        return AnnosRaakaAineet;
    }

    public Integer viimeisinJarjestysAnnoksella(Integer annos_id) throws SQLException {
        // palauttaa suurimman järjestysnumeron annoksen raaka-aineista
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
    public void poista(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void poistaKaikkiAnnoksella(Integer annos_id) throws SQLException {
        Connection conn = tk.yhteys();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt.setInt(1, annos_id);
        stmt.execute();
        stmt.close();
        conn.close();
    }
    
    public void poistaKaikkiAnnoksellaJaRaakaAineella(Integer annos_id, Integer raakaAine_id) throws SQLException {
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
    public void tallenna(AnnosRaakaAine ar) throws SQLException {
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
