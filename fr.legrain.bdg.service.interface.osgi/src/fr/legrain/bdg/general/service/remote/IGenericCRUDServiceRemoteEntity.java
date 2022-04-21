package fr.legrain.bdg.general.service.remote;

import java.sql.DatabaseMetaData;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.TaFacture;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IGenericCRUDServiceRemoteEntity<Entity> {
	
	public DatabaseMetaData getDbMetaData();
	public int getDbMetaDataLongeur(String table, String champ);
	public int getDbMetaDataPrecision(String table, String champ);
	
	public abstract void persist(Entity transientInstance) throws CreateException;
	public abstract void remove(Entity persistentInstance) throws RemoveException;
	public abstract Entity merge(Entity detachedInstance);
	
	public abstract void persist(Entity transientInstance, String validationContext) throws CreateException;
	public abstract Entity merge(Entity detachedInstance, String validationContext);
	
	public Entity findById(int id) throws FinderException;
	public Entity findByCode(String code) throws FinderException;
	

	
   // public List<Entity> findWithNamedQuery(String queryName, int resultLimit);
	
	public abstract void validateEntity(Entity value);
	public abstract void validateEntityProperty(Entity value, String propertyName);
	public abstract void validateEntity(Entity value,String validationContext);
	public abstract void validateEntityProperty(Entity value, String propertyName, String validationContext);
	
	public List<Entity> selectAll();
	
	public int selectCount();
	
	public RetourPaiementCarteBancaire payerTicketDeCaisseCB(PaiementCarteBancaire cb, IDocumentPayableCB ticket, String libelle) throws EJBException;
	public RetourPaiementCarteBancaire payerCB(PaiementCarteBancaire cb, TaTiers taTiers, String libelle) throws EJBException;
	
}
