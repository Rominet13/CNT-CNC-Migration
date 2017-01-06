/*
 * TODO ajouter un logger plutot que les sysout
 *
 */
package fr.cntcnc.migration.html4joomla1to3;

import static fr.cntcnc.migration.html4joomla1to3.CNTparser.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.*;
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
        logger.setLevel(Level.FINE);
        if(args[args.length-1].contains("debug")){ // pour le mode debug
            System.out.println("Affichage DEBUB ACTIF (VERBOSE)");
        }
        logger.log(Level.INFO, "INFO");
        logger.log(Level.FINE, "FINE");
        logger.log(Level.FINER, "FINER");
        logger.log(Level.FINEST, "FINEST");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.ALL, "ALL");
        logger.log(Level.OFF, "OFF");
        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.SEVERE, "SEVERE");

         //affichage 
        try {
            //récupération du code html de la page web
            Document doc = Jsoup.connect(args[0]).get();

            //inutil
            String pageEntiere = doc.toString();
            //nb articles en comptant les occurrences de "visionner" dans "visionner en HD", mais dans les vieux articles ce n'est pas le terme employés
            int nbArticles = StringUtils.countMatches(pageEntiere, "Visionner");
            //vérification nombre d'articles ( regarde si on trouve autant de balise que d'article sinon risque d'erreur)
            System.out.println(CNTparser.testNombreArticles(pageEntiere));

//Phase 1 : récupération des données            
            //récupération des images
            Elements imgs = doc.select("img[src^=/media/CNT/images/vignettes_videos]");
            List<String> listeVignettes = new ArrayList<String>();

            for (Element imgVignette : imgs) {
                System.out.println(imgVignette.attr("src"));
                listeVignettes.add(imgVignette.attr("src").toString());
            }
            boolean nbVignette = imgs.size() == nbArticles;

            //récupération des vidéos (uniquement les mp4 pas les flv)
            Elements videos = doc.select("a[href^=media/CNT/videos/]");
            List<String> listeVideo = new ArrayList<String>();
            for (Element video : videos) {
                System.out.println(video.attr("href")); // verif à faire: se termine par ".mp4"
                listeVideo.add(video.attr("href").toString());
            }
            boolean nbVideoMP4 = videos.size() == nbArticles;

            //récupération des vidéos FLV 
            Elements videosFLV = doc.select("a[href^=/media/CNT/videos/]");
            List<String> listeVideoFLV = new ArrayList<String>();
            for (Element videoFLV : videosFLV) {
                System.out.println(videoFLV.attr("href")); // verif à faire: se termine par ".mp4"
                listeVideoFLV.add(videoFLV.attr("href").toString().substring(0, videoFLV.attr("href").toString().indexOf(".flv")) + ".flv");
                System.out.println(listeVideoFLV.get(listeVideoFLV.size() - 1));
            }
            boolean nbVideoFLV = videosFLV.size() == nbArticles;

            //récupération des titres (1/article)
            Elements titres = doc.select("td p strong");
            List<String> listeTitres = new ArrayList<String>();
            for (Element titre : titres) {
                System.out.println(titre.text());
                listeTitres.add(titre.text().toString());
            }
            boolean nbTitre = titres.size() == nbArticles;

            //récupération du titre de la page
            Elements TitrePage = doc.select("div span[class=no-link] ");
            String titrePage = "";
            for (Element titre : TitrePage) {
                System.out.println(titre.text());
                titrePage = titre.text().toString();
            }
            boolean un = TitrePage.size() == 1;

//Phase 2 : génération du nouveau code html pour le nouveau site, au format txt.
            // ajouter juste pour article de version finale moderne
            String contenuFinalModerne = "<h2>\n" +
titrePage+"\n" +
"</h2>\n" +
"<p>\n" +
"	<!--description article -->\n" +
"</p>\n" +
"<style>\n" +
"  tr:hover {background-color: #f5f5f5}\n" +
"	td:nth-child(1)\n" +
"	{\n" +
"		width: 60%;\n" +
"		padding-left:2%;\n" +
"		padding-bottom:4.2%;\n" +
"		text-align:center;\n" +
"	}\n" +
"\n" +
"	td:nth-child(2)\n" +
"	{\n" +
"      padding-left:5%;\n" +
"		padding-bottom:4.2%;\n" +
"		vertical-align:middle;\n" +
"	}\n" +
"  img {\n" +
"    margin: 5px;\n" +
"    border: 1px solid #ccc;\n" +
"    width: 50%;\n" +
"    height: 40%;\n" +
"}\n" +
"</style>\n" +
"\n" +
"<table width=\"100%\">\n";
            String contenuFinal = "";
            String contenuFinalFLV = "";
            System.out.println("Nombre de nom de la page: " + un);
            //vérification cohérence nombre de vidéos,titres, et vignette, et aussi 1 titre de page sinon pas d'article
            if (titres.size() == videos.size() && videos.size() == imgs.size() && un) {
                for (int i = 0; i < videos.size(); i++) {
                    String[] infosMediaModerne = {listeVideo.get(i), listeVignettes.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideo.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinalModerne += generateurArticleJ3(ArticlesViergesEnum.SANSFLVMODERNELigne, infosMediaModerne);
                    /*
                    String[] infosMedia = {listeVideo.get(i), listeVignettes.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideo.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinal += generateurArticleJ3(ArticlesViergesEnum.SANSFLV, infosMedia);

                    String[] infosMediaFLV = {listeVignettes.get(i), listeVideoFLV.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideoFLV.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinalFLV += generateurArticleJ3(ArticlesViergesEnum.AVECFLV, infosMediaFLV);   */
                }
                
                imprimeurArticles(contenuFinalModerne+"\n</table>", titrePage);   
                //imprimeurArticles(contenuFinal, titrePage);
                //imprimeurArticles("<link href=\"http://vjs.zencdn.net/c/video-js.css\" rel=\"stylesheet\">\n"+ "<script src=\"http://vjs.zencdn.net/c/video.js\"></script>\n\n" + contenuFinalFLV, titrePage + "FLV");
            }else{logger.log(Level.SEVERE,"ERREUR HTML d'origine");}
            //System.out.println(doc.toString());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

}
