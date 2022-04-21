package fr.legrain.bdg.documents.service.remote;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLAbonnement;

@Remote
public interface ITaLAbonnementServiceRemote extends IGenericCRUDServiceRemote<TaLAbonnement,TaLAbonnementDTO>,
														IAbstractLgrDAOServer<TaLAbonnement>,IAbstractLgrDAOServerDTO<TaLAbonnementDTO>{
	public static final String validationContext = "L_ABONNEMENT";
	
	public TaLAbonnement findByIdLEcheance(Integer id);
	
	public TaLAbonnement donneEtat(TaLAbonnement detachedInstance, TaEtat etat);
	
	public void annuleLigne(TaLAbonnement ligne);
	public TaLAbonnement donneEtatSuspendu(TaLAbonnement detachedInstance);
	public TaLAbonnement donneEtatAnnule(TaLAbonnement detachedInstance);
	public List<TaLAbonnementDTO> selectAllDTOAvecEtat();
	
	public List<TaLAbonnement> findAllByIdAbonnement(Integer id);
	
	public List<TaLAbonnement> findAllAnnule();
	public TaLAbonnement findByIDFetch(Integer id);
	public BigDecimal sumTtcLigneAboEnCoursEtSuspendu();
	public List<TaLAbonnement> findAllSansEtat();
}
