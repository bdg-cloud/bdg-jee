package fr.legrain.moncompte.admin.webapp.admin;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.moncompte.model.TaProduit;



@ManagedBean
@ViewScoped  
public class TopBarController implements Serializable {  


	private List<TaPanier> listePanier;
	private List<TaClient> listeClient;
	private List<TaProduit> listeProduit; 
	private List<TaProduit> listeProduitVendable; 
	
	private int nbPanier = 0;
	private int nbClient = 0;
	private int nbProduit = 0;
	private int nbProduitVendable = 0;
	

//	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaClientServiceRemote taClientService;
	private @EJB ITaProduitServiceRemote taProduitService;

	@PostConstruct
	public void postConstruct(){
		refresh();
	}

	public TopBarController() {  
	}

	public void refresh(){
//		listePanier = taPanierService.selectAll();
		listeClient = taClientService.selectAll();
		listeProduit = taProduitService.selectAll();
		
		listeProduitVendable = new ArrayList<TaProduit>();
		for (TaProduit p : listeProduit) {
			if(p.getVendable()!=null && p.getVendable()) {
				listeProduitVendable.add(p);
			}
		}
		
//		nbPanier = listePanier.size();
		nbClient = listeClient.size();
		nbProduit = listeProduit.size();
		nbProduitVendable = listeProduitVendable.size();
	}

	public List<TaPanier> getListePanier() {
		return listePanier;
	}

	public void setListePanier(List<TaPanier> listePanier) {
		this.listePanier = listePanier;
	}

	public List<TaClient> getListeClient() {
		return listeClient;
	}

	public void setListeClient(List<TaClient> listeClient) {
		this.listeClient = listeClient;
	}

	public List<TaProduit> getListeProduit() {
		return listeProduit;
	}

	public void setListeProduit(List<TaProduit> listeProduit) {
		this.listeProduit = listeProduit;
	}

	public List<TaProduit> getListeProduitVendable() {
		return listeProduitVendable;
	}

	public void setListeProduitVendable(List<TaProduit> listeProduitVendable) {
		this.listeProduitVendable = listeProduitVendable;
	}

	public int getNbPanier() {
		return nbPanier;
	}

	public void setNbPanier(int nbPanier) {
		this.nbPanier = nbPanier;
	}

	public int getNbClient() {
		return nbClient;
	}

	public void setNbClient(int nbClient) {
		this.nbClient = nbClient;
	}

	public int getNbProduit() {
		return nbProduit;
	}

	public void setNbProduit(int nbProduit) {
		this.nbProduit = nbProduit;
	}

	public int getNbProduitVendable() {
		return nbProduitVendable;
	}

	public void setNbProduitVendable(int nbProduitVendable) {
		this.nbProduitVendable = nbProduitVendable;
	}



}  
