/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.harjoitus.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.harjoitus.domain.AnnosRaakaAine;
import tikape.harjoitus.domain.Askel;
import tikape.harjoitus.domain.RaakaAine;

/**
 *
 * @author simosahla
 */
public class Askellin {
    private RaakaAineDao rDao;
    private AnnosRaakaAineDao arDao;

    public Askellin(RaakaAineDao rdao, AnnosRaakaAineDao ardao) {
        this.rDao = rdao;
        this.arDao = ardao;
    }
    
    public List<Askel> Askeleet(Integer annos_id) throws SQLException {
        List<Askel> askeleet = new ArrayList<>();
        List<AnnosRaakaAine> annosRaakaAineet = arDao.findAllByAnnos(annos_id);
        for (AnnosRaakaAine ar : annosRaakaAineet) {
            askeleet.add(new Askel(rDao.findOne(ar.getRaakaAine_id()).getNimi(), ar));
        }
        return askeleet;
    }
    
}
