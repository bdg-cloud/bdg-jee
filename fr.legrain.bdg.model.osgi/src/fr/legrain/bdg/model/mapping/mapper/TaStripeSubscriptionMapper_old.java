//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//
//
//public class TaStripeSubscriptionMapper_old implements ILgrMapper<TaStripeSubscriptionDTO, TaStripeSubscription_old> {
//
//	@Override
//	public TaStripeSubscription_old mapDtoToEntity(TaStripeSubscriptionDTO dto, TaStripeSubscription_old entity) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionDTO mapEntityToDto(TaStripeSubscription_old entity, TaStripeSubscriptionDTO dto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
////	@Override
////	public TaStripeSubscription mapDtoToEntity(TaStripeSubscriptionDTO dto, TaStripeSubscription entity) {
////		if(entity==null)
////			entity = new TaStripeSubscription();
////
////		entity.setIdStripeSubscription(dto.getId()!=null?dto.getId():0);
////		
////		entity.setIdExterne(dto.getIdExterne());
////		entity.setQuantity(dto.getQuantity());
////		entity.setStatus(dto.getStatus());
////		
////		if(entity.getTaStripeCustomer()!=null) {
////			entity.getTaStripeCustomer().setIdExterne(dto.getIdExterneCustomer());
////			entity.getTaStripeCustomer().setIdStripeCustomer(dto.getIdStripeCustomer());
////		}
////		
////		entity.setVersionObj(dto.getVersionObj());
////
////		return entity;
////	}
////
////	@Override
////	public TaStripeSubscriptionDTO mapEntityToDto(TaStripeSubscription entity, TaStripeSubscriptionDTO dto) {
////		if(dto==null)
////			dto = new TaStripeSubscriptionDTO();
////
////		dto.setId(entity.getIdStripeSubscription());
////		
////		dto.setIdExterne(entity.getIdExterne());
////		dto.setQuantity(entity.getQuantity());
////		dto.setStatus(entity.getStatus());
////		
////		if(entity.getTaStripeCustomer()!=null) {
////			dto.setIdExterneCustomer(entity.getTaStripeCustomer().getIdExterne());
////			dto.setIdStripeCustomer(entity.getTaStripeCustomer().getIdStripeCustomer());
////		}
////		
////		dto.setVersionObj(entity.getVersionObj());
////
////		return dto;
////	}
//
//}
