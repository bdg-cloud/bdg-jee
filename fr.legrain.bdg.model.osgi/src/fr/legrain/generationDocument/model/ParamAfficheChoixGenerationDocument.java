package fr.legrain.generationDocument.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import javax.swing.JComponent;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.lib.data.ParamAffiche;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;



public class ParamAfficheChoixGenerationDocument extends ParamAffiche implements java.io.Serializable {
	private String typeSrc = "";
	private String typeDest = "";
	private IDocumentTiers documentSrc = null;
	private List<IDocumentTiers> listeDocumentSrc=null;
	private List<ILigneDocumentTiers> listeLigneDocumentSrc=null;
	private String libelle ="";
	private Date dateDocument = null;
	private Date dateLivraison = null;
	private Boolean repriseReferenceSrc=true;
	private Boolean repriseLibelleSrc=false;
	private Boolean repriseAucun=false;
	private Boolean retour=false;
	private String codeTiers="";
	private String idDocumentExterne="";
	private String codeServiceExterne="";
	private String typeEntiteExterne="";
	private boolean tiersModifiable=true;
	private  boolean relation=false;
	private boolean multiple=false;
	private boolean generationModele=false;
	private boolean generationLigne=false;
		private boolean ouvrir=false;
	protected Boolean ligneSeparatrice=false;
//	protected Boolean RecupLibelleSeparateurDoc=false;
	private boolean conserveNbDecimalesDoc=false;
	private boolean accepterMultiType=false;
	
	private List<TaFlash> listeFlashSrc=null;
	private List<TaLFlash> listeLigneFlashSrc=null;
	private TaFlash documentFlashSrc = null;
	
	private boolean capturerReglement=false;
//	private boolean mouvementerCrdAvoir = false;
	
	//rajouter mes liste de TaLigneServiceWebExterne
	private List<TaLigneServiceWebExterne> listeLigneServiceWebExterneSrc=null;
	
	private List<TaLEcheance> listeLigneEcheanceSrc=null;
	
	public ParamAfficheChoixGenerationDocument() {

		if (listeDocumentSrc==null)listeDocumentSrc=new ArrayList<IDocumentTiers>(0);
		if (listeLigneDocumentSrc==null)listeLigneDocumentSrc=new ArrayList<ILigneDocumentTiers>(0);
		
		if (listeFlashSrc==null)listeFlashSrc=new ArrayList<TaFlash>(0);
		if (listeLigneFlashSrc==null)listeLigneFlashSrc=new ArrayList<TaLFlash>(0);
		
		if (listeLigneServiceWebExterneSrc==null)listeLigneServiceWebExterneSrc=new ArrayList<TaLigneServiceWebExterne>(0);
		
		if (listeLigneEcheanceSrc==null)listeLigneEcheanceSrc=new ArrayList<TaLEcheance>(0);
		
		
		listeDocumentSrc.clear();
		listeLigneDocumentSrc.clear();
		listeFlashSrc.clear();
		listeLigneFlashSrc.clear();
		listeLigneServiceWebExterneSrc.clear();
		listeLigneEcheanceSrc.clear();
		
		repriseReferenceSrc=true;
		repriseLibelleSrc=false;
		codeTiers="";
		tiersModifiable=true;
		ligneSeparatrice=false;
		repriseAucun=false;
		accepterMultiType=false;
//		RecupLibelleSeparateurDoc=false;
	}

//	public JComponent getFocusDefaut() {
//		return focusDefaut;
//	}
//	
//	public void setFocusDefaut(JComponent focusDefaut) {
//		this.focusDefaut = focusDefaut;
//	}
	
	public String getSousTitre() {
		return sousTitre;
	}
	
	public void setSousTitre(String sousTitre) {
		this.sousTitre = sousTitre;
	}
	
	public String getTitreFormulaire() {
		return titreFormulaire;
	}
	
	public void setTitreFormulaire(String titreFormulaire) {
		this.titreFormulaire = titreFormulaire;
	}
	
	public String getTitreGrille() {
		return titreGrille;
	}
	
	public void setTitreGrille(String titreGrille) {
		this.titreGrille = titreGrille;
	}
	


	public String getTypeSrc() {
		return typeSrc;
	}

	public void setTypeSrc(String typeSrc) {
		this.typeSrc = typeSrc;
	}

	public IDocumentTiers getDocumentSrc() {
		return documentSrc;
	}

	public void setDocumentSrc(IDocumentTiers documentSrc) {
		listeDocumentSrc.add(documentSrc);
		this.documentSrc = documentSrc;
	}

	public List<IDocumentTiers> getListeDocumentSrc() {
		return listeDocumentSrc;
	}

	public void setListeDocumentSrc(List<IDocumentTiers> listeDocumentSrc) {
		this.listeDocumentSrc = listeDocumentSrc;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Date getDateDocument() {
		return dateDocument;
	}

	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	public Date getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public String getTypeDest() {
		return typeDest;
	}

	public void setTypeDest(String typeDest) {
		this.typeDest = typeDest;
	}

	public Boolean getRepriseReferenceSrc() {
		return repriseReferenceSrc;
	}

	public void setRepriseReferenceSrc(Boolean repriseReferenceSrc) {
		this.repriseReferenceSrc = repriseReferenceSrc;
	}

	public Boolean getRepriseLibelleSrc() {
		return repriseLibelleSrc;
	}

	public void setRepriseLibelleSrc(Boolean repriseLibelleSrc) {
		this.repriseLibelleSrc = repriseLibelleSrc;
	}

	public Boolean getRetour() {
		return retour;
	}

	public void setRetour(Boolean retour) {
		this.retour = retour;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public boolean isTiersModifiable() {
		return tiersModifiable;
	}

	public void setTiersModifiable(boolean tiersModifiable) {
		this.tiersModifiable = tiersModifiable;
	}

	public boolean isRelation() {
		return relation;
	}

	public void setRelation(boolean relation) {
		this.relation = relation;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public Boolean getLigneSeparatrice() {
		return ligneSeparatrice;
	}

	public void setLigneSeparatrice(Boolean ligneSeparatrice) {
		this.ligneSeparatrice = ligneSeparatrice;
	}

//	public Boolean getRecupLibelleSeparateurDoc() {
//		return RecupLibelleSeparateurDoc;
//	}
//
//	public void setRecupLibelleSeparateurDoc(Boolean recupLibelleSeparateurDoc) {
//		RecupLibelleSeparateurDoc = recupLibelleSeparateurDoc;
//	}

	public Boolean getRepriseAucun() {
		return repriseAucun;
	}

	public void setRepriseAucun(Boolean repriseAucun) {
		this.repriseAucun = repriseAucun;
	}

	public boolean isGenerationModele() {
		return generationModele;
	}

	public void setGenerationModele(boolean generationModele) {
		this.generationModele = generationModele;
	}

	public List<ILigneDocumentTiers> getListeLigneDocumentSrc() {
		return listeLigneDocumentSrc;
	}

	public void setListeLigneDocumentSrc(List<ILigneDocumentTiers> listeLigneDocumentSrc) {
		this.listeLigneDocumentSrc = listeLigneDocumentSrc;
	}

	public boolean isGenerationLigne() {
		return generationLigne;
	}

	public void setGenerationLigne(boolean generationLigne) {
		this.generationLigne = generationLigne;
	}


	public boolean isOuvrir() {
		return ouvrir;
	}

	public void setOuvrir(boolean ouvrir) {
		this.ouvrir = ouvrir;
	}
	

	public boolean isConserveNbDecimalesDoc() {
		return conserveNbDecimalesDoc;
	}

	public void setConserveNbDecimalesDoc(boolean conserveNbDecimalesDoc) {
		this.conserveNbDecimalesDoc = conserveNbDecimalesDoc;
	}

	public List<TaFlash> getListeFlashSrc() {
		return listeFlashSrc;
	}

	public void setListeFlashSrc(List<TaFlash> listeFlashSrc) {
		this.listeFlashSrc = listeFlashSrc;
	}

	public List<TaLFlash> getListeLigneFlashSrc() {
		return listeLigneFlashSrc;
	}

	public void setListeLigneFlashSrc(List<TaLFlash> listeLigneFlashSrc) {
		this.listeLigneFlashSrc = listeLigneFlashSrc;
	}

	public TaFlash getDocumentFlashSrc() {
		return documentFlashSrc;
	}

	public void setDocumentFlashSrc(TaFlash documentFlashSrc) {
		this.documentFlashSrc = documentFlashSrc;
	}

	public List<TaLigneServiceWebExterne> getListeLigneServiceWebExterneSrc() {
		return listeLigneServiceWebExterneSrc;
	}

	public void setListeLigneServiceWebExterneSrc(List<TaLigneServiceWebExterne> listeLigneServiceWebExterneSrc) {
		this.listeLigneServiceWebExterneSrc = listeLigneServiceWebExterneSrc;
	}

	public String getIdDocumentExterne() {
		return idDocumentExterne;
	}

	public void setIdDocumentExterne(String idDocumentExterne) {
		this.idDocumentExterne = idDocumentExterne;
	}

	public String getCodeServiceExterne() {
		return codeServiceExterne;
	}

	public void setCodeServiceExterne(String codeServiceExterne) {
		this.codeServiceExterne = codeServiceExterne;
	}

	public String getTypeEntiteExterne() {
		return typeEntiteExterne;
	}

	public void setTypeEntiteExterne(String typeEntiteExterne) {
		this.typeEntiteExterne = typeEntiteExterne;
	}

	public List<TaLEcheance> getListeLigneEcheanceSrc() {
		return listeLigneEcheanceSrc;
	}

	public void setListeLigneEcheanceSrc(List<TaLEcheance> listeLigneEcheanceSrc) {
		this.listeLigneEcheanceSrc = listeLigneEcheanceSrc;
	}

	public boolean isAccepterMultiType() {
		return accepterMultiType;
	}

	public void setAccepterMultiType(boolean accepterMultiType) {
		this.accepterMultiType = accepterMultiType;
	}

	public boolean isCapturerReglement() {
		return capturerReglement;
	}

	public void setCapturerReglement(boolean capturerReglement) {
		this.capturerReglement = capturerReglement;
	}
 

//public boolean getMouvementerCrdAvoir() {
//	return mouvementerCrdAvoir;
//}
//
//public void setMouvementerCrdAvoir(boolean mouvementerCrdAvoir) {
//	this.mouvementerCrdAvoir = mouvementerCrdAvoir;
//}
}
