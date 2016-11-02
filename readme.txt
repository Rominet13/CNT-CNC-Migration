https://github.com/Rominet13/CNT-CNC-Migration

Prend l'adresse d'une page de l'ancien site web, dont les articles sont à migrer.
Renvoit un fichier texte contenant exactement le code html à copier-coller dans la zone html
	lors de la création d'un nouvel article avec Joomla 3.x .
	Le nom du fichier texte correspond au nom de l'article, à copier-coller dans le "titre" 
	du nouvel article.

Dans la console: 
> java -jar CNT-UtilitaireDeMigrationV0.1.jar "url"

exemple :
> java -jar CNT-UtilitaireDeMigrationV0.1.jar "http://cnt-cnc.fr/index.php?option=com_content&view=category&id=122&Itemid=146"
 (crée un fichier "Théâtre et Impro.txt" "Théâtre et ImproFLV.txt)

PS: le jar se trouve dans target/CNT-UtilitaireDeMigrationV0.1.jar
PS2: console sur windows -> dossier ou bureau où se trouve le jar, cliquez sur maj+clic souris droit,
	 cliquez sur "ouvrir une fenêtre de commande ici"
	 Ou alors installez la console de git, plus pratique (https://git-for-windows.github.io/)
	 
Historique des versions:
V0.2 prise en compte des vidéos FLV + optimisations trivials
V0.1 
