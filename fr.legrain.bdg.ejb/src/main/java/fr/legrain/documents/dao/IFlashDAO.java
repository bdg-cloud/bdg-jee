package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IFlashDAO extends IGenericDAO<TaFlash> {

	public TaFlash findDocByLDoc(TaLFlash lDoc);
//	public List<TaFlashDTO> findAllLight();
//	public List<TaFlashDTO> findByCodeLight(String code);
}
