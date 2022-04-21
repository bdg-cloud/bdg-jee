package fr.legrain.bdg.general.service.remote;

//@Remote
public interface IAbstractLgrDAOServerDTO<DTO> {

	public void enregistrerPersistDTO(DTO transientInstance) throws Exception;

	public void enregistrerMergeDTO(DTO persistentInstance) throws Exception;

	public void enregistrerPersistDTO(DTO transientInstance, String validationContext) throws Exception;

	public void enregistrerMergeDTO(DTO persistentInstance, String validationContext) throws Exception;
	
	//public Boolean autoriseModificationDTO(DTO persistentInstance);

	//public void messageNonAutoriseModificationDTO() throws Exception;

	public void verifAutoriseModificationDTO(DTO persistentInstance) throws Exception;

	public void modifierDTO(DTO persistentInstance) throws Exception;

	//public void insererDTO(DTO transientInstance) throws Exception;

	public void annulerDTO(DTO persistentInstance) throws Exception;

	public DTO annulerTDTO(DTO persistentInstance) throws Exception;

	public void supprimerDTO(DTO persistentInstance) throws Exception;
	
}