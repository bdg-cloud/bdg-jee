package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaRReglementServiceRemote extends IGenericCRUDServiceRemote<TaRReglement,TaRReglementDTO>,
														IAbstractLgrDAOServer<TaRReglement>,IAbstractLgrDAOServerDTO<TaRReglementDTO>,
														IDocumentService<TaRReglement>{
	public static final String validationContext = "R_REGLEMENT";
	public List<TaRReglement> selectAll(TaTiers taTiers,Date dateDeb,Date dateFin);
	public List<TaRReglementDTO> rechercheDocumentDTO(String codeDoc, String codeTiers);
}
