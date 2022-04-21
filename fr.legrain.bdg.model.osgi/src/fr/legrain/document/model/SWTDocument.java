package fr.legrain.document.model;

import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.math.MathContext;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.swing.event.EventListenerList;
import javax.xml.bind.annotation.XmlTransient;

import com.ibm.icu.math.BigDecimal;

import fr.legrain.article.model.TaArticle;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.EnteteDocument;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.events.SWTModificationDocumentEvent;
import fr.legrain.document.events.SWTModificationDocumentListener;
import fr.legrain.lib.data.ChangeModeEvent;
import fr.legrain.lib.data.ChangeModeListener;
//import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.validator.LgrHibernateValidated;


@SqlResultSetMapping(name="DocumentChiffreAffaireDTO",
classes = {
@ConstructorResult(
        targetClass = DocumentChiffreAffaireDTO.class,
        columns = {
            @ColumnResult(name = "jour", type = int.class),
            @ColumnResult(name = "mois", type = int.class),
            @ColumnResult(name = "annee", type = int.class),
            @ColumnResult(name = "typeReglement"),
            @ColumnResult(name = "mtHtCalc"),
            @ColumnResult(name = "mtTvaCalc"),
            @ColumnResult(name = "mtTtcCalc"),
            @ColumnResult(name = "affectationTotale"),
            @ColumnResult(name = "resteARegler")
        })   
})


@Entity
@SequenceGenerator(name = "gen_facture", sequenceName = "num_id_document", allocationSize = 1)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SWTDocument implements Serializable, PropertyChangeListener, IDocumentTiers{
	

	private static final long serialVersionUID = -5041860063762024200L;

	public static final String C_CODE_T_LIGNE_H = "H";
	protected int idDocument;
	protected Boolean relationDocument;
	protected Integer versionObj;
	protected String quiCree;
	protected Date quandCree;
	protected String quiModif;
	protected Date quandModif;
	
//	protected Boolean utiliseUniteSaisie = true;
	protected TaREtat taREtat;
//	protected Integer nbDecimalesPrix;
//	protected Integer nbDecimalesQte;
	@Transient
	private Boolean accepte=true;
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}
	
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_facture")
	@Column(name = "id_document", unique = true, nullable = false)
	public int getIdDocument() {
		return this.idDocument;
	}
	
	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}

	//passage ejb
//	@Transient
//	protected EnumModeObjet modeDocument; //insertion,modification,suppression
	
	protected EnteteDocument entete = null;
	protected List/*<? extends SWTLigneDocument>*/ lignes = null; //ensemble des lignes du document
//	protected String tableLignes = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected int ligneCourante = 0;
	public static final String C_OLD_CODE_INITIAL = "-1";
	protected String OldCODE = C_OLD_CODE_INITIAL;
	protected Date oldDate;
	
	public SWTDocument(){
	}
	public SWTDocument(String oldCode){		
		this.setOldCODE(oldCode);
	}
	
	@Transient
	public boolean isRemplie(){
		if(lignes!=null && lignes.size()>0){
			for (int i = 0; i < lignes.size(); i++) {
				if(!getLigne(i).ligneEstVide())return true;
			};
		}
		return false;		
	}
	
	
	public void addModificationDocumentListener(SWTModificationDocumentListener l) {
		listenerList.add(SWTModificationDocumentListener.class, l);
	}
	
	public void removeModificationDocumentListener(SWTModificationDocumentListener l) {
		listenerList.remove(SWTModificationDocumentListener.class, l);
	}
	
	protected void fireModificationDocument(SWTModificationDocumentEvent e) throws Exception {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SWTModificationDocumentListener.class) {
				if (e == null)
					e = new SWTModificationDocumentEvent(this);
				( (SWTModificationDocumentListener) listeners[i + 1]).modificationDocument(e);
			}
		}
	}
	
	public void declencheModificationDocument(SWTModificationDocumentEvent e) throws Exception {
		fireModificationDocument(e);
	}
	public void addChangeModeListener(ChangeModeListener l) throws TooManyListenersException {
		listenerList.add(ChangeModeListener.class, l);
//		if(listenerList.getListenerCount()>1) {
//			throw new TooManyListenersException("Un seul listener par ligne");
//		}
	}
	
	public void removeChangeModeListener(ChangeModeListener l) {
		listenerList.remove(ChangeModeListener.class, l);
	}
	
	protected void fireChangeMode(ChangeModeEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeModeListener.class) {
				if (e == null)
					e = new ChangeModeEvent(this,null);
				( (ChangeModeListener) listeners[i + 1]).changementMode(e);
			}
		}
	}
	
	/**
	 * Ajout d'une ligne en fin de liste
	 * avec controle avant ajout et traitement après ajout
	 * @param ligne
	 * @throws ExceptLgr
	 */
	public void addLigne(SWTLigneDocument ligne) throws Exception {
		if(beforeAjoutLigne(ligne)) {
			ligne.setNumLigneLDocument(lignes.size()+1);
			lignes.add(ligne);
			ligne.addPropertyChangeListener(this);
			ligneCourante = lignes.size()-1;
			afterAjoutLigne(ligne);
			//passage ejb
			fireModificationDocument(new SWTModificationDocumentEvent(this,/*EnumModeObjet.C_MO_INSERTION,*/ligne));
		} else {
			throw new ExceptLgr();
		}
	}
	
	/**
	 * Ajout d'une ligne en fin de liste
	 * avec controle avant ajout et traitement après ajout
	 * @param ligne
	 * @throws ExceptLgr
	 */
	public void enregistreLigne(SWTLigneDocument ligne) throws Exception {
		beforeAjoutLigne(ligne);
		
		afterAjoutLigne(ligne);
		//passage ejb
		fireModificationDocument(new SWTModificationDocumentEvent(this,/*EnumModeObjet.C_MO_CONSULTATION,*/ligne));
	}	
	
	/**
	 * Ajout d'une ligne a un rang donné
	 * avec controle avant ajout et traitement après ajout
	 * @param ligne
	 * @param rang - position
	 * @throws ExceptLgr
	 */
	public void insertLigne(SWTLigneDocument ligne, Integer rang) throws Exception {
		if(beforeAjoutLigne(ligne)) {
			ligne.setNumLigneLDocument(rang);
			reinitialiseNumLignes(rang,1);
			lignes.add(rang-1,ligne);
			ligne.addPropertyChangeListener(this);
			ligneCourante = rang;
			afterAjoutLigne(ligne);
			//passage ejb
			fireModificationDocument(new SWTModificationDocumentEvent(this/*,EnumModeObjet.C_MO_INSERTION*/));
		} else {
			throw new ExceptLgr();
		}
	}
	public void deplaceLigne(SWTLigneDocument ligne,Integer nouveauRang){
		int rangActuel=lignes.indexOf(ligne);
		//SWTLigneDocument l=(SWTLigneDocument)lignes.get(rangActuel);
		lignes.remove(ligne);
		reinitialiseNumLignes(0,1);
		ligne.setNumLigneLDocument(nouveauRang);
		reinitialiseNumLignes(nouveauRang,1);
		lignes.add(nouveauRang,ligne);
		reinitialiseNumLignes(0,1);
	}
	
	public void reinitialiseNumLignes(Integer aPartirDe,int increment){
		int i=aPartirDe+increment;
		//int i=aPartirDe;
		for (Object ligne : lignes) {
//			if(((SWTLigneDocument)ligne).getNUM_LIGNE()>=aPartirDe)
			if(((SWTLigneDocument)ligne).getNumLigneLDocument()>=aPartirDe)
			{
				((SWTLigneDocument)ligne).setNumLigneLDocument(i);
				i++;
			}
		}
		reinitialiseNumLignes();
	}
	
	
	protected abstract boolean beforeAjoutLigne(SWTLigneDocument ligne);
	
	protected abstract void afterAjoutLigne(SWTLigneDocument ligne) throws ExceptLgr;
	
	protected abstract boolean beforeRemoveLigne(SWTLigneDocument ligne);
	
	protected abstract void afterRemoveLigne(SWTLigneDocument ligne) throws ExceptLgr;
	
	protected abstract boolean beforeEnregistrerEntete();
	
	protected abstract void afterEnregistrerEntete() throws ExceptLgr;
	
	protected abstract boolean beforeModifierEntete();
	
	protected abstract void afterModifierEntete() throws ExceptLgr;
	
	protected abstract boolean beforeSupprimerEntete();
	
	protected abstract void afterSupprimerEntete() throws ExceptLgr;
	
	protected abstract void reinitialiseNumLignes();
	/**
	 * Suppression d'un ligne donnée
	 * @param ligne
	 * @throws ExceptLgr
	 */
	public void removeLigne(SWTLigneDocument ligne) throws Exception {
		if(beforeRemoveLigne(ligne)) {
			if(lignes.size()-1>=0 ){
				//passage ejb
				fireModificationDocument(new SWTModificationDocumentEvent(this,/*EnumModeObjet.C_MO_SUPPRESSION,*/getLigne(ligneCourante)));
				int rang = lignes.indexOf(ligne);
				lignes.remove(ligne);
				reinitialiseNumLignes(0, 1);
				afterRemoveLigne(ligne);
				//passage ejb
				fireModificationDocument(new SWTModificationDocumentEvent(this,/*EnumModeObjet.C_MO_SUPPRESSION,*/getLigne(ligneCourante)));
				ligne.removePropertyChangeListener(this);
			}
		} else {
			throw new ExceptLgr();
		}
	}
	
	public SWTLigneDocument getLigne(Integer numLigne) {
		if (lignes.size()>numLigne && numLigne>=0)
		return (SWTLigneDocument)lignes.get(numLigne);
		else
			return null;
	}
	
		
	
//	public void enregistrer() throws ExceptLgr {
//		//TODO enregistrement du document dans les bonnes tables
//		//updateDocument.setMode(new ModeObjet(mode));
//		entete.setMode(mode);
//		entete.enregistrer();
//		for (SWTLigneDocument ligne : lignes) {
//			ligne.setMode(mode);
//			ligne.enregistrer();
//		}
//	}
//	
//	public void modifier() throws ExceptLgr {
//		//TODO modification du document dans les bonnes tables
//	}
//	
//	public void supprimer() throws ExceptLgr {
//		//TODO suppression du document
//		//updateDocument.setMode(new ModeObjet(mode));
//	}
	
	
	@Transient
	public EnteteDocument getEntete() {
		return entete;
	}

	public void setEntete(EnteteDocument entete) {
		this.entete = entete;
	}

//	public ArrayList<SWTLigneDocument> getLignes() {
//		return lignes;
//	}
//
//	public void setLignes(ArrayList<SWTLigneDocument> lignes) {
//		this.lignes = lignes;
//	}
	
	//passage ejb
//	@Transient
//	public EnumModeObjet getModeDocument() {
//		return modeDocument;
//	}
//
//	public void setModeDocument(EnumModeObjet mode) {
//		this.modeDocument = mode;
//		fireChangeMode(new ChangeModeEvent(this,mode));
//	}
	
//	@Transient
//	public String getTableLignes() {
//		return tableLignes;
//	}
//
//	public void setTableLignes(String tableLignes) {
//		this.tableLignes = tableLignes;
//	}
	
	@Transient
	public int getLigneCourante() {
		return ligneCourante;
	}

	public void setLigneCourante(int ligneCourante) {
		this.ligneCourante = ligneCourante;
		if (this.ligneCourante== -1)this.ligneCourante=0;
	}
	
	@Transient
	public String getOldCODE() {
		return OldCODE;
	}

	public void setOldCODE(String oldCODE) {
		OldCODE = oldCODE;
	}
	

	@Transient
	@XmlTransient
	public boolean getRelationDocument() {
		// TODO Auto-generated method stub
		return relationDocument;
	}


	//@Transient
	public void setRelationDocument(Boolean relation) {
		this.relationDocument=relation;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		//passage ejb
//		if(evt.getPropertyName().equals("modeLigne")){
//			this.setModeDocument((EnumModeObjet)evt.getNewValue());
//		}
	}

	@Transient
	public Date getOldDate() {
		return oldDate;
	}

	public void setOldDate(Date oldDate) {
		this.oldDate = oldDate;
	}
	
//passage ejb => dans IInfoEntrepriseDAO
//	public Date dateDansExercice(Date valeur) throws Exception {
//		
//		IInfoEntrepriseDAO taInfoEntrepriseDAO = new IInfoEntrepriseDAO(/*new TaInfoEntrepriseDAO().getEntityManager()*/);
//		TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
//		// si date inférieur à dateDeb dossier
//		if (LibDate.compareTo(valeur, taInfoEntreprise.getDatedebInfoEntreprise()) == -1) {
//			return taInfoEntreprise.getDatedebInfoEntreprise();
//		} else
//			// si date supérieur à dateFin dossier
//			if (LibDate.compareTo(valeur, taInfoEntreprise.getDatefinInfoEntreprise()) == 1) {
//				return taInfoEntreprise.getDatefinInfoEntreprise();
//			}
//		//return new Date();
//		return valeur;
//	}
	
	public Boolean changementDateDocument(Date newDate){
		if(newDate==null)return false;
		
		return oldDate==null || newDate == null ||
		LibDate.dateToString(oldDate).compareTo(LibDate.dateToString(newDate))!=0;
		//(oldDate.compareTo(newDate)!=0);
		//newDate.getDate();
	}

	
	@Transient
	public Boolean getAccepte() {
		return accepte;
	}
	@Transient
	public void setAccepte(Boolean accepte) {
		this.accepte = accepte;
	}


	@Transient
	public List<ILigneDocumentTiers> calculNbReelLigneImpression(int coupure,List<ILigneDocumentTiers> ligneInitiales){
		int nbLigne=0;
		int rangImpression=0;
		LinkedList<ILigneDocumentTiers> listeFinale=new LinkedList<ILigneDocumentTiers>();
		LinkedList<ILigneDocumentTiers> liste2=new LinkedList<ILigneDocumentTiers>();
		for (Object ligne : ligneInitiales) {
			liste2.clear();
			liste2=((SWTLigneDocument)ligne).creeLigneImpression_indirect(coupure,rangImpression);
			for (ILigneDocumentTiers ligneFinale : liste2) {
				listeFinale.add(ligneFinale);
			}
			rangImpression++;
			
			//nbLigne=nbLigne+((TaLFacture)ligne).calculNbLigneLibelle(coupure);
		}
	return listeFinale;
	}
	
	@Transient
	public int calculNbLigne(LinkedList listeFinale,int nombreLineBreakTotaux,int nombreLineBreakMaxi, int nombreFois){
		int sizeTotal=0;
		int reste = 0;
		if(listeFinale.size() <= nombreLineBreakTotaux ){
			sizeTotal = nombreLineBreakTotaux;
		}else{
			if(listeFinale.size() <= nombreLineBreakMaxi ){
				sizeTotal = nombreLineBreakMaxi+nombreLineBreakTotaux;
			}else{
				reste=listeFinale.size()-(nombreFois*nombreLineBreakMaxi);
				if(reste<=nombreLineBreakTotaux){
					sizeTotal=  (nombreFois*nombreLineBreakMaxi)+(nombreLineBreakTotaux);
				}else{
					sizeTotal =  (nombreFois*nombreLineBreakMaxi)+(nombreLineBreakMaxi)+nombreLineBreakTotaux;
				}
			}
		}
		return sizeTotal;
	}
	@Transient
	public double calculNbLigneFacture (boolean lot, int paramCaracteres) {
		int caracteres = 0;
		
		if(lot==false) {
			 caracteres=paramCaracteres; //40;//58
		} else {
			 caracteres=35;
		}
		String param ="libelle-codeArticle";
		return calculNbLigneFacture(caracteres, param);
	}
	@Transient
	public double calculNbLigneFacture(int caracteres, String param) {
		
		BigDecimal lengthCaractereCodeArticle = BigDecimal.ZERO;
		BigDecimal lignesCodeArticle = BigDecimal.ZERO;
		BigDecimal sizeTotalLignesCodeArticle = BigDecimal.ZERO;
		BigDecimal lignesCodeArticleVide = BigDecimal.ZERO;
//		BigDecimal sizeTotalLignesCodeArticleVide = BigDecimal.ZERO;
		
//		BigDecimal lengthCaractereLibelleArticle = BigDecimal.ZERO;
//		BigDecimal lignesLibelleArticle = BigDecimal.ZERO;
//		BigDecimal sizeTotalLignesLibelleArticle = BigDecimal.ZERO;
		BigDecimal lengthCaractereNumLot = BigDecimal.ZERO;
		BigDecimal lignesNumLot = BigDecimal.ZERO;
		BigDecimal sizeTotalLignesNumLot = BigDecimal.ZERO;
		
		BigDecimal lengthCaractereLibelleArticle = BigDecimal.ZERO;
		BigDecimal lignesLibelleArticle = BigDecimal.ZERO;
		
		BigDecimal lengthCaractereLibelleCommentaireArticle = BigDecimal.ZERO;
		BigDecimal lignesLibelleCommentaireArticle = BigDecimal.ZERO;
//		BigDecimal sizeTotalLignesLibelleCommentaireArticle = BigDecimal.ZERO;
		
		BigDecimal lengthCaractereLibelleDescriptionArticle = BigDecimal.ZERO;
		BigDecimal lignesLibelleDescriptionArticle = BigDecimal.ZERO;
		BigDecimal sizeTotalLignesLibelleDescriptionArticle = BigDecimal.ZERO;
		BigDecimal lignesLibelleParArticle = BigDecimal.ZERO;
		BigDecimal sizeLigne = BigDecimal.ZERO;
		BigDecimal sizeTotalLignesDocument = BigDecimal.ZERO;

		double sizeTotalLignes=0.0;
		
		
		for (Object a : lignes) {
			
		if (param.contains("codeArticle")) {
			
			if (((SWTLigneDocument)a).getTaArticle()==null){
				lignesCodeArticleVide = lignesCodeArticleVide.add(new BigDecimal(1));
//				sizeTotalLignesCodeArticleVide = sizeTotalLignesCodeArticleVide.add(new BigDecimal(1));
			}
			if (((SWTLigneDocument)a).getTaArticle()!=null){
				
			if (((SWTLigneDocument)a).getTaArticle().getCodeArticle()!=null); {
				
				lengthCaractereCodeArticle = new BigDecimal(((SWTLigneDocument)a).getTaArticle().getCodeArticle().length());//renvoi le nombre de caractéres
				lengthCaractereCodeArticle = new BigDecimal(((SWTLigneDocument)a).getTaArticle().getCodeArticle().length()).divide(new BigDecimal(10),4,BigDecimal.ROUND_HALF_DOWN);//renvoi le nombre de caractére divisé
		
					lignesCodeArticle = lignesCodeArticle.add( new BigDecimal (lengthCaractereCodeArticle.doubleValue()-(lengthCaractereCodeArticle.doubleValue() %1)));
				if (lengthCaractereCodeArticle.doubleValue() %1 > 0) {
					lignesCodeArticle = lignesCodeArticle.add(new BigDecimal(1));
				}
//				sizeTotalLignesCodeArticle = sizeTotalLignesCodeArticle.add(lignesCodeArticle);
			}
						
			}
		}
			
		if (param.contains("libelle")) {
			
			if (((SWTLigneDocument)a).libLDocument!=null && ((SWTLigneDocument)a).getTaArticle()!=null) {
				lengthCaractereLibelleArticle = new BigDecimal(((SWTLigneDocument)a).libLDocument.length());//renvoi le nombre de caractéres
				lengthCaractereLibelleArticle = new BigDecimal(((SWTLigneDocument)a).libLDocument.length()).divide(new BigDecimal(caracteres),4,BigDecimal.ROUND_HALF_DOWN);//renvoi le nombre de caractére divisé
				
				lignesLibelleArticle = lignesLibelleArticle.add( new BigDecimal (lengthCaractereLibelleArticle.doubleValue()-(lengthCaractereLibelleArticle.doubleValue() %1)));
				if (lengthCaractereLibelleArticle.doubleValue() %1 > 0) {
					lignesLibelleArticle = lignesLibelleArticle.add(new BigDecimal(1));
				}
//				sizeTotalLignesLibelleCommentaireArticle = sizeTotalLignesLibelleCommentaireArticle.add(lignesLibelleCommentaireArticle);				
			} else if (((SWTLigneDocument)a).libLDocument!=null && ((SWTLigneDocument)a).libLDocument.length()>0){
				
				lengthCaractereLibelleCommentaireArticle = new BigDecimal(((SWTLigneDocument)a).libLDocument.length());//renvoi le nombre de caractéres
				lengthCaractereLibelleCommentaireArticle = new BigDecimal(((SWTLigneDocument)a).libLDocument.length()).divide(new BigDecimal(150),4,BigDecimal.ROUND_HALF_DOWN);//renvoi le nombre de caractére divisé
				
				lignesLibelleCommentaireArticle = lignesLibelleCommentaireArticle.add( new BigDecimal (lengthCaractereLibelleCommentaireArticle.doubleValue()-(lengthCaractereLibelleCommentaireArticle.doubleValue() %1)));
				if (lengthCaractereLibelleCommentaireArticle.doubleValue() %1 > 0) {
					lignesLibelleCommentaireArticle = lignesLibelleCommentaireArticle.add(new BigDecimal(1));
				}
				
			} 
				if (param.contains("description")) {
				
				if (((SWTLigneDocument)a).getTaArticle()!=null && ((SWTLigneDocument)a).getTaArticle().getLibellelArticle()!=null) {
					lengthCaractereLibelleDescriptionArticle = new BigDecimal(((SWTLigneDocument)a).getTaArticle().getLibellelArticle().length());//renvoi le nombre de caractéres
					lengthCaractereLibelleDescriptionArticle = new BigDecimal(((SWTLigneDocument)a).getTaArticle().getLibellelArticle().length()).divide(new BigDecimal(150),4,BigDecimal.ROUND_HALF_DOWN);//renvoi le nombre de caractére divisé
					
					lignesLibelleDescriptionArticle = lignesLibelleDescriptionArticle.add( new BigDecimal (lengthCaractereLibelleDescriptionArticle.doubleValue()-(lengthCaractereLibelleDescriptionArticle.doubleValue() %1)));
					if (lengthCaractereLibelleDescriptionArticle.doubleValue() %1 > 0) {
						lignesLibelleDescriptionArticle = lignesLibelleDescriptionArticle.add(new BigDecimal(2));
					}
//					sizeTotalLignesLibelleDescriptionArticle = sizeTotalLignesLibelleDescriptionArticle.add(lignesLibelleDescriptionArticle);					
				} 				
			}							
		}
		
		if (param.contains("numLot")) {
			
			if (((SWTLigneDocument)a).getTaLot()!=null){
			
				if (((SWTLigneDocument)a).getTaLot().getNumLot()!=null && !((SWTLigneDocument)a).getTaLot().getNumLot().startsWith("VIRT_")) {
					lengthCaractereNumLot = new BigDecimal(((SWTLigneDocument)a).getTaLot().getNumLot().length());//renvoi le nombre de caractéres du numéro de lot
					lengthCaractereNumLot = new BigDecimal(((SWTLigneDocument)a).getTaLot().getNumLot().length()).divide(new BigDecimal(17),4,BigDecimal.ROUND_HALF_DOWN);//renvoi le nombre de caractére divisé				
					lignesNumLot = lignesNumLot.add( new BigDecimal (lengthCaractereNumLot.doubleValue()-(lengthCaractereNumLot.doubleValue() %1)));
					if (lengthCaractereNumLot.doubleValue() %1 > 0) {
						lignesNumLot = lignesNumLot.add(new BigDecimal(1));
					}
//					sizeTotalLignesNumLot = sizeTotalLignesNumLot.add(lignesNumLot);
				}
			}			
		}
		
		lignesCodeArticle=lignesCodeArticle.add(lignesCodeArticleVide);
		lignesLibelleParArticle=lignesLibelleParArticle.add(lignesLibelleArticle).add(lignesLibelleCommentaireArticle).add(lignesLibelleDescriptionArticle);
		
		if (lignesCodeArticle.doubleValue() < lignesLibelleParArticle.doubleValue()) {
			sizeLigne = lignesLibelleParArticle.add(new BigDecimal(1));
		} else {
			sizeLigne=lignesCodeArticle.add(new BigDecimal(1));
		}
		if (sizeLigne.doubleValue() < lignesNumLot.doubleValue()+1) {
			sizeLigne= lignesNumLot.add(new BigDecimal(1));
		} 

		sizeTotalLignesDocument = sizeTotalLignesDocument.add(sizeLigne);
		
			lengthCaractereCodeArticle = BigDecimal.ZERO;
			lengthCaractereLibelleArticle = BigDecimal.ZERO;
			lengthCaractereLibelleCommentaireArticle = BigDecimal.ZERO;
			lengthCaractereLibelleDescriptionArticle = BigDecimal.ZERO;
			lignesCodeArticle = BigDecimal.ZERO;
			lignesCodeArticleVide = BigDecimal.ZERO;
			lignesLibelleArticle = BigDecimal.ZERO;
			lignesLibelleCommentaireArticle = BigDecimal.ZERO;
			lignesLibelleDescriptionArticle = BigDecimal.ZERO;
			lengthCaractereNumLot = BigDecimal.ZERO;
			lignesNumLot =  BigDecimal.ZERO;
			sizeLigne = BigDecimal.ZERO;
			lignesLibelleParArticle = BigDecimal.ZERO;
		}
		
		
		System.out.print("le nombre de lignes code article est de "+sizeTotalLignesCodeArticle.doubleValue()+lignesCodeArticleVide.doubleValue());
		System.out.print("le nombre de lignes commentaire est de "+sizeTotalLignesDocument.doubleValue());
		System.out.print("le nombre de lignes description est de "+sizeTotalLignesLibelleDescriptionArticle.doubleValue());
		System.out.print("le nombre de lignes numéro de lot est de "+sizeTotalLignesNumLot.doubleValue());
		System.out.print("le nombre de lignes est de "+sizeTotalLignes+" lignes d'écriture ");
		return sizeTotalLignesDocument.doubleValue();
		
	}
	
	@Transient
	public double calculMarginTop(double calcul) {
		double valeurs=0.0;
		
		if (calcul %1 >= 0.00 && calcul %1 < 0.05){
			valeurs = 350;		
		} else if (calcul %1 >= 0.05 && calcul %1 < 0.10) {
			valeurs = 330;
		} else if (calcul %1 >= 0.10 && calcul %1 < 0.15) {
			valeurs = 290;			
		} else if (calcul %1 >= 0.15 && calcul %1 < 0.20) {
			valeurs = 270;			
		} else if (calcul %1 >= 0.20 && calcul %1 < 0.25) {
			valeurs = 250;			
		} else if (calcul %1 >= 0.25 && calcul %1 < 0.30) {
			valeurs = 220;			
		} else if (calcul %1 >= 0.30 && calcul %1 < 0.35) {
			valeurs = 190;		
		} else if (calcul %1 >= 0.35 && calcul %1 < 0.40) {
			valeurs = 170;			
		} else if (calcul %1 >= 0.40 && calcul %1 < 0.45) {
			valeurs = 135;		
		} else if (calcul %1 >= 0.45 && calcul %1 < 0.50) {
			valeurs = 110;			
		} else if (calcul %1 >= 0.50 && calcul %1 < 0.55) {
			valeurs = 80;		
		} else if (calcul %1 >= 0.55 && calcul %1 < 0.60) {
			valeurs = 60;		
		} else if (calcul %1 >= 0.60 && calcul %1 < 0.65) {
			valeurs = 20;	
		} else if (calcul %1 >= 0.65 && calcul %1 < 0.70) {
			valeurs = 0;	
		} else if (calcul %1 >= 0.70 && calcul %1 < 0.80) {
			valeurs = 390;			
		} else if (calcul %1 >= 0.80 && calcul %1 < 0.90) {
			valeurs = 400;		
		} else if (calcul %1 >= 0.90 && calcul %1 < 1) {
			valeurs = 400;		
		}
		
		if(getTxRemHtDocument().floatValue()>0) {
			valeurs=valeurs-47;
		}
		if(getTxRemTtcDocument().floatValue()>0) {
			valeurs=valeurs-19;
		}
		System.out.print("La valeur du footer est de"+valeurs);
		return valeurs;		
	}
	
	// Calcul du positionement du bas de page selon (nombres de lignes différente, nombres de lignes constituant le code articles,
	// nombres de lignes de commentaires ou de libellés d'articles, la présence d'un escompte, d'une remise ou d'un avoir)
	@Transient
	public double calculFooter(double calcul) {
			double valeurs=0;
		// c représente le nombre maximum de ligne sur une page sans bas de page
			int c = 56;
			double r=0;
		// r récupére le modulo entre le nombres de lignes et le nombre de pages 	
			 r = calcul %c;			
		// Si le nombres de lignes incluant les interlignes est inférieur ou = à 38
					if (r <= 38 ) {
						valeurs = 380-(10 * r);//440
		// Si le nombres de lignes est supérieur à 38 et inférieur ou = à c qui est le maxi de lignes sur une page
		// Le footer dans cette plage de lignes conserve la même valeur 
					} else {
						valeurs = 440;
					}				

		// Si un avoir est appliqué, 1 lignes du footer apparait
		// Si l'objet SWTDocument courant est une instance de TaFacture 
			if(this instanceof TaFacture) {
		// Un cast de la classe peut s'operer
				if(((TaFacture)this).getTaRAvoirs()!=null && !((TaFacture)this).getTaRAvoirs().isEmpty()) {
					if (r <= 36 ) {
						valeurs=valeurs-19;
		// Sinon pour un nombre de lignes plus important qu'un modulo de 21 le bas de page sera positionné sur la page suivante
					} else if (r >36 ){
						valeurs = 410;
					}				
				}
			}
		// Si seulement une remise globale est appliqué, 3 lignes du footer apparaisse
		if(getTxRemHtDocument().floatValue()>0 && getTxRemTtcDocument().floatValue()==0) {
			// Si r est inférieur ou = à 19 lignes le margin-top du footer est abaissé de 47 pixels pour le déploiement
			// des 3 lignes de remise précedément caché	
				if (r <= 34 ) {
					valeurs=valeurs-47;
			// Sinon pour un nombre de lignes plus important qu'un modulo de 19 le bas de page sera positionné sur la page suivante 
				} else if (r >34){
					valeurs = 360;
				}							
			}
		// Si seulement un escompte est appliqué, 1 lignes du footer apparait
		if(getTxRemTtcDocument().floatValue()>0 && getTxRemHtDocument().floatValue()==0) {
		// Si r est inférieur ou = à 20 lignes le margin-top du footer est abaissé de 19 pixels pour le déploiement
		// de la ligne d'escompte précedément caché	
			if (r <= 36 ) {
				valeurs=valeurs-19;
		// Sinon pour un nombre de lignes plus important qu'un modulo de 20 le bas de page sera positionné sur la page suivante
			} else if (r >36 ){
				valeurs = 390;
			}				
		}
		
		// Si un escompte est une remise sont appliqué, 4 lignes du footer apparaisse
				if(getTxRemTtcDocument().floatValue()>0 && getTxRemHtDocument().floatValue()>0) {
				// Si r est inférieur ou = à 18 lignes le margin-top du footer est abaissé de 66 pixels pour le déploiement
				// de la ligne d'escompte et celle des remises précedément caché	
					if (r <= 32 ) {
						valeurs=valeurs-66;
				// Sinon pour un nombre de lignes plus important qu'un modulo de 18 le bas de page sera positionné sur la page suivante
					} else if (r >32 ){
						valeurs = 335;
					}				
				}
		System.out.print("La valeur du footer est de"+valeurs);
		return valeurs;		
	}
	
	@Transient
	public boolean retourneValeurs(LinkedList listeFinale,int count, int sizeTotal){
		if(count<= sizeTotal ) {
			//a = count%nombreLineBreakMaxi;
//			if(a == 0 && count != 0 && count != sizeTotal){
//				row["flagVisible"] = false;
//			}
//			else{
				//row["flagVisible"] = true;
//			}
			
			if(count <= listeFinale.size()){
				
			}
			count++;
			return true;	
		}
		return false;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}


//	@Column(name = "nb_decimales_qte")
//	public Integer getNbDecimalesQte() {
//		return nbDecimalesQte;
//	}
//
//	public void setNbDecimalesQte(Integer nbDecimalesQte) {
//		this.nbDecimalesQte = nbDecimalesQte;
//	}
//
//	@Column(name = "nb_decimales_prix")
//	public Integer getNbDecimalesPrix() {
//		return nbDecimalesPrix;
//	}
//
//	public void setNbDecimalesPrix(Integer nbDecimalesPrix) {
//		this.nbDecimalesPrix = nbDecimalesPrix;
//	}

	
	
//	@PostPersist
//	public void beforePost()throws Exception{
//		if(this.getTaInfosDocument()==null) {
//			throw new Exception("Il manque l'infoDocument du document n°: "+this.getCodeDocument());
//		}
//	}
//	
//	@PostUpdate
//	public void beforeUpdate()throws Exception{
//		if(this.getTaInfosDocument()==null) {
//			throw new Exception("Il manque l'infoDocument du document n°: "+this.getCodeDocument());
//		}
//	}
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_etat")
//	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat",champEntite="TaEtat.idEtat", clazz = TaEtat.class)
//	public TaEtat getTaEtat() {
//		return this.taEtat;
//	}
//
//	public void setTaEtat(TaEtat taEtat) {
//		this.taEtat = taEtat;
//	}	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_r_etat")
	@LgrHibernateValidated(champBd = "id_r_etat",table = "ta_r_etat",champEntite="TaREtat.idREtat", clazz = TaREtat.class)
	public TaREtat getTaREtat() {
		return this.taREtat;
	}

	public void setTaREtat(TaREtat taREtat) {
		this.taREtat = taREtat;
	}
	
//	@Column(name = "utilise_unite_saisie")
//	public Boolean getUtiliseUniteSaisie() {
//		return utiliseUniteSaisie;
//	}
//
//	public void setUtiliseUniteSaisie(Boolean utiliseUniteSaisie) {
//		this.utiliseUniteSaisie = utiliseUniteSaisie;
//	}
	
/*
 * 
 * 
	@PrePersist
	public void beforePost()throws Exception{
//		   New.CODE_DOCUMENT = Upper(New.CODE_DOCUMENT);
//		   New.QUI_CREE_DOCUMENT  = USER;
//		   New.QUAND_CREE_DOCUMENT = 'NOW';
//		   New.QUI_MODIF_DOCUMENT = USER;
//		   New.QUAND_MODIF_DOCUMENT = 'NOW';
//		   new.IP_ACCES = current_connection;
//		   select num_version from ta_version into new."VERSION";
		TaVersionDAO daoVersion =new TaVersionDAO();
		TaVersion version= daoVersion.findInstance();
		this.setCodeDocument(getCodeDocument().toUpperCase());
		this.setQuiCreeDocument("");
		this.setQuandCreeDocument(new Date());
		this.setQuiModifDocument("");
		this.setQuandModifDocument(new Date());
		this.setIpAcces("");
		this.setVersion(version.getNumVersion());
	}
	
//	@PreUpdate
	public void beforeUpdate()throws Exception{
		TaVersionDAO daoVersion =new TaVersionDAO();
		TaVersion version= daoVersion.findInstance();
		this.setCodeDocument(getCodeDocument().toUpperCase());
		this.setQuiModifDocument("");
		this.setQuandModifDocument(new Date());
		this.setIpAcces("");
		this.setVersion(version.getNumVersion());
	}
*/
	

	public void verifInfoDocument()throws Exception{
		if(this.getTaInfosDocument()==null) {
			throw new Exception("Il manque l'infoDocument du document n°: "+this.getCodeDocument());
		}else {
			if(this.getTaInfosDocument().getTaDocument()==null
					|| this.getTaInfosDocument().getTaDocument().getCodeDocument()== null || !this.getTaInfosDocument().getTaDocument().getCodeDocument().equals(this.getCodeDocument()))
				throw new Exception("l'infoDocument du document n°: "+this.getCodeDocument() +" n'est pas cohérent");
		}
	}	



}
