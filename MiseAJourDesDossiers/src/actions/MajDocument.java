package actions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;


import fr.legrain.abonnement.dao.TaAbonnement;
import fr.legrain.abonnement.dao.TaAbonnementDAO;
import fr.legrain.articles.dao.TaArticle;
import fr.legrain.articles.dao.TaArticleDAO;
import fr.legrain.avisecheance.divers.Impression;
import fr.legrain.document.divers.IImpressionDocumentTiers;
import fr.legrain.documents.dao.TaApporteur;
import fr.legrain.documents.dao.TaApporteurDAO;
import fr.legrain.documents.dao.TaAvisEcheance;
import fr.legrain.documents.dao.TaAvisEcheanceDAO;
import fr.legrain.documents.dao.TaAvoir;
import fr.legrain.documents.dao.TaAvoirDAO;
import fr.legrain.documents.dao.TaBonliv;
import fr.legrain.documents.dao.TaBonlivDAO;
import fr.legrain.documents.dao.TaDevis;
import fr.legrain.documents.dao.TaDevisDAO;
import fr.legrain.documents.dao.TaFacture;
import fr.legrain.documents.dao.TaFactureDAO;
import fr.legrain.documents.dao.TaLFacture;
import fr.legrain.documents.dao.TaLFactureDAO;
import fr.legrain.documents.dao.TaRReglement;
import fr.legrain.dossier.dao.TaInfoEntreprise;
import fr.legrain.dossier.dao.TaInfoEntrepriseDAO;
import fr.legrain.edition.actions.BaseImpressionEdition;
import fr.legrain.edition.actions.ConstEdition;
import fr.legrain.gestCom.Module_Document.IDocumentTiers;
import fr.legrain.gestCom.Module_Document.IHMEnteteAvisEcheance;
import fr.legrain.licenceBdg.dao.TaLicenceBdg;
import fr.legrain.licenceEpicea.dao.TaLicenceEpicea;
import fr.legrain.licenceEpicea.dao.TaLicenceEpiceaDAO;
import fr.legrain.licenceSara.dao.TaLicenceSara;
import fr.legrain.pointLgr.dao.TaComptePointDAO;
import fr.legrain.tiers.dao.TaFamilleTiers;
import fr.legrain.tiers.dao.TaFamilleTiersDAO;
import fr.legrain.tiers.dao.TaTiers;
import fr.legrain.tiers.dao.TaTiersDAO;

public class MajDocument implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		MettreAJourLesTotauxDocuments(action);
	}
	
	public void MettreAJourLesPointsBonusUtilises(IAction action) {
		try {
			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"ATTENTION CALCUL POINTS BONUS UTILISES !!!", "Voulez-vous vraiment mettre à jour les points bonus utilisés dans l'année ?"
							+"\r\n"+"Cette procédure peut prendre du temps.")){
				TaInfoEntrepriseDAO daoInfoEntreprise = new TaInfoEntrepriseDAO();
				TaInfoEntreprise infosEntreprise=daoInfoEntreprise.findInstance();
				List<TaAbonnement> liste=null;
				TaComptePointDAO daoComptePoint=new TaComptePointDAO();
				TaTiersDAO daoTiers = new TaTiersDAO();
				List<TaTiers>listeTiers =daoTiers.selectAll();
				for (TaTiers taTiers : listeTiers) {
					daoComptePoint.calculPointUtilise(taTiers.getIdTiers(), infosEntreprise.getDatedebInfoEntreprise());
				}

			}			

		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void MettreAJourLesPointsBonusAnnee(IAction action) {
		try {
			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"ATTENTION CALCUL POINTS BONUS !!!", "Voulez-vous vraiment mettre à jour les points bonus des abonnements de l'année ?"
							+"\r\n"+"Cette procédure peut prendre du temps.")){
				List<TaAbonnement> liste=null;
				TaComptePointDAO daoComptePoint=new TaComptePointDAO();
				TaAbonnementDAO daoAbonnement = new TaAbonnementDAO(daoComptePoint.getEntityManager());
				TaInfoEntrepriseDAO daoInfoEntreprise = new TaInfoEntrepriseDAO();
				TaInfoEntreprise infosEntreprise=daoInfoEntreprise.findInstance();
				liste=daoAbonnement.findAbonnementBetweenDateDebDateFin(infosEntreprise.getDatedebInfoEntreprise(),infosEntreprise.getDatefinInfoEntreprise(),"2517");
				for (TaAbonnement taAbonnement2 : liste) {
					daoComptePoint.calculPointTotalAnnee(taAbonnement2,true);
				}

			}		

		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void MettreAJourLesTotauxDocuments(IAction action) {
		try {
			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"ATTENTION", "Voulez-vous vraiment mettre à jour les totaux des documents ?"
							+"\r\n"+"Cette procédure peut prendre du temps.")){
				//gestion des bonliv
				TaBonlivDAO daoBonliv = new TaBonlivDAO();
				daoBonliv.begin(daoBonliv.getEntityManager().getTransaction());
				List<TaBonliv> lBonliv = daoBonliv.selectAll();
				for (TaBonliv taDocument : lBonliv) {
					taDocument.calculeTvaEtTotaux();
					daoBonliv.merge(taDocument);
				}
				daoBonliv.commit(daoBonliv.getEntityManager().getTransaction());
				//gestion des devis
				TaDevisDAO daoDevis = new TaDevisDAO();
				daoDevis.begin(daoDevis.getEntityManager().getTransaction());
				List<TaDevis> lDevis = daoDevis.selectAll();
				for (TaDevis taDocument : lDevis) {
					taDocument.calculeTvaEtTotaux();
					daoDevis.merge(taDocument);
				}
				daoDevis.commit(daoDevis.getEntityManager().getTransaction());


				//gestion des factures
				TaFactureDAO daoFacture = new TaFactureDAO();
				daoFacture.begin(daoFacture.getEntityManager().getTransaction());
				List<TaFacture> lFacture = daoFacture.selectAll();
				//List<TaFacture> lFacture  =daoFacture.rechercheDocument("0400117", "0400117");			
				for (TaFacture taDocument : lFacture) {
					taDocument.calculeTvaEtTotaux();
					daoFacture.merge(taDocument);
				}


				daoFacture.commit(daoFacture.getEntityManager().getTransaction());

				//gestion des factures
				TaAvoirDAO daoAvoir = new TaAvoirDAO();
				daoAvoir.begin(daoAvoir.getEntityManager().getTransaction());
				List<TaAvoir> lAvoir = daoAvoir.selectAll();
				for (TaAvoir taDocument : lAvoir) {
					taDocument.calculeTvaEtTotaux();
					daoAvoir.merge(taDocument);
				}
				daoAvoir.commit(daoAvoir.getEntityManager().getTransaction());

				//gestion des factures
				TaApporteurDAO daoApporteur = new TaApporteurDAO();
				daoApporteur.begin(daoApporteur.getEntityManager().getTransaction());
				List<TaApporteur> lApporteur = daoApporteur.selectAll();
				for (TaApporteur taDocument : lApporteur) {
					taDocument.calculeTvaEtTotaux();
					daoApporteur.merge(taDocument);
				}
				daoApporteur.commit(daoApporteur.getEntityManager().getTransaction());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void ImprimerAvisEcheance(IAction action) {
		try {
			
			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(),
					"ATTENTION EDITION DES AVIS D'ECHEANCE !!!", "Voulez-vous vraiment éditer les avis d'échéance ?"
							+"\r\n"+"Cette procédure peut prendre du temps.")){
				TaInfoEntrepriseDAO daoInfoEntreprise = new TaInfoEntrepriseDAO();
				TaInfoEntreprise infosEntreprise=daoInfoEntreprise.findInstance();
				TaAvisEcheanceDAO daoAvisEcheance = new TaAvisEcheanceDAO();
				TaFamilleTiersDAO daoFamilleTiers = new TaFamilleTiersDAO();
				TaFamilleTiers groupe = daoFamilleTiers.rechercheFamilleCogere();
				List<TaAvisEcheance> liste;
//				//telechargement et groupe cogere
//				liste=daoAvisEcheance.rechercheAvisTelechargement(infosEntreprise.getDatedebInfoEntreprise(),
//						infosEntreprise.getDatefinInfoEntreprise(),true,groupe.getCodeFamille());
//
//				imprimerAvis(liste);
				
				//telechargement et groupe Autres
				
//				liste=daoAvisEcheance.rechercheAvisTelechargement(infosEntreprise.getDatedebInfoEntreprise(),
//						infosEntreprise.getDatefinInfoEntreprise(),false,"autres");

//				imprimerAvis(liste);

				
				//Non telechargeable et groupe cogere
				liste=daoAvisEcheance.rechercheAvisTelechargement(infosEntreprise.getDatedebInfoEntreprise(),
						infosEntreprise.getDatefinInfoEntreprise(),false,groupe.getCodeFamille());
				
				imprimerAvis(liste);
//				
//				//Non telechargeable et groupe Autres
//				liste=daoAvisEcheance.rechercheAvisTelechargement(infosEntreprise.getDatedebInfoEntreprise(),
//						infosEntreprise.getDatefinInfoEntreprise(),false,"autres");
////				
//				imprimerAvis(liste);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	protected void imprimerAvis(List<TaAvisEcheance> liste) throws Exception{
		List<TaAvisEcheance>listeTemp1 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp2 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp3 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp4 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp5 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp6 =new LinkedList<TaAvisEcheance>();
		List<TaAvisEcheance>listeTemp7 =new LinkedList<TaAvisEcheance>();
		int i=1;
		for (TaAvisEcheance taAvisEcheance : liste) {
			if(i<=110)listeTemp1.add(taAvisEcheance);
			if(i<=200 && i>110)listeTemp2.add(taAvisEcheance);
			if(i<=300 && i>200)listeTemp3.add(taAvisEcheance);
			if(i<=400 && i>300)listeTemp4.add(taAvisEcheance);
			if(i<=500 && i>400)listeTemp5.add(taAvisEcheance);
			if(i<=600 && i>500)listeTemp6.add(taAvisEcheance);
			if(i<=700 && i>600)listeTemp7.add(taAvisEcheance);
			i++;
		}		
		actImprimer(listeTemp1);
//		actImprimer(listeTemp2);
//		actImprimer(listeTemp3);
//		actImprimer(listeTemp4);
//		actImprimer(listeTemp5);
//		actImprimer(listeTemp6);
//		actImprimer(listeTemp7);
	}
	protected void actImprimer(List<TaAvisEcheance> listDocumentAImprimer) throws Exception {
		//Récupération des paramètres dans l'ihm
				//String[] idFactureAImprimer = null;
					TaAvisEcheanceDAO dao = new TaAvisEcheanceDAO();
				final boolean preview = true;
				final boolean printDirect = false;
				TaAvisEcheance avis;
				//listDocumentAImprimer=new LinkedList<IDocumentTiers>();
				Impression impressionDocument = new fr.legrain.avisecheance.divers.Impression();
				final BaseImpressionEdition impressionEdition = new BaseImpressionEdition(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),dao.getEntityManager());
//				for (IHMEnteteAvisEcheance obj : modelLDocument) {
//					if(obj.getAccepte()){
//					avis=taAvisEcheanceDAO.findById(obj.getIdDocument());
//					listDocumentAImprimer.add(avis);
//					}
//				}

				
				if(listDocumentAImprimer==null ||listDocumentAImprimer.size()==0){
					MessageDialog.openWarning(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(),
							"ATTENTION", "Aucun document valide n'a été sélectionné !!!");
					throw new Exception("pas de factures à imprimer");
				}

				final List<IDocumentTiers> finalListDocumentAImprimer = new LinkedList<IDocumentTiers>();
				for (TaAvisEcheance taAvisEcheance : listDocumentAImprimer) {
					finalListDocumentAImprimer.add(taAvisEcheance);
				}
				final boolean finalPreview = preview;
				String fichierEdition = null;
				String nomOnglet = null;
				String nomEntity = null;
				String typeTraite = null;
				final LinkedList<TaRReglement> listeTraite=new LinkedList<TaRReglement>();
				listeTraite.clear();
				/** 01/03/2010 modifier les editions (zhaolin) **/
				IPreferenceStore preferenceStore = null;
				String namePlugin = null;
				/************************************************/
//					impressionDocument = new Impression(vue.getShell());
					fichierEdition = ConstEdition.FICHE_FILE_REPORT_AVIS_ECHEANCE;
					nomOnglet = "Avis d'échéance";
					nomEntity = TaAvisEcheance.class.getSimpleName();
					

					
				preferenceStore=impressionDocument.getPreferenceStore();
				namePlugin = impressionDocument.getPluginName();
				
				final String finalFichierEdition = fichierEdition;
				final String finalNomOnglet = nomOnglet;
				final String finalNomEntity = nomEntity;
				final String finalTypeTraite = typeTraite;
				
				/** 01/03/2010 modifier les editions (zhaolin) **/
				final String finalNamePlugin = namePlugin;
				final IPreferenceStore finalPreferenceStore = preferenceStore;
				final ConstEdition constEdition = new ConstEdition(null);

				PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell().getDisplay().asyncExec(new Thread() {
					public void run() {
						try {	
							impressionEdition.setConstEdition(constEdition);
							impressionEdition.setCollection(finalListDocumentAImprimer);
							impressionEdition.setTypeTraite(finalTypeTraite);
							impressionEdition.impressionEdition(finalPreferenceStore, finalNomEntity,/*true,*/ 
							        null, finalNamePlugin, finalFichierEdition, true, 
							        false, true, true , true,false,"");
							
							

						} catch (Exception e) {
							//logger.error("Erreur à l'impression ",e);
						} finally {
						}
					}
				});

			}

	public class Version{
		String libVersion="";
		 int numVersion=0;
		 
		 public Version(String libVersion, int numVersion) {
			super();
			this.libVersion = libVersion;
			this.numVersion = numVersion;
		}

		}
	
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}
	
	public void MettreAJourLesComptesInternet(IAction action) {
//		try {
//			if(MessageDialog.openConfirm(PlatformUI.getWorkbench()
//					.getActiveWorkbenchWindow().getShell(),
//					"ATTENTION MAJ COMPTES LEGRAIN.FR !!!", "Voulez-vous vraiment mettre à jour les comptes 'legrain.fr' en ce qui concerne" +
//							" les dates de maintenances  ?"
//							+"\r\n"+"Cette procédure peut prendre du temps.")){
//
//						try {      
//							
//							StringBuffer ligne = new StringBuffer("");
//							
//							FileWriter fw = new FileWriter(fichierExportation);
//							BufferedWriter bw = new BufferedWriter(fw);
//				CallableStatement cs;
//				//			cs = ibTaFacture.getFIBBase().getJdbcConnection().prepareCall("{?,?,?,?,?,?,?,?,?,?,?,? = call CALCUL_TOTAL_DIRECT(?)}");
//				//			cs.registerOutParameter(1,Types.DOUBLE);
//				cs.execute();
//				
//				ligne.append(completeChaine(String.valueOf(numPieceExport),5,c,0));
//				fw.write(ligne.toString());
//				bw.close();
//							bw.close();
//						} catch(IOException ioe){
//							logger.error("",ioe);
//						} catch (SQLException e) {
//							logger.error("",e);
//						} catch (Exception e) {
//							logger.error("",e);
//						}
//					}		
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}

}
