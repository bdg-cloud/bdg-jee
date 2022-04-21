package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaEtatDTO;
import fr.legrain.document.model.TaEtat;

public interface ITaEtatServiceRemote extends IGenericCRUDServiceRemote<TaEtat,TaEtatDTO>,
IAbstractLgrDAOServer<TaEtat>,IAbstractLgrDAOServerDTO<TaEtatDTO>{
public static final String validationContext = "ETAT";


public List<TaEtat> findByIdentifiantAndTypeEtat(String identifiant, String typeEtat);


public TaEtat documentTerminePartiellementAbandonne(String doc);


public TaEtat documentPartiellementAbandonne(String doc);


public TaEtat documentTotalementTransforme(String doc);


public TaEtat documentPartiellementTransforme(String doc);


public TaEtat documentAbandonne(String doc);


public TaEtat documentEncours(String doc);


public TaEtat documentPreparation(String doc);


public TaEtat documentCommercial(String doc);


public TaEtat documentBrouillon(String doc);


public TaEtat documentTermine(String doc);


public List<TaEtat> listeDocumentTerminePartiellementAbandonne(String doc);


public List<TaEtat> listeDocumentPartiellementAbandonne(String doc);


public List<TaEtat> listeDocumentTotalementTransforme(String doc);


public List<TaEtat> listeDocumentPartiellementTransforme(String doc);


public List<TaEtat> listeDocumentAbandonne(String doc);


public List<TaEtat> listeDocumentEncours(String doc);


public List<TaEtat> listeDocumentPreparation(String doc);


public List<TaEtat> listeDocumentCommercial(String doc);


public List<TaEtat> listeDocumentBrouillon(String doc);


public List<TaEtat> listeDocumentTermine(String doc);


public void razEtatDocument();


public TaEtat documentSuspendu(String doc);


public List<TaEtat> listeDocumentSuspendu(String doc);

}
