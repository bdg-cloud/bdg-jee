package fr.legrain.facture.recherche;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.document.model.TaFacture;
import fr.legrain.lib.ejb.EJBLookup;
import fr.legrain.tiers.clientutility.JNDILookupClass;
import fr.legrain.visualisation.controller.IFonctionRecherche;

public class TestFonctionRecherche implements IFonctionRecherche {

	@Override
	public List/*<Object>*/ recherche() {
		List<Object> resultat = new ArrayList<Object>();
		
		ITaFactureServiceRemote dao;
		try {
			dao = new EJBLookup<ITaFactureServiceRemote>().doLookup(JNDILookupClass.getInitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName());
		
		List<TaFacture> l = dao.selectAll();
//		EntityTransaction tx = dao.getEntityManager().getTransaction();
		
		//select CODE_FACTURE,DATE_FACTURE,CODE_TIERS,NOM_TIERS,(MTNETTTC)as MTNETTTC ,EXPORT FROM  V_facture , calcul_total_direct(''facture'',id_facture)
		//System.gc();
		for (TaFacture f : l) {
			//System.err.println("=====================");
			//System.err.println(new Date());
			f.calculeTvaEtTotaux();
			//System.err.println(new Date());
//			tx.begin();
			try {
				dao.enregistrerMerge(f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			tx.commit();
			
			resultat.add(new Object[]{
					f.getCodeDocument(),
					f.getDateDocument(),
					f.getTaTiers().getCodeTiers(),
					f.getTaTiers().getNomTiers(),
					f.getNetTtcCalc(),
					f.getExport()
					});
		}
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return resultat;
	}

	@Override
	public List recherche(String param) {
		// TODO Auto-generated method stub
		return null;
	}

}
