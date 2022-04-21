package fr.legrain.bdg.compteclient.service.interfaces.remote.general;

import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.jws.WebMethod;


///@Remote
public interface IGenericCRUDServiceRemoteDTO<DTO> {
	
	public void persistDTO(DTO transientInstance) throws CreateException;
	public void removeDTO(DTO persistentInstance) throws RemoveException;
	public void mergeDTO(DTO detachedInstance) throws EJBException;
	
	public void persistDTO(DTO transientInstance, String validationContext) throws CreateException;
	public void mergeDTO(DTO detachedInstance, String validationContext) throws EJBException;
	
	public List<DTO> findWithNamedQueryDTO(String queryName) throws FinderException;
	public List<DTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException;
	
	public List<DTO> selectAllDTO();
	
	public DTO findByIdDTO(int id) throws FinderException;
	public DTO findByCodeDTO(String code) throws FinderException;

	//http://chamerling.wordpress.com/2009/05/14/an-operation-with-name-already-exists-in-this-service/
	//@WebMethod(operationName = "validateDTO") //pour éviter ""An operation with name [{xxxxx}validateDTO] already exists in this service
	public abstract void validateDTO(DTO dto);
	//@WebMethod(operationName = "validateDTOProperty")
	public abstract void validateDTOProperty(DTO dto, String propertyName) throws BusinessValidationException;
	//@WebMethod(operationName = "validateDTOValidationContext")
	public abstract void validateDTO(DTO dto, String validationContext);
	//@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public abstract void validateDTOProperty(DTO dto, String propertyName, String validationContext) throws BusinessValidationException;
	
	//public void validateDTOProperty(TaTiersDTO dto, String propertyName, ModeObjetEcranServeur modeObjetEcranServeur, String validationContext) throws BusinessValidationException;

	
	public int selectCount();
	
	public void error(DTO dto);
}
