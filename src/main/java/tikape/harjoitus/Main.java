// aineen poisto smoothiesta

package tikape.harjoitus;


import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.harjoitus.database.AnnosDao;
import tikape.harjoitus.database.AnnosRaakaAineDao;
import tikape.harjoitus.database.Askellin;
import tikape.harjoitus.database.RaakaAineDao;
import tikape.harjoitus.database.Tietokanta;
import tikape.harjoitus.domain.Annos;
import tikape.harjoitus.domain.AnnosRaakaAine;
import tikape.harjoitus.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        Tietokanta tk = new Tietokanta("jdbc:sqlite:smoothiet.db");
        tk.init();

        AnnosDao aDao = new AnnosDao(tk);
        RaakaAineDao rDao = new RaakaAineDao(tk);
        AnnosRaakaAineDao arDao = new AnnosRaakaAineDao(tk);
        Askellin ask = new Askellin(rDao, arDao);
//        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(tk);
//
        get("/", (req, res) -> {
            res.redirect("/smoothiet");
            return "";
        });

        get("/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", aDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        post("/smoothiet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (aDao.findOneByName(nimi) == null) {
                aDao.save(new Annos(-1, nimi));
                res.redirect("/smoothiet/" + aDao.findOneByName(nimi).getId());
            } else {
                res.redirect("/smoothiet/");
            }
            return "";
        });
                
        get("/smoothiet/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            HashMap map = new HashMap<>();
            map.put("smoothie", aDao.findOne(annos_id));
            map.put("askeleet", ask.Askeleet(annos_id));
            map.put("aineet", rDao.findAll());

            if (!rDao.findAll().isEmpty()) {
                return new ModelAndView(map, "smoothie");
            } else {
                return new ModelAndView(map, "smoothie_ei_raaka-aineita");
            }
        }, new ThymeleafTemplateEngine());

        post("/smoothiet/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            Integer raakaAine_id = Integer.parseInt(req.queryParams("aine"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            if (!arDao.raakaAineOnAnnoksessa(annos_id, raakaAine_id)) {
                arDao.save(new AnnosRaakaAine(annos_id, raakaAine_id, arDao.latestOrderNumberByAnnos(annos_id) + 1, maara, ohje));
            }
            res.redirect("/smoothiet/" + annos_id);
            return "";
        });

        post("/smoothiet/poista/:id", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":id"));
            aDao.delete(annos_id);
            arDao.deleteAllByAnnos(annos_id);
            res.redirect("/smoothiet");
            return "";
        });
        
        post("/smoothiet/:aid/poista/:rid", (req, res) -> {
            Integer annos_id = Integer.parseInt(req.params(":aid"));
            Integer raakaAine_id = Integer.parseInt(req.params(":rid"));
            arDao.deleteByAnnosAndRaakaAine(annos_id, raakaAine_id);
            res.redirect("/smoothiet/" + annos_id);
            return "";
        });

        get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aineet", rDao.findAll());

            return new ModelAndView(map, "raaka-aineet");
        }, new ThymeleafTemplateEngine());

        post("/raaka-aineet", (req, res) -> {
            String nimi = req.queryParams("nimi");
            if (rDao.findOneByName(nimi) == null) {
                rDao.save(new RaakaAine(-1, nimi));
            }
            res.redirect("/raaka-aineet");
            return "";
        });

        post("/raaka-aineet/poista/:id", (req, res) -> {
            Integer raakaAine_id = Integer.parseInt(req.params(":id"));
            // tarkista onko käytössä!
            if (arDao.findAllByRaakaAine(raakaAine_id).isEmpty()) {
                rDao.delete(raakaAine_id);
            }
            res.redirect("/raaka-aineet");
            return "";
        });

    }
}
