package fr.legrain.bdg.webapp.verrouillage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.documents.service.remote.IDocumentService;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IRelationDoc;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped 
public class VerrouillageDocumentBean extends AbstractController{
	
	public static final String C_EXPORTE = "verrouillé";
	public static final String C_PAS_EXPORTE = "pas_verrouillé";
	public static final String C_TOUS = "tous";
	
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaReglementServiceRemote taReglementService;
	@EJB private ITaRemiseServiceRemote taRemiseService;
	
	@EJB private ITaRAvoirServiceRemote taRAvoirService;
	@EJB private ITaRReglementServiceRemote taRReglementService;
	
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	private List<IDocumentTiers> modelDocument;
	private IDocumentTiers[] selection;
	private IDocumentTiers detailLigne;
	private TaTiers detailLigneTiers;
	
	private TaInfoEntreprise infosEntreprise = null;
	
	private Date dateDebut;
	private Date dateFin;
	private TaTiersDTO taTiersDTO;
	
	private String codeTiers;
	private String selectedTypeDocument;
	private List<SelectItem> listeTypeDocument;
	
	private String paramExport;
	
	@PostConstruct
	public void init() {
		listeTypeDocument = new ArrayList<>();
		listeTypeDocument.add(new SelectItem(TypeDoc.TYPE_TOUS, "Tous"));
		listeTypeDocument.add(new SelectItem(TaFacture.TYPE_DOC, "Facture"));
		listeTypeDocument.add(new SelectItem(TaAvoir.TYPE_DOC, "Avoir"));
		listeTypeDocument.add(new SelectItem(TaReglement.TYPE_DOC, "Règlement"));
		listeTypeDocument.add(new SelectItem(TaRemise.TYPE_DOC, "Remise"));
//		listeTypeDocument.add(new SelectItem(TaRReglement.TYPE_DOC, "Affectation des règlements"));
//		listeTypeDocument.add(new SelectItem(TaRAvoir.TYPE_DOC, "Affectation des avoirs"));
		
		paramExport = C_PAS_EXPORTE;
		
		infosEntreprise =taInfoEntrepriseService.findInstance();

		dateDebut = infosEntreprise.getDatedebInfoEntreprise();
		dateFin = infosEntreprise.getDatefinInfoEntreprise();
		selectedTypeDocument=TypeDoc.TYPE_TOUS;
	}
	
	public void actRecherche(ActionEvent e) {
		try{
			String type=TypeDoc.TYPE_TOUS;

			List<IDocumentTiers> listeTemp =new LinkedList<IDocumentTiers>();
			modelDocument=new LinkedList<IDocumentTiers>();
			
			codeTiers = null;
			if(taTiersDTO!=null) {
				codeTiers = taTiersDTO.getCodeTiers();
			}  
			
			type = selectedTypeDocument;
			if(type.equalsIgnoreCase(TypeDoc.TYPE_TOUS)){
				for (SelectItem item : listeTypeDocument) {
					remonteDocument((String)item.getValue());
				}
			}else{
				remonteDocument(type);
			}
			List<IDocumentTiers> listeNonExporte = new ArrayList<IDocumentTiers>();
			for (IDocumentTiers iDocumentTiers : modelDocument) {
				if(iDocumentTiers.getDateVerrouillage()==null)
					listeNonExporte.add(iDocumentTiers);
			}
			selection=(IDocumentTiers[]) listeNonExporte.toArray(new IDocumentTiers[listeNonExporte.size()] );


		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	public void actAnnuler(ActionEvent e) {
			
		}
	
	public void actInverser(ActionEvent e) {
		IDocumentTiers[] tmp = selection;
		selection = modelDocument.toArray(new IDocumentTiers[1]);
		
		List<IDocumentTiers> list = new ArrayList<IDocumentTiers>(Arrays.asList(selection));
		list.removeAll(Arrays.asList(tmp));
		selection = list.toArray(new IDocumentTiers[1]);
	}
	
	public void actToutCocher(ActionEvent e) {
		selection = modelDocument.toArray(new IDocumentTiers[1]);
	}
	
	private boolean trouveDansSelection(IDocumentTiers doc){
		for (int i = 0; i < selection.length; i++) {
			if(selection[i].equals(doc))return true;
		}
		return false;
	}
	public void actEnregistrer(ActionEvent e) {
		List<IRelationDoc> listeRelation =new LinkedList<>();
		try{
			List<IDocumentTiers> listeAEnregistrer = new ArrayList<IDocumentTiers>();
			for (IDocumentTiers document : selection) {
				boolean trouve=trouveDansSelection((IDocumentTiers)document);
				
				if(trouve && ((IDocumentTiers)document).getDateVerrouillage()==null ){
					((IDocumentTiers)document).setDateVerrouillage(new Date());
					listeAEnregistrer.add(((IDocumentTiers)document));
				}
				else if(!trouve && ((IDocumentTiers)document).getDateVerrouillage()!=null ){
					((IDocumentTiers)document).setDateVerrouillage(null);
					listeAEnregistrer.add(((IDocumentTiers)document));
				}
			}
			for (IDocumentTiers document : listeAEnregistrer) {
				IDocumentTiers docFinal;		
				if(((IDocumentTiers)document).getTypeDocument().equals(TaFacture.TYPE_DOC)) {
					for (TaRReglement rr : ((TaFacture)document).getTaRReglements()) {
						rr.setDateVerrouillage(((TaFacture)document).getDateVerrouillage());
						if(!listeRelation.contains(rr))listeRelation.add(rr);
					}
					docFinal=taFactureService.findById(document.getIdDocument());
					docFinal.setDateVerrouillage(document.getDateVerrouillage());
					docFinal=taFactureService.enregistrerMerge((TaFacture)docFinal);
				}
				if(((IDocumentTiers)document).getTypeDocument().equals(TaAvoir.TYPE_DOC)) {
					for (TaRAvoir rr : ((TaAvoir)document).getTaRAvoirs()) {
						rr.setDateVerrouillage(((TaAvoir)document).getDateVerrouillage());
						if(!listeRelation.contains(rr))listeRelation.add(rr);
					}
					docFinal=taAvoirService.findById(document.getIdDocument());
					docFinal.setDateVerrouillage(document.getDateVerrouillage());
					docFinal=taAvoirService.enregistrerMerge((TaAvoir)docFinal);
				}
				if(((IDocumentTiers)document).getTypeDocument().equals(TaReglement.TYPE_DOC)) {
					for (TaRReglement rr : ((TaReglement)document).getTaRReglements()) {
						rr.setDateVerrouillage(((TaReglement)document).getDateVerrouillage());
						if(!listeRelation.contains(rr))listeRelation.add(rr);
					}
					docFinal=taReglementService.findById(document.getIdDocument());
					docFinal.setDateVerrouillage(document.getDateVerrouillage());
					docFinal=taReglementService.enregistrerMerge((TaReglement)docFinal);
				}
				if(((IDocumentTiers)document).getTypeDocument().equals(TaRemise.TYPE_DOC)) {
					docFinal=taRemiseService.findById(document.getIdDocument());
					docFinal.setDateVerrouillage(document.getDateVerrouillage());
					docFinal=taRemiseService.enregistrerMerge((TaRemise)docFinal);
				}				
			}
			for (IRelationDoc l : listeRelation) {
				if(l.getTypeDocument().equals(TaRReglement.TYPE_DOC)) {
					TaRReglement rr=taRReglementService.findById(l.getId());
					rr.setDateVerrouillage(l.getDateVerrouillage());
					taRReglementService.enregistrerMerge(rr);
				}
				if(l.getTypeDocument().equals(TaRAvoir.TYPE_DOC)) {
					TaRAvoir rr=taRAvoirService.findById(l.getId());
					rr.setDateVerrouillage(l.getDateVerrouillage());
					taRAvoirService.enregistrerMerge(rr);
				}
			}
			modelDocument.clear();
			selection = new IDocumentTiers[0];
			//initEtatBouton(false);
		
		} catch (ExceptLgr e1) {
			e1.printStackTrace();
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
	
	private void remonteDocument(String type){
		List<? extends IDocumentTiers> listeTemp =new LinkedList<>();
		Boolean export =null;
		if(paramExport.equals(C_EXPORTE)) {
			export = true;
		} else if(paramExport.equals(C_PAS_EXPORTE)) {
			export = false;
		} else {
			export = null;
		}
		//si export=null cela represente le tous
		if(type.equals(TaFacture.TYPE_DOC)){
				listeTemp =  ((IDocumentService<TaFacture>)taFactureService).rechercheDocumentVerrouille(dateDebut,dateFin,codeTiers,export);
		}
		if(type.equals(TaAvoir.TYPE_DOC)){
			listeTemp = ((IDocumentService<TaAvoir>) taAvoirService).rechercheDocumentVerrouille(dateDebut,dateFin,codeTiers,export);
		}
		if(type.equals(TaReglement.TYPE_DOC)){
			listeTemp = ((IDocumentService<TaReglement>) taReglementService).rechercheDocumentVerrouille(dateDebut,dateFin,codeTiers,export);
		}
		if(type.equals(TaRemise.TYPE_DOC)){
			listeTemp = ((IDocumentService<TaRemise>) taRemiseService).rechercheDocumentVerrouille(dateDebut,dateFin,codeTiers,export);
		}
		for (IDocumentTiers doc : listeTemp) {
				modelDocument.add(doc);
		}
	}
	
	public void actToutDechocher(ActionEvent e) {
		selection = new IDocumentTiers[0];
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

	public List<IDocumentTiers> getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(List<IDocumentTiers> modelDocument) {
		this.modelDocument = modelDocument;
	}

	public IDocumentTiers[] getSelection() {
		return selection;
	}

	public void setSelection(IDocumentTiers[] selection) {
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

	public IDocumentTiers getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(IDocumentTiers detailLigne) {
		this.detailLigne = detailLigne;
	}

	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}

	public void setDetailLigneTiers(TaTiers detailTiers) {
		this.detailLigneTiers = detailTiers;
	}

	public void actFermer(ActionEvent e) {
		
	}

	
	public void actRefresh()  {
		
	}
}
