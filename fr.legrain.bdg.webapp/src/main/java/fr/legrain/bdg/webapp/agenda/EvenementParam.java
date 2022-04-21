package fr.legrain.bdg.webapp.agenda;

import java.util.Date;
import java.util.List;

import fr.legrain.document.model.SWTDocument;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.tiers.model.TaTiers;

public class EvenementParam {
	
	public static final String NOM_OBJET_EN_SESSION = "paramEvenement";
	
	private TaAgenda taAgenda;
	private TaTypeEvenement taTypeEvenement;
	
	private String titre;
	private String description;
	private Date debut;
	private Date fin;
	
	private boolean recurent;
	private Date du;
	private Date au;
	
	private List<TaUtilisateur> listeUtilisateurs;
	private List<TaTiers> listeTiers;
	private List<SWTDocument> listeDocuments;
	
	
	public TaAgenda getTaAgenda() {
		return taAgenda;
	}
	public void setTaAgenda(TaAgenda taAgenda) {
		this.taAgenda = taAgenda;
	}
	public TaTypeEvenement getTaTypeEvenement() {
		return taTypeEvenement;
	}
	public void setTaTypeEvenement(TaTypeEvenement taTypeEvenement) {
		this.taTypeEvenement = taTypeEvenement;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public boolean isRecurent() {
		return recurent;
	}
	public void setRecurent(boolean recurent) {
		this.recurent = recurent;
	}
	public Date getDu() {
		return du;
	}
	public void setDu(Date du) {
		this.du = du;
	}
	public Date getAu() {
		return au;
	}
	public void setAu(Date au) {
		this.au = au;
	}
	public List<TaUtilisateur> getListeUtilisateurs() {
		return listeUtilisateurs;
	}
	public void setListeUtilisateurs(List<TaUtilisateur> listeUtilisateurs) {
		this.listeUtilisateurs = listeUtilisateurs;
	}
	public List<TaTiers> getListeTiers() {
		return listeTiers;
	}
	public void setListeTiers(List<TaTiers> listeTiers) {
		this.listeTiers = listeTiers;
	}
	public List<SWTDocument> getListeDocuments() {
		return listeDocuments;
	}
	public void setListeDocuments(List<SWTDocument> listeDocuments) {
		this.listeDocuments = listeDocuments;
	}
}
