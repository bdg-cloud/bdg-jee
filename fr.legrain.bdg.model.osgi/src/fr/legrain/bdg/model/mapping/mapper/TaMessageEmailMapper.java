package fr.legrain.bdg.model.mapping.mapper;

import java.util.ArrayList;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.email.dto.TaContactMessageEmailDTO;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.email.model.TaPieceJointeEmail;


public class TaMessageEmailMapper implements ILgrMapper<TaMessageEmailDTO, TaMessageEmail> {

	@Override
	public TaMessageEmail mapDtoToEntity(TaMessageEmailDTO dto, TaMessageEmail entity) {
		if(entity==null)
			entity = new TaMessageEmail();

		entity.setIdEmail(dto.getId()!=null?dto.getId():0);
		
		entity.setSubject(dto.getSubject());
		entity.setBodyHtml(dto.getBodyHtml());
		entity.setBodyPlain(dto.getBodyPlain());
		entity.setDateCreation(dto.getDateCreation());
		entity.setDateEnvoi(dto.getDateEnvoi());
		
		entity.setAccuseReceptionLu(dto.isAccuseReceptionLu());
		entity.setLu(dto.isLu());
		entity.setRecu(dto.isRecu());
		entity.setSpam(dto.isSpam());
		entity.setSuivi(dto.isSuivi());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaMessageEmailDTO mapEntityToDto(TaMessageEmail entity, TaMessageEmailDTO dto) {
		if(dto==null)
			dto = new TaMessageEmailDTO();

		dto.setId(entity.getIdEmail());
		
		dto.setSubject(entity.getSubject());
		dto.setBodyHtml(entity.getBodyHtml());
		dto.setBodyPlain(entity.getBodyPlain());
		dto.setDateCreation(entity.getDateCreation());
		dto.setDateEnvoi(entity.getDateEnvoi());
		
		dto.setAccuseReceptionLu(entity.isAccuseReceptionLu());
		dto.setLu(entity.isLu());
		dto.setRecu(entity.isRecu());
		dto.setSpam(entity.isSpam());
		dto.setSuivi(entity.isSuivi());
		
		if(entity.getTaUtilisateur()!=null) dto.setIdUtilisateur(entity.getTaUtilisateur().getId());
		
		if(entity.getDestinataires()!=null) {
			dto.setDestinataires(new ArrayList<>());
			TaContactMessageEmailDTO contactDto = null;
			for (TaContactMessageEmail c : entity.getDestinataires()) {
				contactDto = new TaContactMessageEmailDTO();
				contactDto.setId(c.getIdContactMessageEmail());
				contactDto.setAdresseEmail(c.getAdresseEmail());
				if(c.getTaTiers()!=null) {
					contactDto.setIdTiers(c.getTaTiers().getIdTiers());
					contactDto.setNomTiers(c.getTaTiers().getNomTiers());
					contactDto.setPrenomTiers(c.getTaTiers().getPrenomTiers());
					contactDto.setCodeTiers(c.getTaTiers().getCodeTiers());
				}
				dto.getDestinataires().add(contactDto);
			}
		}
		
		if(entity.getPiecesJointes()!=null) {
			dto.setPiecesJointes(new ArrayList<>());
			TaPieceJointeEmailDTO pjDTO = null;
			for (TaPieceJointeEmail c : entity.getPiecesJointes()) {
				pjDTO = new TaPieceJointeEmailDTO();
				pjDTO.setId(c.getIdLogPieceJointeEmail());
				pjDTO.setNomFichier(c.getNomFichier());

				dto.getPiecesJointes().add(pjDTO);
			}
		}
		
		if(entity.getEtiquettes()!=null) {
			dto.setEtiquettes(new ArrayList<>());
			TaEtiquetteEmailDTO etiquetteDTO = null;
			Integer ordreMin = null;
			for (TaEtiquetteEmail c : entity.getEtiquettes()) {
				if(ordreMin==null) {
					ordreMin = c.getOrdre();
				}
				etiquetteDTO = new TaEtiquetteEmailDTO();
				etiquetteDTO.setId(c.getIdEtiquetteEmail());
				etiquetteDTO.setCode(c.getCode());
				etiquetteDTO.setLibelle(c.getLibelle());
				etiquetteDTO.setDescription(c.getDescription());
				etiquetteDTO.setSysteme(c.isSysteme());
				etiquetteDTO.setVisible(c.isVisible());
//				etiquetteDTO.setCouleur(c.getCouleur());

				dto.getEtiquettes().add(etiquetteDTO);
				
				if(c.getOrdre()!=null && (c.getOrdre() <= ordreMin)) {
					dto.setStyleClass(TaMessageEmailDTO.C_DEBUT_NOM_CLASSE_CSS_ETIQUETTE_EMAIL+c.getCode());
					ordreMin = c.getOrdre();
				}
			}
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
