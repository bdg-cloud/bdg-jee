package fr.legrain.facture.actions;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import fr.legrain.article.model.TaArticle;
import fr.legrain.articles.dao.ILgrValidable;
import fr.legrain.articles.dao.TaArticleDAO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.TaFactureDAO;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.documents.dao.TaTLigneDAO;
import fr.legrain.documents.dao.TaTPaiementDAO;
import fr.legrain.gestCom.Appli.Const;
import fr.legrain.gestCom.Appli.EntityManagerUtil;
import fr.legrain.gestCom.Module_Document.IHMEnteteFacture;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.tiers.dao.TaTiersDAO;
import fr.legrain.tiers.model.TaTiers;



public class TestFactureJPA implements IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		try {
			System.out.println("ActionCnxBDD.run() : "+new Date());
			EntityManagerUtil.getEntityManagerFactory();
			EntityManagerUtil.getEntityManager();
			System.out.println("ActionCnxBDD.run() : "+new Date());
			
			int nb = 50000;
			//EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample");
			//EntityManager em = emf.createEntityManager(); // Retrieve an application managed entity manager
			EntityManager em = EntityManagerUtil.getEntityManager();
//			Work with the EM

			em.getTransaction().begin();


			System.out.println("init");
//			Session session = HibernateUtil.getSessionFactory().openSession();
//			session.beginTransaction();

			//Session s = HibernateUtil.getSession();
			//s.beginTransaction();
			System.out.println("init ok");

			TaFacture taFacture = null;
			TaLFacture taLFacture = null;
			
			ITaFactureServiceRemote dao = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
			ITaTPaiementServiceRemote taTPaiementDAO = new EJBLookup<ITaTPaiementServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_PAIEMENT_SERVICE, ITaTPaiementServiceRemote.class.getName());
			ITaTiersServiceRemote taTiersDAO = new EJBLookup<ITaTiersServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_TIERS_SERVICE, ITaTiersServiceRemote.class.getName());
			ITaTLigneServiceRemote taTLigneDAO = new EJBLookup<ITaTLigneServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_T_LIGNE_SERVICE, ITaTLigneServiceRemote.class.getName());
			ITaArticleServiceRemote taArticleDAO = new EJBLookup<ITaArticleServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName());
			
//			EntityTransaction transaction = dao.getEntityManager().getTransaction();
//			dao.begin(transaction);
			
			
			TaTPaiement taTPaiement = taTPaiementDAO.findByCode("C");
			TaTLigne taTLigne = taTLigneDAO.findByCode(Const.C_CODE_T_LIGNE_C);
			TaArticle taArticle = taArticleDAO.findById(1);
			TaTiers taTiers = taTiersDAO.findById(1);
			
			//int i = 0;
			int i = 23401;
			while (i<nb) {
				i++;
				System.out.println("taFacture N°"+i);
				taFacture = new TaFacture();
				//taArticle.setIdArticle(i);
				taFacture.setCodeDocument("FACT__"+i);
				taFacture.setLibelleDocument("Libelle facture num "+i);
				taFacture.setCommentaire("commentaire_"+i);
				taFacture.setDateEchDocument(new Date());
				taFacture.setDateDocument(new Date());
				taFacture.setDateLivDocument(new Date());
				
//				if(taTPaiement!=null) {
//					taFacture.setTaTPaiement(taTPaiement);
//				}
				
				taFacture.setTaTiers(taTiers);
				
				for (int j = 0; j < 10; j++) {
					taLFacture = new TaLFacture();
					taLFacture.setLegrain(true);
					taLFacture.setNumLigneLDocument(j);
					taLFacture.setTaDocument(taFacture);
					taFacture.getLignes().add(taLFacture);
					
					taLFacture.setTaTLigne(taTLigne);
					taLFacture.setTaArticle(taArticle);
				}
				
				taFacture.calculeTvaEtTotaux();
				
				dao.inserer(taFacture);
				dao.enregistrerMerge(taFacture);
				
				taFacture = null;
				
				if(i%100==0) {
//					dao.commit(transaction);
//					transaction = null;
//					transaction = dao.getEntityManager().getTransaction();
//					dao.begin(transaction);
				}
				
			}
			
//			dao.commit(transaction);
//			transaction = null;
			
		//	System.out.println("save ok (id = "+taArticle.getIdArticle()+")");
			//s.getTransaction().commit();
			em.getTransaction().commit();
			
			
			
			System.out.println("ActionCloseCnxBdd.run() : "+new Date());
			EntityManagerUtil.closeEntityManager();
			System.out.println("ActionCloseCnxBdd.run() : "+new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
