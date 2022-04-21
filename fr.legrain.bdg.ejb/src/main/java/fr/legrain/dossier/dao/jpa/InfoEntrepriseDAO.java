package fr.legrain.dossier.dao.jpa;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

// Generated 14 mai 2009 11:08:14 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.dossier.dao.IInfoEntrepriseDAO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaWeb;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfoEntreprise.
 * @see fr.legrain.dossier.dao.TaInfoEntreprise
 * @author Hibernate Tools
 */
public class InfoEntrepriseDAO implements IInfoEntrepriseDAO {

	static Logger logger = Logger.getLogger(InfoEntrepriseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	@Inject ITiersDAO daoTiers;
	private String defaultJPQLQuery = "select a from TaInfoEntreprise a";
	
	public InfoEntrepriseDAO(){
		//this(null);
	}
	
//	public TaInfoEntrepriseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfoEntreprise.class.getSimpleName());
//		initChampId(TaInfoEntreprise.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfoEntreprise());
//	}
	
//	public TaInfoEntreprise refresh(TaInfoEntreprise detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//////			entityManager.refresh(detachedInstance);
//////			logger.debug("refresh successful");
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfoEntreprise.class, detachedInstance.getIdInfoEntreprise());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfoEntreprise transientInstance) {
		logger.debug("persisting TaInfoEntreprise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfoEntreprise persistentInstance) {
		logger.debug("removing TaInfoEntreprise instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfoEntreprise merge(TaInfoEntreprise detachedInstance) {
		logger.debug("merging TaInfoEntreprise instance");
		try {
			TaInfoEntreprise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfoEntreprise findById(int id) {
		logger.debug("getting TaInfoEntreprise instance with id: " + id);
		try {
			TaInfoEntreprise instance = entityManager.find(
					TaInfoEntreprise.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @return - l'unique instance de taInfoEntreprise si elle existe, sinon retourne null.
	 */
	public TaInfoEntreprise findInstance() {
		int premierId = 1;
		logger.debug("getting TaInfoEntreprise instance with id: "+premierId);
		try {
			TaInfoEntreprise instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaInfoEntreprise trouve, creation d'une nouvelle instance vide");
				instance = new TaInfoEntreprise();
				instance.setNomInfoEntreprise("Mon entreprise");
				instance.setAdresse1InfoEntreprise("Mon adresse");
				instance.setCodepostalInfoEntreprise("00000");
				instance.setVilleInfoEntreprise("Ma ville");
				Calendar datedeb = Calendar.getInstance();
				Date dateDuJour = LibDate.dateDuJour();
				try {
					datedeb.setTime(LibDate.stringToDate("01/01/"+LibDate.getAnnee(dateDuJour)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar datefin = Calendar.getInstance();
				try {
					datefin.setTime(LibDate.stringToDate("31/12/"+LibDate.getAnnee(dateDuJour).toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String codexo = LibDate.getCodeAnnee(new Date()); 
				instance.setDatedebInfoEntreprise(datedeb.getTime());
				instance.setDatefinInfoEntreprise(datefin.getTime());
				instance.setCodexoInfoEntreprise(codexo);
				instance.setWebInfoEntreprise("");
				instance.setEmailInfoEntreprise("");
				instance = merge(instance);
				
				//enregistrement dans le tiers ZPO
				TaTiers tiersZpo=daoTiers.findByCode("ZPO");
				tiersZpo.setNomTiers(instance.getNomInfoEntreprise());
				TaAdresse adresse = new TaAdresse();
				adresse.setAdresse1Adresse(instance.getAdresse1InfoEntreprise());
				adresse.setAdresse2Adresse(instance.getAdresse2InfoEntreprise());
				adresse.setAdresse3Adresse(instance.getAdresse3InfoEntreprise());
				adresse.setCodepostalAdresse(instance.getCodepostalInfoEntreprise());
				adresse.setVilleAdresse(instance.getVilleInfoEntreprise());
				adresse.setPaysAdresse(instance.getPaysInfoEntreprise());
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				adresse.setOrdre(0);
				adresse.setTaTiers(tiersZpo);
				tiersZpo.addAdresse(adresse);
				TaEntreprise ent = new TaEntreprise();
				ent.setNomEntreprise(instance.getNomInfoEntreprise());
				tiersZpo.addEntreprise(ent);
				daoTiers.merge(tiersZpo);
				
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaInfoEntreprise> selectAll() {
		logger.debug("selectAll TaInfoEntreprise");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfoEntreprise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public int selectCount() {
		// dans ce cas on peu faire un selectAll car il ne doit y avoir qu'un seul enregistrement au maximum
		//return 1;
		return selectAll().size();
	}
	
	@Override
	public List<TaInfoEntreprise> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfoEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfoEntreprise> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfoEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfoEntreprise value) throws Exception {
		BeanValidator<TaInfoEntreprise> validator = new BeanValidator<TaInfoEntreprise>(TaInfoEntreprise.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfoEntreprise value, String propertyName) throws Exception {
		BeanValidator<TaInfoEntreprise> validator = new BeanValidator<TaInfoEntreprise>(TaInfoEntreprise.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfoEntreprise transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfoEntreprise findByCode(String code) {
		return null;
	}
}
