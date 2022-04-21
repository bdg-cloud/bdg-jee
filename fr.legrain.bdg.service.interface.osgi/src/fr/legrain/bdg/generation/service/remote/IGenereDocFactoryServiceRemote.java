package fr.legrain.bdg.generation.service.remote;

import fr.legrain.document.dto.IDocumentTiers;

public interface IGenereDocFactoryServiceRemote {

	IAbstractGenereDocServiceRemote create(IDocumentTiers iDocumentTiers, IDocumentTiers docGenere);

}
