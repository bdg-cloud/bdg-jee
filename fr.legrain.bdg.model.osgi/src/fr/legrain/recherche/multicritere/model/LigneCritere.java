
package fr.legrain.recherche.multicritere.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public abstract class LigneCritere implements Serializable{

	private static final long serialVersionUID = 7973350209239980839L;

	private LigneCritere cetteLigne = this;

	// Icones
	public static String iconDelete = "/icons/bin_empty.png";
	public static String iconUp = "/icons/arrow_up.png";
	public static String iconDown = "/icons/arrow_down.png";
	public static String iconWarning = "/icons/error.png";

	public static final String TYPE_LIGNE_CRITERE_TIERS = "Tiers";
	public static final String TYPE_LIGNE_CRITERE_ARTICLE = "Article";
	public static final String TYPE_LIGNE_CRITERE_FACTURE = "Facture";
	public static final String TYPE_LIGNE_CRITERE_DEVIS = "Devis";
	public static final String TYPE_LIGNE_CRITERE_COMMANDE = "Commande";
	public static final String TYPE_LIGNE_CRITERE_AVOIR = "Avoir";
	public static final String TYPE_LIGNE_CRITERE_APPORTEUR = "Apporteur";
	public static final String TYPE_LIGNE_CRITERE_ACOMPTE = "Acompte";
	public static final String TYPE_LIGNE_CRITERE_PROFORMA = "Proforma";
	public static final String TYPE_LIGNE_CRITERE_LIVRAISON = "Livraison";
	
	protected GroupeLigne groupe;

	protected String type = null;
	protected boolean btnDelete = true;
	protected boolean btnUp = true;
	protected boolean btnDown = true;
	
	protected List<String> andOr = new LinkedList<>();
	protected List<String> critere = new LinkedList<>();
	protected List<String> comparaison = new LinkedList<>();
	protected List<String> booleen = new LinkedList<>();
	protected String selectedAndOr = null;
	protected String selectedCritere = null;
	protected String selectedComparaison = null;
	protected String selectedBooleen = null;
	
	protected String valeur1 = null;
	protected String valeur2 = null;
	protected String warning = null;
	
	protected boolean valeur1Visible = true;
	protected boolean valeur1Enabled = true;
	protected boolean valeur2Visible = false;
	protected boolean booleenVisible = false;
	protected boolean booleenEnabled = false;
	protected boolean andOrVisible = false;
	protected boolean warningVisible = false;
	protected boolean comparaisonEnabled = false;

	public static final String BOOLEAN_TRUE_TEXT = "Oui";
	public static final String BOOLEAN_TRUE_VALUE = "1";
	public static final String BOOLEAN_FALSE_TEXT = "Non";
	public static final String BOOLEAN_FALSE_VALUE = "0";
	public static final String BOOLEAN_LIBELLE_CRITERE = "Oui/Non";
	
	abstract public Map<String,String> getListeCorrespondance();
	abstract public Map<String,String> getListeTitreChamps();
	abstract public Map<String,Object> getListeObjet();
	abstract public Map<String,String> getListeTitreChampsComparateurs();

	public LigneCritere(GroupeLigne g) {
		this.groupe = g;
		initComposants();
	}

	protected void initComposants() {		
//		warning.setVisible(false);
		andOr.add("ET");
		andOr.add("OU");

		if ( groupe.getGroupe().size() >= 1 ){ 
			andOrVisible = true;
		} else {
			andOrVisible = false;
		}

		// Initialisation des champs à remplir
		valeur1Enabled = false;
		
		// Initialisation du combo pour les booleen
		booleen.add(BOOLEAN_TRUE_TEXT);
		booleen.add(BOOLEAN_FALSE_TEXT);

		valeur2Visible = false;
	}

	public void actLigneDown(){
		int numeroLigne = groupe.lastPos(cetteLigne);
		Collections.swap(groupe.getGroupe(),numeroLigne,numeroLigne+1);
	}

	public void actLigneUp(){
		int numeroLigne = groupe.lastPos(cetteLigne);
		Collections.swap(groupe.getGroupe(),numeroLigne,numeroLigne-1);
	}

	public void actLigneDelete(){
		int numeroLigne = groupe.lastPos(cetteLigne);
		groupe.getGroupe().remove(numeroLigne);

	}

	public void actChoixCritere(){
		if (!selectedCritere.equals("<Choisir>")){
			selectedComparaison = comparaison.get(0);
			comparaisonEnabled = true;
			valeur1Enabled = false;
			valeur2Visible = false;
			booleenVisible = false;
		} else {
			valeur1 = "";
			valeur2 = "";
			valeur1Enabled = false;
			valeur2Visible = false;
			selectedComparaison = comparaison.get(0);
			comparaisonEnabled = false;
			booleenVisible = false;
			booleenEnabled = false;
		}
	}

	public void actChoixComparaison(){
		if (!selectedComparaison.equals("<Choisir>")){
			valeur1Visible = true;
			valeur1Enabled = true;
			warningVisible = false;
			if(selectedComparaison.equals("est compris entre")){
				valeur2Visible = true;
			} else if(selectedComparaison.equals(BOOLEAN_LIBELLE_CRITERE)){
				valeur1Enabled = false;
				valeur1Visible = false;
				booleenVisible = true;
				booleenEnabled = true;
			} else {
				valeur2 = "";
				valeur2Visible = false;
				booleenVisible = false;
			}
		} else {
			valeur1 = "";
			valeur2 = "";
			valeur1Enabled = false;
			valeur2Visible = false;
		}
	}

	public String transformSQLandOr(String champ) {
		return champ.equals("ET")?"AND":"OR";
	}

	public GroupeLigne getGroupe() {
		return groupe;
	}

	public void setGroupe(GroupeLigne groupe) {
		this.groupe = groupe;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(boolean btnDelete) {
		this.btnDelete = btnDelete;
	}

	public boolean isBtnUp() {
		return btnUp;
	}

	public void setBtnUp(boolean btnUp) {
		this.btnUp = btnUp;
	}

	public boolean isBtnDown() {
		return btnDown;
	}

	public void setBtnDown(boolean btnDown) {
		this.btnDown = btnDown;
	}

	public List<String> getAndOr() {
		return andOr;
	}

	public void setAndOr(List<String> andOr) {
		this.andOr = andOr;
	}

	public List<String> getCritere() {
		return critere;
	}

	public void setCritere(List<String> critere) {
		this.critere = critere;
	}

	public List<String> getComparaison() {
		return comparaison;
	}

	public void setComparaison(List<String> comparaison) {
		this.comparaison = comparaison;
	}

	public List<String> getBooleen() {
		return booleen;
	}

	public void setBooleen(List<String> booleen) {
		this.booleen = booleen;
	}

	public String getValeur1() {
		return valeur1;
	}

	public void setValeur1(String valeur1) {
		this.valeur1 = valeur1;
	}

	public String getValeur2() {
		return valeur2;
	}

	public void setValeur2(String valeur2) {
		this.valeur2 = valeur2;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getSelectedAndOr() {
		return selectedAndOr;
	}

	public void setSelectedAndOr(String selectedAndOr) {
		this.selectedAndOr = selectedAndOr;
	}

	public String getSelectedCritere() {
		return selectedCritere;
	}

	public void setSelectedCritere(String selectedCritere) {
		this.selectedCritere = selectedCritere;
	}

	public String getSelectedComparaison() {
		return selectedComparaison;
	}

	public void setSelectedComparaison(String selectedComparaison) {
		this.selectedComparaison = selectedComparaison;
	}

	public String getSelectedBooleen() {
		return selectedBooleen;
	}

	public void setSelectedBooleen(String selectedBooleen) {
		this.selectedBooleen = selectedBooleen;
	}

	public boolean isValeur1Visible() {
		return valeur1Visible;
	}

	public void setValeur1Visible(boolean valeur1Visible) {
		this.valeur1Visible = valeur1Visible;
	}

	public boolean isValeur2Visible() {
		return valeur2Visible;
	}

	public void setValeur2Visible(boolean valeur2Visible) {
		this.valeur2Visible = valeur2Visible;
	}

	public boolean isWarningVisible() {
		return warningVisible;
	}

	public void setWarningVisible(boolean warningVisible) {
		this.warningVisible = warningVisible;
	}

	public boolean isBooleenVisible() {
		return booleenVisible;
	}

	public void setBooleenVisible(boolean booleenVisible) {
		this.booleenVisible = booleenVisible;
	}

	public boolean isAndOrVisible() {
		return andOrVisible;
	}

	public void setAndOrVisible(boolean andOrVisible) {
		this.andOrVisible = andOrVisible;
	}

	public boolean isValeur1Enabled() {
		return valeur1Enabled;
	}

	public void setValeur1Enabled(boolean valeur1Enabled) {
		this.valeur1Enabled = valeur1Enabled;
	}

	public boolean isBooleenEnabled() {
		return booleenEnabled;
	}

	public void setBooleenEnabled(boolean booleenEnabled) {
		this.booleenEnabled = booleenEnabled;
	}

	public boolean isComparaisonEnabled() {
		return comparaisonEnabled;
	}

	public void setComparaisonEnabled(boolean comparaisonEnabled) {
		this.comparaisonEnabled = comparaisonEnabled;
	}

}
