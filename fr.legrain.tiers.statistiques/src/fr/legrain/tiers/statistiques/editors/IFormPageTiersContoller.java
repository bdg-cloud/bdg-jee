package fr.legrain.tiers.statistiques.editors;

import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.tiers.model.TaTiers;

public interface IFormPageTiersContoller {
	
	public TaTiers getMasterEntity();
	
	public void setMasterEntity(TaTiers masterEntity);
	
	public ITaTiersServiceRemote getMasterDAO();
	
	public void setMasterDAO(ITaTiersServiceRemote masterDAO);
	
}
