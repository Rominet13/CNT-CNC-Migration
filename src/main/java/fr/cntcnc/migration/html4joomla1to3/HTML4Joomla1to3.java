/*
 * TODO ajouter un logger plutot que les sysout
 *
 */
package fr.cntcnc.migration.html4joomla1to3;

import static fr.cntcnc.migration.html4joomla1to3.CNTparser.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
//import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author volph
 */
public class HTML4Joomla1to3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(HTML4Joomla1to3.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);
        if (args[args.length - 1].contains("verbose")) { // pour le mode debug ou verbose
            logger.setLevel(Level.CONFIG);
            handler.setLevel(Level.CONFIG);
            logger.log(Level.CONFIG, "Affichage VERBOSE - DEBUB ACTIF (vous avez \"verbose\" en 2nd argument)\n");
        }// http://postprod.cnt-cnc.fr/index.php?option=com_content&view=category&id=119&Itemid=146 verbose
        //affichage 
        try {
            //récupération du code html de la page web
            Document doc = Jsoup.connect(args[0]).get();

            //inutil deprecated - tient trop compte du code html de la page. Peut être faux alors que l'article sera bon. On regarde juste la correspondance nombre image, vidéo titre
            /*  String pageEntiere = doc.toString();
            //nb articles en comptant les occurrences de "visionner" dans "visionner en HD", mais dans les vieux articles ce n'est pas le terme employés
            int nbArticles = StringUtils.countMatches(pageEntiere, "Visionner");
            //vérification nombre d'articles ( regarde si on trouve autant de balise que d'article sinon risque d'erreur)
            System.out.println(CNTparser.testNombreArticles(pageEntiere));
             */
//Phase 1 : récupération des données            
            //récupération des images
            Elements imgs = doc.select("img[src^=/media/CNT/images/vignettes_videos]");
            List<String> listeVignettes = new ArrayList<String>();
            for (Element imgVignette : imgs) {
                listeVignettes.add(imgVignette.attr("src").toString().replaceFirst("_videos", "").replace(".png", ".jpg"));
            }
            logger.log(Level.CONFIG, "Liste des VIGNETTES:\n" + listeVignettes + "\n");
            //boolean nbVignette = imgs.size() == nbArticles;

            //récupération des vidéos (uniquement les mp4 pas les flv)
            Elements videos = doc.select("a[href^=media/CNT/videos/]");
            List<String> listeVideo = new ArrayList<String>();
            for (Element video : videos) {
                if(!listeVideo.contains(video.attr("href").toString())){
                listeVideo.add(video.attr("href").toString());}
            }
            logger.log(Level.CONFIG, "Liste des VIDEOS MP4:\n" + listeVideo + "\n"); // verif à faire: se termine par ".mp4"
            //boolean nbVideoMP4 = videos.size() == nbArticles;

            //récupération des vidéos FLV 
            Elements videosFLV = doc.select("a[href^=/media/CNT/videos/]");
            List<String> listeVideoFLV = new ArrayList<String>();
            for (Element videoFLV : videosFLV) {
                listeVideoFLV.add(videoFLV.attr("href").toString().substring(0, videoFLV.attr("href").toString().indexOf(".flv")) + ".flv");
            }
            logger.log(Level.CONFIG, "Liste des VIDEOS FLV:\n" + listeVideoFLV + "\n");
            // boolean nbVideoFLV = videosFLV.size() == nbArticles;

            //récupération des titres (1/article)
            Elements titres = doc.select("td p strong");
            List<String> listeTitres = new ArrayList<String>();
            for (Element titre : titres) {
                listeTitres.add(titre.text().toString());
            }
            logger.log(Level.CONFIG, "Liste des TITRES de chaque vidéo:\n" + listeTitres + "\n");
            //System.out.println(Arrays.toString(listeTitres.toArray()));
            //listeTitres.forEach(action);//forEach(System.out::println);
            //boolean nbTitre = titres.size() == nbArticles;

            //récupération du titre de la page
            Elements TitrePage = doc.select("div span[class=no-link] ");
            String titrePage = "";
            for (Element titre : TitrePage) {
                titrePage = titre.text().toString();
            }
            logger.log(Level.CONFIG, "Titre de la PAGE: " + titrePage + "\n");
            boolean un = TitrePage.size() == 1;

//Phase 2 : génération du nouveau code html pour le nouveau site, au format txt.
            // ajouter juste pour article de version finale moderne
            String contenuFinalModerne = "<h2>\n"
                    + titrePage + "\n"
                    + "</h2>\n"
                    + "<p>\n"
                    + "	<!--description article -->\n"
                    + "</p>\n"
                    + "<style>\n"
                    + "  tr:hover {background-color: #f5f5f5}\n"
                    + "	td:nth-child(1)\n"
                    + "	{\n"
                    + "		width: 60%;\n"
                    + "		padding-left:2%;\n"
                    + "		padding-bottom:4.2%;\n"
                    + "		text-align:center;\n"
                    + "	}\n"
                    + "\n"
                    + "	td:nth-child(2)\n"
                    + "	{\n"
                    + "      padding-left:5%;\n"
                    + "		padding-bottom:4.2%;\n"
                    + "		vertical-align:middle;\n"
                    + "	}\n"
                    + " table img {\n"
                    + "    margin: 5px;\n"
                    + "    border: 1px solid #ccc;\n"
                    + "    width: 50%;\n"
                    + "    height: 40%;\n"
                    + "}\n"
                    + "</style>\n"
                    + "\n"
                    + "<table width=\"100%\">\n";
            String contenuFinal = "";
            String contenuFinalFLV = "";
            System.out.println("Nombre de nom de la page: " + un + "    (égal à 1 si pas d'erreur)");
            System.out.println("Nombre de titre/vidéo : " + listeTitres.size());
            System.out.println("Nombre de vidéos mp4 trouvés: " + listeVideo.size());
            System.out.println("Nombre de vignettes trouvées: " + listeVignettes.size() + "    (ces 3 dernières valeurs doivent être égale" + "\n");
            //vérification cohérence nombre de vidéos,titres, et vignette, et aussi 1 titre de page sinon pas d'article
            if (listeTitres.size() == listeVideo.size() && listeVideo.size() == listeVignettes.size() && un) {
                System.out.println(titrePage);
                for (int i = 0; i < listeVideo.size(); i++) {
                    String[] infosMediaModerne = {listeVideo.get(i), listeVignettes.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeTitres.get(i) + " " + listeVideo.get(i) + " " + listeVignettes.get(i));
                    contenuFinalModerne += generateurArticleJ3(ArticlesViergesEnum.SANSFLVMODERNELigne, infosMediaModerne);
                    /*
                    String[] infosMedia = {listeVideo.get(i), listeVignettes.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideo.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinal += generateurArticleJ3(ArticlesViergesEnum.SANSFLV, infosMedia);

                    String[] infosMediaFLV = {listeVignettes.get(i), listeVideoFLV.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideoFLV.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinalFLV += generateurArticleJ3(ArticlesViergesEnum.AVECFLV, infosMediaFLV);   */
                }

                imprimeurArticles(contenuFinalModerne + "\n</table>", titrePage);
                //imprimeurArticles(contenuFinal, titrePage);
                //imprimeurArticles("<link href=\"http://vjs.zencdn.net/c/video-js.css\" rel=\"stylesheet\">\n"+ "<script src=\"http://vjs.zencdn.net/c/video.js\"></script>\n\n" + contenuFinalFLV, titrePage + "FLV");
            } else {
                logger.log(Level.SEVERE, "ERREUR HTML d'origine.\n Code HTML de la page web comporte des Erreurs. Informez Romain pour qu'il s'occupe de cette page en Manuel.");
            }
            //System.out.println(doc.toString());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}
