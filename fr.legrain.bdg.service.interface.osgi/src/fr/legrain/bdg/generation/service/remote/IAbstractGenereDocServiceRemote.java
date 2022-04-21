package fr.legrain.bdg.generation.service.remote;

import java.util.Date;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaFacture;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;

public interface IAbstractGenereDocServiceRemote {

	public void setRecupLibelleSeparateurDoc(boolean b);

	public Boolean getRecupLibelleSeparateurDoc();

	public void setLigneSeparatrice(Boolean ligneSeparatrice);

	public boolean dejaGenere(IDocumentTiers document, boolean accepterMultiType);

	public void setLibelleSeparateurDoc(String string);
	public String getLibelleSeparateurDoc();

	public void setLibelleDoc(String nouveauLibelle);

	public void setTaCPaiement(TaCPaiement taCPaiement2);

	public void setDateDoc(Date dateDansExercice);
	
	public void setCapturerReglement(boolean setCapturerReglement);

	public void setCodeTiers(String codeTiers);
	
	public void setDocumentGereLesLots(boolean documentGereLesLots);
	public void setDocumentGereLesCrd(boolean documentGereLesCrd);

	public IDocumentTiers genereDocument(IDocumentTiers document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple);


	public IDocumentTiers enregistreDocument(IDocumentTiers docGenere);
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd);

	
	

	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd);



	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) ;


	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) ;


	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) ;


	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd);

	public void setRelationDocument(boolean relationDocument);
	

}
