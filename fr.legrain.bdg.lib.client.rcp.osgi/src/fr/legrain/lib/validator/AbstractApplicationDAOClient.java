package fr.legrain.lib.validator;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.validator.LgrHibernateValidated;

public class AbstractApplicationDAOClient<EntityOrDTO> {

	static Logger logger = Logger.getLogger(AbstractApplicationDAOClient.class);

	protected JPACtrl_ApplicationClient ctrlGeneraux = null;

	public AbstractApplicationDAOClient() {
		super();
		//		initConnexion();
		ctrlGeneraux = new JPACtrl_ApplicationClient();
		//System.err.println("CHEMIN EN DUR A CHANGER : (Const.C_FICHIER_MODIF) (Modif.properties)");
		//gestionModif.setListeGestionModif("/home/nicolas/public/lgrdoss/BureauDeGestion/demo/Bd/Modif.properties");
	}


	public boolean validateAll(EntityOrDTO entity, String validationContext) throws Exception {
		Class a = (Class<EntityOrDTO>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		boolean res = true;
		PropertyDescriptor propertyDescriptor = null;
		//for (Field field : a.getClass().getDeclaredFields()) {
		for (Field field : a.getDeclaredFields()) {
			LgrHibernateValidated col = null;
			// check if field has annotation
			System.out.println(a.getName()+" *** "+field.getName());

			//if ((col = field.getAnnotation(LgrHibernateValidated.class)) != null) {
			
			propertyDescriptor = PropertyUtils.getPropertyDescriptor(entity, field.getName());
			
			if (propertyDescriptor!=null)
				col = PropertyUtils.getReadMethod(propertyDescriptor).getAnnotation(LgrHibernateValidated.class);

			if (propertyDescriptor!=null && col != null) {
				res = validate(entity, field.getName(), validationContext);
				if(!res) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param entity - Entite contenant la valeur
	 * @param field - Nom du champ de l'entité à valider
	 * @param validationContext - contexte de validation
	 * @return
	 */
	public boolean validate(EntityOrDTO entity, String field, String validationContext) throws Exception{
		//public IStatus validate(Object entity, String field, String contexte){
		boolean s = false;
		try {
			//				ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
			//				InvalidValue[] iv = uniteValidator.getInvalidValues(entity,field);

			System.out.println("Client (validate appellé manuellement, pas automatiquement à partir des Beans Validation) => xxxDAO.validate() : "+field);

			MessCtrlLgr mess = new MessCtrlLgr();
			LgrHibernateValidated a = PropertyUtils.getReadMethod(PropertyUtils.getPropertyDescriptor(entity, field)).getAnnotation(LgrHibernateValidated.class);
			
			mess.setContexte(validationContext);
			if(a!=null){//isa le 19-03-2009 car existant dans taPrixDao par exemple
				mess.setNomChampBd(a.champBd());
				mess.setNomChampEntite(field);
				Table t = entity.getClass().getAnnotation(Table.class);
				if(t!=null) {
					mess.setNomTable(t.name());
				}
				mess.setEntityClass(a.clazz());
				//mess.setModeObjet(getModeObjet());
				if(entity!=null && PropertyUtils.getProperty(entity, field)!=null) {
					if(PropertyUtils.getProperty(entity, field).getClass() == java.util.Date.class)
						mess.setValeur(LibDate.dateToString((java.util.Date)PropertyUtils.getProperty(entity, field)));
					else
						mess.setValeur(PropertyUtils.getProperty(entity, field).toString());

					//annulerCodeGenere(mess.getValeur(),mess.getNomChampDB());
					//						if(verrouilleCode) {
					//							rentreCodeGenere(mess.getValeur(),mess.getNomChampDB());
					//						}
				} else {
					mess.setValeur(null);
				}

				ctrlGeneraux.ctrlSaisie(mess);
			} else {
				logger.error("Annotation de controle (LgrHibernateValidated) non presente.");
			}
			//s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
			s = true;
			//if(s.getSeverity()!=IStatus.ERROR ){
			if(s){
				ctrlSaisieSpecifique(entity, field);
			}
			return s;
		} catch (ExceptLgr e) {
			logger.error("",e);
			//s = new Status(Status.ERROR,gestComBdPlugin.PLUGIN_ID,e.getMessage(),e);
			s = false;
			throw new Exception(e.getMessage());
			//return s;

			//valid = false;
		} catch (IllegalAccessException e) {
			logger.error("",e);
		} catch (InvocationTargetException e) {
			logger.error("",e);
		} catch (NoSuchMethodException e) {
			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		}
		return s;

	}

	/**
	 * Verification entre les champs, permet par exemple d'initialiser ou de modifier une valeurs dès qu'une autre valeur est saisie
	 * @param entity - l'objet modifier
	 * @param field - le champ modifier dans cet objet
	 * @throws ExceptLgr
	 */
	public void ctrlSaisieSpecifique(EntityOrDTO entity, String field) throws ExceptLgr {}
	
}