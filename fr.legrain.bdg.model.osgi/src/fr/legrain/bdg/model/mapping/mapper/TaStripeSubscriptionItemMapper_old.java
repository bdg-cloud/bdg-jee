package fr.legrain.bdg.model.mapping.mapper;
//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
////import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem_old;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//
//
//public class TaStripeSubscriptionItemMapper implements ILgrMapper<TaStripeSubscriptionItemDTO, TaStripeSubscriptionItem_old> {
//
//	@Override
//	public TaStripeSubscriptionItem_old mapDtoToEntity(TaStripeSubscriptionItemDTO dto,
//			TaStripeSubscriptionItem_old entity) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionItemDTO mapEntityToDto(TaStripeSubscriptionItem_old entity, TaStripeSubscriptionItemDTO dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
////	@Override
////	public TaStripeSubscriptionItem mapDtoToEntity(TaStripeSubscriptionItemDTO dto, TaStripeSubscriptionItem entity) {
////		if(entity==null)
////			entity = new TaStripeSubscriptionItem();
////
////		entity.setIdStripeSubscriptionItem(dto.getId()!=null?dto.getId():0);
////		
////		entity.setIdExterne(dto.getIdExterne());
////		
////		
////		if(entity.getTaStripeSubscription()!=null) {
////			entity.getTaStripeSubscription().setIdExterne(dto.getIdExterneSubscription());
////			entity.getTaStripeSubscription().setIdStripeSubscription(dto.getIdStripeSubscription());
////		}
////		if(entity.getTaPlan()!=null) {
////			entity.getTaPlan().setIdExterne(dto.getIdExternePlan());
////			entity.getTaPlan().setIdStripePlan(dto.getIdStripePlan());
////		}
////		
////		entity.setVersionObj(dto.getVersionObj());
////
////		return entity;
////	}
////
////	@Override
////	public TaStripeSubscriptionItemDTO mapEntityToDto(TaStripeSubscriptionItem entity, TaStripeSubscriptionItemDTO dto) {
////		if(dto==null)
////			dto = new TaStripeSubscriptionItemDTO();
////
////		dto.setId(entity.getIdStripeSubscriptionItem());
////		
////		dto.setIdExterne(entity.getIdExterne());
////		
////		if(entity.getTaStripeSubscription()!=null) {
////			dto.setIdExterneSubscription(entity.getTaStripeSubscription().getIdExterne());
////			dto.setIdStripeSubscription(entity.getTaStripeSubscription().getIdStripeSubscription());
////		}
////		if(entity.getTaPlan()!=null) {
////			dto.setIdExternePlan(entity.getTaPlan().getIdExterne());
////			dto.setIdStripePlan(entity.getTaPlan().getIdStripePlan());
////		}
////		
////		dto.setVersionObj(entity.getVersionObj());
////
////		return dto;
////	}
//
//}
