package fr.legrain.paiement.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;
import fr.legrain.tache.model.TaAgenda;
//import javax.ejb.Remote;

//@Remote
public interface ILogPaiementInternetDAO extends IGenericDAO<TaLogPaiementInternet> {
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
}
