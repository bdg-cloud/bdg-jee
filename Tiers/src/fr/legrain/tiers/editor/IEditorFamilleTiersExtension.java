package fr.legrain.tiers.editor;

import fr.legrain.bdg.tiers.service.remote.ITaFamilleTiersServiceRemote;
import fr.legrain.tiers.model.TaFamilleTiers;

public interface IEditorFamilleTiersExtension {
	
	public TaFamilleTiers getMasterEntity();
	
	public void setMasterEntity(TaFamilleTiers masterEntity);
	
	public ITaFamilleTiersServiceRemote getMasterDAO();
	
	public void setMasterDAO(ITaFamilleTiersServiceRemote masterDAO);
	
	/**
	 * Methode qui doit être appelée automatiquement lors de l'appel de la méthode <code>pageChange()</code> du MultipageEditor
	 * contenant c'est Editeur.
	 */
	public void activate();
	
}
