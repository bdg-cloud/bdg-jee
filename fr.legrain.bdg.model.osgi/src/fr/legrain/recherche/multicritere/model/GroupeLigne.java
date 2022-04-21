package fr.legrain.recherche.multicritere.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupeLigne implements Serializable{

	private static final long serialVersionUID = 9095678056725633578L;
	
	private List<LigneCritere> groupe;
	private String libelleGroupe;
	private List<String> andOrGroup;
	private String selectedAndOrGroup;
	private boolean andOrGroupVisible = false;
	private String lblandOr;
	private int numero;

	public GroupeLigne(int numero){
		this.numero = numero;
		groupe = new ArrayList<LigneCritere>();
		
		libelleGroupe = "Groupe "+numero;
		if (numero != 1){
			andOrGroupVisible = true;
			
			lblandOr = "Relation avec le groupe précédent :";
			andOrGroup = new ArrayList<>();
			andOrGroup.add("ET");
			andOrGroup.add("OU");
//			andOrGroup.select(1);
		}

	}

	/**
	 * On ajoute une nouvelle ligne au groupe
	 * @param laligne la ligne à ajouter au groupe
	 */
	public void addLigne(LigneCritere laligne){
		groupe.add(laligne);
	}

	/**
	 * On insère la ligne à la position demandée
	 * @param pos la position de la ligne
	 * @param laligne la ligne à insérer
	 */
	public void insertLigne(int pos, LigneCritere laligne){
		groupe.add(pos,laligne);
	}

	/**
	 * Méthode permettant de monter une ligne dans le groupe
	 * @param laligne la ligne que l'on souhaite monter
	 */
	public void upLigne(LigneCritere laligne){
		int posLigne = groupe.lastIndexOf(laligne); // on récupère l'index de la ligne
		if (posLigne !=0 ){ // la ligne n'est pas la première du groupe
			// On procède à l'échange entre les lignes
			LigneCritere temp = groupe.set(posLigne-1,groupe.get(posLigne));
			groupe.set(posLigne, temp); 
		}
	}

	/**
	 * Méthode permettant de descendre une ligne dans le groupe
	 * @param laligne la ligne que l'on souhaite descendre
	 */
	public void downLigne(LigneCritere laligne){
		int posLigne = groupe.lastIndexOf(laligne); // on récupère l'index de la ligne
		if (posLigne < groupe.size()){ // la ligne n'est pas la dernière du groupe
			// On procède à l'échange entre les lignes
			LigneCritere temp = groupe.set(posLigne+1,groupe.get(posLigne));
			groupe.set(posLigne, temp);
		}
	}
	
	
	/** 
	 * Méthode retourant la taille du groupe
	 * @return la taille du groupe
	 */
	public int getSize() {
		return groupe.size();
	}

	/**
	 * Méthode permettant de supprimer une ligne dans le groupe
	 * @param pos
	 */
	public void remove(int pos) {
		groupe.remove(pos);
	}

	/** 
	 * Méthode permettant de récupérer le groupe lui 
	 * même sous forme de List de ligne
	 * @return
	 */
	public List<LigneCritere> getGroupe() {
		return groupe;
	}

	/**
	 * Renvoi la dernière position connue de la ligne passée en paramètre
	 * @param laligne dont on souhaite connaître la position
	 * @return
	 */
	public int lastPos(LigneCritere laligne){
		return groupe.lastIndexOf(laligne);
	}

	
	
	public void refreshNumGroupe(int numero){
		libelleGroupe = "Groupe "+numero;
	}

	
	public int getNumero() {
		return numero;
	}

	public String getLibelleGroupe() {
		return libelleGroupe;
	}

	public void setLibelleGroupe(String libelleGroupe) {
		this.libelleGroupe = libelleGroupe;
	}

	public List<String> getAndOrGroup() {
		return andOrGroup;
	}

	public void setAndOrGroup(List<String> andOrGroup) {
		this.andOrGroup = andOrGroup;
	}

	public String getLblandOr() {
		return lblandOr;
	}

	public void setLblandOr(String lblandOr) {
		this.lblandOr = lblandOr;
	}

	public void setGroupe(List<LigneCritere> groupe) {
		this.groupe = groupe;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getSelectedAndOrGroup() {
		return selectedAndOrGroup;
	}

	public void setSelectedAndOrGroup(String selectedAndOrGroup) {
		this.selectedAndOrGroup = selectedAndOrGroup;
	}

	public boolean isAndOrGroupVisible() {
		return andOrGroupVisible;
	}

	public void setAndOrGroupVisible(boolean andOrGroupVisible) {
		this.andOrGroupVisible = andOrGroupVisible;
	}





}
