package fr.legrain.bdg.webapp.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
//import fr.legrain.silentPdfPrint.LgrSpooler;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class ImprimerDocumentController extends AbstractController{

	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	private @Inject DocFusionAImprimer docFusionAImprimer;
	private @Inject TenantInfo tenantInfo;


	private final String C_MESS_SELECTION_VIDE = "Aucun document n'a été sélectionné !!!";
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private IDocumentTiers detailLigne;
	private TaTiers detailLigneTiers;
	private Date dateDeb=null;
	private Date dateFin=null;

	private String libelle="";
	private TaTiersDTO taTiersDTO;
	private TaFactureDTO taFactureDTO;
	private TaAvisEcheanceDTO taAvisEcheanceDTO;
	private DocumentDTO documentDebDTO = null;
	private DocumentDTO documentFinDTO = null;
	private IDocumentTiers document = null;
	private String codeDocumentDeb="";
	private String codeDocumentFin="";
	private String codeTiers="";

	@EJB private ITaTiersServiceRemote daoTiers;
	@EJB private ITaFactureServiceRemote daoFacture;
	@EJB private ITaAvoirServiceRemote daoAvoir;
	@EJB private ITaBonlivServiceRemote daoBonLiv;
	@EJB private ITaBoncdeServiceRemote daoBoncde;
	@EJB private ITaApporteurServiceRemote daoApporteur;
	@EJB private ITaProformaServiceRemote daoProforma;
	@EJB private ITaDevisServiceRemote daoDevis;
	@EJB private ITaPrelevementServiceRemote daoPrelevement;
	@EJB private ITaAcompteServiceRemote daoAcompte;
	@EJB private ITaTicketDeCaisseServiceRemote daoTicketDeCaisse;
	@EJB private ITaAvisEcheanceServiceRemote daoAvisEcheance;
	
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaInfoEntrepriseServiceRemote daoInfos;
	@EJB private ITaActionEditionServiceRemote taActionEditionService;

	
	TaInfoEntreprise infos =null;
    
	private List<DocumentDTO> listeDocumentDTO;
	private List<DocumentDTO> selectedDocument;
	private TaTPaiement taTPaiement=null;

	private List<ImageLgr> listeTypeDocSelection=new LinkedList<ImageLgr>();
	private ImageLgr selectedTypeSelection ;
	private ImageLgr oldSelectedTypeSelection=new ImageLgr() ;

	private String pathImageSelectionDocSelection=TypeDoc.PATH_IMAGE_ROUE_DENTEE;



	@PostConstruct
	public void init() {

		infos=daoInfos.findInstance();
		
		dateDeb=infos.getDatedebInfoEntreprise();
		dateFin=infos.getDatefinInfoEntreprise();


		for (String type : autorisation.getTypeDocCompletPresent().values()) {
//			if(!type.equals(TypeDoc.TYPE_REGLEMENT)
//					&& !type.equals(TypeDoc.TYPE_REMISE)
//					&& !type.equals(TypeDoc.TYPE_ACOMPTE)
//				){				
//				listeTypeDocSelection.add(TypeDoc.getInstance().getPathImageCouleurDoc().get(type));
//			}
			if(type.equals(TypeDoc.TYPE_FACTURE)
					//|| type.equals(TypeDoc.TYPE_BON_LIVRAISON)
					|| type.equals(TypeDoc.TYPE_AVIS_ECHEANCE)
					//TODO implémenter les autres documents
				){				
				listeTypeDocSelection.add(TypeDoc.getInstance().getPathImageCouleurDoc().get(type));
			}
		}

		int rang=0;
		rang=(listeTypeDocSelection.indexOf(TypeDoc.getInstance().getPathImageCouleurDoc().get(TypeDoc.TYPE_BON_LIVRAISON)));
		if(rang==-1)rang=0;
		selectedTypeSelection=listeTypeDocSelection.get(rang);
	}


	public void actFermer(ActionEvent e) {

	}
	public void actAnnuler(ActionEvent e) {

	}
	
	public void actChangeDocSelection() {
//		resetTous();
		if(selectedTypeSelection!=null){
			pathImageSelectionDocSelection=selectedTypeSelection.getDisplayName();
		}
//		selectionComboSelection(null);
	}

	public void actImprimerSelection(ActionEvent actionEvent) {

		List<String> l = new LinkedList<>();
		try {
			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			
			switch (selectedTypeSelection.getName()) {
			case TypeDoc.TYPE_FACTURE:
				TaFacture t = null;
				for (DocumentDTO documentDTO : selectedDocument) {
					
					String pdfPath = taFactureService.generePDF(documentDTO.getId(), new HashMap<String,Object>(), null, null,taActionEdition);
					l.add(pdfPath);
					
//					if(editionClient || impressionDirectClient) {
						//Mise à jour de la mise à dispostion
					t = taFactureService.findByIDFetch(documentDTO.getId());
						if(t.getTaMiseADisposition()==null) {
							t.setTaMiseADisposition(new TaMiseADisposition());
						}
						Date dateMiseADispositionClient = new Date();
						t.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
						t = taFactureService.merge(t,ITaFactureServiceRemote.validationContext);
//					}
				}
				break;
			case TypeDoc.TYPE_AVIS_ECHEANCE:
				for (DocumentDTO documentDTO : selectedDocument) {
					TaAvisEcheance ae = daoAvisEcheance.findByIDFetch(documentDTO.getId());
					//String pdfPath = daoAvisEcheance.generePDF(t.getIdDocument(), new HashMap<String,Object>(), null, null,taActionEdition);
					String pdfPath = daoAvisEcheance.generePDF(ae.getIdDocument(), null, null, null);
					l.add(pdfPath);
				}

			default:
				break;
			}
			
			BdgProperties bdgProperties = new BdgProperties();
//			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId()/*sr.resolveCurrentTenantIdentifier()*/)+"/"+bdgProperties.tmpFileName("liste.pdf");
			docFusionAImprimer.setFichierAImprimer(localPath);
			
			fusionPdf(l,localPath);
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void fusionPdf(List<String> l, String localPath) {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			PdfCopy writer = new PdfCopy(document, baos);
			document.open();
//			if (requ.getParameter("preview") == null)
//				writer.addJavaScript("this.print(false);", false);
//			LgrSpooler spool = LgrSpooler.getInstance();
			for (Object fichier : /*spool*/ l) {
				if (fichier instanceof String) {
					PdfReader reader = new PdfReader((String) fichier);
					PdfImportedPage page = null;
					for (int i = 1; i <= reader.getNumberOfPages(); i++) {
						// writer.getImportedPage(reader,i);
						page = writer.getImportedPage(reader, i);
						writer.addPage(page);
					}
				} else if (fichier instanceof OutputStream) {
					PdfReader reader = new PdfReader(
							new ByteArrayInputStream(
									((ByteArrayOutputStream) fichier)
											.toByteArray()));
					PdfImportedPage page = null;
					for (int i = 1; i <= reader.getNumberOfPages(); i++) {
						// writer.getImportedPage(reader,i);
						page = writer.getImportedPage(reader, i);
						writer.addPage(page);
					}
				}
			}
			document.close();
			
//			resp.setContentType("application/pdf");
//
//			resp.setContentLength(baos.size());
//			baos.writeTo(out);
			try(OutputStream outputStream = new FileOutputStream(localPath)) {
					baos.writeTo(outputStream);
			}
//			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrer(ActionEvent e) {
		FacesContext context = FacesContext.getCurrentInstance();
	}

	public void actRefresh(ActionEvent e) {
		actRefresh();
	}

	public void actRefresh() {
		try{
//			codeTiers="%";
//			codeDocumentDeb="%";
			codeDocumentDeb = null;
			codeDocumentFin = null;
			if(taTiersDTO!=null) {
				codeTiers=taTiersDTO.getCodeTiers();
			}
			if(documentDebDTO!=null){
				codeDocumentDeb=documentDebDTO.getCodeDocument();
				codeTiers="";
			}
			if(documentFinDTO!=null){
				codeDocumentFin=documentFinDTO.getCodeDocument();
				codeTiers="";
			}
			
			IDashboardDocumentServiceRemote service = null;
			
			switch (selectedTypeSelection.getName()) {
			case TypeDoc.TYPE_FACTURE:
				//afficher toutes factures non réglées pour le tiers sélectionné ou tous les tiers
//				listeDocumentDTO= taFactureService.findAllDTOPeriode(dateDeb,dateFin,null);
				service = taFactureService;
				break;
			case TypeDoc.TYPE_AVIS_ECHEANCE:
				//afficher toutes factures non réglées pour le tiers sélectionné ou tous les tiers
//				listeDocumentDTO= daoAvisEcheance.findAllDTOPeriode(dateDeb,dateFin,null);
				service = daoAvisEcheance;
				break;

			default:
				break;
			}
			
			if(codeDocumentDeb!=null) {//1 code => cette facture
//				logger.debug("Exportation - selection par code");
				if(codeDocumentFin!=null) {//2 codes => factures entre les 2 codes si intervalle correct
					listeDocumentDTO = service.findAllDTOIntervalle(codeDocumentDeb,codeDocumentFin,null);
				} else {
					codeDocumentFin = codeDocumentDeb;
					listeDocumentDTO = service.findAllDTOIntervalle(codeDocumentDeb,codeDocumentFin,null);
				}
			} else if(dateDeb!=null) {//1 date => toutes les factures à cette date
//				logger.debug("Exportation - selection par date");
				if(dateFin!=null) {//2 dates => factures entre les 2 dates si intervalle correct
					if(dateDeb.compareTo(dateFin)>0) {
//						MessageDialog.openWarning(PlatformUI.getWorkbench()
//								.getActiveWorkbenchWindow().getShell(),
//								"ATTENTION", "La date de début doit être antérieure à la date fin");
						throw new Exception("probleme intervalle des dates");
					}
					listeDocumentDTO = service.findAllDTOPeriode(dateDeb,dateFin,null);
				} else {
					dateFin = dateDeb;
					listeDocumentDTO = service.findAllDTOPeriode(dateDeb,dateFin,null);
				}
			} 
			
			if(listeDocumentDTO!=null && !listeDocumentDTO.isEmpty()){
				selectedDocument=listeDocumentDTO/*.get(0)*/;
			}
		}catch (Exception e1) {

		}
		finally{

		}
	}
	
	public class TaDocComparator implements Comparator<IDocumentDTO> {

		public int compare(IDocumentDTO doc1, IDocumentDTO doc2) {

			int premier = doc1.getCodeTiers().compareTo(doc2.getCodeTiers());

			int deuxieme = doc1.getDateDocument().compareTo(doc2.getDateDocument());

			int compared = premier;
			if (compared == 0) {
				compared = deuxieme;
			}

			return compared;
		}
	}

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = false;
			case "modifier":
			case "supprimer":
			case "imprimer":
				retour = false;
				break;				
			default:
				break;
		}
			break;
		default:
			break;
		}
		
		return retour;

	}
	
	public TaTiers recupCodetiers(String code){
		FacesContext context = FacesContext.getCurrentInstance();
		if(code!=null && !code.isEmpty())
			try {
				return taTiersService.findByCode(code);
			} catch (FinderException e) {
				context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
			}
		return null;
	}
	
	public IDocumentTiers recupCodeDocument(String code){
		FacesContext context = FacesContext.getCurrentInstance();
		if(code!=null && !code.isEmpty())
			return documentValide(code);
		return null;
	}
	
	public void actToutcocherDocuments(ActionEvent event)  {

	}

	public void actToutDecocherDocuments(ActionEvent event)  {

	}
	
	private IDocumentTiers documentValide(String code){
		try {	
			switch (selectedTypeSelection.getName()) {
			case TypeDoc.TYPE_FACTURE:
				document=taFactureService.findByCode(code);
				break;
			case TypeDoc.TYPE_AVIS_ECHEANCE:
				document=daoAvisEcheance.findByCode(code);
				break;

			default:
				break;
			}
				
			return document;
		} catch (FinderException e) {
			return null;
		}
	}
	
	public void handleReturnDialogGestionReglement(SelectEvent event) {
//		int idCourant=selectedDocument.getId();
//		actRefresh();
//		if(idCourant!=0 && !listeDocumentDTO.isEmpty()){
//			for (DocumentDTO taFactureDTO : listeDocumentDTO) {
//				if(idCourant==taFactureDTO.getId())
//					selectedDocument=taFactureDTO;
//			}
//		}

	}
	

	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<DocumentDTO> factureAutoCompleteDTOLight(String query) {
		List<DocumentDTO> allValues = null;
		List<DocumentDTO> filteredValues = new ArrayList<DocumentDTO>();

		
		switch (selectedTypeSelection.getName()) {
		case TypeDoc.TYPE_FACTURE:
			allValues = taFactureService.findAllDTOPeriode(dateDeb,dateFin,null);
			break;
		case TypeDoc.TYPE_AVIS_ECHEANCE:
			allValues = daoAvisEcheance.findAllDTOPeriode(dateDeb,dateFin,null);
			break;

		default:
			break;
		}
		
		for (int i = 0; i < allValues.size(); i++) {
			DocumentDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeDocument().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}
		
		

		return filteredValues;
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("FactureController.autcompleteSelection() : "+nomChamp);


		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}
//		validateUIField(nomChamp,value);
	}

	public Date getDateDeb() {
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public AutorisationBean getAutorisation() {
		return autorisation;
	}

	public void setAutorisation(AutorisationBean autorisation) {
		this.autorisation = autorisation;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public IDocumentTiers getDocument() {
		return document;
	}

	public void setDocument(IDocumentTiers document) {
		this.document = document;
	}

	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}


	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public String getCodeDocumentDeb() {
		return codeDocumentDeb;
	}

	public void setCodeDocumentDeb(String codeDocument) {
		this.codeDocumentDeb = codeDocument;
	}

	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}


	public void setDetailLigneTiers(TaTiers detailLigneTiers) {
		this.detailLigneTiers = detailLigneTiers;
	}
	
	public IDocumentTiers getDetailLigne() {
		return detailLigne;
	}


	public void setDetailLigne(IDocumentTiers detailLigne) {
		this.detailLigne = detailLigne;
	}

		

	public boolean ImageEstPasVide(String pathImage){
		return ( pathImage!=null && !pathImage.isEmpty() );
	}


	public List<DocumentDTO> getListeDocumentDTO() {
		return listeDocumentDTO;
	}


	public void setListeDocumentDTO(List<DocumentDTO> listeDocumentDTO) {
		this.listeDocumentDTO = listeDocumentDTO;
	}


	public List<DocumentDTO> getSelectedDocument() {
		return selectedDocument;
	}


	public void setSelectedDocument(List<DocumentDTO> selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}


	public TaFactureDTO getTaFactureDTO() {
		return taFactureDTO;
	}


	public void setTaFactureDTO(TaFactureDTO taFactureDTO) {
		this.taFactureDTO = taFactureDTO;
	}

	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}


	public String getCodeDocumentFin() {
		return codeDocumentFin;
	}


	public void setCodeDocumentFin(String codeDocumentFin) {
		this.codeDocumentFin = codeDocumentFin;
	}


	public TaAvisEcheanceDTO getTaAvisEcheanceDTO() {
		return taAvisEcheanceDTO;
	}


	public void setTaAvisEcheanceDTO(TaAvisEcheanceDTO taAvisEcheanceDTO) {
		this.taAvisEcheanceDTO = taAvisEcheanceDTO;
	}


	public IDocumentDTO getDocumentFinDTO() {
		return documentFinDTO;
	}


	public void setDocumentFinDTO(DocumentDTO documentFinDTO) {
		this.documentFinDTO = documentFinDTO;
	}


	public IDocumentDTO getDocumentDebDTO() {
		return documentDebDTO;
	}


	public void setDocumentDebDTO(DocumentDTO documentDebDTO) {
		this.documentDebDTO = documentDebDTO;
	}


	public List<ImageLgr> getListeTypeDocSelection() {
		return listeTypeDocSelection;
	}


	public void setListeTypeDocSelection(List<ImageLgr> listeTypeDocSelection) {
		this.listeTypeDocSelection = listeTypeDocSelection;
	}


	public ImageLgr getSelectedTypeSelection() {
		return selectedTypeSelection;
	}


	public void setSelectedTypeSelection(ImageLgr selectedTypeSelection) {
		this.selectedTypeSelection = selectedTypeSelection;
	}


	public ImageLgr getOldSelectedTypeSelection() {
		return oldSelectedTypeSelection;
	}


	public void setOldSelectedTypeSelection(ImageLgr oldSelectedTypeSelection) {
		this.oldSelectedTypeSelection = oldSelectedTypeSelection;
	}


	public String getPathImageSelectionDocSelection() {
		return pathImageSelectionDocSelection;
	}


	public void setPathImageSelectionDocSelection(String pathImageSelectionDocSelection) {
		this.pathImageSelectionDocSelection = pathImageSelectionDocSelection;
	}



}