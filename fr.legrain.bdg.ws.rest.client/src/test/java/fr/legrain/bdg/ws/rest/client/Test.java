package fr.legrain.bdg.ws.rest.client;

import fr.legrain.article.dto.TaArticleDTO;

public class Test {
	
	public static void main(String[] args) {
		
		String dossierTenant = "demo";
		
		String loginDeLaTableUtilisateurWs = "test";
		String passwordDeLaTableUtilisateurWs = "test";
		
		String loginDeLaTableEspaceClient = "nicolas@legrain.fr";
		String passwordDeLaTableEspaceClient = "pwdlgr";
		
		/*********************************************************************************************************************************
		 * Avec token JWT - login/password d'utilisateur Espace client
		 * *******************************************************************************************************************************
		 */
		System.out.println("************* TOKEN JWT ESPACE CLIENT **********************");
		//Création et configuration du client
		Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		BdgRestClient bdg = BdgRestClient.build(config);
		
		//Connexion du client pour une connexion par token JWT
		String codeTiers = bdg.connexionEspaceClient();
		String accessToken = bdg.connexionEspaceClient();
		System.out.println("Code tiers : "+codeTiers);
		System.out.println("Access Token : "+BdgRestClient.accessToken);
//		bdg.refreshTokenEspaceClient();
		
		//Appel utilisant le token
		String listeFactJson = bdg.factures().listeFactureJson("CESCUD", "2000-01-01", "2030-12-31");
		System.out.println("Liste factures Espace Client :"+listeFactJson);
		
		
		
		
		/********************************************************************************************************************************
		 * Avec token JWT - login/password d'utilisateur web service (utilisateur du dossier + utilisateur uniquement pour les WS)
		 * ******************************************************************************************************************************
		 */
		System.out.println("************* TOKEN JWT DOSSIER **********************");
		BdgRestClient.accessToken=null;
		config = new Config(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		bdg = BdgRestClient.build(config);
		//Connexion du client pour une connexion par token JWT
		accessToken = bdg.connexion();
		
		System.out.println("Access Token : "+accessToken);
		listeFactJson = bdg.factures().listeFactureJson("CESCUD", "2000-01-01", "2030-12-31");
		System.out.println("Liste factures Dossier : "+listeFactJson);
		
//		System.out.println("Access Token : "+accessToken);
//		String listeBonlivJson = bdg.bonliv().listeBonlivJson("CESCUD", "2000-01-01", "2030-12-31");
//		System.out.println("Liste bonliv Dossier : "+listeBonlivJson);
		
		TaArticleDTO dto = bdg.articles().findArticle(2);
		System.out.println("Code Article : "+dto.getCodeArticle());
		
		/********************************************************************************************************************************
		 * Sans token JWT - login/password d'utilisateur web service (utilisateur du dossier + utilisateur uniquement pour les WS)
		 * Test d'appel sans les tokens, (login/password en paramètre et vérifier à chaque appel)
		 * Ancienne méthode qui devrait surement peu à peu disparaitre
		 * ******************************************************************************************************************************
		 */
		System.out.println("************* ANCIENNE METHODE **********************");
		BdgRestClient.accessToken=null;
		dto = bdg.articles().findArticleAncienneMethode(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs, 2);
		System.out.println("Code Article : "+dto.getCodeArticle());
		
		bdg.tiers().findTiersAncienneMethode(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs, 2);

	}
	
}
