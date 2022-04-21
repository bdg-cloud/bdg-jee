package fr.legrain.bdg.webapp.etats;

import java.io.IOException;
import java.io.Serializable;
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

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;



@Named
@ViewScoped 
public class EtatFournisseurBean implements Serializable {
	


	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	private TaTiers detailLigneTiers;
	private List<TaTiersDTOJSF> modelDocument;
	private TaTiersDTOJSF[] selection;
	private TaTiersDTOJSF detailLigne;
	
	private TaTiersDTOJSF ligneTotaux;
	

//	
	private TaArticleDTO taArticle;
	private List<String> listeTaArticle  = new ArrayList<String>();
	

	
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
		

	}
	
	public void actRecherche(ActionEvent e) {
		try{
			
			String codeArticle=null;
			if(taArticle!=null)codeArticle=taArticle.getCodeArticle();
			
			List<TaTiersDTO> listeTemp =new LinkedList<TaTiersDTO>();
			modelDocument=new LinkedList<TaTiersDTOJSF>();
			//dateDebut, dateFin,
			listeTemp=taBonReceptionService.findTiersDTOByCodeArticleAndDate(codeArticle,dateDebut,dateFin);
			for (TaTiersDTO obj : listeTemp) {
				TaTiersDTOJSF dto=new TaTiersDTOJSF();
				dto.setDto(obj);
				dto.setTaTiers(taTiersService.findByCode(obj.getCodeTiers()));
				modelDocument.add(dto);
			}
//			ligneTotaux= calculTotaux();

		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	public void actAnnuler(ActionEvent e) {
			
		}
	
	

	
	public List<TaArticleDTO> articleAutoComplete(String query) {
        List<TaArticleDTO> allValues = taArticleService.findAllLight();
        TaArticleDTO entrepotTous=new TaArticleDTO();
        entrepotTous.setCodeArticle(Const.C_CHOISIR);
        entrepotTous.setLibcFamille("Selectionner un article");
        allValues.add(0,entrepotTous);
        List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaArticleDTO obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
        return filteredValues;
    }

	public void actImprimerListeEtatFournisseurPourUnArticle(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeEtatFournisseurPourUnArticle", modelDocument);

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

	

	public List<TaTiersDTOJSF> getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(List<TaTiersDTOJSF> modelDocument) {
		this.modelDocument = modelDocument;
	}

	public TaTiersDTOJSF[] getSelection() {
		return selection;
	}

	public void setSelection(TaTiersDTOJSF[] selection) {
		this.selection = selection;
	}

	

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public TaTiersDTOJSF getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(TaTiersDTOJSF detailLigne) {
		this.detailLigne = detailLigne;
	}




	public TaArticleDTO getTaArticle() {
		return taArticle;
	}


	public void setTaArticle(TaArticleDTO taArticle) {
		this.taArticle = taArticle;
	}


	public List<String> getListeTaArticle() {
		return listeTaArticle;
	}


	public void setListeTaArticle(List<String> listeTaArticle) {
		this.listeTaArticle = listeTaArticle;
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


	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}


	public void setDetailLigneTiers(TaTiers detailLigneTiers) {
		this.detailLigneTiers = detailLigneTiers;
	}
}
