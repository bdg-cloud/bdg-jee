package fr.legrain.bdg.webapp.etats;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.webapp.app.ChoixEditionController;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.stock.dto.MouvementStocksDTO;



@Named
@ViewScoped 
public class EtatStocksBean implements Serializable {
	


	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaEntrepotServiceRemote taEntrepotService;
	private @EJB ITaFamilleServiceRemote taFamilleService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaMouvementStockServiceRemote taMouvStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;	
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	@Inject @Named(value="etatChoixEditionBean")
	private EtatChoixEditionBean etatChoixEditionBean;

	
	private List<MouvementStocksDTO> modelDocument;
	private List<MouvementStocksDTO> filteredValues=new LinkedList<MouvementStocksDTO>();
	
	private MouvementStocksDTO[] selection;
	private MouvementStocksDTO detailLigne;
	
	private MouvementStocksDTO ligneTotaux;
	
	private TaEntrepotDTO taEntrepot;
	private List<String> listeTaEntrepot = new ArrayList<String>();
	
	private TaFamilleDTO taFamille;
	private List<String> listeTaFamille = new ArrayList<String>();
	
	private TaArticleDTO taArticle;
	private List<String> listeTaArticle  = new ArrayList<String>();
	

	private boolean afficherLesTermine=false;
	private boolean parFamille=false;
	private String emplacement;
	
	private TaInfoEntreprise infosEntreprise = null;
	
	private Date dateDebut;
	private Date dateFin;
	
	private Map<String,Object> mapParametre = new HashMap<String,Object>();
//    private ExcelOptions excelOpt;
//    
//    private PDFOptions pdfOpt;
    
//    class Rotate extends PdfPageEventHelper {
//    	 
//        protected PdfNumber orientation = PdfPage.PORTRAIT;
// 
//        public void setOrientation(PdfNumber orientation) {
//            this.orientation = orientation;
//        }
// 
//        @Override
//        public void onStartPage(PdfWriter writer, Document document) {
//            writer.addPageDictEntry(PdfName.ROTATE, orientation);
//        }
//    }
	
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
		
		List<TaFamilleDTO> liste1 = taFamilleService.selectAllDTO();
		for (TaFamilleDTO obj : liste1) {
			listeTaFamille.add(obj.getCodeFamille());
		}
		List<TaEntrepotDTO> liste2 = taEntrepotService.findAllLight();
		for (TaEntrepotDTO obj : liste2) {
			listeTaEntrepot.add(obj.getCodeEntrepot());
		}
		List<TaArticleDTO> liste3 = taArticleService.findAllLight();
		for (TaArticleDTO obj : liste3) {
			listeTaArticle.add(obj.getCodeArticle());
		}
	}
	public void raz() {
//		if(modelDocument!=null)modelDocument.clear();
//		if(filteredValues!=null)filteredValues.clear();
//		ligneTotaux=new MouvementStocksDTO();
	}
	public void actRecherche(ActionEvent e) {
		try{
			
			String codeEntrepot=null;
			if(taEntrepot!=null)codeEntrepot=taEntrepot.getCodeEntrepot();
			
			List<MouvementStocksDTO> listeTemp =new LinkedList<MouvementStocksDTO>();
			modelDocument=new LinkedList<MouvementStocksDTO>();
			filteredValues=new LinkedList<MouvementStocksDTO>();
			
			modelDocument=taMouvStockService.etatStocksEntrepotEmplacement(dateDebut, dateFin,codeEntrepot,emplacement,parFamille,afficherLesTermine);
			
			for (MouvementStocksDTO mouvementStocksDTO : modelDocument) {
				filteredValues.add(mouvementStocksDTO);
			}
			ligneTotaux= calculTotaux();
			etatChoixEditionBean.setTcheckEdition(true);
			
//			mapParametre.put("entrepot", taEntrepot.getCodeEntrepot());
//			mapParametre.put("enplacement", emplacement);
//			mapParametre.put("dateDebut", getDateDebut());
//			mapParametre.put("dateFin", getDateFin());
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	public void actAnnuler(ActionEvent e) {
			
		}
	
	
	
	public void detailDocument(){
//		String tabEcran="";
//		LgrTab lgrTab=LgrTab.getInstance();
//		if(detailLigne!=null){
//			tabEcran=lgrTab.getTabDocCorrespondance().get(detailLigne.getTypeDocument());
//			if(tabEcran!=null && !tabEcran.isEmpty()){
//				ouvertureDocumentBean.setEvent(new LgrTabEvent());
//				ouvertureDocumentBean.getEvent().setCodeObjet(detailLigne.getCodeDocument());
//				ouvertureDocumentBean.getEvent().setData(detailLigne);
//				ouvertureDocumentBean.getEvent().setCssLgrTab(tabEcran);
//				ouvertureDocumentBean.getEvent().setAfficheDoc(true);
//				ouvertureDocumentBean.openDocument(null);
//			}
//		}
	}
	
	
	
	public List<TaEntrepotDTO> entrepotAutoComplete(String query) {
		List<TaEntrepotDTO> allValues = taEntrepotService.selectAllDTO();
		TaEntrepotDTO entrepotTous=new TaEntrepotDTO();
		entrepotTous.setCodeEntrepot(Const.C_CHOISIR);
		entrepotTous.setLibelle("Selectionner un entrepôt");
		allValues.add(0,entrepotTous);
		List<TaEntrepotDTO> filteredValues = new ArrayList<TaEntrepotDTO>();


		for (int i = 0; i < allValues.size(); i++) {
			TaEntrepotDTO obj = allValues.get(i);
			if(query.equals("*") || obj.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(obj);
			}
		}
		return filteredValues;
	}
	
	public List<String> emplacementAutoComplete(String query) {
		List<String> filteredValues = new ArrayList<String>();
		List<String> allValues= new ArrayList<String>(); 
		allValues.add(0,Const.C_CHOISIR);
		if(taEntrepot!=null ) {
			allValues = taEtatStockService.emplacementEntrepot(taEntrepot.getCodeEntrepot(),false);
			allValues.add(0,Const.C_CHOISIR);
			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(query.equals("*") || civ.toLowerCase().contains(query.toLowerCase())) {
					if(!civ.isEmpty())filteredValues.add(civ);
				}
			}
		}
		return filteredValues;
	}
	
	public void actImprimerListeEtatDesStocks(ActionEvent actionEvent) {
		
		try {
				
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			mapParametre = new HashMap<String,Object>();
			
			sessionMap.remove("parametre");

			if(taEntrepot==null) {

					 mapParametre.put("entrepot", "Selectionner un entrepôt");
				
				} else {
					mapParametre.put("entrepot", taEntrepot.getLibelle());
				}
			
			mapParametre.put("enplacement", emplacement);
			mapParametre.put("dateDebut", getDateDebut());
			mapParametre.put("dateFin", getDateFin());
			
			sessionMap.put("parametre", mapParametre);
//			sessionMap.put("listeEtatDesStocks", modelDocument);
			sessionMap.put("listeEtatDesStocks", filteredValues);

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

	

	public List<MouvementStocksDTO> getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(List<MouvementStocksDTO> modelDocument) {
		this.modelDocument = modelDocument;
	}

	public MouvementStocksDTO[] getSelection() {
		return selection;
	}

	public void setSelection(MouvementStocksDTO[] selection) {
		this.selection = selection;
	}

	

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public MouvementStocksDTO getDetailLigne() {
		return detailLigne;
	}

	public void setDetailLigne(MouvementStocksDTO detailLigne) {
		this.detailLigne = detailLigne;
	}


	public TaEntrepotDTO getTaEntrepot() {
		return taEntrepot;
	}


	public void setTaEntrepot(TaEntrepotDTO taEntrepot) {
		this.taEntrepot = taEntrepot;
	}


	public List<String> getListeTaEntrepot() {
		return listeTaEntrepot;
	}


	public void setListeTaEntrepot(List<String> listeTaEntrepot) {
		this.listeTaEntrepot = listeTaEntrepot;
	}


	public TaFamilleDTO getTaFamille() {
		return taFamille;
	}


	public void setTaFamille(TaFamilleDTO taFamille) {
		this.taFamille = taFamille;
	}


	public List<String> getListeTaFamille() {
		return listeTaFamille;
	}


	public void setListeTaFamille(List<String> listeTaFamille) {
		this.listeTaFamille = listeTaFamille;
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


	public String getEmplacement() {
		return emplacement;
	}


	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
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
	

    public MouvementStocksDTO calculTotaux(){
    	MouvementStocksDTO total=new MouvementStocksDTO();
    	total.setDepart(BigDecimal.ZERO);
    	total.setEntree(BigDecimal.ZERO);
    	total.setSortie(BigDecimal.ZERO);
    	total.setDispo(BigDecimal.ZERO);
    	for (MouvementStocksDTO obj : filteredValues) {
    		total.setDepart(total.getDepart().add(obj.getDepart()));
    		total.setEntree(total.getEntree().add(obj.getEntree()));
    		total.setSortie(total.getSortie().add(obj.getSortie()));
    		total.setDispo(total.getDispo().add(obj.getDispo()));
		}
    	return total;
    }


	public boolean isParFamille() {
		return parFamille;
	}


	public void setParFamille(boolean parFamille) {
		this.parFamille = parFamille;
	}


	public MouvementStocksDTO getLigneTotaux() {
		return ligneTotaux;
	}


	public void setLigneTotaux(MouvementStocksDTO ligneTotaux) {
		this.ligneTotaux = ligneTotaux;
	}

	
	public void filterListener(FilterEvent filterEvent) {
		  ligneTotaux=calculTotaux();
		}


	public List<MouvementStocksDTO> getFilteredValues() {
		return filteredValues;
	}


	public void setFilteredValues(List<MouvementStocksDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}


	public boolean isAfficherLesTermine() {
		return afficherLesTermine;
	}


	public void setAfficherLesTermine(boolean afficherLesTermine) {
		this.afficherLesTermine = afficherLesTermine;
	}
	
}
