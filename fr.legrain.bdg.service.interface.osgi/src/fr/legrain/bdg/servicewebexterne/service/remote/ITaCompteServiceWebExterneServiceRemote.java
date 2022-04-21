package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;


@Remote
public interface ITaCompteServiceWebExterneServiceRemote extends IGenericCRUDServiceRemote<TaCompteServiceWebExterne,TaCompteServiceWebExterneDTO>,
														IAbstractLgrDAOServer<TaCompteServiceWebExterne>,IAbstractLgrDAOServerDTO<TaCompteServiceWebExterneDTO>{
	public static final String validationContext = "COMPTE_SERVICEWEB_EXTERNE";
	
	public void changeDefaut(TaCompteServiceWebExterne detachedInstance);
	
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne, boolean compteTest);
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne, boolean compteTest);
	
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne, boolean compteTest);
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne, boolean compteTest);
	
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne); //param TaServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne, boolean compteTest);
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne); //param TaServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne, boolean compteTest);
	
	
}
