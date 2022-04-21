package fr.legrain.bdg.compteclient.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;

@Entity
@Table(name = "ta_mes_fournisseurs")
public class TaMesFournisseurs implements java.io.Serializable{

	private static final long serialVersionUID = 5396549841401454180L;
	
	private int idFournisseur;
	private TaUtilisateur taUtilisateur;
	private TaFournisseur taFournisseur;
	private String codeClient;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fournisseur", unique = true, nullable = false)
	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	@ManyToOne (fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name="id_ta_utilisateur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}
	
	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}
	
	@ManyToOne (fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name="id_ta_fournisseur")
	public TaFournisseur getTaFournisseur() {
		return taFournisseur;
	}
	
	public void setTaFournisseur(TaFournisseur taFournisseur) {
		this.taFournisseur = taFournisseur;
	}
	
	public TaMesFournisseurs(){
		
	}
	
	public TaMesFournisseurs(int idFournisseur){
		this.setIdFournisseur(idFournisseur);
	}
	
	@Column(name="code_client")
	public String getCodeClient() {
		return codeClient;
	}
	
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
}
