package fr.legrain.bdg.generation.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;

@Remote
public interface IAbstractGenereDocLigneEcheanceAbonnementServiceRemote {

	public void setRecupLibelleSeparateurDoc(boolean b);

	public Boolean getRecupLibelleSeparateurDoc();

	public void setLigneSeparatrice(Boolean ligneSeparatrice);

	public boolean dejaGenere(IDocumentTiers document, boolean accepterMultiType);

	public void setLibelleSeparateurDoc(String string);

	public void setLibelleDoc(String nouveauLibelle);

	public void setTaCPaiement(TaCPaiement taCPaiement2);
	
	public void setRegleDoc(boolean regleDoc);
	public boolean isRegleDoc();
	
	public void setCodeServiceWeb(String codeServiceWeb);
	public String getCodeServiceWeb();

	public void setDateDoc(Date dateDansExercice);

	public void setCodeTiers(String codeTiers);

	public IDocumentTiers genereDocument( IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple);
	
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

	public IDocumentTiers genereDocument(List<TaLEcheance> listeEcheances, IDocumentTiers docGenere, String codeTiers,
			boolean b, boolean generationModele, boolean genererCode, boolean multiple);
	
	

}
