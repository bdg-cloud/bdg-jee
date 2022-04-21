package fr.legrain.exportation.model;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IRelationDoc;
import fr.legrain.document.model.LigneTva;
import fr.legrain.lib.data.LibDate;




public class ExportationEpicea implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -929940724507282759L;

	static Logger logger = Logger.getLogger(ExportationEpicea.class.getName());
	
	public static final String CLIENT_AGRIGEST="AGRIGEST"; 
	public static final String CLIENT_EPICEA="EPICEA";
	public static final String CLIENT_CGID="CEGID";
	public static final String CLIENT_SAGE="SAGE";
	public static final String CLIENT_ISAGRI1="ISAGRI1";
	public static final String CLIENT_ISAGRI2="ISAGRI2";
	public static final String CLIENT_ISAGRIFINAL="ISAGRIFINAL";
	
	private String fichierExportation ="D:/divers/Nouveau dossier/E2-IMPOR.TXT";
	private String fichierExportationServeur ="/tmp/E2-IMPOR.TXT";
	public String finDeLigne = "\r\n";
	public String ctpEscompteDebit = "665";
	public String ctpEscompteCredit = "765";
	public static final int RE_EXPORT = 1;
    private String messageRetour="";
    private Boolean retour=true;
    private String locationFichier="";
    
    private int reExportAcompte;
    private int reExportAvoir;
    private int reExportFacture;
    private int reExportReglement;
    private int reExportRemise;
    private String typeClientExport=CLIENT_EPICEA;
    
//    private FileWriter fw = null;
//    private BufferedWriter bw = null;
    public int numDepart=0;

    
    public List<IDocumentTiers> listeVerif= new LinkedList<IDocumentTiers>();
    public List<IDocumentTiers> listeDocumentsManquants= new LinkedList<IDocumentTiers>();
    
    public List<String> listComplete=new LinkedList<String>();
    public List<IDocumentTiers> listCompleteDoc=new LinkedList<IDocumentTiers>();
    public List<IRelationDoc> listCompleteRelation= new LinkedList<IRelationDoc>();
    private List<IDocumentTiers[]> listCompletePointage= new LinkedList<IDocumentTiers[]>();
    private List<LigneTva> listeLignesTva=new LinkedList<>();
    
    public int numeroDePiece = 0;
    
	public String TypeAchat="A";
	public String TypeVente="V";
	public String TypeReglement="T";
	public String TypeOd="O";
	public String typePointage="P";
	
	public Integer numLignePiece =0;
	public String typePiece = null;
	public String codePiece = null;
	public Date datePiece = new Date();
	public String numCptLigne = null;
	public String libelleLigne = null;
	public String libellePiece = null;
	public Double mtDebitLigne = 0d;
	public Double mtCreditLigne = 0d;
	public Double mtHT = 0d;
	public Double mtTTC = 0d;
	public Double qte1 = 0d;
	public Double qte2 = 0d;
	public String codeTva = null;
	public Double tauxTva = 0d;
	public Double mtDebitTva = 0d;
	public Double mtCreditTva = 0d;
	public Double mtTva = 0d;
	public Date dateEcheance = new Date();
	public String cptCollectif = null;
	public String nomTiers = null;
	public String adresse1Tiers = null;
	public String adresse2Tiers = null;
	public String codePostalTiers = null;
	public String villeTiers = null;
	public String exigibiliteTva = null;
	public Date dateLivraisonLigne = new Date();

	public String refContrepartie = null;		
	public Date dateContrepartie  = null;
	public Double mtContrepartieDebit = 0d;
	public Double mtContrepartieCredit= 0d;
	public Double mtAffectation= 0d;
	public String codeTvaComplet = null;
	public String refReglement = null;
	public String typeReglement = null;
	
	
	
	
	//CEGID
	public String journal = null;
	public Integer cptGeneral = null;
	public String cptAuxiliaire  = null;
	public String sens  = null;
	public Double montant = 0d;
	public String etablissement  = null;
	public String qualifPiece  = null;
	public String affaire  = null;
	public Date dateExterne = new Date();
	public String refExterne  = null;
	
	public String numLigne = null;
	
	
    
	public ExportationEpicea() {}



	public ExportationEpicea(String typeClientExport) {
		this.typeClientExport = typeClientExport;
	}



	public List<IDocumentTiers> verifCoherenceExportation(){
		List<IDocumentTiers> listeRetour = new LinkedList<IDocumentTiers>();
		for (IDocumentTiers[] tab : listCompletePointage) {
			IDocumentTiers docDebit =tab[0];
			IDocumentTiers docCredit=tab[1];
			int rgDebit=listeVerif.indexOf(docDebit);
			int rgCredit=listeVerif.indexOf(docCredit);
			IDocumentTiers docDebitVerif ;
			IDocumentTiers docCreditVerif;

			if(rgDebit!=-1){
				docDebitVerif =listeVerif.get(rgDebit);
				if (docDebitVerif!=null && (docDebitVerif.getDateExport()==null )){					
					if(listeRetour.indexOf(docDebitVerif)==-1)
						listeRetour.add(docDebitVerif);
				}
			}
			
			if(rgCredit!=-1){
				docCreditVerif =listeVerif.get(rgCredit);
				if(docCreditVerif.getDateExport()==null ){
					if(listeRetour.indexOf(docCreditVerif)== -1)
						listeRetour.add(docCreditVerif);
				}
			}
		}
		return listeRetour;
	}
	
//	public int rechercheDocDansListe(List<IDocumentTiers> listeAVerifier, IDocumentTiers occurence){
//		int n=-1;
//		for (IDocumentTiers l : listeAVerifier) {
//			n++;
//			if(l.getCodeDocument().equals(occurence.getCodeDocument()))
//				return n;
//		}
//		return -1;
//	}
	public void effaceFichierTexte(String chemin) {
		try{
		File f = new File(chemin);
		f.delete();
		} catch (Exception e1) {
			logger.error("", e1);
		}
	}
	




	public String creationLigne(Integer numPiece,
			Integer numLignePiece ,
			String typePiece ,
			String codePiece ,
			Date datePiece ,
			String numCptLigne ,
			String libelleLigne ,
			Double mtDebitLigne ,
			Double mtCreditLigne ,
			Double qte1 ,
			Double qte2 ,
			String codeTva ,
			Double tauxTva ,
			Double mtDebitTva ,
			Double mtCreditTva ,
			Date dateEcheance ,
			String cptCollectif ,
			String nomTiers ,
			String adresse1Tiers ,
			String adresse2Tiers ,
			String codePostalTiers ,
			String villeTiers ,
			String exigibiliteTva ,
			Date dateLivraisonLigne,
			String refContrepartie,			
			Date dateContrepartie ,
			Double mtContrepartieDebit ,
			Double mtContrepartieCredit,
			Double mtAffectation,
			String codeTvaComplet,
			String refReglement) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		//1
		ligne.append(completeChaine(String.valueOf(numPiece),5,c,0));
		//2
		ligne.append(completeChaine(String.valueOf(numLignePiece),3,c,0));
		//3
		ligne.append(completeChaine(String.valueOf(typePiece),1,c,0));
		//4
		ligne.append(completeChaine(String.valueOf(codePiece),9,c,1));
		//5
		ligne.append(completeChaine(LibDate.dateToStringAA(datePiece),8,c,0));
		//6
		ligne.append(completeChaine(String.valueOf(numCptLigne),8,c,0));
		//7
		ligne.append(completeChaine(String.valueOf(libelleLigne),23,c,1));
		//8
		ligne.append(completeChaine(String.valueOf(mtDebitLigne),12,c,0));
		//9
		ligne.append(completeChaine(String.valueOf(mtCreditLigne),12,c,0));
		//10
		ligne.append(completeChaine(String.valueOf(qte1),9,c,0));
		//11
		ligne.append(completeChaine(String.valueOf(qte2),9,c,0));
		//12
		ligne.append(completeChaine(String.valueOf(codeTva),1,c,0));
		//13
		ligne.append(completeChaine(String.valueOf(tauxTva),6,c,0));
		//14
		ligne.append(completeChaine(String.valueOf(mtDebitTva),11,c,0));
		//15
		ligne.append(completeChaine(String.valueOf(mtCreditTva),11,c,0));
		//16
		ligne.append(completeChaine(LibDate.dateToStringAA(dateEcheance),8,c,0));
		//17
		ligne.append(completeChaine(String.valueOf(cptCollectif),8,c,0));
		//18
		ligne.append(completeChaine(String.valueOf(nomTiers),30,c,1));
		//19
		ligne.append(completeChaine(String.valueOf(adresse1Tiers),30,c,1));
		//20
		ligne.append(completeChaine(String.valueOf(adresse2Tiers),30,c,1));
		//21
		ligne.append(completeChaine(String.valueOf(codePostalTiers),5,c,1));
		//22
		ligne.append(completeChaine(String.valueOf(villeTiers),25,c,1));
		//23
		ligne.append(completeChaine(String.valueOf(exigibiliteTva),1,c,0));
		//24
		ligne.append(completeChaine(LibDate.dateToStringAA(dateLivraisonLigne),8,c,0));
		//25
		ligne.append(completeChaine(String.valueOf(refContrepartie),9,c,1));
		//26
		ligne.append(completeChaine(LibDate.dateToStringAA(dateContrepartie),8,c,0));
		//27
		ligne.append(completeChaine(String.valueOf(mtContrepartieDebit),11,c,0));
		//28
		ligne.append(completeChaine(String.valueOf(mtContrepartieCredit),11,c,0));
		//29
		ligne.append(completeChaine(String.valueOf(mtAffectation), 11, c, 0));
		//30
		ligne.append(completeChaine(String.valueOf(codeTvaComplet), 7, c, 0));
		//31
		ligne.append(completeChaine(String.valueOf(refReglement), 9, c, 0));

		return ligne.toString();
	}

	
	public String creationLigneCEGID(
			Date datePiece ,
			String journal ,
			Integer cptGeneral ,
			String cptAuxiliaire ,
			String sens ,
			Double montant ,
			String libelleLigne ,
			String codePiece ,
			String etablissement ,
			String qualifPiece ,
			String affaire ,
			Double qte1 ,
			Date dateEcheance ,
			Date dateExterne ,
			String refExterne ) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		SimpleDateFormat datePattern =new SimpleDateFormat("ddMMyyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
//		symbols.setGroupingSeparator('');
		NumberFormat f = new DecimalFormat("###0.00#", symbols);
		//1
		if(datePiece!=null)
//		ligne.append(completeChaine(datePattern.format(datePiece),8,c,0));
		ligne.append(datePattern.format(datePiece));
		else ligne.append("");
		ligne.append(";");
		//2
//		ligne.append(completeChaine(String.valueOf(journal).replace(";", ","),3,c,0));
		ligne.append(String.valueOf(journal).replace(";", ","));
		ligne.append(";");
		//3
//		ligne.append(completeChaine(String.valueOf(cptGeneral),17,c,1));
		ligne.append(String.valueOf(cptGeneral));
		ligne.append(";");
		//4
//		ligne.append(completeChaine(String.valueOf(cptAuxiliaire).replace(";", ","),17,c,1));
		ligne.append(String.valueOf(cptAuxiliaire).replace(";", ","));
		ligne.append(";");
		//5
//		ligne.append(completeChaine(String.valueOf(sens),1,c,0));
		ligne.append(String.valueOf(sens));
		ligne.append(";");
		//6
//		ligne.append(completeChaine(String.valueOf(f.format(montant)),20,c,0));
		ligne.append(String.valueOf(f.format(montant)));
		ligne.append(";");
		//7
//		ligne.append(completeChaine(String.valueOf(libelleLigne).replace(";", ","),35,c,1));
		ligne.append(String.valueOf(libelleLigne).replace(";", ","));
		ligne.append(";");
		//8
//		ligne.append(completeChaine(String.valueOf(codePiece).replace(";", ","),35,c,1));
		ligne.append(String.valueOf(codePiece).replace(";", ","));
		ligne.append(";");
		//9
//		ligne.append(completeChaine(String.valueOf(etablissement).replace(";", ","),3,c,0));
		ligne.append(String.valueOf(etablissement).replace(";", ","));
		ligne.append(";");
		//10
//		ligne.append(completeChaine(String.valueOf(qualifPiece).replace(";", ","),1,c,0));
		ligne.append(String.valueOf(qualifPiece).replace(";", ","));
		ligne.append(";");
		//11
//		ligne.append(completeChaine(String.valueOf(affaire).replace(";", ","),17,c,0));
		ligne.append(String.valueOf(affaire).replace(";", ","));
		ligne.append(";");
		//12
//		ligne.append(completeChaine(String.valueOf(f.format(qte1)),20,c,0));
		ligne.append(String.valueOf(f.format(qte1)));
		ligne.append(";");
		//13
		if(dateEcheance!=null) {
//			ligne.append(completeChaine(datePattern.format(dateEcheance),8,c,0));
			ligne.append(datePattern.format(dateEcheance));
		}
		else  ligne.append("");
		ligne.append(";");
		//14
		if(dateExterne!=null) {
//		ligne.append(completeChaine(datePattern.format(dateExterne),8,c,0));
		ligne.append(datePattern.format(dateExterne));
		}
		else ligne.append("");
		ligne.append(";");
		//15
//		ligne.append(completeChaine(String.valueOf(refExterne).replace(";", ","),35,c,0));
		ligne.append(String.valueOf(refExterne).replace(";", ","));
		

		return ligne.toString();
	}

	public String creationLigneSAGE(			
			String journal ,
			Date datePiece ,
			Integer cptGeneral ,
			String codePiece ,
			String cptAuxiliaire ,
			String libelleLigne ,
			Double debit ,
			Double credit ,
			Date dateEcheance,
			String typeReglement ) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		SimpleDateFormat datePattern =new SimpleDateFormat("ddMMyyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		NumberFormat f = new DecimalFormat("###0.00#", symbols);

		//1
		ligne.append(String.valueOf(journal).replace(";", ","));
		ligne.append(";");		
		//2
		if(datePiece!=null)
		ligne.append(datePattern.format(datePiece));
		else ligne.append("");
		ligne.append(";");
		//3
		ligne.append(String.valueOf(cptGeneral));
		ligne.append(";");
		//4
		ligne.append(String.valueOf(codePiece).replace(";", ","));
		ligne.append(";");		
		//5
		ligne.append(String.valueOf(cptAuxiliaire).replace(";", ","));
		ligne.append(";");
		//6
		ligne.append(String.valueOf(libelleLigne).replace(";", ","));
		ligne.append(";");
		
		//7
		ligne.append(String.valueOf(f.format(debit)));
		ligne.append(";");
		//8
		ligne.append(String.valueOf(f.format(credit)));
		ligne.append(";");
		//9
		if(dateEcheance!=null) {
			ligne.append(datePattern.format(dateEcheance));
		}
		else  ligne.append("");
		ligne.append(";");
		//10
		ligne.append(String.valueOf(typeReglement).replace(";", ","));
	

		return ligne.toString();
	}

	public String creationLigneISAGRI1(			
			int numLignePiece,
			String journal ,
			Date datePiece,
			String codePiece ,
			String cptAuxiliaire ,
			String nomTiers ,
			String libelleLigne ,
			Double qte1 ,
			Double mtHT ,
			Double mtTva ,
			Double mtTTC ,
			String numCptLigne,
			String codeTva)
	{
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		SimpleDateFormat datePattern =new SimpleDateFormat("ddMMyyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		NumberFormat f = new DecimalFormat("###0.00#", symbols);
		
		
		ligne.append(String.valueOf(numLignePiece));
		ligne.append(";");	
		
		ligne.append(String.valueOf(journal).replace(";", ","));
		ligne.append(";");		
		
		if(datePiece!=null)
		ligne.append(datePattern.format(datePiece));
		else ligne.append("");
		ligne.append(";");		
		
		ligne.append(String.valueOf(codePiece).replace(";", ","));
		ligne.append(";");		

		ligne.append(String.valueOf(cptAuxiliaire).replace(";", ","));
		ligne.append(";");
		
		ligne.append(String.valueOf(nomTiers).replace(";", ","));
		ligne.append(";");
	
		ligne.append(String.valueOf(libelleLigne).replace(";", ","));
		ligne.append(";");		
		
		ligne.append(String.valueOf(f.format(qte1)));
		ligne.append(";");
		
		ligne.append(String.valueOf(f.format(mtHT)));
		ligne.append(";");
		
		ligne.append(String.valueOf(f.format(mtTva)));
		ligne.append(";");
		
		ligne.append(String.valueOf(f.format(mtTTC)));
		ligne.append(";");
		
		ligne.append(String.valueOf(numCptLigne).replace(";", ","));
		ligne.append(";");	
		
		ligne.append(String.valueOf(codeTva).replace(";", ","));
		ligne.append(";");	
		

		return ligne.toString();
	}
	
	public String creationLigneISAGRI2(			
			String journal ,
			Date datePiece ,
			String cptGeneral ,
			String libelleCpt,
			String codePiece ,
			String libellePiece ,
			Double Qté,
			Double debit ,
			Double credit ,
			String lettrage,
			String codeTva ) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		SimpleDateFormat datePattern =new SimpleDateFormat("ddMMyyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		NumberFormat f = new DecimalFormat("###0.00#", symbols);

		//1
		ligne.append(String.valueOf(journal).replace(";", ","));
		ligne.append(";");		
		//2
		if(datePiece!=null)
		ligne.append(datePattern.format(datePiece));
		else ligne.append("");
		ligne.append(";");
		//3
		ligne.append(String.valueOf(cptGeneral).replace(";", ","));
		ligne.append(";");
		//4
		ligne.append(String.valueOf(libelleCpt).replace(";", ","));
		ligne.append(";");	
		//4
		ligne.append(String.valueOf(codePiece).replace(";", ","));
		ligne.append(";");		
		//5
		ligne.append(String.valueOf(libellePiece).replace(";", ","));
		ligne.append(";");
		//6
		ligne.append(String.valueOf(Qté).replace(";", ","));
		ligne.append(";");
		
		//7
		ligne.append(String.valueOf(f.format(debit)));
		ligne.append(";");
		//8
		ligne.append(String.valueOf(f.format(credit)));
		ligne.append(";");
		//9
		ligne.append(String.valueOf(lettrage).replace(";", ","));
		ligne.append(";");
		//10
		ligne.append(String.valueOf(codeTva).replace(";", ","));
	

		return ligne.toString();
	}
	
	private String creationLignePointage(Integer numReglement,
			Integer numLignePiece ,
			String typePiece ,
			String refReglement,
			Date dateReglement ,
			String tiersReglement ,
			Double mtAffectationReglementDebit ,
			Double mtAffectationReglementCredit ,
			Integer numContrepartie,
			String refContrepartie,			
			Date dateContrepartie ,
			String tiersContrepartie ,
			Double mtAffectationContrepartieDebit ,
			Double mtAffectationContrepartieCredit) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DU POINTAGE DANS LE FICHIER D'EXPORTATION
		//Entete
		//1
		ligne.append(completeChaine(String.valueOf(numReglement),5,c,0));
		//2
		ligne.append(completeChaine(String.valueOf(numLignePiece),3,c,0));
		//3
		ligne.append(completeChaine(String.valueOf(typePiece),1,c,0));
		//4
		ligne.append(completeChaine(String.valueOf(refReglement),9,c,1));
		//5
		ligne.append(completeChaine(LibDate.dateToStringAA(dateReglement),8,c,0));
		//6
		ligne.append(completeChaine(String.valueOf(tiersReglement),8,c,0));
		//7
		ligne.append(completeChaine(String.valueOf(mtAffectationReglementDebit),12,c,0));
		//8
		ligne.append(completeChaine(String.valueOf(mtAffectationReglementDebit),12,c,0));
		//9
		ligne.append(completeChaine(String.valueOf(numContrepartie),5,c,0));
		//10
		ligne.append(completeChaine(String.valueOf(refContrepartie),9,c,1));
		//11
		ligne.append(completeChaine(LibDate.dateToStringAA(dateContrepartie),8,c,0));
		//12
		ligne.append(completeChaine(String.valueOf(tiersContrepartie),8,c,0));
		//13
		ligne.append(completeChaine(String.valueOf(mtAffectationContrepartieDebit),12,c,0));
		//14
		ligne.append(completeChaine(String.valueOf(mtAffectationContrepartieCredit),12,c,0));

		return ligne.toString();
	}


	/**
	 * Si la <code>chaine</code> de caractère est plus courte que <code>longueur</code>, ajoute <code>c</code>
	 *  autant de fois que necessaire pour atteindre la longueur demandée. Si la chaine est plus longue, elle est coupée.
	 * @param chaine - chaine initiale
	 * @param longueur - longeur souhaitée
	 * @param c - caractère à répéter pour compléter la chaine
	 * @param position - 0 la chaine est complétée par l'avant, 1 par l'arrière
	 * @return 
	 */
	public String completeChaine(String chaine, int longueur, char c, int position) {
		if(chaine==null) chaine="";
		if(chaine.length()<longueur) { //plus court
			int nbCaractere = longueur - chaine.length();
			StringBuffer complement = new StringBuffer("");
			for(int i=0; i<nbCaractere; i++) {
				complement.append(c);
			}
			if(position==0) {
				chaine=complement.toString()+chaine;
			} else if(position==1) {
				chaine=chaine+complement.toString();
			}
		} else { //plus long
			chaine = chaine.substring(0,longueur);
		}
		return chaine;
	}


	public String getMessageRetour() {
		return messageRetour;
	}

	public void setMessageRetour(String messageRetour) {
		this.messageRetour = messageRetour;
	}

	public String getLocationFichier() {
		return locationFichier;
	}

	public void setLocationFichier(String locationFichier) {
		this.locationFichier = locationFichier;
	}

	public Boolean getRetour() {
		return retour;
	}

	public void setRetour(Boolean retour) {
		this.retour = retour;
	}
	
	

	public int getReExportAcompte() {
		return reExportAcompte;
	}

	public void setReExportAcompte(int reExportAcompte) {
		this.reExportAcompte = reExportAcompte;
	}

	public int getReExportAvoir() {
		return reExportAvoir;
	}

	public void setReExportAvoir(int reExportAvoir) {
		this.reExportAvoir = reExportAvoir;
	}

	public int getReExportFacture() {
		return reExportFacture;
	}

	public void setReExportFacture(int reExportFacture) {
		this.reExportFacture = reExportFacture;
	}

	public int getReExportReglement() {
		return reExportReglement;
	}

	public void setReExportReglement(int reExportReglement) {
		this.reExportReglement = reExportReglement;
	}

	public int getReExportRemise() {
		return reExportRemise;
	}

	public void setReExportRemise(int reExportRemise) {
		this.reExportRemise = reExportRemise;
	}

	


	public String correspondanceCodeTvaHUE(String codeTtvaTiers, String codeInitial,String journalTvaDoc,String compte,BigDecimal montant){
		//voir dans Epicea TDMPlan.TvaCodePourCompte
		if(codeInitial==null)codeInitial="";
		if(codeTtvaTiers!=null && codeTtvaTiers.equals("N")){//si franchise de tva
			//si code vide, on renvoie vide
			if(codeInitial!=null && codeInitial.equals(""))return "";
			else if(codeInitial!=null && !codeInitial.equals("")){//si code rempli (ne doit pas arriver)

			}
		}
		else {
			if((codeTtvaTiers.equals("HUE")||codeTtvaTiers.equals("UE"))
					&& (codeInitial!=null && codeInitial.equals(""))){
				if(compte.startsWith("6")){
					if (!(compte.startsWith("609")||compte.startsWith("665"))){
						return "";
					}
					return journalTvaDoc;
				}
				return journalTvaDoc;
			} else if(codeInitial!=null && !codeInitial.equals("")){//si codeTtvaTiers E ou L puisque N pris avant et F dans autre procedure
				if(codeInitial.startsWith("A")) //si codeInitial A on transforme en B = équivalence Epicéa des code sur Achat
				  return "B";
			} else
				return codeInitial;	//sinon on laisse le code initiale puisque qu'il a était mis volontairement			
		}
		return codeInitial; // si rentre dans aucuns cas
	}
	
	public String correspondanceCodeTva(String codeTtvaTiers, String codeInitial,String codeRemplacement,String compte,BigDecimal montant){
		//voir dans Epicea TDMPlan.TvaCodePourCompte
		if(codeRemplacement!=null){
		if(codeTtvaTiers.equalsIgnoreCase("F")){
			if(codeRemplacement.equals("E")){
				if (compte.startsWith("6")) {
					if ((compte.startsWith("609")||compte.startsWith("665"))){
						return "V";
					}else
						return "B";
				}			
			}
			if(codeRemplacement.startsWith("A")){
				return "B";
			}
		}else{
			return correspondanceCodeTvaHUE(codeTtvaTiers,codeInitial,codeRemplacement,compte,montant);
		}
		}
		return codeRemplacement;
	}

	public String getFichierExportation() {
		return fichierExportation;
	}

	public void setFichierExportation(String fichierExportation) {
		this.fichierExportation = fichierExportation;
	}

//	public FileWriter getFw() {
//		return fw;
//	}
//
//	public void setFw(FileWriter fw) {
//		this.fw = fw;
//	}
//
//	public BufferedWriter getBw() {
//		return bw;
//	}
//
//	public void setBw(BufferedWriter bw) {
//		this.bw = bw;
//	}

	public int getNumDepart() {
		return numDepart;
	}

	public void setNumDepart(int numDepart) {
		this.numDepart = numDepart;
	}

	public List<IDocumentTiers> getListeVerif() {
		return listeVerif;
	}

	public void setListeVerif(List<IDocumentTiers> listeVerif) {
		this.listeVerif = listeVerif;
	}

	public List<String> getListComplete() {
		return listComplete;
	}

	public void setListComplete(List<String> listComplete) {
		this.listComplete = listComplete;
	}

	public List<IDocumentTiers[]> getListCompletePointage() {
		return listCompletePointage;
	}

	public void setListCompletePointage(List<IDocumentTiers[]> listCompletePointage) {
		this.listCompletePointage = listCompletePointage;
	}

	public String getFinDeLigne() {
		return finDeLigne;
	}

	public void setFinDeLigne(String finDeLigne) {
		this.finDeLigne = finDeLigne;
	}

	public String getCtpEscompteDebit() {
		return ctpEscompteDebit;
	}

	public void setCtpEscompteDebit(String ctpEscompteDebit) {
		this.ctpEscompteDebit = ctpEscompteDebit;
	}

	public String getCtpEscompteCredit() {
		return ctpEscompteCredit;
	}

	public void setCtpEscompteCredit(String ctpEscompteCredit) {
		this.ctpEscompteCredit = ctpEscompteCredit;
	}

	public List<IDocumentTiers> getListeDocumentsManquants() {
		return listeDocumentsManquants;
	}

	public void setListeDocumentsManquants(List<IDocumentTiers> listeDocumentsManquants) {
		this.listeDocumentsManquants = listeDocumentsManquants;
	}

	public String getFichierExportationServeur() {
		return fichierExportationServeur;
	}

	public void setFichierExportationServeur(String fichierExportationServeur) {
		this.fichierExportationServeur = fichierExportationServeur;
	}

	public List<IDocumentTiers> getListCompleteDoc() {
		return listCompleteDoc;
	}

	public void setListCompleteDoc(List<IDocumentTiers> listCompleteDoc) {
		this.listCompleteDoc = listCompleteDoc;
	}

	public String getTypeClientExport() {
		return typeClientExport;
	}

	public void setTypeClientExport(String typeClientExport) {
		this.typeClientExport = typeClientExport;
	}

	public void estClientEpicea() {
		setTypeClientExport(CLIENT_EPICEA);
	}
	
	public void estClientAgrigest() {
		setTypeClientExport(CLIENT_AGRIGEST);
	}



	public List<LigneTva> getListeLignesTva() {
		return listeLignesTva;
	}



	public void setListeLignesTva(List<LigneTva> listeLignesTva) {
		this.listeLignesTva = listeLignesTva;
	}


	public String creationLigneISAGRIFinal(			
			String journal ,
			Date datePiece ,
			String cptGeneral ,
			String libelleCpt,
			String codePiece ,
			String libellePiece ,			
			Double debit ,
			Double credit ,
			String lettrage,
			String codeTva,
			Double Qté) {
		StringBuffer ligne = new StringBuffer("");
		char c = ' ';
		//ECRITURE DE LA FACTURE DANS LE FICHIER D'EXPORTATION
		//Entete
		SimpleDateFormat datePattern =new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		NumberFormat f = new DecimalFormat("###0.00#", symbols);

		//1
		ligne.append(String.valueOf(journal).replace(";", ","));
		ligne.append(";");		
		//2
		if(datePiece!=null)
		ligne.append(datePattern.format(datePiece));
		else ligne.append("");
		ligne.append(";");
		//3
		ligne.append(String.valueOf(cptGeneral).replace(";", ","));
		ligne.append(";");
		//4
		ligne.append(String.valueOf(libelleCpt).replace(";", ","));
		ligne.append(";");	
		//4
		ligne.append(String.valueOf(codePiece).replace(";", ","));
		ligne.append(";");		
		//5
		ligne.append(String.valueOf(libellePiece).replace(";", ","));
		ligne.append(";");
		//6
		ligne.append(String.valueOf(f.format(debit)));
		ligne.append(";");
		//7
		ligne.append(String.valueOf(f.format(credit)));
		ligne.append(";");
		//8
		ligne.append(String.valueOf(lettrage).replace(";", ","));
		ligne.append(";");
		//9
		ligne.append(String.valueOf(codeTva).replace(";", ","));
		ligne.append(";");
		//10
		ligne.append(String.valueOf(Qté).replace(";", ","));


		return ligne.toString();
	}


}

