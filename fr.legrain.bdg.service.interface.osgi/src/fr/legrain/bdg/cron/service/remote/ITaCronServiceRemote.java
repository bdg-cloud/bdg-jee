package fr.legrain.bdg.cron.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.cron.model.TaCron;
import fr.legrain.cron.model.dto.TaCronDTO;

@Remote
public interface ITaCronServiceRemote extends IGenericCRUDServiceRemote<TaCron,TaCronDTO>,
														IAbstractLgrDAOServer<TaCron>,IAbstractLgrDAOServerDTO<TaCronDTO>{
	public static final String validationContext = "CRON";
	
	public void miseAJourCronSystemeDuDossier();
	public void activerCronSysteme(String codeCron, Object param);
	public void desactiverCronSysteme(String codeCron);
	public boolean verifPassageCRONGenerationEcheance();

}
