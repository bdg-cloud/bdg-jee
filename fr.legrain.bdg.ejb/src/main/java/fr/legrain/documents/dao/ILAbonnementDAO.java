package fr.legrain.documents.dao;

import java.math.BigDecimal;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLDevis;

//@Remote
public interface ILAbonnementDAO extends IGenericDAO<TaLAbonnement> {
	public TaLAbonnement findByIdLEcheance(Integer id);
	public List<TaLAbonnementDTO> selectAllDTOAvecEtat();
	public List<TaLAbonnement> findAllByIdAbonnement(Integer id);
	public List<TaLAbonnement> findAllAnnule();
	public BigDecimal sumTtcLigneAboEnCoursEtSuspendu();
	public List<TaLAbonnement> findAllSansEtat();
}
