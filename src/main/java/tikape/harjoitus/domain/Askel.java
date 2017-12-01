/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.harjoitus.domain;

/**
 *
 * @author simosahla
 */
public class Askel extends AnnosRaakaAine {
    private String nimi;

    public Askel(String nimi, AnnosRaakaAine ar) {
        super(ar.getAnnos_id(), ar.getRaakaAine_id(), ar.getJarjestys(), ar.getMaara(), ar.getOhje());
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }
    
}
