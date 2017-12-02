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
public class AnnosRaakaAine {
    private Integer annos_id;
    private Integer raakaAine_id;
    private Integer jarjestys;
    private String maara;
    private String ohje;
    
    // Raaka-aineen nimeä ei tallenneta AnnosRaakaAine-tauluun, mutta tässä luokassa se on mukana, 
    // jotta luokan avulla voidaan välittää reseptisivulla näytettäväksi smoothien ainesosat nimineen.
    private String nimi; 
    
    public AnnosRaakaAine(Integer annos_id, Integer raakaAine_id, Integer jarjestys, String maara, String ohje, String nimi) {
        this.annos_id = annos_id;
        this.raakaAine_id = raakaAine_id;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
        this.nimi = nimi;
    }

    public Integer getAnnos_id() {
        return annos_id;
    }

    public Integer getRaakaAine_id() {
        return raakaAine_id;
    }

    public Integer getJarjestys() {
        return jarjestys;
    }

    public String getMaara() {
        return maara;
    }

    public String getOhje() {
        return ohje;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    
     
}
