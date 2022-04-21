package fr.legrain.moncompte.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;

//@Remote
public interface ICommissionDAO extends IGenericDAO<TaCommission> {

	public List<TaCommission> findCommissionPartenaire(String codePartenaire);
	public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin);
	
	public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin);
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire);
	public BigDecimal findTauxCommissionPartenaire(String codePartenaire);
}
