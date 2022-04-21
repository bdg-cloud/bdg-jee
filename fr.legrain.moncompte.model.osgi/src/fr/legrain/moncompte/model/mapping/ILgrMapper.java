package fr.legrain.moncompte.model.mapping;

import fr.legrain.moncompte.commun.model.ModelObject;


public interface ILgrMapper<DTO extends ModelObject,Entity> {
	//public DTO entityToDto(Entity entity);
	//public Entity dtoToEntity(DTO dto);
	public Entity mapDtoToEntity(DTO dto,Entity entity);
	public DTO mapEntityToDto(Entity entity, DTO dto);
	
}
