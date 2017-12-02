package tikape.harjoitus;

import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.harjoitus.database.AnnosDao;
import tikape.harjoitus.database.AnnosRaakaAineDao;
import tikape.harjoitus.database.RaakaAineDao;
import tikape.harjoitus.database.Tietokanta;
import tikape.harjoitus.domain.Annos;
import tikape.harjoitus.domain.AnnosRaakaAine;
import tikape.harjoitus.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Tietokanta tk = new Tietokanta("jdbc:sqlite:smoothiet.db");
        tk.alusta();

        AnnosDao aDao = new AnnosDao(tk);
        RaakaAineDao rDao = new RaakaAineDao(tk);
        AnnosRaakaAineDao arDao = new AnnosRaakaAineDao(tk);

        get("/", (req, res) -> {
            res.redirect("/smoothiet");
            return "";
        });

        // luettelo smoothieista
        get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", aDao.haeKaikki()); // kaikki annokset eli smoothiet
            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        // uuden smoothien lisäys
        post("/smoothiet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (aDao.haeYksiNimella(nimi) == null) { // ei ole samannimistä ennestään
                aDao.tallenna(new Annos(-1, nimi));
                res.redirect("/smoothiet/" + aDao.haeYksiNimella(nimi).getId());
            } else {
                res.redirect("/smoothiet/");
            }
            return "";
        });
                
        // smoothien reseptin näyttö
        get("/smoothiet/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            HashMap map = new HashMap<>();
            List<AnnosRaakaAine> ainesosat = arDao.haeKaikkiAnnoksella(annos_id);
            // täydennetään resepti ainesosien nimillä
            for (AnnosRaakaAine ar : ainesosat) {
                ar.setNimi(rDao.haeYksi(ar.getRaakaAine_id()).getNimi());
            }
            map.put("smoothie", aDao.haeYksi(annos_id)); // tämä smoothie
            map.put("ainesosat", ainesosat); // smoothien ainekset määrineen ja ohjeineen
            map.put("aineet", rDao.haeKaikki()); // kaikki raaka-aineet
            if (!rDao.haeKaikki().isEmpty()) { 
                return new ModelAndView(map, "smoothie");
            } else {
                // Raaka-ainelistaus on tyhjä, joten aineksia ei voi lisätä.
                return new ModelAndView(map, "smoothie_ei_raaka-aineita"); 
            }
        }, new ThymeleafTemplateEngine());

        // uuden ainesosan lisäys smoothieen
        post("/smoothiet/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            Integer raakaAine_id = Integer.parseInt(req.queryParams("aine"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            if (!arDao.raakaAineOnAnnoksessa(annos_id, raakaAine_id)) { // jos tämä raaka-aine ei ole jo smoothiessa
                arDao.tallenna(new AnnosRaakaAine(annos_id, raakaAine_id, arDao.viimeisinJarjestysAnnoksella(annos_id) + 1, maara, ohje, ""));
                // järjestysnumeroksi laitetaan suurin tähän mennessä käytössä oleva järjestysnumero + 1
            }
            res.redirect("/smoothiet/" + annos_id);
            return "";
        });

        // smoothien poisto
        post("/smoothiet/poista/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            aDao.poista(annos_id); // poistetaan annos
            arDao.poistaKaikkiAnnoksella(annos_id); // poistetaan AnnosRaakaAine-taulusta viittaukset poistettuun annokseen
            res.redirect("/smoothiet");
            return "";
        });
        
        // ainesosan poisto smoothien reseptistä
        post("/smoothiet/:aid/poista/:rid", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":aid"));
            Integer raakaAine_id = Integer.parseInt(req.params(":rid"));
            arDao.poistaKaikkiAnnoksellaJaRaakaAineella(annos_id, raakaAine_id);
            res.redirect("/smoothiet/" + annos_id);
            return "";
        });

        // raaka-aineluettelo
        get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aineet", rDao.haeKaikki());
            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());

        // raaka-aineen lisäys
        post("/raaka-aineet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (rDao.findOneByName(nimi) == null) { // ei ole samannimistä ennestään
                rDao.tallenna(new RaakaAine(-1, nimi));
            }
            res.redirect("/raaka-aineet");
            return "";
        });

        // raaka-aineen poisto luettelosta
        post("/raaka-aineet/poista/:id", (req, res) -> {
            Integer raakaAine_id = Integer.parseInt(req.params(":id"));
            if (arDao.haeKaikkiRaakaAineella(raakaAine_id).isEmpty()) {
                rDao.poista(raakaAine_id);
            }
            res.redirect("/raaka-aineet");
            return "";
        });

    }
}
