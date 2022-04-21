package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.bdg.article.service.remote.ITaTransporteurServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped 
public class TourneeChoixEditionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7349609492692872073L;
	
	private @EJB ITaTransporteurServiceRemote taTransporteurService;	
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	private boolean choixEdition=false;
	private boolean tcheckEdition=false;
	private String identification="ListeEtatTourneeParTransporteur";
	private TaTransporteurDTO transporteur;

	private Date dateSouhaite;
	private Date dateDebut;
	private Date dateFin;
	private Boolean avecTransporteur=true;
	private Boolean surPeriode=false;
	
	
	
	@PostConstruct
	public void init() {
		TaInfoEntreprise infos= taInfoEntrepriseService.findInstance();
		dateDebut=infos.getDatedebInfoEntreprise();
		dateFin=infos.getDatefinInfoEntreprise();
		dateSouhaite=LibDate.dateDuJour();
	}
	
	public boolean isChoixEdition() {
		return choixEdition;
	}
	
	public void setChoixEdition(boolean choixEdition) {
		this.choixEdition = choixEdition;
	}
	
	public boolean isTcheckEdition() {
		return tcheckEdition;
	}

	public void setTcheckEdition(boolean tcheckEdition) {
		this.tcheckEdition = tcheckEdition;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public TaTransporteurDTO getTransporteur() {
		return transporteur;
	}

	public void setTransporteur(TaTransporteurDTO transporteur) {
		this.transporteur = transporteur;
	}




	public Boolean getAvecTransporteur() {
		return avecTransporteur;
	}

	public void setAvecTransporteur(Boolean avecTransporteur) {
		this.avecTransporteur = avecTransporteur;
	}
	
	public List<TaTransporteurDTO> transporteurAutoCompleteDTOLight(String query) {
		List<TaTransporteurDTO> allValues = taTransporteurService.findByCodeLight("*");
		List<TaTransporteurDTO> filteredValues = new ArrayList<TaTransporteurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTransporteurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTransporteur().toLowerCase().contains(query.toLowerCase())
					|| civ.getLiblTransporteur().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

		if(value!=null) {

		}
//		validateUIField(nomChamp,value);
	}
	
	public void checkAvecTransporteur() {
		if(!avecTransporteur)transporteur=null;
	}



	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateSouhaite() {
		return dateSouhaite;
	}

	public void setDateSouhaite(Date dateSouhaite) {
		this.dateSouhaite = dateSouhaite;
	}

	public Boolean getSurPeriode() {
		return surPeriode;
	}

	public void setSurPeriode(Boolean surPeriode) {
		this.surPeriode = surPeriode;
	}
}
