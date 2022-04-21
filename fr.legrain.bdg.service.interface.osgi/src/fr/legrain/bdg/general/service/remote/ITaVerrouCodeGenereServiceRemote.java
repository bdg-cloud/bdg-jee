package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.controle.dto.TaVerrouCodeGenereDTO;
import fr.legrain.controle.model.TaVerrouCodeGenere;

@Remote
public interface ITaVerrouCodeGenereServiceRemote extends IGenericCRUDServiceRemote<TaVerrouCodeGenere,TaVerrouCodeGenereDTO>,
														IAbstractLgrDAOServer<TaVerrouCodeGenere>,IAbstractLgrDAOServerDTO<TaVerrouCodeGenereDTO>{
	public static final String validationContext = "VERROU_CODE_GENERE";
}
