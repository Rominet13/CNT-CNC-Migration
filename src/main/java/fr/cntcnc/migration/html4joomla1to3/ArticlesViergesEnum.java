/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cntcnc.migration.html4joomla1to3;

/**
 * @TODO : ajouter une version avec balise vidéo (qui à l'air mieux que rokbox = vieux) 
 * @author volph
 */
public enum ArticlesViergesEnum { 
    SANSFLVMODERNELigne("	<tr>\n" +
"		<td>\n" +
"			<a data-rokbox data-rokbox-album=\"clubsHD\" href=\"%s\"><img src=\"%s\" /></a>\n" +
"		</td>\n" +
"		<td>\n" +
"			<b><big>%s</big></b>\n" +
"		</td>\n" +
"	</tr>"), 
    SANSFLV("<table><td style=\"padding-left: 30px;\">\n"
            + "<a data-rokbox data-rokbox-album=\"clubsHD\" href=\"%s\"><img src=\"%s\" /></a>\n"
            + "<br>\n"
            + "<center><a data-rokbox data-rokbox-album=\"clubsSD\" href=\"%s\"> Qualité standard </a></center>\n"
            + "</td>\n"
            + "<td style=\"padding-left: 30px;\"> \n"
            + "  <b><big>   \n"
            + "    %s \n"
            + "   </big> </b>\n"
            + "</td></table>\n\n"),
    AVECFLV("<table><td style=\"padding-left: 30px;\">\n"
            + "   <video id=\"video1\" class=\"video-js vjs-default-skin\" width=\"640\" height=\"480\"\n"
            + "        data-setup='{\"controls\" : true, \"autoplay\" : false, \"preload\" : \"auto\"}' poster=\"%s\">\n"
            + "        <source src=\"%s\" type=\"video/x-flv\">\n"
            + "    </video>\n"
            + "<br>\n"
            + "<center><a data-rokbox data-rokbox-album=\"clubsSD\" href=\"%s\"> Visionner en <img src=\"media/CNT/images/icones/HD_Small.png\" border=\"0\" /> </a></center>\n"
            + "</td>\n"
            + "<td style=\"padding-left: 30px;\"> \n"
            + "  <b><big>   \n"
            + "    %s \n"
            + "   </big> </b>\n"
            + "</td></table>\n\n");
    
    

    private String nvArticleVierge;

    private ArticlesViergesEnum(String nvArticleVierge) {
        this.nvArticleVierge = nvArticleVierge;
    }

    public String getNvArticleVierge() {
        return nvArticleVierge;
    }

}
