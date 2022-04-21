package fr.legrain.bdg.webapp.solstyce;
//package fr.legrain.bdg.webapp.solstyce;
//
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.ejb.FinderException;
//import javax.ejb.RemoveException;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.view.ViewScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//
//import org.primefaces.event.SelectEvent;
//import org.primefaces.event.UnselectEvent;
//
//import fr.legrain.article.dto.TaEntrepotDTO;
//import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
//
//
//
//@Named
//@ViewScoped  
//public class TypeEntrepotController implements Serializable {  
//
//	private List<TaEntrepotDTO> values; 
//	private List<TaEntrepotDTO> filteredValues; 
//
//	private TaEntrepotDTO nouveau ;
//	private TaEntrepotDTO selection ;
//
//
//	private @EJB ITaEntrepotServiceRemote taEntrepotService;
//
//	@PostConstruct
//	public void postConstruct(){
//
//		try {
//			setTableVide(false);
//			values =  taEntrepotService.selectAllDTO();
//			if(values == null){
//				setTableVide(true);
//			}
//			selection = values.get(0);	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		this.setFilteredValues(values);
//	}
//
//	public TypeEntrepotController() {  
//	}  
//
//	public void create(ActionEvent actionEvent)
//	{
//
//		nouveau = new TaEntrepotDTO();
//	}
//
//	public void modification(ActionEvent actionEvent){
//
//		nouveau = selection;
//		creation = true;
//		modification = true;
//	}
//
//	public void suppression(){
//
//		try {
//			taEntrepotService.remove(selection);
//			selection = values.get(0);	
//		} catch (RemoveException e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
//			e.printStackTrace();
//		}
//		values = taEntrepotService.selectAll();
//		selection = null;
//	}
//	public void onChangeQ ()
//	{
//
//	}
//
//
//
//	public void doCreate(ActionEvent actionEvent){
//
//		try {if(nouveau.getCodeEntrepot() == null || nouveau.getCodeEntrepot().equals("") ){
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
//
//		}
//		else{ 
//			if(nouveau.getLibelle() == null || nouveau.getLibelle().equals("") ){
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
//
//			}
//			else{ 
//				if(taEntrepotService.findByCode(nouveau.getCodeEntrepot()) == null){
//					taEntrepotService.merge(nouveau, ITaEntrepotServiceRemote.validationContext);
//					values= taEntrepotService.selectAll();
//					selection = values.get(0);
//					creation = false;
//					modification = false;
//					nouveau = new TaEntrepotDTO();
//				}else{
//					if(modification == true){
//						taEntrepotService.merge(nouveau, ITaEntrepotServiceRemote.validationContext);
//						values= taEntrepotService.selectAll();
//						selection = values.get(0);
//						creation = false;
//						modification = false;
//						nouveau = new TaEntrepotDTO();
//					}
//					else{
//						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
//					}
//				}}}
//		} catch (FinderException e) {
//
//			e.printStackTrace();
//		}
//
//
//	}
//	public void annulerCreation(ActionEvent actionEvent){
//
//
//		values= taEntrepotService.selectAll();
//		if(values.size()>= 1){
//
//			selection = values.get(0);
//		}		nouveau = new TaEntrepotDTO();
//		creation = false;
//		modification = false;
//	}
//
//
//
//	public List<TaEntrepotDTO> getValues(){  
//		//List<Value> l = new LinkedList<Value>();
//		return values;
//
//	}
//
//
//	public Boolean getModification() {
//		return modification;
//	}
//	public void setModification(Boolean modification) {
//		this.modification = modification;
//	}
//
//
//
//
//	public TaEntrepotDTO getNouveau() {
//		return nouveau;
//	}
//
//	public void setNouveau(TaEntrepotDTO newTaEntrepot) {
//		this.nouveau = newTaEntrepot;
//	}
//
//	public TaEntrepotDTO getSelection() {
//		return selection;
//	}
//
//	public void setSelection(TaEntrepotDTO selectedTaEntrepot) {
//		this.selection = selectedTaEntrepot;
//	}
//
//	public void setValues(List<TaEntrepotDTO> values) {
//		this.values = values;
//	}
//
//	public Boolean getCreation() {
//		return creation;
//	}
//
//	public void setCreation(Boolean creationE) {
//		this.creation = creationE;
//	}
//
//	public Boolean getSelectionne() {
//		return selectionne;
//	}
//
//	public void setSelectionne(Boolean selectionne) {
//		this.selectionne = selectionne;
//	}
//
//	public void onRowSelect(SelectEvent event){
//		selectionne = true;
//	}
//
//	public void onRowUnSelect(UnselectEvent event){
//		selectionne = false;
//	}
//
//
//	public List<TaEntrepotDTO> getFilteredValues() {
//		return filteredValues;
//	}
//
//	public void setFilteredValues(List<TaEntrepotDTO> filteredValues) {
//		this.filteredValues = filteredValues;
//	}
//
//
//	public Boolean getTableVide() {
//		return tableVide;
//	}
//
//	public void setTableVide(Boolean tableVide) {
//		this.tableVide = tableVide;
//	}
//
//
//
//}  
