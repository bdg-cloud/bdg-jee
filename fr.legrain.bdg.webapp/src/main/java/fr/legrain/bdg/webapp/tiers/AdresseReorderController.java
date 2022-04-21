package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;

import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class AdresseReorderController implements Serializable {  

	private TaTiers masterEntity;
	
	private List<TaAdresseDTO> values=new LinkedList<TaAdresseDTO>(); 
	private List<TaAdresseDTO> filteredValues=new LinkedList<TaAdresseDTO>(); 
	private TaAdresseDTO nouveau ;
	private TaAdresseDTO selection ;
	
	private String codeTAdr;
	private TaTAdr selectedTypeAdresse;
	private List<TaTAdr> listeTypeAdresse;
	
	private TaAdresse taAdresse = new TaAdresse();
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaTAdrServiceRemote taTAdrService;
	private @EJB ITaTiersServiceRemote taTiersService;
	
	private LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModel  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
	private LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUI  = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();

	@PostConstruct
	public void postConstruct(){

		try {

			listeTypeAdresse=taTAdrService.selectAll();
			if(listeTypeAdresse!=null && !listeTypeAdresse.isEmpty()){
				selectedTypeAdresse=listeTypeAdresse.get(0);
				if(codeTAdr==null)
					codeTAdr=selectedTypeAdresse.getCodeTAdr();
				else{
					selectionTADr(getCodeTAdr());
					refresh();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectionTADr(String codeTAdr){
		for (TaTAdr obj : listeTypeAdresse) {
			if(obj.getCodeTAdr().equals(codeTAdr))
				selectedTypeAdresse=obj;
		}
	}
	
	public void refresh(){
		if(masterEntity!=null) {
			values = new ArrayList<>();			
			for (TaAdresse taAdresse : masterEntity.getTaAdresses()) {
				if(selectedTypeAdresse!=null){
					if(taAdresse.getTaTAdr()!=null && selectedTypeAdresse!=null && 
							selectedTypeAdresse.getCodeTAdr().equals(taAdresse.getTaTAdr().getCodeTAdr()))
						values.add(mapperModelToUI.map(taAdresse, TaAdresseDTO.class));
				}else{
					values.add(mapperModelToUI.map(taAdresse, TaAdresseDTO.class));
				}
			}
		}
	}
	
	public AdresseReorderController() {  
	}  





	
	
//	public void onRowSelect(SelectEvent event) {  
//		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
//			nouveau = selection;
//		}
//	}

	public List<TaAdresseDTO> getValues(){  
		return values;
	}

	public TaAdresseDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaAdresseDTO newTaTAdr) {
		this.nouveau = newTaTAdr;
	}

	public TaAdresseDTO getSelection() {
		return selection;
	}

	public void setSelection(TaAdresseDTO selectedTaTAdr) {
		this.selection = selectedTaTAdr;
	}

	public void setValues(List<TaAdresseDTO> values) {
		this.values = values;
	}

	public List<TaAdresseDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaAdresseDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaAdresseDTO temp=new TaAdresseDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taAdresseService.validateEntityProperty(temp, nomChamp, ITaTAdrServiceRemote.validationContext );
//
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaAdresseDTO>> violations = factory.getValidator().validateValue(TaAdresseDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaAdresseDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public boolean validateUIField(String nomChamp,Object value) {
		try {

			if(nomChamp.equals(Const.C_CODE_T_ADR)) {
				boolean changement=false;
				if(selection.getCodeTAdr()!=null && value!=null && !selection.getCodeTAdr().equals(""))
				{
					if(value instanceof TaTAdr)
						changement=((TaTAdr) value).getCodeTAdr().equals(selection.getCodeTAdr());
					else if(value instanceof String)
					changement=!value.equals(selection.getCodeTAdr());
				}
				if(changement && modeEcran.dataSetEnModeModification()){
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Type d'adresse", Const.C_MESSAGE_CHANGEMENT_CODE));
				}
			}

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}			

	
	
	
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	// Dima -  Fin

	public TaTAdr getSelectedTypeAdresse() {
		return selectedTypeAdresse;
	}

	public void setSelectedTypeAdresse(TaTAdr selectedTypeAdresse) {
		this.selectedTypeAdresse = selectedTypeAdresse;
	}

	public List<TaTAdr> getListeTypeAdresse() {
		return listeTypeAdresse;
	}

	public void setListeTypeAdresse(List<TaTAdr> listeTypeAdresse) {
		this.listeTypeAdresse = listeTypeAdresse;
	}

	public void onReorder() {
		int i=1;
		for (TaAdresseDTO taAdresseDTO : values) {
			taAdresseDTO.setOrdre(i);
			i++;
		}
	}
	
	public TaAdresse rechercheAdresseDansTiers(TaAdresseDTO dto){
		for (TaAdresse adr : masterEntity.getTaAdresses()) {
			if(adr.getIdAdresse()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}
	
	public void actEnregistrerReorder() {
		for (TaAdresseDTO taAdresseDTO : values) {
			try {
//				TaAdresse adresse = taAdresseService.findById(taAdresseDTO.getId());
				TaAdresse adresse=rechercheAdresseDansTiers(taAdresseDTO);
//				masterEntity.removeAdresse(adresse);
				adresse.setOrdre(taAdresseDTO.getOrdre());
//				adresse=taAdresseService.merge(adresse);
//				masterEntity.addAdresse(adresse);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		try {
//			masterEntity=taTiersService.findByCode(masterEntity.getCodeTiers());
//			masterEntity=taTiersService.merge(masterEntity);
//			masterEntity=taTiersService.findByCode(masterEntity.getCodeTiers());
//		} catch (FinderException e) {
//
//		}
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}

	public String getCodeTAdr() {
		return codeTAdr;
	}

	public void setCodeTAdr(String codeTAdr) {
		this.codeTAdr = codeTAdr;
	}

}  
