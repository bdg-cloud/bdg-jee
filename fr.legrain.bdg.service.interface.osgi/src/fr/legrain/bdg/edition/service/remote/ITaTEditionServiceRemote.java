package fr.legrain.bdg.edition.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.edition.dto.TaTEditionDTO;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;


@Remote
public interface ITaTEditionServiceRemote extends IGenericCRUDServiceRemote<TaTEdition,TaTEditionDTO>,
														IAbstractLgrDAOServer<TaTEdition>,IAbstractLgrDAOServerDTO<TaTEditionDTO>{
	public static final String validationContext = "T_EDITION";
	
//	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}
