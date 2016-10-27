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

    public static String generateurArticleJ3(String[] infosMedia) { //infosMedia = [video,image,video,titre]
        //Dans l'ordre: video, img, video, titre
        String nvArticleVierge
                = "<table><td style=\"padding-left: 30px;\">\n"
                + "<a data-rokbox data-rokbox-album=\"clubsHD\" href=\"%s\"><img src=\"%s\" /></a>\n"
                + "<br>\n"
                + "<center><a data-rokbox data-rokbox-album=\"clubsSD\" href=\"%s\"> Qualité standard </a></center>\n"
                + "</td>\n"
                + "<td style=\"padding-left: 30px;\"> \n"
                + "  <b><big>   \n"
                + "    %s \n"
                + "   </big> </b>\n"
                + "</td></table>\n\n";

        return String.format(nvArticleVierge, infosMedia);
    }
    
    public static String generateurArticleJ3AvecFLV(String[] infosMedia) { //infosMedia = [videoFLV,image,video,titre]
        //Dans l'ordre: video, img, video, titre
        String nvArticleVierge
                = //"<link href=\"http://vjs.zencdn.net/c/video-js.css\" rel=\"stylesheet\">\n"
               // + "<script src=\"http://vjs.zencdn.net/c/video.js\"></script>\n" +
                 "<table><td style=\"padding-left: 30px;\">\n"
                + "   <video id=\"video1\" class=\"video-js vjs-default-skin\" width=\"640\" height=\"480\"\n"
                +"        data-setup='{\"controls\" : true, \"autoplay\" : false, \"preload\" : \"auto\"}' poster=\"%s\">\n"
                + "        <source src=\"%s\" type=\"video/x-flv\">\n"
                + "    </video>\n"
                + "<br>\n"
                + "<center><a data-rokbox data-rokbox-album=\"clubsSD\" href=\"%s\"> Visionner en <img src=\"media/CNT/images/icones/HD_Small.png\" border=\"0\" /> </a></center>\n"
                + "</td>\n"
                + "<td style=\"padding-left: 30px;\"> \n"
                + "  <b><big>   \n"
                + "    %s \n"
                + "   </big> </b>\n"
                + "</td></table>\n\n";



 
        return String.format(nvArticleVierge, infosMedia);
    }

    public static void imprimeurArticles(String listeArticles, String nomFichier) {
        try {
//        entrée : String[] listeArticles ...   String articleComplet = "";
//        concaténation des sous article
//        for (String listeArticle : listeArticles) {
//            articleComplet+=listeArticle;
//        }
            FileUtils.writeStringToFile(new File(nomFichier + ".txt"), listeArticles);
        } catch (IOException ex) {
            Logger.getLogger(CNTparser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
