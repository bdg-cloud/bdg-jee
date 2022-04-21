package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaReglementTraca;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IReglementTracaDAO extends IGenericDAO<TaReglementTraca> {

}
