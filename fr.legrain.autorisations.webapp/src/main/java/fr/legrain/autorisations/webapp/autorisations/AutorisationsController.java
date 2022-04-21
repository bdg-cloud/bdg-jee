package fr.legrain.autorisations.webapp.autorisations;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.bdg.autorisations.service.remote.ITaAutorisationsServiceRemote;
import fr.legrain.bdg.autorisations.service.remote.ITaTypeProduitServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@ManagedBean
@ViewScoped  
public class AutorisationsController {  

	
	private @EJB ITaTypeProduitServiceRemote taTypeProduitService;
	
	private List<TaAutorisations> values; 
	private List<TaAutorisations> filteredValues;
	private TaTypeProduit typeProduit;
	private TaAutorisations nouveau ;
	private TaAutorisations selection ;
	private Boolean tableVide;
	
	 private UploadedFile file ;
	
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaAutorisationsServiceRemote TaAutorisationsService;

	@PostConstruct
	public void postConstruct(){
		try {
			setTableVide(false);
			refresh();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty())selection = values.get(0);	
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}
	
	public AutorisationsController() {  
	}
	
	public void refresh(){
		values = TaAutorisationsService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
		
	}

  
	
	public void actFermerDialog(ActionEvent actionEvent) {
		 PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actAnnuler(ActionEvent actionEvent){
		//		values= TaAutorisationsService.selectAll();

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaAutorisations();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			if(nouveau.getCodeDossier() == null || nouveau.getCodeDossier().equals("") ){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code tiers obligatoire"));
			}
			else{ 
				if(nouveau.getModules() == null || nouveau.getModules().equals("") ){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ modules obligatoire"));
				}
				else{ 
						if(TaAutorisationsService.findByCode(nouveau.getCodeDossier()) == null){
							TaAutorisations retour = TaAutorisationsService.merge(nouveau, ITaAutorisationsServiceRemote.validationContext);
							values= TaAutorisationsService.selectAll();
							selection = values.get(0);
							nouveau = new TaAutorisations();

							modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							
							if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
								PrimeFaces.current().dialog().closeDynamic(retour);
							}
						}else{
							if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
								TaAutorisationsService.merge(nouveau, ITaAutorisationsServiceRemote.validationContext);
								values= TaAutorisationsService.selectAll();
								selection = values.get(0);
								nouveau = new TaAutorisations();

								modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							}
							else{
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
							}
						}
					}
				}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<TaTypeProduit> typeProduitAutoComplete(String query) {
        List<TaTypeProduit> allValues = taTypeProduitService.selectAll();
        List<TaTypeProduit> filteredValues = new ArrayList<TaTypeProduit>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTypeProduit obj = allValues.get(i);
            if(query.equals("*") || obj.getCode().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaAutorisations();

		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			TaAutorisationsService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = TaAutorisationsService.selectAll();
		selection = null;
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
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
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
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

	public List<TaAutorisations> getValues(){  
		return values;
	}

	public TaAutorisations getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaAutorisations newTaAutorisations) {
		this.nouveau = newTaAutorisations;
	}

	public TaAutorisations getSelection() {
		return selection;
	}

	public void setSelection(TaAutorisations selectedTaAutorisations) {
		this.selection = selectedTaAutorisations;
	}

	public void setValues(List<TaAutorisations> values) {
		this.values = values;
	}

	public List<TaAutorisations> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaAutorisations> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}

	public TaTypeProduit getTypeProduit() {
		return typeProduit;
	}

	public void setTypeProduit(TaTypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
	}
	

	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
				if(nomChamp.equals("code")) {
						TaTypeProduit entity = new TaTypeProduit();
						if(value instanceof TaTypeProduit)entity=(TaTypeProduit) value;
						else	entity = taTypeProduitService.findByCode((String)value);
						nouveau.setTaTypeProduit(entity);
					
				}
				
			return false;

		} catch (Exception e) {
			
		}
		return false;
	}

    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }

//    public void upload(FileUploadEvent event) {  
//        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        // Do what you want with the file        
//        try {
//            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// 
//    } 
    
    public void upload(FileUploadEvent event) {
    	file=event.getFile();
        if(file != null) {
        	FacesMessage msg = new FacesMessage("Success! ", file.getFileName() + " is uploaded.");  
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
            try {
            	BufferedReader buffer = new BufferedReader(new InputStreamReader(file.getInputStream()));
            	StringBuilder out = new StringBuilder();
            	String line;
            	while ((line = buffer.readLine()) != null) {
            	    out.append(line);   // add everything to StringBuilder
//            	    // here you can have your logic of comparison.
//            	    if(line.toString().equals(".")) {
//            	        // do something
//            	    }

            	} 
            	
            	nouveau.setModules(out.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
    }

}  
