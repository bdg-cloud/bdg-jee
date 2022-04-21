package fr.legrain.general.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.general.dao.IDatabaseDAO;

/**
 * Home object for domain model class TaFichier.
 * @see fr.legrain.tiers.model.old.TaFichier
 * @author Hibernate Tools
 */
public class DatabaseDAO implements IDatabaseDAO {

	static Logger logger = Logger.getLogger(DatabaseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaFichier a ";
	
	public DatabaseDAO() {
	}
	
	public List<String> listeBdd() {
		try {
			List<String> l = new ArrayList<String>();
			Query query = entityManager.createNativeQuery("SELECT datname FROM pg_database WHERE datistemplate = false");
			List<Object> r = query.getResultList();
			for (Object object : r) {
				l.add(object.toString());
				//				System.out.println(object);
			}
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<Object[]> listeSchemaTailleConnection() {
		try {
			Query query = entityManager.createNativeQuery(
					"SELECT table_schema,"
							+ " pg_size_pretty(SUM(pg_relation_size(table_schema || '.' || table_name))) As Taille_donnees,"
							+ " pg_size_pretty(SUM(pg_total_relation_size(table_schema || '.' || table_name))) As Taille_totale"
							+ " FROM information_schema.tables"
							+ " where table_schema<>'public' and table_schema<>'information_schema' and table_schema not like 'pg_%'"
							+ " GROUP BY table_schema"
							+ " ORDER BY Taille_totale DESC");
			
			List<Object[]> r = query.getResultList();
			return r;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}
