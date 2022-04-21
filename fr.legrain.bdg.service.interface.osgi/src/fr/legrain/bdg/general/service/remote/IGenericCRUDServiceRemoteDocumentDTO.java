package fr.legrain.bdg.general.service.remote;

import java.util.List;

///@Remote
public interface IGenericCRUDServiceRemoteDocumentDTO<DTO> extends IGenericCRUDServiceRemoteDTO {

	
	public List<DTO> findAllLight();

}
