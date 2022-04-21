package fr.legrain.bdg.webapp.etats;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
//import org.primefaces.component.export.ExcelOptions;
//import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.data.FilterEvent;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLBonReceptionDTOJSF;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTiersDTO;



@Named
@ViewScoped 
public class EtatProduitsFournisseurBean implements Serializable {
	


	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
//	private @EJB ITaFamilleServiceRemote taFamilleService;
//	private @EJB ITaLotServiceRemote taLotService;
//	private @EJB ITaEtatStockServiceRemote taEtatStockService;
//	private @EJB ITaMouvementStockServiceRemote taMouvStockService;
//	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;	
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
//	private List<TaLBonReceptionDTOJSF> valuesLigne;
	private List<TaLBonReceptionDTOJSF> modelDocument;
	private TaLBonReceptionDTOJSF[] selection;
	private TaArticle detailLigne;
	private TaLot detailLigneLot;

	private TaTiersDTO taFournisseur;
	private TaLBonReceptionDTO ligneTotaux;

	
	private TaInfoEntreprise infosEntreprise = null;
	
	private Date dateDebut;
	private Date dateFin;

	
	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4.rotate());
 
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "images" + File.separator + "bdg-75x48.png";
//         
//        pdf.add(Image.getInstance(logo));
    }


	public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        CellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
 
        for (Row row : sheet) {
            for (Cell cell : row) {
                cell.setCellValue(cell.getStringCellValue().toLowerCase());
                cell.setCellStyle(style);
            }
        }
    }

	
	@PostConstruct
	public void init() {

		infosEntreprise =taInfoEntrepriseService.findInstance();

		dateDebut = infosEntreprise.getDatedebInfoEntreprise();
		dateFin = infosEntreprise.getDatefinInfoEntreprise();
		
//		List<TaFamilleDTO> liste1 = taTiersService.selectAllDTO();
//		for (TaFamilleDTO obj : liste1) {
//			listeTaFamille.add(obj.getCodeFamille());
//		}
//		List<TaEntrepotDTO> liste2 = taEntrepotService.selectAllDTO();
//		for (TaEntrepotDTO obj : liste2) {
//			listeTaEntrepot.add(obj.getCodeEntrepot());
//		}
//		List<TaArticleDTO> liste3 = taArticleService.selectAllDTO();
//		for (TaArticleDTO obj : liste3) {
//			listeTaArticle.add(obj.getCodeArticle());
//		}
	}
	
	public void actRecherche(ActionEvent e) {
		try{
			String code=null;
			if(taFournisseur!=null)code=taFournisseur.getCodeTiers();
			
			List<TaLBonReceptionDTO> listeTemp =new LinkedList<TaLBonReceptionDTO>();
			modelDocument=new LinkedList<TaLBonReceptionDTOJSF>();
			//dateDebut, dateFin,
			listeTemp=taBonReceptionService.bonRecepFindByCodeFournisseurParDate(code,dateDebut, dateFin);
			for (TaLBonReceptionDTO ligne : listeTemp) {
				TaLBonReceptionDTOJSF dto=new TaLBonReceptionDTOJSF();
				dto.setDto(ligne);
				dto.setTaArticle(taArticleService.findByCode(ligne.getCodeArticle()));
				dto.setTaLot(taLotService.findByCode(ligne.getNumLot()));
				modelDocument.add(dto);
			}
			ligneTotaux= calculTotaux();

		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
    public TaLBonReceptionDTO calculTotaux(){
    	TaLBonReceptionDTO total=new TaLBonReceptionDTO();
    	total.setQteLDocument(BigDecimal.ZERO);
    	total.setQte2LDocument(BigDecimal.ZERO);
    	for (TaLBonReceptionDTOJSF obj : modelDocument) {
        	
    		total.setQteLDocument((total.getQteLDocument()).add(obj.getDto().getQteLDocument()!=null?obj.getDto().getQteLDocument():BigDecimal.ZERO));
    		total.setQte2LDocument((total.getQte2LDocument()).add(obj.getDto().getQte2LDocument()!=null?obj.getDto().getQte2LDocument():BigDecimal.ZERO));
		}
    	return total;
    }
    
	public void actAnnuler(ActionEvent e) {
			
		}
	
	
	public void detailLot(){
		String tabEcran="";
		if(detailLigneLot!=null){
			tabEcran=LgrTab.CSS_CLASS_TAB_LOTS;
			if(tabEcran!=null && !tabEcran.isEmpty()){
				ouvertureDocumentBean.setEvent(new LgrTabEvent());
				ouvertureDocumentBean.getEvent().setCodeObjet(detailLigneLot.getNumLot());
				ouvertureDocumentBean.getEvent().setData(detailLigneLot);
				ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
				ouvertureDocumentBean.getEvent().setAfficheDoc(true);
				ouvertureDocumentBean.openDocument(null);
			}
		}
	}	
	
	public void detailArticle(){
		String tabEcran="";
		if(detailLigne!=null){
			tabEcran=LgrTab.CSS_CLASS_TAB_ARTICLE;
			if(tabEcran!=null && !tabEcran.isEmpty()){
				ouvertureDocumentBean.setEvent(new LgrTabEvent());
				ouvertureDocumentBean.getEvent().setCodeObjet(detailLigne.getCodeArticle());
				ouvertureDocumentBean.getEvent().setData(detailLigne);
				ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
				ouvertureDocumentBean.getEvent().setAfficheDoc(true);
				ouvertureDocumentBean.openDocument(null);
			}
		}
	}

	
	public List<TaTiersDTO> fournisseursAutoComplete(String query) {
        List<TaTiersDTO> allValues = taTiersService.findAllLight();
        TaTiersDTO entrepotTous=new TaTiersDTO();
        entrepotTous.setCodeTiers(Const.C_CHOISIR);
        entrepotTous.setNomTiers("Selectionner un fournisseur");
        allValues.add(0,entrepotTous);
        List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTiersDTO obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
        return filteredValues;
    }
	
	public void actImprimerListeEtatProduitFournisseur(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeEtatProduitFournisseur", modelDocument);

			} catch (Exception e) {
				e.printStackTrace();
			}
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

	

	public List<TaLBonReceptionDTOJSF> getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(List<TaLBonReceptionDTOJSF> modelDocument) {
		this.modelDocument = modelDocument;
	}

	public TaLBonReceptionDTOJSF[] getSelection() {
		return selection;
	}

	public void setSelection(TaLBonReceptionDTOJSF[] selection) {
		this.selection = selection;
	}

	

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public TaArticle getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(TaArticle detailLigne) {
		this.detailLigne = detailLigne;
	}

	


	
	public boolean filterByListeString(Object value, Object filter, Locale locale) {
		String filterText = "";
		boolean retour=false;
		String[]tmp=(String[]) filter;
		for (int i = 0; i < tmp.length; i++) {
			filterText=tmp[i].trim();
	         if(value!=null && !((String) value).isEmpty()){
	        	 return ((String) value).toLowerCase().equals(filterText.toLowerCase());					
	         }else if(value==null) return false;
		}
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return true;
        }
		return retour;
    }

    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return ((Comparable) value).compareTo(LibConversion.stringToBigDecimal(filterText)) >= 0;
    }


	public TaTiersDTO getTaFournisseur() {
		return taFournisseur;
	}


	public void setTaFournisseur(TaTiersDTO taFournisseur) {
		this.taFournisseur = taFournisseur;
	}


	public TaLot getDetailLigneLot() {
		return detailLigneLot;
	}


	public void setDetailLigneLot(TaLot detailLigneLot) {
		this.detailLigneLot = detailLigneLot;
	}


	public TaLBonReceptionDTO getLigneTotaux() {
		return ligneTotaux;
	}


	public void setLigneTotaux(TaLBonReceptionDTO ligneTotaux) {
		this.ligneTotaux = ligneTotaux;
	}
	

//    public MouvementStocksDTO calculTotaux(){
//    	MouvementStocksDTO total=new MouvementStocksDTO();
//    	total.setDepart(BigDecimal.ZERO);
//    	total.setEntree(BigDecimal.ZERO);
//    	total.setSortie(BigDecimal.ZERO);
//    	total.setDispo(BigDecimal.ZERO);
//    	for (MouvementStocksDTO obj : modelDocument) {
//    		total.getDepart().add(obj.getDepart());
//    		total.getEntree().add(obj.getEntree());
//    		total.getSortie().add(obj.getSortie());
//    		total.getDispo().add(obj.getDispo());
//		}
//    	return total;
//    }


//	public boolean isParFamille() {
//		return parFamille;
//	}
//
//
//	public void setParFamille(boolean parFamille) {
//		this.parFamille = parFamille;
//	}
//
//
//	public MouvementStocksDTO getLigneTotaux() {
//		return ligneTotaux;
//	}
//
//
//	public void setLigneTotaux(MouvementStocksDTO ligneTotaux) {
//		this.ligneTotaux = ligneTotaux;
//	}

	public void filterListener(FilterEvent filterEvent) {
		  ligneTotaux=calculTotaux();
		}
}
