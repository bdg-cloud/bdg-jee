package fr.legrain.bdg.webapp.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.documents.service.remote.IDocumentService;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.exportation.service.remote.IExportationServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLRemise;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped 
public class EtatExportationDocumentBean extends AbstractController{
	
	public static final String C_EXPORTE = "exporte";
	public static final String C_PAS_EXPORTE = "pas_exporte";
	public static final String C_TOUS = "tous";
	
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaApporteurServiceRemote taApporteurService;
//	@EJB private ITaAcompteServiceRemote taAcompteService;
	@EJB private ITaReglementServiceRemote taReglementService;
	@EJB private ITaRemiseServiceRemote taRemiseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	@EJB private IExportationServiceRemote ExportationService;
	
	private List<IDocumentTiersDTO> modelDocument;
	private IDocumentTiersDTO[] selection;
	private IDocumentTiersDTO detailLigne;
	private TaTiers detailLigneTiers;
	
	private TaInfoEntreprise infosEntreprise = null;
	
	private Date dateDebut;
	private Date dateFin;
	private TaTiersDTO taTiersDTO;
	
	private String codeTiers;
	private String selectedTypeDocument;
	private List<SelectItem> listeTypeDocument;
	
	
	private String selectedDateExport;
	private List<Date> listeDateExport;
	private List<SelectItem> listeDateExportItem=new LinkedList<>();
	
	private String paramExport;
	
	@PostConstruct
	public void init() {
		listeTypeDocument = new ArrayList<>();
		listeTypeDocument.add(new SelectItem(TypeDoc.TYPE_TOUS, "Tous"));
		listeTypeDocument.add(new SelectItem(TaFacture.TYPE_DOC, "Facture"));
		listeTypeDocument.add(new SelectItem(TaAvoir.TYPE_DOC, "Avoir"));
		listeTypeDocument.add(new SelectItem(TaApporteur.TYPE_DOC, "Apporteur"));
//		listeTypeDocument.add(new SelectItem(TaAcompte.TYPE_DOC, "Acompte"));
		listeTypeDocument.add(new SelectItem(TaReglement.TYPE_DOC, "Règlement"));
		listeTypeDocument.add(new SelectItem(TaRemise.TYPE_DOC, "Remise"));
		
		paramExport = C_TOUS;
		
		infosEntreprise =taInfoEntrepriseService.findInstance();

		dateDebut = infosEntreprise.getDatedebInfoEntreprise();
		dateFin = infosEntreprise.getDatefinInfoEntreprise();
		initListeDate();
	}
	
	public void initListeDate() {
		this.listeDateExportItem.clear();
		listeDateExport=ExportationService.selectionListeDateExportation(dateDebut,dateFin);
		for (Date date : listeDateExport) {
			listeDateExportItem.add(new SelectItem(LibDate.dateToStringTimeStampSansMillis(date)));
		}
	}
	public void actRecherche(ActionEvent e) {
		try{
			String type="";

			List<IDocumentTiersDTO> listeTemp =new LinkedList<IDocumentTiersDTO>();
			modelDocument=new LinkedList<IDocumentTiersDTO>();
			
			codeTiers = null;
			if(taTiersDTO!=null) {
				codeTiers = taTiersDTO.getCodeTiers();
			}  
			
			type = selectedTypeDocument;
//			if(type.equalsIgnoreCase(TypeDoc.TYPE_TOUS)){
//				for (SelectItem item : listeTypeDocument) {
//					remonteDocument((String)item.getValue());
//				}
//			}else{
				remonteDocument(type);
//			}
			List<IDocumentTiersDTO> listeDejaExporte = new ArrayList<IDocumentTiersDTO>();
			for (IDocumentTiersDTO iDocumentTiers : modelDocument) {
				if(iDocumentTiers.getDateExport()!=null)
					listeDejaExporte.add(iDocumentTiers);
			}
			selection=(IDocumentTiersDTO[]) listeDejaExporte.toArray(new IDocumentTiersDTO[listeDejaExporte.size()] );


		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	public void actAnnuler(ActionEvent e) {
			
		}
	
	public void actInverser(ActionEvent e) {
		IDocumentTiersDTO[] tmp = selection;
		selection = modelDocument.toArray(new IDocumentTiersDTO[1]);
		
		List<IDocumentTiersDTO> list = new ArrayList<IDocumentTiersDTO>(Arrays.asList(selection));
		list.removeAll(Arrays.asList(tmp));
		selection = list.toArray(new IDocumentTiersDTO[1]);
	}
	
	public void actToutCocher(ActionEvent e) {
		selection = modelDocument.toArray(new IDocumentTiersDTO[1]);
	}
	
	private boolean trouveDansSelection(IDocumentTiersDTO doc){
		for (int i = 0; i < selection.length; i++) {
			if(selection[i].equals(doc))return true;
		}
		return false;
	}
	public void actEnregistrer(ActionEvent e) {
		Date dateExport=new Date();
		List<Integer>listeFactureNull=new LinkedList<>();
		List<Integer>listeApporteurNull=new LinkedList<>();
		List<Integer>listeAvoirNull=new LinkedList<>();
		List<Integer>listeReglementNull=new LinkedList<>();
		List<Integer>listeRemiseNull=new LinkedList<>();

		
		List<Integer>listeFacture=new LinkedList<>();
		List<Integer>listeApporteur=new LinkedList<>();
		List<Integer>listeAvoir=new LinkedList<>();
		List<Integer>listeReglement=new LinkedList<>();
		List<Integer>listeRemise=new LinkedList<>();

		try{
			List<IDocumentTiersDTO> listeAEnregistrer = new ArrayList<IDocumentTiersDTO>();
			for (Object document : modelDocument) {
				boolean trouve=trouveDansSelection((IDocumentTiersDTO)document);
				
				if(trouve &&  ((IDocumentTiersDTO)document).getDateExport()==null){
//					((IDocumentTiersDTO)document).setExport(1);
					((IDocumentTiersDTO)document).setDateExport(dateExport);
					listeAEnregistrer.add(((IDocumentTiersDTO)document));
				}
				else if(!trouve && ((IDocumentTiersDTO)document).getDateExport()!=null) {
//					((IDocumentTiersDTO)document).setExport(0);
					((IDocumentTiersDTO)document).setDateExport(null);
					listeAEnregistrer.add(((IDocumentTiersDTO)document));
				}
			}
			for (IDocumentTiersDTO document : listeAEnregistrer) {
				IDocumentTiers doc=null;			
				if(((IDocumentTiersDTO)document).getTypeDocument().equals(TaFacture.TYPE_DOC)) {
					if(document.getDateExport()!=null)listeFacture.add(document.getId());
					else listeFactureNull.add(document.getId());
//					if(document.getDateExport()!=null)
//						requeteFacture+=" idDocument = "+document.getId();
//					else requeteFactureNull+=" idDocument = "+document.getId();
//					doc=taFactureService.findByCode(document.getCodeDocument());
//					doc.setDateExport(((IDocumentTiersDTO)document).getDateExport());
//					doc=taFactureService.enregistrerMerge((TaFacture)doc);
				}
				if(((IDocumentTiersDTO)document).getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
					if(document.getDateExport()!=null)listeAvoir.add(document.getId());
					else listeAvoirNull.add(document.getId());
//					doc=taAvoirService.findByCode(document.getCodeDocument());
//					doc.setDateExport(((IDocumentTiersDTO)document).getDateExport());
//					doc=taAvoirService.enregistrerMerge((TaAvoir)doc);
				}
				if(((IDocumentTiersDTO)document).getTypeDocument().equals(TaApporteur.TYPE_DOC)) {
					if(document.getDateExport()!=null)listeApporteur.add(document.getId());
					else listeApporteurNull.add(document.getId());
//					doc=taApporteurService.findByCode(document.getCodeDocument());
//					doc.setDateExport(((IDocumentTiersDTO)document).getDateExport());
//					doc=taApporteurService.enregistrerMerge((TaApporteur)doc);
				}
				if(((IDocumentTiersDTO)document).getTypeDocument().equals(TaReglement.TYPE_DOC)) {
					if(document.getDateExport()!=null)listeReglement.add(document.getId());
					else listeReglementNull.add(document.getId());
//					
//					for (TaRReglement l : ((TaReglement)doc).getTaRReglements()) {
//						l.setDateExport(((TaReglement)doc).getDateExport());
//					}
//					doc=taReglementService.enregistrerMerge((TaReglement)doc);
				}
				if(((IDocumentTiersDTO)document).getTypeDocument().equals(TaRemise.TYPE_DOC)) {
					if(document.getDateExport()!=null)listeRemise.add(document.getId());
					else listeRemiseNull.add(document.getId());
					
//					doc=taRemiseService.findByCode(((IDocumentTiersDTO)document).getCodeDocument());
//					doc.setDateExport(((IDocumentTiersDTO)document).getDateExport());
//					dateExport=doc.getDateExport();
//					doc=taRemiseService.enregistrerMerge((TaRemise)doc);
//					
//					doc=taRemiseService.findById(doc.getIdDocument());
//					for (TaLRemise l : ((TaRemise)doc).getTaLRemises()) {
//						l.getTaReglement().setDateExport(dateExport);
//						for (TaRReglement lr : l.getTaReglement().getTaRReglements()) {
//							lr.setDateExport(dateExport);
//						}
//						taReglementService.enregistrerMerge((TaReglement)l.getTaReglement());
//					}
				}
				
			}
			if(listeFacture.size()>0) {
				taFactureService.executeUpdate("UPDATE TaFacture SET dateExport=:dateExport Where idDocument in :ids",listeFacture,dateExport);
			}
			if(listeFactureNull.size()>0) {
				taFactureService.executeUpdate("UPDATE TaFacture SET dateExport=null Where idDocument IN :ids",listeFactureNull,null);
			}
			
			if(listeAvoir.size()>0) {
				taFactureService.executeUpdate("UPDATE TaAvoir SET dateExport=:dateExport Where idDocument in :ids",listeAvoir,dateExport);
			}
			if(listeAvoirNull.size()>0) {
				taFactureService.executeUpdate("UPDATE TaAvoir SET dateExport=null Where idDocument IN :ids",listeAvoirNull,null);
			}
			
			if(listeApporteur.size()>0) {
				taFactureService.executeUpdate("UPDATE TaApporteur SET dateExport=:dateExport Where idDocument in :ids",listeApporteur,dateExport);
			}
			if(listeApporteurNull.size()>0) {
				taFactureService.executeUpdate("UPDATE TaApporteur SET dateExport=null Where idDocument IN :ids",listeApporteurNull,null);
			}
			
			if(listeReglement.size()>0) {
				taFactureService.executeUpdate("UPDATE TaReglement r  SET r.dateExport=:dateExport Where r.idDocument in :ids",listeReglement,dateExport);
				taFactureService.executeUpdate("UPDATE TaRReglement rr  SET rr.dateExport=:dateExport Where rr.taReglement.idDocument in :ids",listeReglement,dateExport);
			}
			if(listeReglementNull.size()>0) {
				taFactureService.executeUpdate("UPDATE TaReglement r  SET r.dateExport=null Where r.idDocument in :ids",listeReglementNull,null);
				taFactureService.executeUpdate("UPDATE TaRReglement rr  SET rr.dateExport=null Where rr.taReglement.idDocument in :ids",listeReglementNull,null);
			}
			
			if(listeRemise.size()>0) {
				taFactureService.executeUpdate("UPDATE TaRemise r SET r.dateExport=:dateExport Where r.idDocument in :ids",listeRemise,dateExport);
				taFactureService.executeUpdate("UPDATE TaReglement r SET r.dateExport=:dateExport Where exists(select rl.taReglement from TaLRemise rl join rl.taDocument doc"
						+ " join rl.taReglement reg where doc.idDocument in :ids and rl.taReglement=r) ",listeRemise,dateExport);
				taFactureService.executeUpdate("UPDATE TaRReglement rr SET rr.dateExport=:dateExport Where exists(select rreg from TaLRemise rl join rl.taDocument doc"
						+ " join rl.taReglement reg join reg.taRReglements rreg where doc.idDocument in :ids and rreg=rr) ",listeRemise,dateExport);				
			}
			if(listeRemiseNull.size()>0) {
				taFactureService.executeUpdate("UPDATE TaRemise SET dateExport=null Where idDocument IN :ids",listeRemiseNull,null);
				taFactureService.executeUpdate("UPDATE TaRemise r SET r.dateExport=null Where r.idDocument in :ids",listeRemiseNull,null);
				taFactureService.executeUpdate("UPDATE TaReglement r SET r.dateExport=null Where exists(select rl.taReglement from TaLRemise rl join rl.taDocument doc"
						+ " join rl.taReglement reg where doc.idDocument in :ids and rl.taReglement=r) ",listeRemiseNull,null);
				taFactureService.executeUpdate("UPDATE TaRReglement rr SET rr.dateExport=null Where exists(select rl.taReglement from TaLRemise rl join rl.taDocument doc"
						+ " join rl.taReglement reg join reg.taRReglements rreg where doc.idDocument in :ids and rreg=rr) ",listeRemiseNull,null);	
			}
			
			
			modelDocument.clear();
			selection = new IDocumentTiersDTO[0];
			initListeDate();
		

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void detailDocument(){
		String tabEcran="";
		LgrTab lgrTab=LgrTab.getInstance();
		if(detailLigne!=null){
			tabEcran=lgrTab.getTabDocCorrespondance().get(detailLigne.getTypeDocument());
			if(tabEcran!=null && !tabEcran.isEmpty()){
				ouvertureDocumentBean.setEvent(new LgrTabEvent());
				ouvertureDocumentBean.getEvent().setCodeObjet(detailLigne.getCodeDocument());
				ouvertureDocumentBean.getEvent().setData(detailLigne);
				ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
				ouvertureDocumentBean.getEvent().setAfficheDoc(true);
				ouvertureDocumentBean.openDocument(null);
			}
		}
	}
	
	public void detailTiers(){
		String tabEcran="";
		if(detailLigneTiers!=null){
			tabEcran=LgrTab.CSS_CLASS_TAB_TIERS;
			if(tabEcran!=null && !tabEcran.isEmpty()){
				ouvertureDocumentBean.setEvent(new LgrTabEvent());
				ouvertureDocumentBean.getEvent().setCodeObjet(detailLigneTiers.getCodeTiers());
				ouvertureDocumentBean.getEvent().setData(detailLigneTiers);
				ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
				ouvertureDocumentBean.getEvent().setAfficheDoc(true);
				ouvertureDocumentBean.openDocument(null);
			}
		}
	}
	
	private void remonteDateExportDocument(){
		List<? extends IDocumentTiersDTO> listeTemp =new LinkedList<>();
		

//		listeTemp =  ((IDocumentService<TaFacture>)taFactureService).rechercheDocument(dateDebut,dateFin,codeTiers,export);


		for (IDocumentTiersDTO doc : listeTemp) {
			modelDocument.add(doc);
		}
	}
	
	private void remonteDocument(String type){
		List<? extends IDocumentTiersDTO> listeTemp =new LinkedList<>();
		List<IDocumentTiersDTO> listeTempMix =new LinkedList<>();
		Boolean export =null;
		Date dateExport=null;
		if(paramExport.equals(C_EXPORTE)) {
			export = true;
		} else if(paramExport.equals(C_PAS_EXPORTE)) {
			export = false;
		} else {
			export = null;
		}
		if(selectedDateExport!=null) {
			dateExport=LibDate.StringToDateTimeStamp(selectedDateExport);
			if(type.equals(TaFacture.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp = taFactureService.rechercheDocumentDTO(dateExport,codeTiers,dateDebut,dateFin);
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaAvoir.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp =  taAvoirService.rechercheDocumentDTO(dateExport,codeTiers,dateDebut,dateFin);
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaApporteur.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp = taApporteurService.rechercheDocumentDTO(dateExport,codeTiers,dateDebut,dateFin);
				listeTempMix.addAll(listeTemp);
			}		
			if(type.equals(TaReglement.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp = taReglementService.rechercheDocumentDTO(dateExport,codeTiers,dateDebut,dateFin);
				for (IDocumentTiersDTO o : listeTemp) {
					((TaReglementDTO)o).setId(((TaReglementDTO)o).getIdDocument());
				}
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaRemise.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp =  taRemiseService.rechercheDocumentDTO(dateExport,codeTiers,dateDebut,dateFin);
				listeTempMix.addAll(listeTemp);
			}

		}else {
			//si export=null cela represente le tous
			if(type.equals(TaFacture.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp =  taFactureService.rechercheDocumentDTO(dateDebut,dateFin,codeTiers,export);
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaAvoir.TYPE_DOC)  || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp = taAvoirService.rechercheDocumentDTO(dateDebut,dateFin,codeTiers,export);
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaApporteur.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp =  taApporteurService.rechercheDocumentDTO(dateDebut,dateFin,codeTiers,export);
				listeTempMix.addAll(listeTemp);
			}		
			if(type.equals(TaReglement.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp = taReglementService.rechercheDocumentDTO(dateDebut,dateFin,codeTiers,export);
				for (IDocumentTiersDTO o : listeTemp) {
					((TaReglementDTO)o).setId(((TaReglementDTO)o).getIdDocument());
				}
				listeTempMix.addAll(listeTemp);
			}
			if(type.equals(TaRemise.TYPE_DOC) || type.equals(TypeDoc.TYPE_TOUS)){
				listeTemp =   taRemiseService.rechercheDocumentDTO(dateDebut,dateFin,codeTiers,export);
				listeTempMix.addAll(listeTemp);
			}
		}
		for (IDocumentTiersDTO doc : listeTempMix) {
			if(doc.getTypeDocument().equals(TaReglement.TYPE_DOC) ){
//				if(taRemiseService.findSiReglementDansRemise(doc.getCodeDocument()).size()==0)
				if(((TaReglementDTO)doc).getCodeRemise()==null || ((TaReglementDTO)doc).getCodeRemise().isEmpty())
					modelDocument.add(doc);
			}
			else modelDocument.add(doc);
		}
	}
	
	public void actToutDechocher(ActionEvent e) {
		selection = new IDocumentTiersDTO[0];
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

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public List<SelectItem> getListeTypeDocument() {
		return listeTypeDocument;
	}

	public void setListeTypeDocument(List<SelectItem> listeTypeDocument) {
		this.listeTypeDocument = listeTypeDocument;
	}

	public String getSelectedTypeDocument() {
		return selectedTypeDocument;
	}

	public void setSelectedTypeDocument(String selectedTypeDocument) {
		this.selectedTypeDocument = selectedTypeDocument;
	}

	public String getParamExport() {
		return paramExport;
	}

	public void setParamExport(String paramExport) {
		this.paramExport = paramExport;
	}

	public List<IDocumentTiersDTO> getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(List<IDocumentTiersDTO> modelDocument) {
		this.modelDocument = modelDocument;
	}

	public IDocumentTiersDTO[] getSelection() {
		return selection;
	}

	public void setSelection(IDocumentTiersDTO[] selection) {
		this.selection = selection;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public IDocumentTiersDTO getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(IDocumentTiersDTO detailLigne) {
		this.detailLigne = detailLigne;
	}

	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}

	public void setDetailLigneTiers(String detailTiers) {
		TaTiers tiers = null;
		try {
			tiers = taTiersService.findByCode(detailTiers);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.detailLigneTiers = tiers;
	}

	public String getSelectedDateExport() {
		return selectedDateExport;
	}

	public void setSelectedDateExport(String selectedDateExport) {
		this.selectedDateExport = selectedDateExport;
	}

	public List<Date> getListeDateExport() {
		return listeDateExport;
	}

	public void setListeDateExport(List<Date> listeDateExport) {
		this.listeDateExport = listeDateExport;
	}

	public List<SelectItem> getListeDateExportItem() {
		return listeDateExportItem;
	}

	public void setListeDateExportItem(List<SelectItem> listeDateExportItem) {
		this.listeDateExportItem = listeDateExportItem;
	}
	
	public void actRefresh() {
		initListeDate();
	}

	public void actFermer() {
		reset();
	}

	public void reset() {
		modelDocument.clear();
		initListeDate();
	}
}
