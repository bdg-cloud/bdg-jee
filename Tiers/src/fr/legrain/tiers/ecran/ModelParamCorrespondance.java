package fr.legrain.tiers.ecran;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.gestCom.Appli.ModelGeneralObjetEJB;
import fr.legrain.gestCom.Module_Tiers.SWTTiers;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaParamCorrespondanceDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

public class ModelParamCorrespondance extends ModelGeneralObjetEJB<TaParamCorrespondanceDTO, TaParamCorrespondanceDTO>{
	
	//AbstractLgrDAO dao=null;
	//private LinkedList<IHMEnteteDocument> listeObjet = new LinkedList<IHMEnteteDocument>();
	public ModelParamCorrespondance(ITaTiersServiceRemote dao, Class<TaTiers> typeObjet ) {
		//super(dao , typeObjet);
		super(dao);
		//this.dao=dao;
		//setListeEntity(new LinkedList<TaTiers>());
		setListeEntity(new LinkedList<TaParamCorrespondanceDTO>());
	}

	//EntityManager em =new TaFactureDAO().getEntityManager();

	private String selectedTypeSelection=null;
	
	public LinkedList<TaParamCorrespondanceDTO> remplirListe(TaTiers tiers) {
		getListeEntity().clear();
		getListeObjet().clear();
		List<TaParamCorrespondanceDTO> listeFinal =	new ArrayList<TaParamCorrespondanceDTO>(0);
		List<TaParamCorrespondanceDTO> listeAdresse =	new ArrayList<TaParamCorrespondanceDTO>(0);
		List<TaParamCorrespondanceDTO> listeEmail =	new ArrayList<TaParamCorrespondanceDTO>(0);
		List<TaParamCorrespondanceDTO> listeTelephone =	new ArrayList<TaParamCorrespondanceDTO>(0);
		
		TaParamCorrespondanceDTO ihm = null;
		for(TaAdresse adr : tiers.getTaAdresses()) {
			String adresse = "";
			if(adr.getAdresse1Adresse()!=null) adresse+= adr.getAdresse1Adresse()+" ";
			if(adr.getAdresse2Adresse()!=null) adresse+= adr.getAdresse2Adresse()+" ";
			if(adr.getAdresse3Adresse()!=null) adresse+= adr.getAdresse3Adresse()+" ";
			if(adr.getCodepostalAdresse()!=null) adresse+= adr.getCodepostalAdresse()+" ";
			if(adr.getVilleAdresse()!=null) adresse+= adr.getVilleAdresse()+" ";
			if(adr.getPaysAdresse()!=null) adresse+= adr.getPaysAdresse()+" ";
			ihm = new TaParamCorrespondanceDTO();
			ihm.setType(TaParamCorrespondanceDTO.TYPE_ADRESSE);
			ihm.setId(adr.getIdAdresse());
			ihm.setLibelle(adresse);
			ihm.setAdministratif(LibConversion.intToBoolean(adr.getCommAdministratifAdresse()));
			ihm.setCommercial(LibConversion.intToBoolean(adr.getCommCommercialAdresse()));
			listeAdresse.add(ihm);
			ihm=null;
		}
		ihm = new TaParamCorrespondanceDTO();
		ihm.setLibelle("Adresse");
		ihm.setType(TaParamCorrespondanceDTO.TYPE_ADRESSE);
		ihm.setList(listeAdresse);
		listeFinal.add(ihm);
		
		for(TaEmail adr : tiers.getTaEmails()) {
			ihm = new TaParamCorrespondanceDTO();
			ihm.setType(TaParamCorrespondanceDTO.TYPE_EMAIL);
			ihm.setId(adr.getIdEmail());
			ihm.setLibelle(adr.getAdresseEmail());
			ihm.setAdministratif(LibConversion.intToBoolean(adr.getCommAdministratifEmail()));
			ihm.setCommercial(LibConversion.intToBoolean(adr.getCommCommercialEmail()));
			listeEmail.add(ihm);
			ihm=null;
		}
		ihm = new TaParamCorrespondanceDTO();
		ihm.setLibelle("EMail");
		ihm.setType(TaParamCorrespondanceDTO.TYPE_EMAIL);
		ihm.setList(listeEmail);
		listeFinal.add(ihm);
		
		for(TaTelephone adr : tiers.getTaTelephones()) {
			ihm = new TaParamCorrespondanceDTO();
			ihm.setType(TaParamCorrespondanceDTO.TYPE_TELEPHONE);
			ihm.setId(adr.getIdTelephone());
			ihm.setLibelle(adr.getNumeroTelephone());
			ihm.setAdministratif(LibConversion.intToBoolean(adr.getCommAdministratifTelephone()));
			ihm.setCommercial(LibConversion.intToBoolean(adr.getCommCommercialTelephone()));
			listeTelephone.add(ihm);
			ihm=null;
		}
		ihm = new TaParamCorrespondanceDTO();
		ihm.setLibelle("Telephone");
		ihm.setType(TaParamCorrespondanceDTO.TYPE_TELEPHONE);
		ihm.setList(listeTelephone);
		listeFinal.add(ihm);
		
		setListeEntity(listeFinal);

		if(getListeEntity()!=null && getListeEntity().size()>0)
			getListeObjet().addAll(this.remplirListe());

		return getListeObjet();
	}
	

	public LinkedList<TaParamCorrespondanceDTO> remplirListe() {
		SWTTiers ihm=null;
		LinkedList<TaParamCorrespondanceDTO> listeObjet=new LinkedList<TaParamCorrespondanceDTO>();
		
		listeObjet.clear();
		for (Object t : getListeEntity()) {
			listeObjet.add((TaParamCorrespondanceDTO)t);
			ihm=null;
		}
		return listeObjet;
	}

}
