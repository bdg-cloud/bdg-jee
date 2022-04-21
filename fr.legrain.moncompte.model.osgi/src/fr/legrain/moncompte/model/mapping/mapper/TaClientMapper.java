package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaClientMapper implements ILgrMapper<TaClientDTO, TaClient> {

	@Override
	public TaClient mapDtoToEntity(TaClientDTO dto, TaClient entity) {
		if(entity==null)
			entity = new TaClient();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setNom(dto.getNom());
		entity.setPrenom(dto.getPrenom());
		
		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setEmail(dto.getEmail());
		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setUsername(dto.getUsername());
		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setPasswd(dto.getPasswd());
		
		if(entity.getAdresse1()!=null && dto.getAdresse1()!=null) {
			entity.getAdresse1().setAdresse1(dto.getAdresse1().getAdresse1());
			entity.getAdresse1().setAdresse2(dto.getAdresse1().getAdresse2());
			entity.getAdresse1().setAdresse3(dto.getAdresse1().getAdresse3());
			entity.getAdresse1().setCodePostal(dto.getAdresse1().getCodePostal());
			entity.getAdresse1().setVille(dto.getAdresse1().getVille());
			entity.getAdresse1().setPays(dto.getAdresse1().getPays());
			
			entity.getAdresse1().setNomEntreprise(dto.getAdresse1().getNomEntreprise());
			entity.getAdresse1().setEmail(dto.getAdresse1().getEmail());
			entity.getAdresse1().setWeb(dto.getAdresse1().getWeb());
			entity.getAdresse1().setNumFax(dto.getAdresse1().getNumFax());
			entity.getAdresse1().setNumPort(dto.getAdresse1().getNumPort());
			entity.getAdresse1().setNumTel1(dto.getAdresse1().getNumTel1());
			entity.getAdresse1().setNumTel2(dto.getAdresse1().getNumTel2());
		}
		if(entity.getAdresse2()!=null && dto.getAdresse2()!=null) {
			entity.getAdresse2().setAdresse1(dto.getAdresse2().getAdresse1());
			entity.getAdresse2().setAdresse2(dto.getAdresse2().getAdresse2());
			entity.getAdresse2().setAdresse3(dto.getAdresse2().getAdresse3());
			entity.getAdresse2().setCodePostal(dto.getAdresse2().getCodePostal());
			entity.getAdresse2().setVille(dto.getAdresse2().getVille());
			entity.getAdresse2().setPays(dto.getAdresse2().getPays());
			
			entity.getAdresse2().setNomEntreprise(dto.getAdresse2().getNomEntreprise());
			entity.getAdresse2().setEmail(dto.getAdresse2().getEmail());
			entity.getAdresse2().setWeb(dto.getAdresse2().getWeb());
			entity.getAdresse2().setNumFax(dto.getAdresse2().getNumFax());
			entity.getAdresse2().setNumPort(dto.getAdresse2().getNumPort());
			entity.getAdresse2().setNumTel1(dto.getAdresse2().getNumTel1());
			entity.getAdresse2().setNumTel2(dto.getAdresse2().getNumTel2());
		}
		if(entity.getAdresse3()!=null && dto.getAdresse3()!=null) {
			entity.getAdresse3().setAdresse1(dto.getAdresse3().getAdresse1());
			entity.getAdresse3().setAdresse2(dto.getAdresse3().getAdresse2());
			entity.getAdresse3().setAdresse3(dto.getAdresse3().getAdresse3());
			entity.getAdresse3().setCodePostal(dto.getAdresse3().getCodePostal());
			entity.getAdresse3().setVille(dto.getAdresse3().getVille());
			entity.getAdresse3().setPays(dto.getAdresse3().getPays());
			
			entity.getAdresse3().setNomEntreprise(dto.getAdresse3().getNomEntreprise());
			entity.getAdresse3().setEmail(dto.getAdresse3().getEmail());
			entity.getAdresse3().setWeb(dto.getAdresse3().getWeb());
			entity.getAdresse3().setNumFax(dto.getAdresse3().getNumFax());
			entity.getAdresse3().setNumPort(dto.getAdresse3().getNumPort());
			entity.getAdresse3().setNumTel1(dto.getAdresse3().getNumTel1());
			entity.getAdresse3().setNumTel2(dto.getAdresse3().getNumTel2());
		}

		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaClientDTO mapEntityToDto(TaClient entity, TaClientDTO dto) {
		if(dto==null)
			dto = new TaClientDTO();

		dto.setId(entity.getId());
		
		dto.setCode(entity.getCode());
		dto.setNom(entity.getNom());
		dto.setPrenom(entity.getPrenom());;
		
		dto.setIdentifiantFtp(entity.getIdentifiantFtp());
		dto.setMdpFtp(entity.getMdpFtp());
		dto.setServeurFtp(entity.getServeurFtp());
		
		if(entity.getTaUtilisateur()!=null) dto.setUsername(entity.getTaUtilisateur().getUsername());
		if(entity.getTaUtilisateur()!=null) dto.setEmail(entity.getTaUtilisateur().getEmail());
		if(entity.getTaUtilisateur()!=null) dto.setPasswd(entity.getTaUtilisateur().getPasswd());
		
		if(entity.getAdresse1()!=null) {
			if(dto.getAdresse1()==null)dto.setAdresse1(new TaAdresseDTO());
			dto.getAdresse1().setAdresse1(entity.getAdresse1().getAdresse1());
			dto.getAdresse1().setAdresse2(entity.getAdresse1().getAdresse2());
			dto.getAdresse1().setAdresse3(entity.getAdresse1().getAdresse3());
			dto.getAdresse1().setCodePostal(entity.getAdresse1().getCodePostal());
			dto.getAdresse1().setVille(entity.getAdresse1().getVille());
			dto.getAdresse1().setPays(entity.getAdresse1().getPays());
			
			dto.getAdresse1().setNomEntreprise(entity.getAdresse1().getNomEntreprise());
			dto.getAdresse1().setEmail(entity.getAdresse1().getEmail());
			dto.getAdresse1().setNumFax(entity.getAdresse1().getNumFax());
			dto.getAdresse1().setNumPort(entity.getAdresse1().getNumPort());
			dto.getAdresse1().setNumTel1(entity.getAdresse1().getNumTel1());
			dto.getAdresse1().setNumTel2(entity.getAdresse1().getNumTel2());
			dto.getAdresse1().setWeb(entity.getAdresse1().getWeb());
			dto.getAdresse1().setVersionObj(entity.getAdresse1().getVersionObj());
		}
		if(entity.getAdresse2()!=null) {
			if(dto.getAdresse2()==null)dto.setAdresse2(new TaAdresseDTO());
			dto.getAdresse2().setAdresse1(entity.getAdresse2().getAdresse1());
			dto.getAdresse2().setAdresse2(entity.getAdresse2().getAdresse2());
			dto.getAdresse2().setAdresse3(entity.getAdresse2().getAdresse3());
			dto.getAdresse2().setCodePostal(entity.getAdresse2().getCodePostal());
			dto.getAdresse2().setVille(entity.getAdresse2().getVille());
			dto.getAdresse2().setPays(entity.getAdresse2().getPays());
			
			dto.getAdresse2().setNomEntreprise(entity.getAdresse2().getNomEntreprise());
			dto.getAdresse2().setEmail(entity.getAdresse2().getEmail());
			dto.getAdresse2().setNumFax(entity.getAdresse2().getNumFax());
			dto.getAdresse2().setNumPort(entity.getAdresse2().getNumPort());
			dto.getAdresse2().setNumTel1(entity.getAdresse2().getNumTel1());
			dto.getAdresse2().setNumTel2(entity.getAdresse2().getNumTel2());
			dto.getAdresse2().setWeb(entity.getAdresse2().getWeb());
			dto.getAdresse2().setVersionObj(entity.getAdresse2().getVersionObj());
		}
		if(entity.getAdresse3()!=null) {
			if(dto.getAdresse3()==null)dto.setAdresse3(new TaAdresseDTO());
			dto.getAdresse3().setAdresse1(entity.getAdresse3().getAdresse1());
			dto.getAdresse3().setAdresse2(entity.getAdresse3().getAdresse2());
			dto.getAdresse3().setAdresse3(entity.getAdresse3().getAdresse3());
			dto.getAdresse3().setCodePostal(entity.getAdresse3().getCodePostal());
			dto.getAdresse3().setVille(entity.getAdresse3().getVille());
			dto.getAdresse3().setPays(entity.getAdresse3().getPays());
			
			dto.getAdresse3().setNomEntreprise(entity.getAdresse3().getNomEntreprise());
			dto.getAdresse3().setEmail(entity.getAdresse3().getEmail());
			dto.getAdresse3().setNumFax(entity.getAdresse3().getNumFax());
			dto.getAdresse3().setNumPort(entity.getAdresse3().getNumPort());
			dto.getAdresse3().setNumTel1(entity.getAdresse3().getNumTel1());
			dto.getAdresse3().setNumTel2(entity.getAdresse3().getNumTel2());
			dto.getAdresse3().setWeb(entity.getAdresse3().getWeb());
			dto.getAdresse3().setVersionObj(entity.getAdresse3().getVersionObj());

		}
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
