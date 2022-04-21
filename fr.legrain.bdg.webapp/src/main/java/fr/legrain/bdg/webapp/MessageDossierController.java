package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.dossier.model.TaInfoEntreprise;

@Named
@ViewScoped
public class MessageDossierController implements Serializable {

	private boolean afficheMessageDossier = false;
	
	
	private String titreMessage = null;
	private String titreCorpsMessage = null;
	private String texte1Message = null;
	private String texte2Message = null;
	private String texte3Message = null;
    private String severiteMessage = null;
     
    List<ListeMessage> listeMessage;
    
    private @EJB ITaInfoEntrepriseServiceRemote taInfosEntrepriseService;
    private boolean infoEntrepriseRaisonSocialeOk = true; 
    private boolean infoEntrepriseLigneAdresseOk = true;
    private boolean infoEntrepriseCodePostalOk = true;
    private boolean infoEntrepriseVilleOk = true;
    
    @PostConstruct
    public void init() {
    	listeMessage = initialiseListeMessage();
        
    }
    //Initialise la liste des messages en fonction du résultat des controles fait sur le dossier  
    public List<ListeMessage> initialiseListeMessage() {
    	listeMessage = new ArrayList<ListeMessage>();
    	ctrlInfosEntreprise();
         
        return listeMessage;
    }
    
    private void ctrlInfosEntreprise() {
		// TODO Auto-generated method stub
    	TaInfoEntreprise infoEntreprise = taInfosEntrepriseService.findInstance();
//		afficheMessageDossier = true;
//		listeMessage.add(new ListeMessage(titreMessage, "/aide_contextuelle/article_actif_article.xhtml", "severe"));
    	infoEntrepriseRaisonSocialeOk =  (infoEntreprise.getNomInfoEntreprise()==null || infoEntreprise.getNomInfoEntreprise().equals("") || infoEntreprise.getNomInfoEntreprise().equals("Mon entreprise"));
    	infoEntrepriseLigneAdresseOk = (infoEntreprise.getAdresse1InfoEntreprise()==null || infoEntreprise.getAdresse1InfoEntreprise().equals("") || infoEntreprise.getAdresse1InfoEntreprise().equals("Mon adresse"));
    	infoEntrepriseCodePostalOk = (infoEntreprise.getCodepostalInfoEntreprise()==null || infoEntreprise.getCodepostalInfoEntreprise().equals("") || infoEntreprise.getCodepostalInfoEntreprise().equals("00000"));
    	infoEntrepriseVilleOk = (infoEntreprise.getVilleInfoEntreprise()==null || infoEntreprise.getVilleInfoEntreprise().equals("") || infoEntreprise.getVilleInfoEntreprise().equals("Ma ville"));
    	if ((infoEntreprise.getNomInfoEntreprise()==null || infoEntreprise.getNomInfoEntreprise().equals("") || infoEntreprise.getNomInfoEntreprise().equals("Mon entreprise")
    			|| infoEntreprise.getAdresse1InfoEntreprise()==null || infoEntreprise.getAdresse1InfoEntreprise().equals("") || infoEntreprise.getAdresse1InfoEntreprise().equals("Mon adresse")
    			|| infoEntreprise.getCodepostalInfoEntreprise()==null || infoEntreprise.getCodepostalInfoEntreprise().equals("") || infoEntreprise.getCodepostalInfoEntreprise().equals("00000")
    			|| infoEntreprise.getVilleInfoEntreprise()==null || infoEntreprise.getVilleInfoEntreprise().equals("") || infoEntreprise.getVilleInfoEntreprise().equals("Ma ville")				
    			|| infoEntreprise.getCodexoInfoEntreprise()==null || infoEntreprise.getCodexoInfoEntreprise().equals("")
				|| infoEntreprise.getDatedebInfoEntreprise()==null 
				|| infoEntreprise.getDatefinInfoEntreprise()==null 
				)){
    			afficheMessageDossier = true;
    			titreMessage = "Infos Entreprise";
    			titreCorpsMessage = "/message_dossier/titre_information_importante.xhtml";
    			texte1Message = "/message_dossier/infos_entreprise_champ_obligatoire.xhtml";
    			texte2Message = "";
    			texte3Message = "";
    			listeMessage.add(new ListeMessage(titreMessage, titreCorpsMessage, texte1Message, texte2Message, texte3Message, "severe"));

    	}
		
	}

    public class ListeMessage{

    	private String titreMessage; 
    	private String titreCorpsMessage;
    	private String texte1Message; 
    	private String texte2Message; 
    	private String texte3Message; 
    	private String severiteMessage;

    	public String getTitreMessage() {
    		return titreMessage;
    	}

    	public void setTitreMessage(String titreMessage) {
    		this.titreMessage = titreMessage;
    	}

    	public String getTexte1Message() {
    		return texte1Message;
    	}

    	public void setTexte1Message(String texte1Message) {
    		this.texte1Message = texte1Message;
    	}

    	
    	public String getSeveriteMessage() {
    		return severiteMessage;
    	}

    	public void setSeveriteMessage(String severiteMessage) {
    		this.severiteMessage = severiteMessage;
    	}



    	public ListeMessage(String titreMessage, String titreCorpsMessage, String texte1Message, String texte2Message, String texte3Message, String severiteMessage) {
    		super();
    		this.titreMessage = titreMessage;
    		this.titreCorpsMessage = titreCorpsMessage;
    		this.texte1Message = texte1Message;
    		this.texte2Message = texte2Message;
    		this.texte3Message = texte3Message;
    		this.severiteMessage = severiteMessage;
    	}

		public String getTitreCorpsMessage() {
			return titreCorpsMessage;
		}

		public void setTitreCorpsMessage(String titreCorpsMessage) {
			this.titreCorpsMessage = titreCorpsMessage;
		}

		public String getTexte2Message() {
			return texte2Message;
		}

		public void setTexte2Message(String texte2Message) {
			this.texte2Message = texte2Message;
		}

		public String getTexte3Message() {
			return texte3Message;
		}

		public void setTexte3Message(String texte3Message) {
			this.texte3Message = texte3Message;
		}

    }


	public boolean isAfficheMessageDossier() {
		return afficheMessageDossier;
	}


	public void setAfficheMessageDossier(boolean afficheMessageDossier) {
		this.afficheMessageDossier = afficheMessageDossier;
	}


	public List<ListeMessage> getListeMessage() {
		return listeMessage;
	}


	public void setListeMessage(List<ListeMessage> listeMessage) {
		this.listeMessage = listeMessage;
	}
	public boolean isInfoEntrepriseRaisonSocialeOk() {
		return infoEntrepriseRaisonSocialeOk;
	}
	public void setInfoEntrepriseRaisonSocialeOk(boolean infoEntrepriseRaisonSocialeOk) {
		this.infoEntrepriseRaisonSocialeOk = infoEntrepriseRaisonSocialeOk;
	}
	public boolean isInfoEntrepriseLigneAdresseOk() {
		return infoEntrepriseLigneAdresseOk;
	}
	public void setInfoEntrepriseLigneAdresseOk(boolean infoEntrepriseLigneAdresseOk) {
		this.infoEntrepriseLigneAdresseOk = infoEntrepriseLigneAdresseOk;
	}
	public boolean isInfoEntrepriseCodePostalOk() {
		return infoEntrepriseCodePostalOk;
	}
	public void setInfoEntrepriseCodePostalOk(boolean infoEntrepriseCodePostalOk) {
		this.infoEntrepriseCodePostalOk = infoEntrepriseCodePostalOk;
	}
	public boolean isInfoEntrepriseVilleOk() {
		return infoEntrepriseVilleOk;
	}
	public void setInfoEntrepriseVilleOk(boolean infoEntrepriseVilleOk) {
		this.infoEntrepriseVilleOk = infoEntrepriseVilleOk;
	}
	   
}
