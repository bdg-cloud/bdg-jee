package fr.legrain.recherche.multicritere.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaProforma;


public class LigneDocument extends LigneCritere  implements Serializable{

	private static final long serialVersionUID = 6404931111171184315L;
	
	private String typedoc ;
	private String prefixe;
	private Object document;
	private Map<String,String> listeCorrespondanceDocs=null;
	private Map<String,String> listeTitreChampsDocs=null;
	private Map<String,Object> listeObjetDocs=null;
	
	private Map<String,String> listeTitreChampsComparateurs=null;
	private Date valeur1;
	private Date valeur2;
	/**
	 * @param parent
	 * @param toolkit
	 * @param masterController 
	 */
	public LigneDocument(GroupeLigne g, String typedoc) {
		super(g);
		type = typedoc;
		this.typedoc = typedoc;
		initChamps();
		initListenersDoc();
	}	
	
	private void initChamps() {
		// *****************************************
		// ** Initialisation du champ de critères **
		// *****************************************
		listeCorrespondanceDocs=new LinkedHashMap<String, String>();
		listeTitreChampsDocs=new LinkedHashMap<String, String>();
		listeObjetDocs=new LinkedHashMap<String,Object>();

		listeTitreChampsDocs.put("Code Document","codeDocument");
		listeTitreChampsDocs.put("Date Document","dateDocument");
		listeTitreChampsDocs.put("Date Echéance","dateEchDocument");
		listeTitreChampsDocs.put("Date Livraison","dateLivDocument");		
//		listeTitreChampsDocs.put("Règlement","regleDocument");
//		if (typedoc.equals("Facture") || typedoc.equals("Acompte")){
//			listeTitreChampsDocs.put("Reste à Régler","resteARegler");
//		}
		listeTitreChampsDocs.put("Net HT","netHtCalc");
		listeTitreChampsDocs.put("Net Tva","netTvaCalc");
		listeTitreChampsDocs.put("Net à Payer","netAPayer");
		listeTitreChampsDocs.put("Chiffre d'Affaires","chiffreAffaire");

		if (typedoc.equals(TYPE_LIGNE_CRITERE_FACTURE)){
			document =new TaFacture();
			prefixe = "fa";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_DEVIS)){
			document =new TaDevis();
			prefixe = "de";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_COMMANDE)) {
			document =new TaBoncde();
			prefixe = "co";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_AVOIR)) {
			document =new TaAvoir();
			prefixe = "av";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_APPORTEUR)) {
			document =new TaApporteur();
			prefixe = "ap";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_ACOMPTE)) {
			document =new TaAcompte();
			prefixe = "ac";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_PROFORMA)) {
			document =new TaProforma();
			prefixe = "pr";
		} else if (typedoc.equals(TYPE_LIGNE_CRITERE_LIVRAISON)) {
			document =new TaBonliv();
			prefixe = "li";
		}
			
		
		listeCorrespondanceDocs.put("codeDocument",prefixe+".codeDocument");
		listeCorrespondanceDocs.put("dateDocument",prefixe+".dateDocument");
		listeCorrespondanceDocs.put("dateEchDocument",prefixe+".dateEchDocument");
		listeCorrespondanceDocs.put("dateLivDocument",prefixe+".dateLivDocument");		
//		listeCorrespondanceDocs.put("regleDocument",prefixe+".regleDocument");
//		listeCorrespondanceDocs.put("resteARegler",prefixe+".resteARegler");
		listeCorrespondanceDocs.put("netHtCalc",prefixe+".netHtCalc");
		listeCorrespondanceDocs.put("netTvaCalc",prefixe+".netTvaCalc");
		listeCorrespondanceDocs.put("netAPayer",prefixe+".netAPayer");
		listeCorrespondanceDocs.put("chiffreAffaire","sum("+prefixe+".netHtCalc)");
		
		listeObjetDocs.put("codeDocument",document);
		listeObjetDocs.put("dateDocument",document);
		listeObjetDocs.put("dateEchDocument",document);
		listeObjetDocs.put("dateLivDocument",document);
//		listeObjetDocs.put("resteARegler",document);
		listeObjetDocs.put("netHtCalc",document);
		listeObjetDocs.put("netTvaCalc",document);
		listeObjetDocs.put("netAPayer",document);
//		listeObjetDocs.put("regleDocument",document);
		listeObjetDocs.put("chiffreAffaire",document);
		
		// -- Insertion des critères dans la combo de comparateurs --
		getCritere().add("<Choisir>");
		for (String champs : listeTitreChampsDocs.keySet()) {
			getCritere().add(champs);
		}
		
		// *********************************************
		// ** Initialisation du champ de comparateurs **
		// *********************************************
		listeTitreChampsComparateurs=new LinkedHashMap<String, String>();
		
		listeTitreChampsComparateurs.put("est égal à","=");
		listeTitreChampsComparateurs.put("n'est pas égal à","<>");
		listeTitreChampsComparateurs.put("est supérieur à",">");
		listeTitreChampsComparateurs.put("est inférieur à","<");
		listeTitreChampsComparateurs.put("est compris entre","BETWEEN");
		listeTitreChampsComparateurs.put("contient","LIKE");
		listeTitreChampsComparateurs.put("ne contient pas","NOT LIKE");
		listeTitreChampsComparateurs.put("commence par","LIKE");
		listeTitreChampsComparateurs.put("ne commence pas par","NOT LIKE");
		listeTitreChampsComparateurs.put("fini par","LIKE");
		listeTitreChampsComparateurs.put("ne fini pas par","NOT LIKE");
		
		// -- Insertion des critères dans la combo de comparateurs --
		getComparaison().add("<Choisir>");
		for (String champs : listeTitreChampsComparateurs.keySet()) {
			getComparaison().add(champs);
		}
	
	}
	
	public void initListenersDoc(){
		//***********
//		critere.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (critere.getText().equals("Date Document") || critere.getText().equals("Date Livraison")
//					||	critere.getText().equals("Date Echéance")){
//					comparaison.remove(6,11);
//					comparaison.select(0);
//				} else {
//					ajoutCritereNonDate();
//					comparaison.select(0);
//				}
//				comparaison.setEnabled(true);
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);
//			}
//		});
	}
	
	public void ajoutCritereNonDate() {
		getComparaison().clear();
		getComparaison().add("<Choisir>");
		for (String champs : listeTitreChampsComparateurs.keySet()) {
			getComparaison().add(champs);
		}
	}
	
	public String transformSQLDoc(String champ) {
		return listeCorrespondanceDocs.get(listeTitreChampsDocs.get(champ));
	}
	
	public String transformSQLcomparateur(String champ) {
		return listeTitreChampsComparateurs.get(champ);
	}
	

	@Override
	public Map<String, String> getListeCorrespondance() {
		return listeCorrespondanceDocs;
	}

	@Override
	public Map<String, String> getListeTitreChamps() {
		return listeTitreChampsDocs;
	}

	@Override
	public Map<String, Object> getListeObjet() {
		return listeObjetDocs;
	}

	@Override
	public Map<String, String> getListeTitreChampsComparateurs() {
		return listeTitreChampsComparateurs;
	}

}
