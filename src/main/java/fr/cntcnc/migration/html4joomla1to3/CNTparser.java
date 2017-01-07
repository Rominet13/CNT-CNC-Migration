/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cntcnc.migration.html4joomla1to3;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author volph
 */
public class CNTparser {

    public static boolean testNombreArticles(String inText) {
        //System.out.println(StringUtils.countMatches(inText, "Visionner"));
        //System.out.println(StringUtils.countMatches(inText, "<tbody>"));

        boolean test = StringUtils.countMatches(inText, "Visionner") == StringUtils.countMatches(inText, "<tbody>");
        return test;
    }

    public static String generateurArticleJ3(ArticlesViergesEnum nvArticleVierge, String[] infosMedia) {
        //Dans l'ordre: video, img, video, titre
        //infosMedia = [video,image,video,titre] si sans FLV
        //infosMedia = [videoFLV,image,video,titre] si avec FLV
        return String.format(nvArticleVierge.getNvArticleVierge(), infosMedia);
    }

    public static void imprimeurArticles(String listeArticles, String nomFichier) {
        try {
//        entrée : String[] listeArticles ...   String articleComplet = "";
//        concaténation des sous article
//        for (String listeArticle : listeArticles) {
//            articleComplet+=listeArticle;
//        }
            FileUtils.writeStringToFile(new File(nomFichier + ".txt"), listeArticles);
            System.out.println("---- Impression de l'article ---" + nomFichier + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(CNTparser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
