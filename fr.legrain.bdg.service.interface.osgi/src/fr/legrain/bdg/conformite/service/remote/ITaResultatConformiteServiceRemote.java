package fr.legrain.bdg.conformite.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaResultatConformiteDTO;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaStatusConformite;

@Remote
public interface ITaResultatConformiteServiceRemote extends IGenericCRUDServiceRemote<TaResultatConformite,TaResultatConformiteDTO>,
														IAbstractLgrDAOServer<TaResultatConformite>,IAbstractLgrDAOServerDTO<TaResultatConformiteDTO>{
	public static final String validationContext = "RESULTAT_CONFORMITE";
	
	public TaResultatConformite findByLotAndControleConformite(int idLot, int idControleConformite);
	public List<TaResultatConformite> findByLotAndControleConformiteHistorique(int idLot, int idControleConformite);
	
	public TaStatusConformite etatLot(int idLot);
	public TaStatusConformite etatLot(int idLot, boolean priseEnCompteDesControleFactultatif);
	public TaStatusConformite etatLotPourLesControlesObligatoires(int idLot);
	public TaStatusConformite etatLotPourLesControlesFacultatif(int idLot);
}
