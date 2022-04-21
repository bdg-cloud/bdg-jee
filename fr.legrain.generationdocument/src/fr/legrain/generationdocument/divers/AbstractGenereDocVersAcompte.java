package fr.legrain.generationdocument.divers;

import javax.persistence.EntityTransaction;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.gestCom.Module_Tiers.IHMAdresseInfosFacturation;
import fr.legrain.gestCom.Module_Tiers.IHMAdresseInfosLivraison;
import fr.legrain.gestCom.Module_Tiers.IHMIdentiteTiers;
import fr.legrain.gestCom.Module_Tiers.IHMInfosCPaiement;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;

public abstract  class AbstractGenereDocVersAcompte extends AbstractGenereDoc{
	
	
	private LgrDozerMapper<IHMInfosCPaiement,TaInfosAcompte> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<IHMInfosCPaiement, TaInfosAcompte>();
	private LgrDozerMapper<IHMAdresseInfosFacturation,TaInfosAcompte> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<IHMAdresseInfosFacturation, TaInfosAcompte>();
	private LgrDozerMapper<IHMAdresseInfosLivraison,TaInfosAcompte> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<IHMAdresseInfosLivraison, TaInfosAcompte>();
	private LgrDozerMapper<IHMIdentiteTiers,TaInfosAcompte> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IHMIdentiteTiers, TaInfosAcompte>();

	private LgrDozerMapper<TaAcompte,TaInfosAcompte> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaAcompte, TaInfosAcompte>();	
	
	@Override
	public void enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				ITaAcompteServiceRemote daoDocumentFinal = new EJBLookup<ITaAcompteServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_ACOMPTE_SERVICE, ITaAcompteServiceRemote.class.getName());
//				EntityTransaction transaction = daoDocumentFinal.getEntityManager().getTransaction();
//				daoDocumentFinal.begin(transaction);
				daoDocumentFinal.inserer(((TaAcompte)dd));				
				((TaAcompte)dd).calculeTvaEtTotaux();	

				daoDocumentFinal.enregistrerMerge(((TaAcompte)dd));		
//				daoDocumentFinal.commit(transaction);
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaAcompte)dd).getTaInfosDocument()==null) {
			((TaAcompte)dd).setTaInfosDocument(new TaInfosAcompte());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(IHMAdresseInfosFacturation infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(IHMAdresseInfosLivraison infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(IHMInfosCPaiement infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaAcompte)dd), ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IHMIdentiteTiers infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}
}
