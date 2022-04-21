package fr.legrain.bdg.importation.service.remote;

import java.io.FileReader;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentDTO;





@Remote
public interface IImportationServiceRemote {
	public static final String validationContext = "IMPORTATION";

	public boolean importationBonReception();
	public boolean importationAbonnement();
	public boolean importationAbonnementPlusieursLignes();
	public boolean importationArticles(int deb,int fin) ;
	public boolean importationTiers(int deb,int fin)  ;
	public boolean importationTiersSpecifique() ;
	public boolean importationArticlesSpecifique() ;
	public boolean importationTiersCapdebon();
	
	public boolean importationArticlesRemanieParDenis(int deb,int fin) ;


	public void setFichierImportBonReception(String fileReader);
	public void setFichierImportArticles(String fileReader);
	public void setFichierImportAbonnements(String fileReader);
	public void setFichierImportTiers(String fileReader);
	boolean importationArticlesCanardises();
//	public void updateEtatTousDocs();
}
