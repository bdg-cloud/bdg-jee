package fr.legrain.import_agrigest.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fr.legrain.import_agrigest.dao.ActiviteDao;
import fr.legrain.import_agrigest.dao.CompteDao;
import fr.legrain.import_agrigest.dao.DossierDao;
import fr.legrain.import_agrigest.dao.LignesDao;
import fr.legrain.import_agrigest.dao.MouvementDao;
import fr.legrain.import_agrigest.dao.PieceDao;
import fr.legrain.import_agrigest.dao.PlanComptableDao;
import fr.legrain.import_agrigest.dao.TvaDao;
import fr.legrain.import_agrigest.model.LigneDocFichier;
import fr.legrain.import_agrigest.model.Lignes;
import fr.legrain.import_agrigest.model.MontantTva;
import fr.legrain.import_agrigest.model.ParametreImport;
import fr.legrain.import_agrigest.model.Pieces;
import fr.legrain.import_agrigest.model.Pointage;
import fr.legrain.import_agrigest.model.Tva;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ImportationController {
	ActiviteDao activiteDao = new ActiviteDao();
	CompteDao compteDao = new CompteDao();
	MouvementDao mouvDao = new MouvementDao();
	DossierDao dossierDao = new DossierDao();
	LignesDao ligneDao = new LignesDao();
	PieceDao pieceDao = new PieceDao();
	PlanComptableDao plDao = new PlanComptableDao();
	TvaDao tvaDao = new TvaDao();


    public Integer BlocnPieceDebutAFacture; //Defaut : 80 000
    public Integer BlocnPieceFinAFacture; //Defaut : 100 000
    public Integer BlocnPieceDebutAReglement; //Defaut : 150 000
    public Integer BlocnPieceFinAReglement; //Defaut : 200 000
    public Integer BlocnPieceDebutVFacture; //Defaut : 50 000
    public Integer BlocnPieceFinVFacture; //Defaut : 80 000
    public Integer BlocnPieceDebutVReglement; //Defaut : 100 000
    public Integer BlocnPieceFinVReglement; //Defaut : 150 000
    public Boolean vFactureNumFactureAgrifact;        //'Gestion de la case à cocher "Conserver les 5 derniers chiffres du numéro de facture pour le numéro de pièce" dans les Paramètres compta
    public Boolean vFactureComposerNumPiece;        //'Gestion de la case à cocher "Composer le numéro de pièce (5 positions maximum) dans les Paramètres compta
    public String vFactureRacineNumPiece;
    
    public List<String> listeMessageError=new LinkedList<>();
    public List<String> listeMessageOk=new LinkedList<>();
    boolean tVASurEncaissement=false;
    
    private List<LigneDocFichier> listeFactureVente=new LinkedList<>();
    private List<LigneDocFichier> listeFactureAchat=new LinkedList<>();
    private List<LigneDocFichier> listeReglement=new LinkedList<>();
    private List<LigneDocFichier> listePointage=new LinkedList<>();

	public void importer(ParametreImport params) {
		try {
			File f = new File(params.getCheminFichierExport());
			if(!f.exists()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Fichier manquant");
				alert.setHeaderText(null);
//				alert.setContentText("L'importation des remises et règlements s'est bien déroulée !");
				alert.setContentText("Le fichier '"+params.getCheminFichierExport()+"'\n  n'existe pas !!!");
				alert.showAndWait();
			}
			Boolean retour=false;
			listeFactureAchat.clear();
			listeFactureVente.clear();
			listeReglement.clear();
			listePointage.clear();
//			Parametre params =SQLServerUtil.getInstance();
			litFichierImport(params.getCheminFichierExport());
			
			listeMessageError.clear();
			listeMessageOk.clear();
			List<Pieces> listePieces =recupFacture(listeFactureVente);
			retour=ExporterFactures(listePieces);
			
			if(retour) {
			listeMessageError.clear();
			listeMessageOk.clear();
			List<Pieces> listeReglements =recupReglement(listeReglement);
			List<Pointage> listePointages =recupPointage(listePointage);
			retour=ExporterRemises(listeReglements,listePointages);
			}
			if(retour) {
				//renommer le fichier
				f = new File(params.getCheminFichierExport());
				String chemin = f.getParentFile().getAbsolutePath();
				f.renameTo(new File(chemin+"/OLD_"+f.getName()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	

	public List<Pieces> recupFacture(List<LigneDocFichier> listeFacture) {
		int numPieceCourante=-1;
		int numLigne =1;
		List<Pieces> listePieces = new LinkedList<>();
		Pieces piece=null;
		for (LigneDocFichier l : listeFacture) {
			if(numPieceCourante!=l.getNumPiece() && l.getNumLigne().equals("1")) {				
				//si changement de piece
				piece=new Pieces();
				numLigne =1;
				listePieces.add(piece);
				piece.setIdDocument(l.getNumPiece());
				piece.setCodeDocument(l.getReference());
				piece.setDateDocument(l.getDatePiece());
				piece.setLibelleDocument(l.getLibelle());
				piece.setCompte(l.getCompteLigne());
				piece.setNomTiers(l.getNomTiers());
				
				piece.setNetHtCalc(l.getMontantDebit().subtract(l.getMontantCredit()));
				piece.setNetTvaCalc(l.getMontantDebitTVA().subtract(l.getMontantCreditTVA()));
				piece.setNetTtcCalc(piece.getNetHtCalc().add(piece.getNetTvaCalc()));
				numPieceCourante=l.getNumPiece();
//				piece.setNetHtCalc(l.getMontantHt());
//				piece.setNetTtcCalc(l.getMontantTtc());
//				piece.setNetTvaCalc(l.getMontantTva());
				
			}
			if(numLigne>1) {
			Lignes ligne=new Lignes();
			piece.getLignes().add(ligne);
			ligne.setCodeTvaLDocument(l.getCodeTVAAgrigest());
			ligne.setCodeTTvaLDocument(l.getExibiliteTVA());
			ligne.setPieces(piece);
			ligne.setCompteLDocument(l.getCompteLigne());
			ligne.setNumLigneLDocument(LibConversion.stringToInteger(l.getNumLigne()));
			ligne.setLibLDocument(l.getLibelle());
			ligne.setQteLDocument(l.getQte1());
			ligne.setQte2LDocument(l.getQte2());
			ligne.setTauxTvaLDocument(l.getTauxTVA());
			ligne.setMtHtLApresRemiseGlobaleDocument(l.getMontantCredit().subtract(l.getMontantDebit()));
			ligne.setMtTvaLApresRemiseGlobaleDocument(l.getMontantCreditTVA().subtract(l.getMontantDebitTVA()));
			ligne.setMtTtcLApresRemiseGlobaleDocument(ligne.getMtHtLApresRemiseGlobaleDocument().add(ligne.getMtTvaLApresRemiseGlobaleDocument()));
			}
			numLigne +=1;
		}
		return listePieces;
	}
	
	/**
	 * 
	 * @param listePointages
	 * @return la liste des pointages à partir du fichier listePointages
	 */
	public List<Pointage> recupPointage(List<LigneDocFichier> listePointages) {
		List<Pointage> listePointage = new LinkedList<>();
		
		for (LigneDocFichier l : listePointages) {
			Pointage p = new Pointage();
			p.setCodeDocument(l.getRefContrepartie());
			p.setTiers(l.getCompteLigne());
			p.setCodeReglement(l.getRefReglement());
			p.setNetTtcCalc(l.getMontantTtcContrepartie());
			p.setAffectation(l.getMontantPointage());
			listePointage.add(p);
		}		
		return listePointage;
	}
	
	
	public List<Pieces> recupReglement(List<LigneDocFichier> listeReglement) {
		int numPieceCourante=-1;
		int numLigne =1;
		List<Pieces> listePieces = new LinkedList<>();
		Pieces piece=null;
		for (LigneDocFichier l : listeReglement) {
			if(numPieceCourante!=l.getNumPiece() && l.getNumLigne().equals("1")) {				
				//si changement de piece
				piece=new Pieces();
				numLigne =1;
				listePieces.add(piece);
				piece.setIdDocument(l.getNumPiece());
				piece.setCodeDocument(l.getReference());
				piece.setDateDocument(l.getDatePiece());
				piece.setLibelleDocument(l.getLibelle());
				piece.setCompte(l.getCompteLigne());
				
				piece.setNetHtCalc(l.getMontantDebit().subtract(l.getMontantCredit()));
				piece.setNetTvaCalc(l.getMontantDebitTVA().subtract(l.getMontantCreditTVA()));
				piece.setNetTtcCalc(piece.getNetHtCalc().add(piece.getNetTvaCalc()));
				piece.setMontantReglement(piece.getNetTtcCalc());
				numPieceCourante=l.getNumPiece();
				
			}
			if(numLigne>1) {
			Lignes ligne=new Lignes();
			piece.getLignes().add(ligne);
			ligne.setCodeTvaLDocument(l.getCodeTVAAgrigest());
			ligne.setTiersLDocument(l.getNomTiers());
			ligne.setCodeTTvaLDocument(l.getExibiliteTVA());
			ligne.setPieces(piece);
			ligne.setCompteLDocument(l.getCompteLigne());
			ligne.setNumLigneLDocument(LibConversion.stringToInteger(l.getNumLigne()));
			ligne.setLibLDocument(l.getLibelle());
			ligne.setQteLDocument(l.getQte1());
			ligne.setQte2LDocument(l.getQte2());
			ligne.setTauxTvaLDocument(l.getTauxTVA());
			ligne.setMtHtLApresRemiseGlobaleDocument(l.getMontantCredit().subtract(l.getMontantDebit()));
			ligne.setMtTvaLApresRemiseGlobaleDocument(l.getMontantCreditTVA().subtract(l.getMontantDebitTVA()));
			ligne.setMtTtcLApresRemiseGlobaleDocument(ligne.getMtHtLApresRemiseGlobaleDocument().add(ligne.getMtTvaLApresRemiseGlobaleDocument()));
			ligne.setCodeReglement(l.getRefReglement());
			}
			numLigne +=1;
		}
		return listePieces;
	}



//	public Boolean ExporterFactures(List<LigneDocFichier> liste, Boolean tVASurEncaissement) {
//		int numPieceCourante = -1;
//		String keepDossier = "";
//		BigDecimal cumulDebit =BigDecimal.ZERO;
//		BigDecimal cumulCredit =BigDecimal.ZERO;
//		BigDecimal totalTTC =BigDecimal.ZERO;
//		String libelleContrepartie="";
//		String compteClient="";
//		String codeTVA  = "";
//		BigDecimal tauxTva =BigDecimal.ZERO;
//		Boolean tVASansCompte = false;
//		String journalTVA = "";
//		String montantTVA = "";
//		Integer ligne = 0;
//		int numPiece = 0;
//		Date datePiece = null;
//		String nomTiers = null;
//		String numFacture= null;
//		List<MontantTva> recapTxTVA = new LinkedList<MontantTva>();
//		try {
//			for (LigneDocFichier l : liste) {				
//				if(numPieceCourante!=l.getNumPiece()) {
//					
//					if(numPieceCourante!=-1) {
//						//rajouter les lignes de tva avant de passer à la nouvelle pièce
//						//' On génére une ligne par code TVA
//						for (MontantTva mtva : recapTxTVA) {				
//							//                    For Each txTVA As String In RecapTxTVA.GetLstTx()
//							if(mtva.getCodeTVA()!=null ) {
//								Tva tva= tvaDao.findByCode(mtva.getCodeTVA());
//								if( tva!=null) {
//									if( tva.gettCpt()!=null && !tva.gettCpt().isEmpty()) {
//										//'TV 22/12/2011
//										BigDecimal montantTTCTaux  =mtva.getMontantTTC();
//										BigDecimal montantTVATaux  =mtva.getMontantTTC().subtract(mtva.getMontantHT());
//										BigDecimal montantHTTaux  =mtva.getMontantHT();
//
//
//
//										ligneDao.ajout_Ligne(keepDossier, numPiece,datePiece, ligne, true, mtva.getCodeTVA(), journalTVA, tva.gettCtrlCl_DC(), nomTiers, bigDecimalToString(mtva.getTauxTva()));
//										mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, "48800000", "0000", montantTTCTaux, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//										mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 2, tva.gettCpt(), "0000", BigDecimal.ZERO, montantTVATaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "", "0");
//										mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, montantHTTaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//
//										//'Gestion des variables de contrôle d'ecart
//										cumulDebit = cumulDebit.add(montantTTCTaux);
//										cumulCredit = cumulCredit.add(montantHTTaux);
//
//
//										ligne += 1;
//									}
//								}
//							}
//						}	
//
////						//  On solde le compte 488 par le compte client
////						//Decimal.Round(CDec(rwFacture.Item("MontantTTC")), 2);
////						String numFacture  = l.getReference();
////						if(totalTTC.signum()>=0)numFacture="FA "+numFacture;else numFacture="AV "+numFacture;
//
//
//						ligneDao.ajout_Ligne(keepDossier, numPiece, datePiece, ligne, true, "", "O", "N", nomTiers, numFacture);
//						mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, compteClient, "0000", totalTTC, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//						mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, totalTTC, "C", BigDecimal.ZERO, BigDecimal.ZERO, compteClient, "0000");
//						ligne += 1;
//						//                'Gestion des variables de contrôle d'ecart
//						cumulCredit = cumulCredit.add(totalTTC);
//
//						//                'Gestion des écarts
//						BigDecimal ecart  = cumulDebit.subtract(cumulCredit) ;
//						recapTxTVA.clear();
//						if (ecart.signum()!= 0) {
//							//					Me.GererEcartExportFacture(ecart, CBool(rwFacture("FacturationTTC")), keepDossier, NumPiece, String.Format("{0:dd/MM/yy}", rwFacture.Item("DateFacture")))
//						}
//					}
//					Date dtExport = dateDuJour();
//					datePiece=l.getDatePiece();
//					nomTiers=l.getNomTiers();
//					numFacture  = l.getReference();
//					if(totalTTC.signum()>=0)numFacture="FA "+numFacture;else numFacture="AV "+numFacture;
//					//Chercher le numéro de dossier de l'exercice correspondant à la facture
//					keepDossier = dossierDao.trouverDossier(l.getDatePiece());
//					if(keepDossier!=null) {
//						//                'S'assurer de la présence du compte 488
//						compteDao.ajout_Compte(keepDossier, "48800000", "COMPTE DE REPARTITION","","",false);
//						activiteDao.ajout_Activite(keepDossier, "0000", "ACTIVITE GENERALE",false);
//						plDao.ajout_PlanComptable(keepDossier, "48800000", "0000",false);
//
//						////                'Déterminer le n° de la pièce à utiliser pour la facture
//
//						if(BlocnPieceDebutVFacture==null)  BlocnPieceDebutVFacture = 50000;
//						if(BlocnPieceFinVFacture==null)  BlocnPieceFinVFacture = 80000;
//						numPiece = creerNumeroPiece(l.getNumPiece(), BlocnPieceDebutVFacture, BlocnPieceFinVFacture);
//
//
//						// Création de la pièce
//						pieceDao.ajout_Piece(keepDossier, numPiece, l.getDatePiece());
//						//Trouver les infos du client de la facture
//						compteClient = l.getCompteLigne();
//						String activiteClient = "";
//
//						//  S'assurer de l'exisence du compte  client dans AGRIGEST
//						compteDao.ajout_Compte(keepDossier, compteClient, "CLIENT " + l.getNomTiers(),"","",false);
//						activiteDao.ajout_Activite(keepDossier, activiteClient, "ACTIVITE " + activiteClient,false);
//						plDao.ajout_PlanComptable(keepDossier, compteClient, activiteClient,false);	
//						totalTTC=l.getMontantTtc();
//						numPieceCourante=l.getNumPiece();
//					}
//
//
//				}
//				
//
//				
//				if(!l.getNumLigne().equals("1")) {
//
//				//  Variables de vérification écart
//				cumulDebit =BigDecimal.ZERO;
//				cumulCredit =BigDecimal.ZERO;
//				// Génération des comptes TVA si nécessaire
//				codeTVA  = l.getCodeTVAAgrigest();
//				tauxTva =BigDecimal.ZERO;
//				tVASansCompte = false;
//				journalTVA = "";
//				montantTVA = "";
//
//
//				if( l.getCodeTVA()!=null  && !l.getCodeTVA().isEmpty()) {
//					Tva tva =tvaDao.findByCode(codeTVA);
//					if( tva!=null ) {
//						if(tva.gettCpt()!=null && !tva.gettCpt().isEmpty()) {
//							compteDao.ajout_Compte(keepDossier, tva.gettCpt(), tva.getLibelle());
//							plDao.ajout_PlanComptable(keepDossier, tva.gettCpt(), "0000");
//							journalTVA = tva.gettJournal();
//							montantTVA = tva.gettCtrlCl_DC();
//						}
//						else 
//							tVASansCompte = true;	       					
//						if (tva.gettTaux()!=null) {
//							tauxTva = tva.gettTaux();
//						}
//					}
//					else 
//						tVASansCompte = true;
//
//				}
//				else
//					tVASansCompte = true;       			
//
//				//TODO voir pour les unités que je ne récupère pas dans mon importation bdg !!!
//				//	       			String unite1  = ReplaceDbNull(ligneDetail.Item("LibUnite1"), "");
//				//	       			String unite2  = ReplaceDbNull(ligneDetail.Item("LibUnite2"), "");
//				//	       			String nomProduit = l..Item("CodeProduit").toUpper;
//
//
//				//TODO voir pour le nomProduit que je ne récupère pas dans mon importation bdg !!!
//				compteDao.ajout_Compte(keepDossier, l.getCompteLigne(), "PRODUIT " + "NomProduit");
//				//	       			activiteDao.ajout_Activite(keepDossier, ligneDetail.Item("NActivite"), "ACTIVITE " & ligneDetail.Item("NActivite"));
//				plDao.ajout_PlanComptable(keepDossier, l.getCompteLigne(), "0000");	
//
//				//Récupérer les infos de quantité
//				BigDecimal qte1  = ((BigDecimal)replaceDbNull(l.getQte1(), 0)).setScale(2, BigDecimal.ROUND_HALF_UP);
//				BigDecimal qte2  = ((BigDecimal)replaceDbNull(l.getQte2(), 0)).setScale(2, BigDecimal.ROUND_HALF_UP);
//				BigDecimal montantTTC  = ((BigDecimal)replaceDbNull(l.getMontantTtc(), 0));
//				BigDecimal montantHt  = ((BigDecimal)replaceDbNull(l.getMontantHt(), 0));
//
//				if( ! tVASansCompte ) {
//					boolean ajoute=false;
//					MontantTva mt=rechercheCodeTvaDansListe(recapTxTVA, codeTVA);
//					if(mt==null) {
//						mt=new MontantTva();
//						ajoute=true;
//					}
//					mt.setCodeTVA(codeTVA);
//					mt.setTauxTva(tauxTva);
//					mt.setMontantHT(mt.getMontantHT().add(montantHt));
//					mt.setMontantTTC(mt.getMontantTTC().add(montantTTC));
//					if(ajoute)recapTxTVA.add(mt);
//				}
//				//'Créer la ligne et les mouvements correspondant à la ligne de facture
//				ligneDao.ajout_Ligne(keepDossier, numPiece, l.getDatePiece(), ligne, tVASansCompte, codeTVA, journalTVA, montantTVA, l.getNomTiers(), l.getLibelle());
//				mouvDao.ajout_Mouvement(keepDossier, numPiece, l.getDatePiece(), ligne, 1, "48800000", "0000", montantHt, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, l.getCompteLigne(), "0000");
//				mouvDao.ajout_Mouvement(keepDossier, numPiece, l.getDatePiece(), ligne, 3, l.getCompteLigne(), "0000", BigDecimal.ZERO, montantHt, "C", qte1, qte2, "48800000", "0000");
//
//				ligne += 1;
//
//				//'Gestion des variables de contrôle d'ecart
//				cumulDebit = cumulDebit.add(montantHt);
//
//				//si ok, à la fin on commit tout
//				}
//			}
//			//rajouter les lignes de tva avant de passer à la nouvelle pièce
//			//' On génére une ligne par code TVA
//			for (MontantTva mtva : recapTxTVA) {				
//				//                    For Each txTVA As String In RecapTxTVA.GetLstTx()
//				if(mtva.getCodeTVA()!=null ) {
//					Tva tva= tvaDao.findByCode(mtva.getCodeTVA());
//					if( tva!=null) {
//						if( tva.gettCpt()!=null && !tva.gettCpt().isEmpty()) {
//							//'TV 22/12/2011
//							BigDecimal montantTTCTaux  =mtva.getMontantTTC();
//							BigDecimal montantTVATaux  =mtva.getMontantTTC().subtract(mtva.getMontantHT());
//							BigDecimal montantHTTaux  =mtva.getMontantHT();
//
//
//
//							ligneDao.ajout_Ligne(keepDossier, numPiece,datePiece, ligne, true, mtva.getCodeTVA(), journalTVA, tva.gettCtrlCl_DC(), nomTiers, bigDecimalToString(mtva.getTauxTva()));
//							mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, "48800000", "0000", montantTTCTaux, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//							mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 2, tva.gettCpt(), "0000", BigDecimal.ZERO, montantTVATaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "", "0");
//							mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, montantHTTaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//
//							//'Gestion des variables de contrôle d'ecart
//							cumulDebit = cumulDebit.add(montantTTCTaux);
//							cumulCredit = cumulCredit.add(montantHTTaux);
//
//
//							ligne += 1;
//						}
//					}
//				}
//			}	
//			
////			//  On solde le compte 488 par le compte client
////			//Decimal.Round(CDec(rwFacture.Item("MontantTTC")), 2);
////			String numFacture  = l.getReference();
////			if(totalTTC.signum()>=0)numFacture="FA "+numFacture;else numFacture="AV "+numFacture;
//
//
//			ligneDao.ajout_Ligne(keepDossier, numPiece, datePiece, ligne, true, "", "O", "N", nomTiers, numFacture);
//			mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, compteClient, "0000", totalTTC, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
//			mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, totalTTC, "C", BigDecimal.ZERO, BigDecimal.ZERO, compteClient, "0000");
//			ligne += 1;
//			//                'Gestion des variables de contrôle d'ecart
//			cumulCredit = cumulCredit.add(totalTTC);
//
//			//                'Gestion des écarts
//			BigDecimal ecart  = cumulDebit.subtract(cumulCredit) ;
//			recapTxTVA.clear();
//			if (ecart.signum()!= 0) {
//				//					Me.GererEcartExportFacture(ecart, CBool(rwFacture("FacturationTTC")), keepDossier, NumPiece, String.Format("{0:dd/MM/yy}", rwFacture.Item("DateFacture")))
//			}
//			
//			dossierDao.getSqlServer().connection().commit();
//			Alert alert = new Alert(AlertType.INFORMATION);
//			alert.setTitle("Importation dans Agrigest");
//			alert.setHeaderText(null);
//			alert.setContentText("L'importation s'est bien déroulée !");
//			alert.showAndWait();
//			return true;
////			dossierDao.getSqlServer().connection().rollback();
//		} catch (Exception e) {
//			e.printStackTrace();
//			try {
//				dossierDao.getSqlServer().connection().rollback();
//				Alert alert = new Alert(AlertType.WARNING);
//				alert.setTitle("Importation dans Agrigest");
//				alert.setHeaderText(null);
//				alert.setContentText("L'importation est abandonnée !");
//				alert.showAndWait();
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			// TODO: handle exception
//		}
//
//		return false;
//
//
//		//            Catch ex As WarningExport
//		////                'Probleme sur cette facture. On logue le message et on passe à la ligne suivante
//		//                AjouterRapport(ex.Message)
//		//            End Try
//		//
//		//            i += 1 : ReportProgress(i * 100 \ rwFacturesAExporter.Length)
//		//        Next
//		//
//		//        Me._NbFacturesExportees = rwFacturesAExporter.Length
//		//    Else
//		//        Me._NbFacturesExportees = 0
//		//    End If
//		//
//		//    Return True
//
//	}

	
	public Boolean ExporterFactures(List<Pieces> liste) {
		int numPieceCourante = -1;
		String keepDossier = "";
		BigDecimal cumulDebit =BigDecimal.ZERO;
		BigDecimal cumulCredit =BigDecimal.ZERO;
		BigDecimal totalTTC =BigDecimal.ZERO;
		String libelleContrepartie="";
		String compteClient="";
		String codeTVA  = "";
		BigDecimal tauxTva =BigDecimal.ZERO;
		Boolean tVASansCompte = false;
		String journalTVA = "";
		String montantTVA = "";
		Integer ligne = 0;
		int numPiece = 0;
		Date datePiece = null;
		String nomTiers = null;
		String numFacture= null;
		List<MontantTva> recapTxTVA = new LinkedList<MontantTva>();
		try {
			for (Pieces p : liste) {
				String messageDocumentCourantRejete=String.format("le document %s est rejeté", p.getCodeDocument());
				String messageDocumentCourantImporte=String.format("le document %s a été importé avec succès", p.getCodeDocument());
				ligne=1;
				try {
					Date dtExport = LibDate.dateDuJour();
					datePiece=p.getDateDocument();
					nomTiers=p.getNomTiers();
					numFacture  = p.getCodeDocument();
					totalTTC=p.getNetTtcCalc();
					if(totalTTC.signum()>=0)numFacture="FA "+numFacture;else numFacture="AV "+numFacture;
					//Chercher le numéro de dossier de l'exercice correspondant à la facture
					keepDossier = dossierDao.trouverDossier(p.getDateDocument());

					if(keepDossier==null) {
						listeMessageError.add(String.format("Pas de dossier disponible pour le document %s du ",p.getCodeDocument())+ formatDate(datePiece));
						throw new Exception();
					}
					if(p.getCompte()==null ||p.getCompte().equals("")) {
						listeMessageError.add(String.format("Les infos compta ne sont pas renseignées pour le client %s", nomTiers));
						throw new Exception();
					}
					for (Lignes l : p.getLignes()) {
						if(l.getCompteLDocument()==null ||l.getCompteLDocument().equals("") ) {
							listeMessageError.add(String.format("Les infos compta ne sont pas renseignées pour le produit %s dans le document %s en date du ",l.getLibLDocument(), p.getCodeDocument())+formatDate(datePiece));
							throw new Exception();
						}
						//'Vérif TVA
						if(l.getCodeTvaLDocument()!=null && !l.getCodeTvaLDocument().equals("")) {
							if(tvaDao.findByCode(l.getCodeTvaLDocument())==null) {
								listeMessageError.add(String.format("Le code TVA %s de la facture %s en date du ",l.getCodeTvaLDocument(),p.getCodeDocument())+formatDate(datePiece)+" n'existe pas en comptabilité.");
								throw new Exception();
							}
						}
					}


					if(keepDossier!=null) {
						//                'S'assurer de la présence du compte 488
						compteDao.ajout_Compte(keepDossier, "48800000", "COMPTE DE REPARTITION","","",false);
						activiteDao.ajout_Activite(keepDossier, "OOOO", "ACTIVITE GENERALE",false);
						plDao.ajout_PlanComptable(keepDossier, "48800000", "OOOO",false);

						////                'Déterminer le n° de la pièce à utiliser pour la facture

						if(totalTTC.signum()>=0) {
							if(BlocnPieceDebutVFacture==null)  BlocnPieceDebutVFacture = 70000;
							if(BlocnPieceFinVFacture==null)  BlocnPieceFinVFacture = 99999;
						}else {
							if(BlocnPieceDebutVFacture==null)  BlocnPieceDebutVFacture = 60000;
							if(BlocnPieceFinVFacture==null)  BlocnPieceFinVFacture = 69999;							
						}
						
						numPiece = 0;
						try {
							//'On essaye d'utiliser le nRemiseBanque
							if(p.getCodeDocument()!=null) {
								numPiece = LibConversion.stringToInteger(p.getCodeDocument().substring(p.getCodeDocument().length()-5, p.getCodeDocument().length()));
							}
						}catch (Exception ex  ) {
						}

						numPiece = creerNumeroPiece(keepDossier, numPiece,datePiece, BlocnPieceDebutVFacture, BlocnPieceFinVFacture);


						// Création de la pièce
						pieceDao.ajout_Piece(keepDossier, numPiece, p.getDateDocument());
						messageDocumentCourantImporte=String.format("le document %s a été importé sous le numéro %s", p.getCodeDocument(),numPiece);
						//Trouver les infos du client de la facture
						compteClient = p.getCompte();

						String activiteClient = "";

						//  S'assurer de l'exisence du compte  client dans AGRIGEST
						compteDao.ajout_Compte(keepDossier, compteClient, "CLIENT " + p.getNomTiers(),"","",false);
						activiteDao.ajout_Activite(keepDossier, activiteClient, "ACTIVITE " + activiteClient,false);
						plDao.ajout_PlanComptable(keepDossier, compteClient, activiteClient,false);	
						totalTTC=p.getNetTtcCalc();
					


					//				}

						//  Variables de vérification écart
						cumulDebit =BigDecimal.ZERO;
						cumulCredit =BigDecimal.ZERO;
					for (Lignes l : p.getLignes()) {

						// Génération des comptes TVA si nécessaire
						codeTVA  = l.getCodeTvaLDocument();
						tauxTva =BigDecimal.ZERO;
						tVASansCompte = false;
						journalTVA = "";
						montantTVA = "";


						if( l.getCodeTvaLDocument()!=null  && !l.getCodeTvaLDocument().isEmpty()) {
							Tva tva =tvaDao.findByCode(codeTVA);
							if( tva!=null ) {
								if(tva.gettCpt()!=null && !tva.gettCpt().isEmpty()) {
									compteDao.ajout_Compte(keepDossier, tva.gettCpt(), tva.getLibelle());
									plDao.ajout_PlanComptable(keepDossier, tva.gettCpt(), "0000");
									journalTVA = tva.gettJournal();
									montantTVA = tva.gettCtrlCl_DC();
								}
								else 
									tVASansCompte = true;	       					
								if (tva.gettTaux()!=null) {
									tauxTva = tva.gettTaux();
								}
							}
							else 
								tVASansCompte = true;

						}
						else
							tVASansCompte = true;       			

						//TODO voir pour les unités que je ne récupère pas dans mon importation bdg !!!
						//	       			String unite1  = ReplaceDbNull(ligneDetail.Item("LibUnite1"), "");
						//	       			String unite2  = ReplaceDbNull(ligneDetail.Item("LibUnite2"), "");
						//	       			String nomProduit = l..Item("CodeProduit").toUpper;


						//TODO voir pour le nomProduit que je ne récupère pas dans mon importation bdg !!!
						compteDao.ajout_Compte(keepDossier, l.getCompteLDocument(), "PRODUIT " + "NomProduit");
						//	       			activiteDao.ajout_Activite(keepDossier, ligneDetail.Item("NActivite"), "ACTIVITE " & ligneDetail.Item("NActivite"));
						plDao.ajout_PlanComptable(keepDossier, l.getCompteLDocument(), "0000");	

						//Récupérer les infos de quantité
						BigDecimal qte1  = ((BigDecimal)replaceDbNull(l.getQteLDocument(), 0)).setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal qte2  = ((BigDecimal)replaceDbNull(l.getQte2LDocument(), 0)).setScale(2, BigDecimal.ROUND_HALF_UP);
						BigDecimal montantTTC  = ((BigDecimal)replaceDbNull(l.getMtTtcLApresRemiseGlobaleDocument(), 0));
						BigDecimal montantHt  = ((BigDecimal)replaceDbNull(l.getMtHtLApresRemiseGlobaleDocument(), 0));

						if( ! tVASansCompte ) {
							boolean ajoute=false;
							MontantTva mt=rechercheCodeTvaDansListe(recapTxTVA, codeTVA);
							if(mt==null) {
								mt=new MontantTva();
								ajoute=true;
							}
							mt.setCodeTVA(codeTVA);
							mt.setTauxTva(tauxTva);
							mt.setMontantHT(mt.getMontantHT().add(montantHt));
							mt.setMontantTTC(mt.getMontantTTC().add(montantTTC));
							if(ajoute)recapTxTVA.add(mt);
						}
						//'Créer la ligne et les mouvements correspondant à la ligne de facture
						ligneDao.ajout_Ligne(keepDossier, numPiece, p.getDateDocument(), ligne, tVASansCompte, codeTVA, journalTVA, montantTVA, p.getNomTiers(), l.getLibLDocument());
						mouvDao.ajout_Mouvement(keepDossier, numPiece, p.getDateDocument(), ligne, 1, "48800000", "0000", montantHt, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, l.getCompteLDocument(), "0000");
						mouvDao.ajout_Mouvement(keepDossier, numPiece, p.getDateDocument(), ligne, 3, l.getCompteLDocument(), "0000", BigDecimal.ZERO, montantHt, "C", qte1, qte2, "48800000", "0000");

						ligne += 1;

						//'Gestion des variables de contrôle d'ecart
						cumulCredit = cumulCredit.add(montantHt);

						//si ok, à la fin on commit tout
					}
					//rajouter les lignes de tva avant de passer à la nouvelle pièce
					//' On génére une ligne par code TVA
					for (MontantTva mtva : recapTxTVA) {				
						//                    For Each txTVA As String In RecapTxTVA.GetLstTx()
						if(mtva.getCodeTVA()!=null ) {
							Tva tva= tvaDao.findByCode(mtva.getCodeTVA());
							if( tva!=null) {
								if( tva.gettCpt()!=null && !tva.gettCpt().isEmpty()) {
									//'TV 22/12/2011
									BigDecimal montantTTCTaux  =mtva.getMontantTTC();
									BigDecimal montantTVATaux  =mtva.getMontantTTC().subtract(mtva.getMontantHT());
									BigDecimal montantHTTaux  =mtva.getMontantHT();



									ligneDao.ajout_Ligne(keepDossier, numPiece,datePiece, ligne, true, mtva.getCodeTVA(), journalTVA, tva.gettCtrlCl_DC(), nomTiers, LibConversion.bigDecimalToString(mtva.getTauxTva()));
									mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, "48800000", "0000", montantTTCTaux, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
									mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 2, tva.gettCpt(), "0000", BigDecimal.ZERO, montantTVATaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "", "0");
									mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, montantHTTaux, "C", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");

									//'Gestion des variables de contrôle d'ecart
//									cumulDebit = cumulDebit.add(montantTTCTaux);
									cumulCredit = cumulCredit.add(montantTVATaux);


									ligne += 1;
								}
							}
						}
					}	

					//				//  On solde le compte 488 par le compte client
					//				//Decimal.Round(CDec(rwFacture.Item("MontantTTC")), 2);
					//				String numFacture  = l.getReference();
					//				if(totalTTC.signum()>=0)numFacture="FA "+numFacture;else numFacture="AV "+numFacture;


					ligneDao.ajout_Ligne(keepDossier, numPiece, datePiece, ligne, true, "", "O", "N", nomTiers, numFacture);
					mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 1, compteClient, "0000", totalTTC, BigDecimal.ZERO, "D", BigDecimal.ZERO, BigDecimal.ZERO, "48800000", "0000");
					mouvDao.ajout_Mouvement(keepDossier, numPiece, datePiece, ligne, 3, "48800000", "0000", BigDecimal.ZERO, totalTTC, "C", BigDecimal.ZERO, BigDecimal.ZERO, compteClient, "0000");
					ligne += 1;
					//                'Gestion des variables de contrôle d'ecart
					cumulDebit = cumulDebit.add(totalTTC);

					//                'Gestion des écarts
					BigDecimal ecart  = cumulDebit.subtract(cumulCredit) ;
					recapTxTVA.clear();
					if (ecart.signum()!= 0) {
						listeMessageError.add(String.format("Ecart de %.2f sur la facture : %s du "+LibDate.dateToString(datePiece,"/")+" ", ecart,p.getCodeDocument()));
//						listeMessageError.add(String.format("Ecart de %.2f sur la facture : %s du %1$tY %1$tm %1$te", ecart,p.getCodeDocument(),datePiece));
						throw new Exception();
						//					Me.GererEcartExportFacture(ecart, CBool(rwFacture("FacturationTTC")), keepDossier, NumPiece, String.Format("{0:dd/MM/yy}", rwFacture.Item("DateFacture")))
					}
					listeMessageOk.add(messageDocumentCourantImporte);
					}
				} catch (Exception e) {
					// TODO: handle exception
					listeMessageError.add(messageDocumentCourantRejete);
					listeMessageError.add(System.getProperty("line.separator")+"L'importation des factures est abandonnée !!!");
//					throw new Exception();
				}
			}
			
			if(listeMessageError.size()>0) {
				String message = "";
				for (String me : listeMessageError) {
					message+=me+System.getProperty("line.separator");
				}
				throw new Exception(message);
			}
			
			String rapport = "L'importation des factures s'est bien déroulée !"+System.getProperty("line.separator");
			if(listeMessageOk.size()>0) {
				for (String me : listeMessageOk) {
					rapport+=me+System.getProperty("line.separator");
				}
			}
			
			dossierDao.getSqlServer().getConnDossier().commit();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Importation des factures dans Agrigest ");
			alert.setHeaderText(null);
//			alert.setContentText("L'importation des factures s'est bien déroulée !");
			alert.setContentText(rapport);
			alert.showAndWait();
			return true;
			//			dossierDao.getSqlServer().connection().rollback();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				dossierDao.getSqlServer().getConnDossier().rollback();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Importation des factures dans Agrigest");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO: handle exception
		}

		return false;
	}


	public String substringProtege(String str, int beginIndex, int endIndex) {
		String retour="";
		if(str.length()>=endIndex)return str.substring(beginIndex, endIndex);
		if(str.length()>=beginIndex)return str.substring(beginIndex, str.length());
		return "";
	}
	
	
    private Object replaceDbNull(Object value , Object defaultValue) {
    	if (value==null)return defaultValue; else return value;
    }
	
	public void litFichierImport(String cheminFichier) throws Exception {
		//		List<LigneDocFichier> retour = new LinkedList<LigneDocFichier>();
		//		FileReader fr = new FileReader(cheminFichier);

					BufferedReader br = new BufferedReader(new InputStreamReader(
						    new FileInputStream(cheminFichier), "UTF-8"));

//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				new FileInputStream(cheminFichier), "ISO-8859-1"));

		String ligne = br.readLine();
		while (ligne!=null && !ligne.isEmpty()){
			LigneDocFichier l=new LigneDocFichier();
			l.setNumPiece(LibConversion.stringToInteger(substringProtege(ligne,0, 5).trim()));
			l.setNumLigne(substringProtege(ligne,5, 8).trim());
			l.setTypePiece(substringProtege(ligne,8,9 ).trim());
			l.setReference(substringProtege(ligne,9,18 ).trim());
			l.setDatePiece(LibDate.stringToDate(substringProtege(ligne,18,26 ).trim()));
			l.setCompteLigne(substringProtege(ligne,26,34 ).trim());
			l.setLibelle(substringProtege(ligne,34,57 ).trim());
			l.setMontantDebit(LibConversion.stringToBigDecimal(substringProtege(ligne,57, 69).trim()));
			l.setMontantCredit(LibConversion.stringToBigDecimal(substringProtege(ligne,69,81 ).trim()));
			l.setQte1(LibConversion.stringToBigDecimal(substringProtege(ligne,81,90 ).trim()));
			l.setQte2(LibConversion.stringToBigDecimal(substringProtege(ligne,90,99 ).trim()));
			l.setCodeTVA(substringProtege(ligne,99,100 ).trim());
			l.setTauxTVA(LibConversion.stringToBigDecimal(substringProtege(ligne,100,106 ).trim()));
			l.setMontantDebitTVA(LibConversion.stringToBigDecimal(substringProtege(ligne,106, 117).trim()));
			l.setMontantCreditTVA(LibConversion.stringToBigDecimal(substringProtege(ligne,117,128 ).trim()));
			l.setDateEcheancePiece(LibDate.stringToDate(substringProtege(ligne,128,136 ).trim()));
			l.setCompteColl(substringProtege(ligne,136,144 ).trim());
			l.setNomTiers(substringProtege(ligne,144,174 ).trim());
			l.setAdr1(substringProtege(ligne,173,204 ).trim());
			l.setAdr2(substringProtege(ligne,204, 234).trim());
			l.setCodePostal(substringProtege(ligne,234,239 ));
			l.setVille(substringProtege(ligne,239,264 ).trim());
			l.setExibiliteTVA(substringProtege(ligne,264,265 ).trim());
			l.setDateLivraisonLigne(LibDate.stringToDate(substringProtege(ligne,265,273 ).trim()));
			l.setRefContrepartie(substringProtege(ligne,273,282 ).trim());
			l.setDateContrepartie(LibDate.stringToDate(substringProtege(ligne,282,290 ).trim()));
			l.setMontantDebitContrepartie(LibConversion.stringToBigDecimal(substringProtege(ligne,290, 301).trim()));
			l.setMontantCreditContrepartie(LibConversion.stringToBigDecimal(substringProtege(ligne,301,312 ).trim()));
			l.setMontantPointage(LibConversion.stringToBigDecimal(substringProtege(ligne,312,323 ).trim()));
			l.setCodeTVAAgrigest(substringProtege(ligne,323,330 ).trim());
			l.setRefReglement(substringProtege(ligne,330,339 ).trim());
			
			l.setMontantTtcContrepartie(l.getMontantCreditContrepartie().subtract(l.getMontantDebitContrepartie()));
			
			l.setMontantHt(l.getMontantCredit().subtract(l.getMontantDebit()));
			l.setMontantTva(l.getMontantCreditTVA().subtract(l.getMontantDebitTVA()));
			l.setMontantTtc(l.getMontantHt().add(l.getMontantTva()));
			//				retour.add(l);
			if(l.getTypePiece().equals("V"))listeFactureVente.add(l);
			if(l.getTypePiece().equals("A"))listeFactureAchat.add(l);
			if(l.getTypePiece().equals("T"))listeReglement.add(l);
			if(l.getTypePiece().equals("P"))listePointage.add(l);
			

			ligne = br.readLine();
			
		}

		br.close();


		//		return retour;



	}

	

	public Integer cadreFourchette(Integer num, Integer debFourchette , Integer finFourchette) {
		if(num> finFourchette + 1 - debFourchette) {
			String n=LibConversion.integerToString(num);
			n=n.substring(1, n.length());
			num=LibConversion.stringToInteger(n);
		}
		return (num % (finFourchette + 1 - debFourchette)) + debFourchette;
	}


	/**
	 * 
	 * @param dossier
	 * @param nFacture
	 * @param dateDocument
	 * @param debFourchette
	 * @param finFourchette
	 * @return numéro de pièce utilisable
	 */
	public Integer creerNumeroPiece(String dossier,Integer nFacture ,Date dateDocument , Integer debFourchette , Integer finFourchette ) {
		Integer numPiece ;
		String numPieceStr ="";

        numPiece = cadreFourchette(nFacture, debFourchette, finFourchette);

//      // 'En cas de collision, on incrémente
        try {
			numPiece=pieceDao.maxNumPiece(dossier, numPiece, dateDocument, debFourchette, finFourchette);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return numPiece;
	}

	public List<LigneDocFichier> getListeFactureVente() {
		return listeFactureVente;
	}

	public void setListeFactureVente(List<LigneDocFichier> listeFactureVente) {
		this.listeFactureVente = listeFactureVente;
	}

	public List<LigneDocFichier> getListeFactureAchat() {
		return listeFactureAchat;
	}

	public void setListeFactureAchat(List<LigneDocFichier> listeFactureAchat) {
		this.listeFactureAchat = listeFactureAchat;
	}

	public List<LigneDocFichier> getListeReglement() {
		return listeReglement;
	}

	public void setListeReglement(List<LigneDocFichier> listeReglement) {
		this.listeReglement = listeReglement;
	}
	
	
	
//	public static String bigDecimalToString(BigDecimal value, String defaut){
//		if (value==null){
//			return defaut;
//		}
//		else
//			return value.toString();
//	}
//	
//	public static String bigDecimalToString(BigDecimal value){
//		return bigDecimalToString(value,new BigDecimal(0).toString());
//	}
//	
//	public static BigDecimal stringToBigDecimal(String value, BigDecimal defaut) {
//		if(value!=null && !value.equals("")){
//			return BigDecimal.valueOf(Double.parseDouble(value));
//		} else {
//			return defaut;
//		}
//	}
//	
//	public static BigDecimal stringToBigDecimal(String value) {
//		return  stringToBigDecimal(value,BigDecimal.ZERO);
//	}
	
	
//	public static Integer stringToInteger(String value) {
//		return stringToInteger(value,0);
//	}
//	
//	public static Integer stringToInteger(String value, Integer defaut) {
//		if(value!=null && !value.equals("")){
//			return Integer.parseInt(value);
//		} else {
//			return defaut;
//		}
//	}
	
//	public static String integerToString(Integer value, String defaut){
//		if (value==null){
//			return defaut;
//		}
//		else
//			return value.toString();
//	}
	
//	
//	public static Date dateDuJour(){
//		return new Date();
//	}
//	
//	static public Date stringToDate(String date) throws ParseException {
//		return stringToDate(date,new Date());
//	}
//	
//	static public Date stringToDate(String dateValeur, Date defaut) throws ParseException {
//		String jourLoc,moisLoc,anneeLoc;
//		Date dateTmp;
//		String[] part,tmp;
//		if(dateValeur!=null && !dateValeur.equals("")) {
//			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//			df.setCalendar(Calendar.getInstance());
//			df.getCalendar().setTime(defaut);
//			
//			part = dateValeur.split("/"); 
//			jourLoc="";
//			moisLoc="";
//			anneeLoc="";
//			if (part.length>0) jourLoc=part[0];
//			if (part.length>1) moisLoc=part[1];
//			if (part.length>2) anneeLoc=part[2];
//			
//			tmp=dateToString(defaut).split("/");
//			jourLoc=jourLoc.replaceAll("-","");
//			moisLoc=moisLoc.replaceAll("-","");
//			anneeLoc=anneeLoc.replaceAll("-","");
//			if (jourLoc.equals("") || jourLoc.equals("0")){
//				jourLoc =tmp[0]; 
//			}
//			if (moisLoc.equals("")|| moisLoc.equals("0")){
//				moisLoc = tmp[1];
//			}
//			if (anneeLoc.equals("")|| anneeLoc.equals("0")){
//				anneeLoc = tmp[2];
//			}
//			while (moisLoc.length()<2)
//				moisLoc="0"+moisLoc;
//			
//			while (jourLoc.length()<2)
//				jourLoc="0"+jourLoc;
//			
//			while (anneeLoc.length()<4)
//				if (anneeLoc.length()<2)
//				anneeLoc="0"+anneeLoc;
//				else if (anneeLoc.length()<3)
//					anneeLoc="0"+anneeLoc;
//					else anneeLoc="2"+anneeLoc;
//
//				dateTmp = new Date(df.parse(jourLoc+"/"+moisLoc+"/"+anneeLoc).getTime());
//		} else {
//			dateTmp = defaut;
//		}
//		return dateTmp;
//	}
//	
//	static public String dateToString(Date d) {
//		return dateToString(d,"/");
//	}
//	
//	static public String dateToString(Date d,String separateur) {
//		 String mois;
//		 String jour;
//		 String annee;
//		if(d!=null) {
//			Calendar date = Calendar.getInstance();
//			date.setTime(d);
//			//TODO Gestion des symbols
//			jour=String.valueOf(date.get(Calendar.DAY_OF_MONTH));
//			mois=String.valueOf(date.get(Calendar.MONTH)+1);
//			annee=String.valueOf(date.get(Calendar.YEAR));
//			
//			while (mois.length()<2)
//				mois="0"+mois;
//			
//			while (jour.length()<2)
//				jour="0"+jour;
//			while (annee.length()<4)
//				annee="0"+annee;
//	
//			return jour+separateur+mois+separateur+annee;
//		} else {
//			return "";
//		}
//	}

	public MontantTva rechercheCodeTvaDansListe(List<MontantTva> liste, String code) {
		for (MontantTva montantTva : liste) {
			if(montantTva.getCodeTVA().equals(code))return montantTva;
		}
		return null;
	}
	
	
	
	public Boolean ExporterRemises(List<Pieces> liste,List<Pointage> listePointages) {
		//' REMISE ET REGLEMENTS RESULTANT DES VENTES
		int numPieceCourante = -1;
		String keepDossier = "";
		BigDecimal cumulDebit =BigDecimal.ZERO;
		BigDecimal cumulCredit =BigDecimal.ZERO;
		BigDecimal totalTTC =BigDecimal.ZERO;
		BigDecimal montantTTCFacture =BigDecimal.ZERO;
		String libelleContrepartie="";
		String compteClient="";
		String compteBanque="";
		String activiteBanque="";
		String activiteClient="";
		String codeTVA  = "";
		BigDecimal tauxTva =BigDecimal.ZERO;
		Boolean tVASansCompte = false;
		String journalTVA = "";
		String montantTVA = "";
		Integer ligne = 0;
		int numPiece = 0;
		Date datePiece = null;
		String nomTiers = null;
		String numFacture= null;
		Integer i = 0;
		Date dtExport = LibDate.dateDuJour();
		BigDecimal avanceClient=BigDecimal.ZERO;
		List<MontantTva> recapTxTVA = new LinkedList<MontantTva>();
		List<MontantTva> globalRecapTxTVA = new LinkedList<MontantTva>();

		try {
			for (Pieces p : liste) {
				dtExport = LibDate.dateDuJour();
				datePiece=p.getDateDocument();
				nomTiers=p.getNomTiers();
				numFacture  = p.getCodeDocument();
				ligne=1;
				String messageDocumentCourantRejete=String.format("le document %s est rejeté", p.getCodeDocument());
				String messageDocumentCourantImporte=String.format("le document %s a été importé avec succès", p.getCodeDocument());
				try {
					//'Trouver le dossier de l'exercice
					keepDossier = dossierDao.trouverDossier(p.getDateDocument());
					if(keepDossier==null) {
						listeMessageError.add(String.format("Pas de dossier disponible pour le document %s du ",p.getCodeDocument())+ formatDate(datePiece));
						throw new Exception();
					}
					//              'Verifs avant création de la pièce
					if(p.getCompte()==null ||p.getCompte().equals("")) {
						listeMessageError.add(String.format("Pas de banque renseignée pour le document %s du ",p.getCodeDocument())+ formatDate(datePiece));
						throw new Exception();
					}
					//              'Vérif que les infos comptables du tiers payeur sont renseignées
					for (Lignes l : p.getLignes()) {
						if(l.getCompteLDocument()==null ||l.getCompteLDocument().equals("") ) {
							listeMessageError.add(String.format("Les infos compta ne sont pas renseignées pour le client %s dans le document %s en date du ",l.getTiersLDocument(), p.getCodeDocument())+formatDate(datePiece));
							throw new Exception();
						}
					}


					if(keepDossier!=null) {

						//'Création si nécessaire du compte 48800000
						compteDao.ajout_Compte(keepDossier, "48800000", "COMPTE DE REPARTITION","","",false);
						activiteDao.ajout_Activite(keepDossier, "0000", "ACTIVITE GENERALE",false);
						plDao.ajout_PlanComptable(keepDossier, "48800000", "0000",false);

						//'Création si nécessaire du compte banque
						compteBanque   = p.getCompte();
						activiteBanque  = "";

						compteDao.ajout_Compte(keepDossier, compteBanque, compteBanque);
						activiteDao.ajout_Activite(keepDossier, activiteBanque, "ACTIVITE " + activiteBanque);
						plDao.ajout_PlanComptable(keepDossier, compteBanque, activiteBanque);


						//'Déterminer le n° de la piece
						if(BlocnPieceDebutVReglement==null)  BlocnPieceDebutVReglement = 40000;
						if(BlocnPieceFinVReglement==null)  BlocnPieceFinVReglement = 59999;

						numPiece = 0;
						try {
							//'On essaye d'utiliser le nRemiseBanque
							if(p.getCodeDocument()!=null) {
								numPiece = LibConversion.stringToInteger(p.getCodeDocument().substring(p.getCodeDocument().length()-5, p.getCodeDocument().length()));
							}
						}catch (Exception ex  ) {
						}

						// 'On cadre le n° de piece dans la fourchette
						numPiece = creerNumeroPiece(keepDossier,numPiece,datePiece, BlocnPieceDebutVReglement, BlocnPieceFinVReglement);


						// Création de la pièce
						pieceDao.ajout_Piece(keepDossier, numPiece, p.getDateDocument());
						messageDocumentCourantImporte=String.format("le document %s a été importé sous le numéro %s", p.getCodeDocument(),numPiece);



						//'POUR CHAQUE lignes DE LA REMISE (règlement)
						for (Lignes l : p.getLignes()) {
							avanceClient=l.getMtTtcLApresRemiseGlobaleDocument();
							montantTTCFacture = BigDecimal.ZERO;

							//POUR CHAQUE FACTURE REGLEE PAR LE REGLEMENT
							for (Pointage po : listePointages) {
								if(po.getCodeReglement().equals(l.getCodeReglement()) && po.getTiers().equals(l.getCompteLDocument())) {
									avanceClient=avanceClient.subtract(po.getAffectation());

									//'Déterminer les informations CLIENT
									nomTiers = l.getTiersLDocument();
									compteClient = l.getCompteLDocument();
									activiteClient ="";
									//
									//                           // 'Créer le compte CLIENT
									compteDao.ajout_Compte(keepDossier, compteClient, "CLIENT " + nomTiers);
									activiteDao.ajout_Activite(keepDossier, activiteClient, "ACTIVITE " + activiteClient);
									plDao.ajout_PlanComptable(keepDossier, compteClient, activiteClient);


									//'Créer les lignes pour le réglement
									String libPaiement   = l.getCodeReglement();
									String libPaiement2  = l.getLibLDocument();
									libPaiement2 = libPaiement2 +" - "+po.getCodeDocument();
									//
									//                            //'On garde volontairement le n° de facture car n° chèque trop long pour affichage
									//                            If Not (rwFacture.IsNull("nFacture")) Then
									//                                libPaiement2 = libPaiement2 & "FA " & rwFacture.Item("nFacture")
									//                            End If
									//
									//                            If Not (rwReglement.IsNull("nMode")) Then
									//                                libPaiement2 = libPaiement2 & " " & CStr(rwReglement.Item("nMode"))
									//                            End If
									//
									//
									ligneDao.ajout_Ligne_SansTVA(keepDossier, numPiece, datePiece, ligne, libPaiement, libPaiement2, "48800000", "0000", compteClient, activiteClient, po.getAffectation());
									ligne += 1;
									//
									//                           // ' CALCUL DES SOMMES REGLEES ET DUES POUR LA FACTURE
									//                            Dim SommeDue As Decimal = 0
									//                            Dim SommePayee As Decimal = 0
									//                            Dim SommeDejaPayee As Decimal = 0
									//                            Dim ratioPaye As Decimal = 1
									//
									//                            With dsAgrifact.Tables("FactureMontantExport")
									//                                Dim obj As Object = .Compute("Sum(MontantTTC)", "nDevis=" & nDevis & "")
									//                                If Not IsDBNull(obj) Then SommeDejaPayee = Decimal.Round(CDec(obj), 2)
									//                            End With
									//
									//                            SommePayee = CDec(rwReglementDetail.Item("Montant"))
									//
									//
									//                            SommeDue = CDec(rwFacture.Item("MontantTTC"));
									//
									//                            montantTTCFacture = CDec(rwFacture.Item("MontantTTC"))
									//
									//
									//
									//                            If SommeDue <> SommePayee Then
									//                                If Math.Abs(SommeDejaPayee + SommePayee - SommeDue) <= 0.02 Then ' ON EST OBLIGE AFIN DE POUVOIR SOLDER LA FACTURE CF lUDO
									//                                    SoldeFacture = True
									//                                    ratioPaye = 1 //'Dans ce cas on prend 100% du HT, on retranchera ensuite le déjà payé
									//                                Else
									//                                    ratioPaye = SommePayee / SommeDue
									//                                End If
									//                            End If
									//
									//                            //'Faire un récap de la TVA de la facture
									//                            Dim rwFacturesDetail() As DataRow = dsAgrifact.Tables("VFacture_Detail").Select("nDevis='" & nDevis & "' And CodeProduit not is null And Codeproduit<>''", "nDevis")
									//
									//                            For Each rwFactureDetail As DataRow In rwFacturesDetail
									//                                Dim CodeTVA As String = String.Empty
									//                                Dim JournalTVA As String = String.Empty
									//                                Dim MontantTVA As String = String.Empty
									//                                Dim TauxTva As Decimal = 0
									//
									//                                If Convert.ToString(rwFactureDetail("TTVA")).Length > 0 Then
									//                                    CodeTVA = Convert.ToString(rwFactureDetail("TTVA"))
									//
									//                                    Dim rwTauxTva() As DataRow = dsAgrigest.Tables("TVA").Select("TTVA='" & CodeTVA.Replace("'", "''") & "'")
									//
									//                                    If rwTauxTva.Length > 0 Then
									//                                        Dim rwTVA As DataRow = rwTauxTva(0)
									//
									//                                        If Convert.ToString(rwTVA("TCpt")).Length > 0 Then
									//                                            //'CREATION EVENTUELLE DU COMPTE TVA
									//                                            Ajout_Compte(keepDossier, rwTVA("TCpt"), rwTVA("Libellé"))
									//                                            Ajout_PlanComptable(keepDossier, rwTVA("TCpt"), "0000")
									//
									//                                            Debug.Print("rwTVA(TCpt) : " & rwTVA("TCpt"))
									//
									//                                            JournalTVA = rwTVA("TJournal")
									//                                            MontantTVA = rwTVA("TCtrlCl_DC")
									//                                        End If
									//
									//                                        If Not IsDBNull(rwTVA("TTaux")) Then
									//                                            TauxTva = CDec(rwTVA.Item("TTaux"))
									//                                        End If
									//                                    End If
									//                                End If
									//
									//                               // 'stocker pour chacun des codes TVA présents le montant d'HT qui va être payé par la facture
									//                               // 'TODO : ON NE GERE COMPLETEMENT PAS LA TVA RPCH
									//                                If Not IsDBNull(rwFactureDetail("MontantLigneHt")) Then
									//                                    RecapTxTVA.AddMontant(CodeTVA, TauxTva, Decimal.Round((rwFactureDetail("MontantLigneHt") * ratioPaye), 2), 0)
									//                                End If
									//                            Next
									//
									//
									////                            If (TVASurEncaissement) Then
									////                                //'Si le réglement solde la facture alors on retranche le déjà payé
									////                                If SoldeFacture Then
									////                                    For Each codeTVA As String In RecapTxTVA.GetLstTx()
									////                                        Dim elRecap As ElementRecapTva = RecapTxTVA.GetElementRecap(codeTVA)
									////                                        Dim rwDejaPayeeII() As DataRow = dsAgrifact.Tables("factureMontantExport").Select("nDevis='" & nDevis & "' And TTVA='" & codeTVA.Replace("'", "''") & "'", "nDevis")
									////                                        Dim rwDejapayee As DataRow
									////
									////                                        For Each rwDejapayee In rwDejaPayeeII
									////                                            With rwDejapayee
									////                                                elRecap.MontantHT -= .Item("MontantHT")
									////                                                elRecap.MontantTVA -= .Item("MontantTVA")
									////                                                elRecap.MontantTTC -= .Item("MontantTTC")
									////                                            End With
									////                                        Next
									////                                    Next
									////                                End If
									//
									//
									////                                //écriture dans la table historique dans Bdg (en Web service)
									////                                If TVASurEncaissement Then
									////                                    For Each codeTVA As String In RecapTxTVA.GetLstTx()
									////                                        Dim elRecap As ElementRecapTva = RecapTxTVA.GetElementRecap(codeTVA)
									////                                        Dim rws() As DataRow = dsAgrifact.Tables("FactureMontantExport").Select(String.Format("nDetailReglement={0} AND TTVA='{1}'", rwReglementDetail.Item("nDetailReglement"), codeTVA.Replace("'", "''")))
									////
									////                                        If rws.Length = 0 Then
									////                                            Dim rwFactureMontantExport As DataRow = dsAgrifact.Tables("FactureMontantExport").NewRow
									////
									////                                            With rwFactureMontantExport
									////                                                .Item("nDevis") = nDevis
									////                                                .Item("nDetailReglement") = rwReglementDetail.Item("nDetailReglement")
									////                                                .Item("TTVA") = codeTVA
									////                                                .Item("MontantHT") = elRecap.MontantHT
									////                                                .Item("MontantTVA") = elRecap.MontantTVA
									////                                                .Item("MontantTTC") = elRecap.MontantTTC
									////                                                .Item("DateExportCompta") = Now
									////                                            End With
									////
									////                                            dsAgrifact.Tables("FactureMontantExport").Rows.Add(rwFactureMontantExport)
									////                                        Else
									////                                            With rws(0)
									////                                                .BeginEdit()
									////                                                .Item("nDevis") = nDevis
									////                                                .Item("MontantHT") = elRecap.MontantHT
									////                                                .Item("MontantTVA") = elRecap.MontantTVA
									////                                                .Item("MontantTTC") = elRecap.MontantTTC
									////                                                .Item("DateExportCompta") = Now
									////                                                .EndEdit()
									////                                            End With
									////                                        End If
									////
									////                                        GlobalRecapTxTVA.AddMontant(codeTVA, elRecap.TxTva, elRecap.MontantHT, elRecap.MontantTTC, elRecap.MontantTVA)
									////                                    Next
									////                                End If
									//
									//                            End If
									//                        End If
								}
							}
							//
							//                    
							//                    End If
							//
							//
							//                    If TVASurEncaissement = True Then
							//                        For Each codeTVA As String In GlobalRecapTxTVA.GetLstTx()
							//                            Dim MontantHT As Decimal = Decimal.Round(GlobalRecapTxTVA.MontantHTTaux(codeTVA), 2)
							//                            Dim MontantTVA As Decimal = Decimal.Round(GlobalRecapTxTVA.MontantTVATaux(codeTVA), 2)
							//                            Dim MontantTTC As Decimal = Decimal.Round(MontantHT + MontantTVA, 2)
							//
							//                            'Dim txEscompte As Decimal = 0
							//                            'If Not IsDBNull(rwReglement.Item("TxEscompte")) Then
							//                            '    txEscompte = rwReglement.Item("TxEscompte")
							//                            'End If
							//
							//                            If dsAgrigest.Tables("TVA").Select("TTVA='" & codeTVA.Replace("'", "''") & "'", "").Length > 0 Then
							//                                Dim rwTva As DataRow = dsAgrigest.Tables("TVA").Select("TTVA='" & codeTVA.Replace("'", "''") & "'", "")(0)
							//
							//                                If Not IsDBNull(rwTva.Item("TCpt")) Then
							//                                    Ajout_Ligne(keepDossier, NumPiece, dtRemise, Ligne, True, codeTVA, rwTva.Item("TJournal"), rwTva.Item("TCtrlCl_DC"), "REGUL TVA", "")
							//                                    Ajout_Mouvement(keepDossier, NumPiece, dtRemise, Ligne, 1, "48800000", "0000", MontantTTC, 0, "D", 0, 0, "48800000", "0000")
							//                                    Ajout_Mouvement(keepDossier, NumPiece, dtRemise, Ligne, 2, rwTva.Item("TCpt"), "0000", 0, MontantTVA, "C", 0, 0, "", "0")
							//                                    Ajout_Mouvement(keepDossier, NumPiece, dtRemise, Ligne, 3, "48800000", "0000", 0, MontantHT, "C", 0, 0, "48800000", "0000")
							//                                    Ligne += 1
							//                                End If
							//                            End If
							//
							//                            Dim CodeTVAEnc As String = CodeTVAEncaissementCorrespondant(codeTVA)
							//
							//                            If dsAgrigest.Tables("TVA").Select("TTVA='" & CodeTVAEnc.Replace("'", "''") & "'", "").Length > 0 Then
							//                                Dim rwTva As DataRow = dsAgrigest.Tables("TVA").Select("TTVA='" & CodeTVAEnc.Replace("'", "''") & "'", "")(0)
							//
							//                                If Not IsDBNull(rwTva.Item("TCpt")) Then
							//                                    Ajout_Ligne_SansTVA(keepDossier, NumPiece, dtRemise, Ligne, "REGUL. TVA", "", rwTva.Item("TCpt"), "0000", "48800000", "0000", MontantTVA)
							//                                    Ligne += 1
							//                                End If
							//                            End If
							//                        Next
							//                    End If
							//
							//'S'il reste de l'avance client :
							if(avanceClient.compareTo(BigDecimal.ZERO)!=0) {
								//'Créer une ligne pour équilibrer la pièce vers le payeur
								//'Déterminer les informations CLIENT
								nomTiers = l.getTiersLDocument();
								compteClient = l.getCompteLDocument();
								activiteClient ="";
								//
								//                           // 'Créer le compte CLIENT
								compteDao.ajout_Compte(keepDossier, compteClient, "CLIENT " + nomTiers);
								activiteDao.ajout_Activite(keepDossier, activiteClient, "ACTIVITE " + activiteClient);
								plDao.ajout_PlanComptable(keepDossier, compteClient, activiteClient);
								ligneDao.ajout_Ligne_SansTVA(keepDossier, numPiece, datePiece, ligne, "AVANCE " + nomTiers, "", "48800000", "0000", compteClient, activiteClient, avanceClient);
								ligne += 1;
								avanceClient=BigDecimal.ZERO;
							}
							//                    If AvanceClient <> 0 Then
							//                        //'Créer une ligne pour équilibrer la pièce vers le payeur
							//                       // 'Déterminer les informations PAYEUR
							//                        Dim rwEntreprise As DataRow = dsAgrifact.Tables("Entreprise").Select("nEntreprise='" & rwReglement("nEntreprise") & "'", "")(0)
							//                        Dim NomPayeur As String = rwEntreprise.Item("Nom").toUpper
							//                        Dim ComptePayeur As String = rwEntreprise.Item("NCompteC")
							//                        Dim ActivitePayeur As String = rwEntreprise.Item("NActiviteC")
							//
							//                        //'Créer le compte PAYEUR
							//                        Ajout_Compte(keepDossier, ComptePayeur, "CLIENT " & NomPayeur)
							//                        Ajout_Activite(keepDossier, ActivitePayeur, "ACTIVITE " & ActivitePayeur)
							//                        Ajout_PlanComptable(keepDossier, ComptePayeur, ActivitePayeur)
							//
							//                       // 'Créer la ligne
							//                        Ajout_Ligne_SansTVA(keepDossier, NumPiece, dtRemise, Ligne, "AVANCE " & NomPayeur, "", "48800000", "0000", ComptePayeur, ActivitePayeur, AvanceClient)
							//                        Ligne += 1
							//                    End If
							//
							//                   // 'Marquer le reglement comme exporté
							//                    rwReglement.Item("ExportCompta") = True
							//                    rwReglement.Item("DateExportCompta") = Now
							//                Next //'NEXT REGLEMENT
							//
						}
						//'ON SOLDE LA REMISE PAR LA BANQUE
						String libLigneTotalRemise  = "TOTAL REMISE ";
						ligneDao.ajout_Ligne_SansTVA(keepDossier, numPiece, datePiece, ligne, libLigneTotalRemise, p.getCodeDocument(), compteBanque, activiteBanque, "48800000", "0000", p.getMontantReglement());
						ligne += 1;
						
						listeMessageOk.add(messageDocumentCourantImporte);
					}

				} catch (Exception e) {
					// TODO: handle exception
					listeMessageError.add(messageDocumentCourantRejete);
					listeMessageError.add(System.getProperty("line.separator")+"L'importation des remises et règlements est abandonnée !!!");
					throw new Exception();
				}
			}

			if(listeMessageError.size()>0) {
				String message = "";
				for (String me : listeMessageError) {
					message+=me+System.getProperty("line.separator");
				}
				throw new Exception(message);
			}

			String rapport = "L'importation des remises et règlements s'est bien déroulée !"+System.getProperty("line.separator");
			if(listeMessageOk.size()>0) {
				for (String me : listeMessageOk) {
					rapport+=me+System.getProperty("line.separator");
				}
			}

			dossierDao.getSqlServer().getConnDossier().commit();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Importation des remises et règlements dans Agrigest ");
			alert.setHeaderText(null);
//			alert.setContentText("L'importation des remises et règlements s'est bien déroulée !");
			alert.setContentText(rapport);
			alert.showAndWait();
			return true;
			//			dossierDao.getSqlServer().connection().rollback();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				dossierDao.getSqlServer().getConnDossier().rollback();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Importation des remises et règlements dans Agrigest");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO: handle exception
		}

		return false;
	}




	public List<LigneDocFichier> getListePointage() {
		return listePointage;
	}




	public void setListePointage(List<LigneDocFichier> listePointage) {
		this.listePointage = listePointage;
	}


public String formatDate(Date date) {
	return  String.format("%1$td/%1$tm/%1$tY",date);
}

}

