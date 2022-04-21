package fr.legrain.tiers.editor;

//import fr.legrain.tiers.dao.TaTiers;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.tiers.model.TaTiers;

public interface IEditorTiersExtension {
	
	public TaTiers getMasterEntity();
	
	public void setMasterEntity(TaTiers masterEntity);
	
	public ITaTiersServiceRemote getMasterDAO();
	
	public void setMasterDAO(ITaTiersServiceRemote masterDAO);
	
	/**
	 * Methode qui doit être appelée automatiquement lors de l'appel de la méthode <code>pageChange()</code> du MultipageEditor
	 * contenant c'est Editeur.
	 */
	public void activate();
	
}
