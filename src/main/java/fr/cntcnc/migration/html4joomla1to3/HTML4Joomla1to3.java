/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

        try {
            //récupération du code html de la page web
            //Document doc = Jsoup.connect("http://cnt-cnc.fr/index.php?option=com_content&view=category&id=122&Itemid=146").get();
            Document doc = Jsoup.connect("http://cnt-cnc.fr/index.php?option=com_content&view=category&id=111&Itemid=145").get();
            
            String pageEntiere = doc.toString();

            //nb articles
            int nbArticles = StringUtils.countMatches(pageEntiere, "Visionner");

            //vérification nombre d'articles ( regarde si on trouve autant de balise que d'article sinon risque d'erreur)
            System.out.println(CNTparser.testNombreArticles(pageEntiere));

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

            //récupération des titres
            Elements titres = doc.select("td p strong");
            List<String> listeTitres = new ArrayList<String>();
            for (Element titre : titres) {
                System.out.println(titre.text());
                listeTitres.add(titre.text().toString());
            }
            boolean nbTitre = titres.size() == nbArticles;

            Elements TitrePage = doc.select("div span[class=no-link] ");
            String titrePage = "";
            for (Element titre : TitrePage) {
                System.out.println(titre.text());
                titrePage = titre.text().toString();
            }
            boolean un = TitrePage.size() == 1;

            String contenuFinal = "";
            System.out.println(un);
            if (nbTitre && nbVideoMP4 && nbVignette && un) {
//                System.out.println("test");
                for (int i = 0; i < nbArticles; i++) {
                    String[] infosMedia = {listeVideo.get(i), listeVignettes.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideo.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinal += generateurArticleJ3(infosMedia);
                }
                imprimeurArticles(contenuFinal, titrePage);
            }else if(titres.size()==videos.size() && videos.size()==imgs.size()){
            for (int i = 0; i < videos.size(); i++) {
                    String[] infosMedia = {listeVideo.get(i), listeVignettes.get(i), listeVideo.get(i), listeTitres.get(i)};
                    System.out.println(i + " " + listeVideo.get(i) + listeVignettes.get(i) + listeVideo.get(i) + listeTitres.get(i));
                    contenuFinal += generateurArticleJ3(infosMedia);
                }
                imprimeurArticles(contenuFinal, titrePage);
        }

            //System.out.println(doc.toString());
        } catch (IOException ex) {
            Logger.getLogger(HTML4Joomla1to3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
