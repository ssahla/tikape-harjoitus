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

    public AnnosRaakaAine(Integer annos_id, Integer raakaAine_id, Integer jarjestys, String maara, String ohje) {
        this.annos_id = annos_id;
        this.raakaAine_id = raakaAine_id;
        this.jarjestys = jarjestys;
        this.maara = maara;
        this.ohje = ohje;
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
    
    
     
}
