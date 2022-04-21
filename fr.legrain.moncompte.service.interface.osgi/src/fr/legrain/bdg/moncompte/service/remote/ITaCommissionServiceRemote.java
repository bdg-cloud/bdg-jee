package fr.legrain.bdg.moncompte.service.remote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaCommissionDTO;
import fr.legrain.moncompte.dto.TaLPanierDTO;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaLignePanier;


@Remote
public interface ITaCommissionServiceRemote extends IGenericCRUDServiceRemote<TaCommission,TaCommissionDTO>,
														IAbstractLgrDAOServer<TaCommission>,IAbstractLgrDAOServerDTO<TaCommissionDTO>{
	public static final String validationContext = "COMMISSION";
	
	public List<TaCommission> findCommissionPartenaire(String codePartenaire);
	public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin);
	
	public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin);
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire);
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire);
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire, boolean appliqueDecote);

}
