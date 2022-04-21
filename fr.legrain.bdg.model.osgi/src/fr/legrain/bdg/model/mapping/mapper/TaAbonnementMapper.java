package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.lib.data.LibDate;


public class TaAbonnementMapper implements ILgrMapper<TaAbonnementDTO, TaAbonnement> {

	@Override
	public TaAbonnement mapDtoToEntity(TaAbonnementDTO dto, TaAbonnement entity) {
		if(entity==null)
			entity = new TaAbonnement();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		entity.setDateSuspension(dto.getDateSuspension());
		entity.setSuspension(dto.getSuspension());
		
		if(entity.getTaTiers()!=null) {
			entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		}
		entity.setTypeAbonnement(dto.getTypeAbonnement());
		
		entity.setVersionObj(dto.getVersionObj());
		
		entity.setDateDebutPeriodeActive(dto.getDateDebutPeriodeActive());
		entity.setDateFinPeriodeActive(dto.getDateFinPeriodeActive());
		
		entity.setDateFinEngagement(dto.getDateFinEngagement());
		
		entity.setReconductionTacite(dto.getReconductionTacite());
		entity.setReconductionTaciteEngagement(dto.getReconductionTaciteEngagement());
		entity.setNbMoisDureeEngagement(dto.getNbMoisDureeEngagement());
		entity.setNbMoisFrequenceFacturation(dto.getNbMoisFrequenceFacturation());
		entity.setNbMoisFrequencePaiement(dto.getNbMoisFrequencePaiement());

		return entity;
	}

	@Override
	public TaAbonnementDTO mapEntityToDto(TaAbonnement entity, TaAbonnementDTO dto) {
		if(dto==null)
			dto = new TaAbonnementDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		dto.setDateSuspension(entity.getDateSuspension());
		dto.setSuspension(entity.getSuspension());
		
		if(entity.getTaTiers()!=null) {
			dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
			dto.setNomTiers(entity.getTaTiers().getNomTiers());
		}
		
//		if(entity.getTaStripeSubscription()!=null) {
//			dto.setDateDebut(entity.getTaStripeSubscription().getDateDebut());
//			//dto.setDateDebut(entity.getTaStripeSubscription().getDateFin());
//		}
		dto.setDateDebut(entity.getDateDebut());
		dto.setDateFin(entity.getDateFin());
		
		dto.setDateAnnulation(entity.getDateAnnulation());
		dto.setCommentaireAnnulation(entity.getCommentaireAnnulation());
		
		if(entity.getTaFrequence() != null) {
			dto.setCodeFrequence(entity.getTaFrequence().getCodeFrequence());
			dto.setIdFrequence(entity.getTaFrequence().getIdFrequence());
			dto.setLiblFrequence(entity.getTaFrequence().getLiblFrequence());
		}
		
		dto.setDateDebutPeriodeActive(entity.getDateDebutPeriodeActive());
		dto.setDateFinPeriodeActive(entity.getDateFinPeriodeActive());
		dto.setTypeAbonnement(entity.getTypeAbonnement());
		
		dto.setVersionObj(entity.getVersionObj());
		dto.setDateFinEngagement(entity.getDateFinEngagement());
		dto.setReconductionTacite(entity.getReconductionTacite());
		dto.setReconductionTaciteEngagement(entity.getReconductionTaciteEngagement());
		dto.setNbMoisDureeEngagement(entity.getNbMoisDureeEngagement());
		dto.setNbMoisFrequenceFacturation(entity.getNbMoisFrequenceFacturation());
		dto.setNbMoisFrequencePaiement(entity.getNbMoisFrequencePaiement());

		return dto;
	}

}
