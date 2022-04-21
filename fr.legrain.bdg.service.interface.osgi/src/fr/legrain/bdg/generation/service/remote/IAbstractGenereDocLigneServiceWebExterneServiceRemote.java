package fr.legrain.bdg.generation.service.remote;

import java.util.Date;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaFlash;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;

public interface IAbstractGenereDocLigneServiceWebExterneServiceRemote {

	public void setRecupLibelleSeparateurDoc(boolean b);

	public Boolean getRecupLibelleSeparateurDoc();

	public void setLigneSeparatrice(Boolean ligneSeparatrice);

	public boolean dejaGenere(TaFlash document);

	public void setLibelleSeparateurDoc(String string);

	public void setLibelleDoc(String nouveauLibelle);

	public void setTaCPaiement(TaCPaiement taCPaiement2);

	public void setDateDoc(Date dateDansExercice);

	public void setCodeTiers(String codeTiers);

	public IDocumentTiers genereDocument(TaFlash document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple);


	public IDocumentTiers enregistreDocument(IDocumentTiers docGenere);
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd);

	
	

	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, TaFlash dd);



	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,TaFlash dd) ;


	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,TaFlash dd) ;


	public void mapUIToModelDocumentVersInfosDoc(TaFlash dd) ;


	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,TaFlash dd);

	public void setRelationDocument(boolean relationDocument);
	
	public void setDocumentGereLesCrd(boolean documentGereLesCrd);
}
