package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.IEditionEtatTracabiliteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped 
public class EtatNonConformiteController implements Serializable {

	private TaLot lot;
	private List<TaLot> liste;
	private boolean testConf;
	private String numLot;
	private String nomLot;
	private String libelleLot;
	private String codeArticle;
	private String codeFabrication;
	private String commentControle;
	private String resText;
	private String commentAction;
	private boolean resBool;
	private TaFabrication fabrication;
	private TaArticle article;
//	private TaLFabrication lFabrication;
	private Date dateProduit;
	private Date dluo;
	
	private ModeObjetEcranServeur modeEcran;
	
	private @EJB IEditionEtatTracabiliteServiceRemote editionsService;
	private @EJB ITaLotServiceRemote lotService;
	
	public EtatNonConformiteController(){}
	
	public TaLot getLot() {
		return lot;
	}
	public void setLot(TaLot lot) {
		this.lot = lot;
	}
	public List<TaLot> getListe() {
		return liste;
	}
	public void setListe(List<TaLot> liste) {
		this.liste = liste;
	}
	public boolean isTestConf() {
		return testConf;
	}
	public void setTestConf(boolean testConf) {
		this.testConf = testConf;
	}
	public String getNumLot() {
		return numLot;
	}
	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}
	public String getNomLot() {
		return nomLot;
	}
	public void setNomLot(String nomLot) {
		this.nomLot = nomLot;
	}
	public String getLibelleLot() {
		return libelleLot;
	}
	public void setLibelleLot(String libelleLot) {
		this.libelleLot = libelleLot;
	}
	public String getCodeArticle() {
		return codeArticle;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public String getCodeFabrication() {
		return codeFabrication;
	}
	public void setCodeFabrication(String codeFabrication) {
		this.codeFabrication = codeFabrication;
	}
	public String getCommentControle() {
		return commentControle;
	}
	public void setCommentControle(String commentControle) {
		this.commentControle = commentControle;
	}
	public String getResText() {
		return resText;
	}
	public void setResText(String resText) {
		this.resText = resText;
	}
	public String getCommentAction() {
		return commentAction;
	}
	public void setCommentAction(String commentAction) {
		this.commentAction = commentAction;
	}
	public boolean isResBool() {
		return resBool;
	}
	public void setResBool(boolean resBool) {
		this.resBool = resBool;
	}
	public TaFabrication getFabrication() {
		return fabrication;
	}
	public void setFabrication(TaFabrication fabrication) {
		this.fabrication = fabrication;
	}
	public TaArticle getArticle() {
		return article;
	}
	public void setArticle(TaArticle article) {
		this.article = article;
	}
//	public TaLFabrication getlFabrication() {
//		return lFabrication;
//	}
//	public void setlFabrication(TaLFabrication lFabrication) {
//		this.lFabrication = lFabrication;
//	}
	public Date getDateProduit() {
		return dateProduit;
	}
	public void setDateProduit(Date dateProduit) {
		this.dateProduit = dateProduit;
	}
	public Date getDluo() {
		return dluo;
	}
	public void setDluo(Date dluo) {
		this.dluo = dluo;
	}
	
	public boolean etatBouton(String bouton) {
		return etatBouton(bouton,modeEcran);
	}
	
	public boolean etatBouton(String bouton, ModeObjetEcranServeur mode) {
		boolean retour = true;
		switch (mode.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return retour;

	}
	
	public void actImprimer(ActionEvent actionEvent) throws FinderException {
		/*
		FacesContext context = FacesContext.getCurrentInstance();  
		
		context.addMessage(null, new FacesMessage("Lots - Resultats de Correction", "actImprimer : \"Etat Non-Conformité\"")); 
		System.out.println("********************************** actImprimer : \"Etat Non-Conformité\" *************************************************");
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		System.out.println("EtatNonConformiteController.actImprimer() - findByNonConformeCorrection()");
		
		List<TaResultatCorrection> listeRes = new ArrayList<TaResultatCorrection>(lotService.findByNonConformeCorrection());
		
		System.out.println("********************************** listeRes : "+ listeRes);
		
		for (TaResultatCorrection res : listeRes){
			
			System.out.println("************** getObservation() : "+ res.getObservation());
			System.out.println("************** getTaBareme().getTaConformite().getLibelleConformite() : "+ res.getTaBareme().getTaConformite().getLibelleConformite());
			System.out.println("************** getTaBareme().getTaConformite().getValeurDefaut() : "+ res.getTaBareme().getTaConformite().getValeurDefaut());
			System.out.println("************** getTaBareme().getTaConformite().getValeurCalculee() : "+ res.getTaBareme().getTaConformite().getValeurCalculee());
			System.out.println("************** getTaBareme().getTaConformite().getDeuxiemeValeur() : "+ res.getTaBareme().getTaConformite().getDeuxiemeValeur());
			System.out.println("************** getTaResultatConformite().getTaConformite().getLibelleConformite() : "+ res.getTaResultatConformite().getTaConformite().getLibelleConformite());
			System.out.println("************** getTaResultatConformite().getTaConformite().getValeurDefaut() : "+ res.getTaResultatConformite().getTaConformite().getValeurDefaut());
			System.out.println("************** getTaResultatConformite().getTaConformite().getValeurCalculee() : "+ res.getTaResultatConformite().getTaConformite().getValeurCalculee());
			System.out.println("************** getTaResultatConformite().getTaConformite().getDeuxiemeValeur() : "+ res.getTaResultatConformite().getTaConformite().getDeuxiemeValeur());
			System.out.println("************** getTaResultatConformite().getTaLot().getNumLot() : "+ res.getTaResultatConformite().getTaLot().getNumLot());
			System.out.println("************** getTaResultatConformite().getTaLot().getTaArticle().getCodeArticle() : "+ res.getTaResultatConformite().getTaLot().getTaArticle().getCodeArticle());
			System.out.println("************** getTaResultatConformite().getTaLot().getTaArticle().getLibellecArticle() : "+ res.getTaResultatConformite().getTaLot().getTaArticle().getLibellecArticle());
			System.out.println("************** FOR - BEGIN **************" );
			for (int i = 0 ; i < res.getTaResultatConformite().getTaResultatCorrections().size() ; i++){
				System.out.println("************** getTaResultatConformite().getTaResultatCorrections().get("+i+").getEffectuee() : "+ res.getTaResultatConformite().getTaResultatCorrections().get(i).getEffectuee());
				System.out.println("************** getTaResultatConformite().getTaResultatCorrections().get("+i+").getObservation() : "+ res.getTaResultatConformite().getTaResultatCorrections().get(i).getObservation());
				System.out.println("************** getTaResultatConformite().getTaResultatCorrections().get("+i+").getIdResultatCorrection() : "+ res.getTaResultatConformite().getTaResultatCorrections().get(i).getIdResultatCorrection());
				if (i != res.getTaResultatConformite().getTaResultatCorrections().size()){
					System.out.println("************** i = "+i+"  **************" );
				}
			}
			System.out.println("************** FOR -  END  **************" );
			
			System.out.println("-- res.getValide().toString() : "+res.getValide().toString()+" -------");
//			System.out.println("---- listeRes.get("+i+").getObservation().toString() : "+listeRes.get(i).getObservation().toString()+" -------");
//			System.out.println("------ listeRes.get("+i+").getEffectuee().toString() : "+listeRes.get(i).getEffectuee().toString()+" -------");
			System.out.println("** res.getTaResultatConformite().getTaLot().getNumLot() : "+res.getTaResultatConformite().getTaLot().getNumLot());
			//System.out.println("** listeRes.get(i).getTaResultatConformite().getTaConformite().getTaGroupe().getLibelle() : "+listeRes.get(i).getTaResultatConformite().getTaConformite().getTaGroupe().getLibelle());
		}
		
		System.out.println("---------------- ***************** ----------------- **************** ----------------");
		
		sessionMap.put("resultatCorrection", listeRes); */
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Impression d'Etat des Non-Conformités", "actImprimer")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		List<EditionEtatTracabilite> edition = new ArrayList<EditionEtatTracabilite>();
		
		edition = editionsService.editionNonConforme(null,null);
		
//		for (EditionEtatTracabilite e : edition){
//			System.out.println("***************************************> ");
//			System.out.println("e.getNumLot() : "+e.getNumLot());
//			System.out.println("e.getDateModif() : "+e.getDateModif());
//			System.out.println("e.getCodeArticle() : "+e.getCodeArticle());
//			System.out.println("e.getLibelleGroupe() : "+e.getLibelleGroupe());
//			System.out.println("e.getLibelleConformite() : "+e.getLibelleConformite());
//			System.out.println("e.getResultat() : "+e.getResultat());
//			System.out.println("e.getActionCorrective() : "+e.getActionCorrective());
//			System.out.println("e.isEffectuee() : "+e.isEffectuee());
//			System.out.println("***************************************> ");
//		}
		
		sessionMap.put("edition", edition);
		
	}
	
	public void actImprimerTmp(ActionEvent actionEvent) throws FinderException {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Lots", "actImprimer : \"Etat Non-Conformité\"")); 
		}
		System.out.println("******************************************************************************************************************");
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("lot", lotService.findByNonConformeNoDate());
		System.out.println("EtatNonConformiteController.actImprimer() - findByNonConformeNoDate()");
		this.liste = lotService.findByNonConformeNoDate();
		System.out.println("*************** La liste : "+this.liste.toString());
		
		int c=1;
		List<TaResultatConformite> resConBon = new ArrayList<TaResultatConformite>();
		for (TaLot element : this.liste) {
			List<TaResultatConformite> resCon = new ArrayList<TaResultatConformite>(element.getTaResultatConformites());
			System.out.println("********* resCon.size() ************** : "+resCon.size() );
			for (int x = 0 ; x < resCon.size() ; x++){
				if (resCon != null){
					resConBon.add(resCon.get(x));
					System.out.println("********* Premier élément : "+resCon.toString()+" NR° "+c);
				}
				c++;
			}
		}
		int b=1;
		for (TaResultatConformite taResConf : resConBon) {
			List<TaResultatCorrection> resCorr = new ArrayList<TaResultatCorrection>(taResConf.getTaResultatCorrections());
			System.out.println("****** Deuxième élément : "+resCorr.toString()+" NR° "+b);
			int a=1;
			for (TaResultatCorrection taResCorr : resCorr) {
				System.out.println("*** Dernier élément : "+taResCorr.toString()+" NR° "+b+"."+a);
				a++;
			}
			b++;
		}
		
	}
	
	
}
