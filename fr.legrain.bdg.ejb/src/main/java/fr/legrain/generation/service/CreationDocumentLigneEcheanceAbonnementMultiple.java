package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocLigneEcheanceAbonnementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersBonlivServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersfactureServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class CreationDocumentLigneEcheanceAbonnementMultiple implements java.io.Serializable {


	static Logger logger = Logger.getLogger(CreationDocumentMultiple.class.getName());
	private static final long serialVersionUID = -2836730580231914189L;

	private String nouveauLibelle="";
//	private List<TaAbonnement> listeDocument = null;
	private List<TaLEcheance> listeEcheances = null;
	private String selectedType="";
	private Date dateDocument = null;
	private Date dateLivDocument = null;
	private IDocumentTiers documentSrc = null;
	private ParamAfficheChoixGenerationDocument param;
	private String codeTiers=null;
//	private Map<String,Object> listeGestionnaireExtension = new LinkedHashMap<String,Object>();
	private boolean relation=false;
	private boolean changeTiers=false;
	private boolean generationModele=false;
	private boolean generationLigne=false;
	private boolean conserveNbDecimalesDoc=false;
	private String idDocumentExterne=null;
	private String codeServiceWeb = "";
	private TaTPaiement taTPaiement;
	
	private boolean regleDoc = false;
	private IAbstractGenereDocLigneEcheanceAbonnementServiceRemote genereDocument;
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	
	private @EJB IGenereDocLigneEcheanceAbonnementVersfactureServiceRemote genereDocLigneEcheanceAbonnementVersFactureService;
	private @EJB IGenereDocLigneEcheanceAbonnementVersAvisEcheanceServiceRemote genereDocLigneEcheanceAbonnementVersAvisEcheanceService;
	private @EJB IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote genereDocLigneEcheanceAbonnementVersPanierService;
	
	private @EJB IGenereDocLigneEcheanceAbonnementVersProformaServiceRemote genereDocLigneEcheanceAbonnementVersProformaService;	
	private @EJB IGenereDocLigneEcheanceAbonnementVersBonlivServiceRemote genereDocLigneEcheanceAbonnementVersBonlivService;
	private @EJB IGenereDocLigneEcheanceAbonnementVersDevisServiceRemote genereDocLigneEcheanceAbonnementVersDevisService;
	

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTCPaiementServiceRemote taTCPaiementService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;	
	private @EJB ITaFactureServiceRemote taFactureService;	
	private @EJB ITaTAdrServiceRemote taTAdrService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaRDocumentServiceRemote taRDocumentService;
	private @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
	
	@EJB private ITaApporteurServiceRemote taApporteurService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaAcompteServiceRemote taAcompteService;
	@EJB private ITaProformaServiceRemote taProformaService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaPrelevementServiceRemote taPrelevementService;	
	
	@EJB private ITaBonReceptionServiceRemote taBonReceptionService;
	@EJB private ITaFabricationServiceRemote taFabricationService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaPreparationServiceRemote taPreparationService;
	
	@EJB private ITaPanierServiceRemote taPanierService;
	

	public CreationDocumentLigneEcheanceAbonnementMultiple() {
		// TODO Auto-generated constructor stub
	}
	

	public RetourGenerationDoc creationDocument(boolean controleExistance) {
		RetourGenerationDoc retour=new RetourGenerationDoc();

		try {
	
			IDocumentTiers docGenere = null;
			final TypeDoc typeDocPresent = TypeDoc.getInstance();
//			String idEditor ="";
			String typeCPaiement="";
			TaCPaiement taCPaiement1=null;
			TaCPaiement taCPaiement2=null;
			TaTiers tiers = null;
			String commentaireDefaut="";
			String libelleDocument="";
			String libelleComplementaire="";

			if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_AVOIR;
				docGenere = new TaAvoir();
				libelleDocument="Avoir N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_DEVIS)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_DEVIS;
				docGenere = new TaDevis();
				libelleDocument="Devis N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_FACTURE;
				docGenere = new TaFacture();
				libelleDocument="Facture N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_PROFORMA)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PROFORMA;
				docGenere = new TaProforma();
				libelleDocument="Proforma N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_APPORTEUR)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_APORTEUR;
				docGenere = new TaApporteur();
				libelleDocument="Apporteur N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_COMMANDE_ACHAT;
				docGenere = new TaBoncdeAchat();
				libelleDocument="Bon cde N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_RECEPTION)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_RECEPTION;
				docGenere = new TaBonReception();
				libelleDocument="Bon réception N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_COMMANDE;
				docGenere = new TaBoncde();
				libelleDocument="Bon cde N°: ";
			} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_BON_LIVRAISON;
				docGenere = new TaBonliv();
				libelleDocument="Bon liv. N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_ACOMPTE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_ACOMPTE;
				docGenere = new TaAcompte();
				libelleDocument="Acompte N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_PRELEVEMENT)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PRELEVEMENT;
				docGenere = new TaPrelevement();
				libelleDocument="Prélèvement N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_AVIS_ECHEANCE;
				docGenere = new TaAvisEcheance();
				libelleDocument="Avis N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE;
				docGenere = new TaTicketDeCaisse();
				libelleDocument="Ticket N°: ";
			}else if(selectedType.equals(TypeDoc.TYPE_PANIER)) {
				typeCPaiement=TaTCPaiement.C_CODE_TYPE_PANIER;
				//docGenere = new TaPanier();
				//rajout yann car on n'est pas sensé créer un nouveau panier ici, on doit juste récup le panier existant
				docGenere = taPanierService.findByActif(codeTiers);
				if(docGenere == null) {
					docGenere = new TaPanier();
				}
				libelleDocument="Panier N°: ";
			}

			if(conserveNbDecimalesDoc==false) {
				TaPreferences nb;
				nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
				if(nb!=null && nb.getValeur() != null) {
					docGenere.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
				}

			}
			else {
				docGenere.setNbDecimalesQte(3);
			}
			commentaireDefaut=taPreferencesService.retourPreferenceString(selectedType.toLowerCase(), ITaPreferencesServiceRemote.P_COMMENTAIRE);
			if(commentaireDefaut==null)commentaireDefaut="";
	
			TaTCPaiement tcp=taTCPaiementService.findByCode(typeCPaiement);
			if(tcp!=null){
				List<TaCPaiement> liste=taCPaiementService.rechercheParType(tcp.getCodeTCPaiement());
				if(liste!=null && !liste.isEmpty()) {
				taCPaiement1=liste.get(0);
				taCPaiement2=liste.get(0);
				}
			}
			
			
			
			Boolean continuer=!controleExistance;

			if(listeEcheances.size()>0){
				//genereDocument est null ici
				genereDocument = create(listeEcheances.get(0), docGenere);
			}
			genereDocument.setRegleDoc(regleDoc);
			genereDocument.setCodeServiceWeb(codeServiceWeb);
			
			genereDocument.setRecupLibelleSeparateurDoc(!getParam().getRepriseAucun());
			genereDocument.setLigneSeparatrice(genereDocument.getRecupLibelleSeparateurDoc());
			int nbListeDoc=0;
			boolean genererCode=false;
			if(controleExistance)controleExistance=!getParam().isGenerationLigne();
			
			
			//rajout yann
			tiers = taTiersService.findByCode(codeTiers);
			docGenere.setTaTiers(tiers);
			docGenere = genereDocument.genereDocument(listeEcheances,docGenere,codeTiers,false,generationModele,genererCode,param.isMultiple());
			if(!LibChaine.empty(nouveauLibelle)) {
				docGenere.setLibelleDocument(nouveauLibelle);
			} else {
				
				docGenere.setLibelleDocument(libelleDocument+" "+docGenere.getCodeDocument());
			}
			
			//rajout yann 11/05/21 parce que sinon on avait un plantage quand on merge le doc parce que le reglement n'avait pas de libelleDocument
			if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
				if(((TaFacture)docGenere).getTaRReglements() != null && !((TaFacture) docGenere).getTaRReglements().isEmpty()) {
					for (TaRReglement rreglement : ((TaFacture)docGenere).getTaRReglements()) {
						rreglement.getTaReglement().setLibelleDocument(docGenere.getLibelleDocument());
					}
				}
			}

		
			
			if(continuer){
				if(dateDocument!=null){
					docGenere.setDateDocument(dateDocument);
				}
				if(dateLivDocument!=null){
					docGenere.setDateLivDocument(dateLivDocument);
				}
				TaPreferences gestionLot= taPreferencesService.findByCode("gestion_des_lots");
				docGenere.setGestionLot(LibConversion.StringToBoolean(gestionLot.getValeur()));
				

				
				if(generationModele) {
					//général
					docGenere.setDateLivDocument(dateDocument);

					if(selectedType.equals(TypeDoc.TYPE_AVOIR)) {
						if(((TaAvoir)docGenere).getDateEchDocument()==null)
							((TaAvoir)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_DEVIS)) {
						if(((TaDevis)docGenere).getDateEchDocument()==null)
							((TaDevis)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
						if(((TaFacture)docGenere).getDateEchDocument()==null)
							((TaFacture)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_PROFORMA)) {
						if(((TaProforma)docGenere).getDateEchDocument()==null)
							((TaProforma)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_APPORTEUR)) {
						if(((TaApporteur)docGenere).getDateEchDocument()==null)
							((TaApporteur)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE)) {
						if(((TaBoncde)docGenere).getDateEchDocument()==null)
							((TaBoncde)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
						if(((TaBoncdeAchat)docGenere).getDateEchDocument()==null)
							((TaBoncdeAchat)docGenere).setDateEchDocument(dateDocument);
					} else if(selectedType.equals(TypeDoc.TYPE_BON_LIVRAISON)) {
					}else if(selectedType.equals(TypeDoc.TYPE_ACOMPTE)) {
						if(((TaAcompte)docGenere).getDateEchDocument()==null)
							((TaAcompte)docGenere).setDateEchDocument(dateDocument);
					}else if(selectedType.equals(TypeDoc.TYPE_PRELEVEMENT)) {
						if(((TaPrelevement)docGenere).getDateEchDocument()==null)
							((TaPrelevement)docGenere).setDateEchDocument(dateDocument);
					}else if(selectedType.equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
						if(((TaAvisEcheance)docGenere).getDateEchDocument()==null)
							((TaAvisEcheance)docGenere).setDateEchDocument(dateDocument);
					}else if(selectedType.equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
						if(((TaTicketDeCaisse)docGenere).getDateEchDocument()==null)
							((TaTicketDeCaisse)docGenere).setDateEchDocument(dateDocument);
					}else if(selectedType.equals(TypeDoc.TYPE_PANIER)) {
						if(((TaPanier)docGenere).getDateEchDocument()==null)
							((TaPanier)docGenere).setDateEchDocument(dateDocument);
					}						
						
					
				}
				
				if(docGenere.getCommentaire() == null || docGenere.getCommentaire().trim().equals("") )
					docGenere.setCommentaire(commentaireDefaut);
				docGenere.setDateExport(null);
				
			
				//genereDocLigneServiceVersFactureService.creationMouvement()
				docGenere = genereDocument.enregistreDocument(docGenere);
				
				
				
				 
				
				

				retour.setRetour(true);
				retour.setDoc(docGenere);
				retour.setIdDoc(docGenere.getIdDocument());
				retour.setMessage("Le document '"+docGenere.getCodeDocument()+"' à été généré correctement.");
			}
//		}
			
		} catch (Exception e) {
			logger.error("", e);
			retour=null;
		}
			return retour;

	}

	
	public String getNouveauLibelle() {
		return nouveauLibelle;
	}


	public void setNouveauLibelle(String nouveauLibelle) {
		this.nouveauLibelle = nouveauLibelle;
	}



	public String getSelectedType() {
		return selectedType;
	}


	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}


	public Date getDateDocument() {
		return dateDocument;
	}


	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}


	public Date getDateLivDocument() {
		return dateLivDocument;
	}


	public void setDateLivDocument(Date dateLivDocument) {
		this.dateLivDocument = dateLivDocument;
	}



	public IDocumentTiers getDocumentSrc() {
		return documentSrc;
	}

	public void setDocumentSrc(IDocumentTiers documentSrc) {
		this.documentSrc = documentSrc;
	}

	public ParamAfficheChoixGenerationDocument getParam() {
		return param;
	}

	public void setParam(ParamAfficheChoixGenerationDocument param) {
		//typeSrc = ((ParamAfficheChoixGenerationDocument)param).getTypeSrc();
		selectedType =((ParamAfficheChoixGenerationDocument)param).getTypeDest();
		documentSrc = ((ParamAfficheChoixGenerationDocument)param).getDocumentSrc();
		listeEcheances=((ParamAfficheChoixGenerationDocument)param).getListeLigneEcheanceSrc();
//		listeLigneDocument=((ParamAfficheChoixGenerationDocument)param).getListeLigneDocumentSrc();
		setNouveauLibelle(((ParamAfficheChoixGenerationDocument)param).getLibelle());
		dateDocument=((ParamAfficheChoixGenerationDocument)param).getDateDocument();
		dateLivDocument=((ParamAfficheChoixGenerationDocument)param).getDateLivraison();
		if(((ParamAfficheChoixGenerationDocument)param).isTiersModifiable())
			codeTiers=((ParamAfficheChoixGenerationDocument)param).getCodeTiers();
		relation=((ParamAfficheChoixGenerationDocument)param).isRelation();
		generationModele=((ParamAfficheChoixGenerationDocument)param).isGenerationModele();
		generationLigne=((ParamAfficheChoixGenerationDocument)param).isGenerationLigne();
		this.param=param;
		conserveNbDecimalesDoc=((ParamAfficheChoixGenerationDocument)param).isConserveNbDecimalesDoc();
			
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	

	public boolean isGenerationModele() {
		return generationModele;
	}

	public void setGenerationModele(boolean generationModele) {
		this.generationModele = generationModele;
	}


		public IAbstractGenereDocLigneEcheanceAbonnementServiceRemote create(TaLEcheance src, IDocumentTiers dest) {
			IAbstractGenereDocLigneEcheanceAbonnementServiceRemote genereDoc = null;	
			if(dest instanceof TaDevis) {
				genereDoc = genereDocLigneEcheanceAbonnementVersDevisService;
			} else if(dest instanceof TaBonliv) {
				genereDoc = genereDocLigneEcheanceAbonnementVersBonlivService;
			} else if(dest instanceof TaProforma) {
				genereDoc = genereDocLigneEcheanceAbonnementVersProformaService;
			}else if(dest instanceof TaFacture) {
				genereDoc = genereDocLigneEcheanceAbonnementVersFactureService;
			}else if(dest instanceof TaAvisEcheance) {
				genereDoc = genereDocLigneEcheanceAbonnementVersAvisEcheanceService;
			}else if(dest instanceof TaPanier) {
				genereDoc = genereDocLigneEcheanceAbonnementVersPanierService;
			}

			//			genereDoc.setRelationDocument(src.getRelationDocument());
			return genereDoc;
		}



	public boolean isGenerationLigne() {
		return generationLigne;
	}



	public void setGenerationLigne(boolean generationLigne) {
		this.generationLigne = generationLigne;
	}
	


	public boolean isRegleDoc() {
		return regleDoc;
	}



	public void setRegleDoc(boolean regleDoc) {
		this.regleDoc = regleDoc;
	}



	public String getCodeServiceWeb() {
		return codeServiceWeb;
	}



	public void setCodeServiceWeb(String codeServiceWeb) {
		this.codeServiceWeb = codeServiceWeb;
	}



	public String getIdDocumentExterne() {
		return idDocumentExterne;
	}



	public void setIdDocumentExterne(String idDocumentExterne) {
		this.idDocumentExterne = idDocumentExterne;
	}



	public List<TaLEcheance> getListeEcheances() {
		return listeEcheances;
	}



	public void setListeEcheances(List<TaLEcheance> listeEcheances) {
		this.listeEcheances = listeEcheances;
	}
	
}
