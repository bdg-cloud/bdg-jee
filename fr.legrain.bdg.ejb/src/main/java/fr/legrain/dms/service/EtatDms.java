package fr.legrain.dms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.bdg.dms.service.remote.IEtatDmsServiceRemote;
import fr.legrain.dms.model.ITaEtatDms;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.documents.dao.ILAvoirDAO;
import fr.legrain.documents.dao.ILFactureDAO;
import fr.legrain.documents.dao.jpa.TaLAvoirDAO;
import fr.legrain.documents.dao.jpa.TaLFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class EtatDms implements IEtatDmsServiceRemote {

	
	@Inject private ILFactureDAO taLDocumentServiceFacture;
	@Inject private ILAvoirDAO taLDocumentServiceAvoir;
	@Inject private IMouvementDAO taMouvementStockService;
	
	private List<TaEtatMouvementDms>  listeEtatMouvement = new ArrayList<TaEtatMouvementDms>(0);
	List<TaEtatMouvementDms>  listeEtatFinalMouvement = new ArrayList<TaEtatMouvementDms>();
	
	private List<TaEtatMouvementDms>  listeEtatReport = new ArrayList<TaEtatMouvementDms>(0);
	List<TaEtatMouvementDms>  listeEtatFinalReport = new ArrayList<TaEtatMouvementDms>();
	
	private List<TaEtatMouvementDms>  listeEtatFin = new ArrayList<TaEtatMouvementDms>(0);
	List<TaEtatMouvementDms>  listeEtatFinalFin = new ArrayList<TaEtatMouvementDms>();
	
	
	public List<TaEtatMouvementDms> calculEtatDms(TaEtatMouvementDms criteres,boolean synthese){
		//LinkedList<SWTStocks> listObject = new LinkedList<SWTStocks>();
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		List<TaEtatMouvementDms>  listeEtatFinalMouvementLocal= new ArrayList<TaEtatMouvementDms>();

		listeEtatFin.clear();
		listeEtatReport.clear();
		listeEtatMouvement.clear();
		listeEtatFinalMouvement.clear();
		listeEtatFinalReport.clear();
		listeEtatFinalFin.clear();
		
		listeEtatTemp.clear();
		listeEtatFinalMouvementLocal.clear();

		//les mouvements d'entrée
		criteres.setMouvementEntree(true);
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =taMouvementStockService.selectSumSyntheseMouvEntreesSortiesDms(criteres);
		else
		listeEtatTemp =taMouvementStockService.selectSumMouvementEntreesSortiesDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		
		// les mouvements de sortie
		criteres.setMouvementEntree(false);
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =taMouvementStockService.selectSumSyntheseMouvEntreesSortiesDms(criteres);
		else
		listeEtatTemp =taMouvementStockService.selectSumMouvementEntreesSortiesDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		
		
		//TaSumMouvDocument
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =((TaLFactureDAO)taLDocumentServiceFacture).selectSumSyntheseMouvDms(criteres);
		else
		listeEtatTemp =((TaLFactureDAO)taLDocumentServiceFacture).selectSumMouvementDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =((TaLAvoirDAO)taLDocumentServiceAvoir).selectSumSyntheseMouvDms(criteres);
		else listeEtatTemp =((TaLAvoirDAO)taLDocumentServiceAvoir).selectSumMouvementDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}

		if(synthese){

		for (ITaEtatDms taEtat : listeEtatFinalMouvementLocal) {
			int rang =((TaEtatMouvementDms)taEtat).existeArticleUnite(taEtat,listeEtatMouvement);
			if(rang==-1){
				listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
			}
				else{
				TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
				if(((TaEtatMouvementDms)taEtat).getQte1()!=null)
					tmp.setQte1(tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()));
				if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2()!=null)
					tmp.setQte2(tmp.getQte2().add(((TaEtatMouvementDms)taEtat).getQte2()));
			}
		}

			List<TaEtatMouvementDms> listeEtatReport = calculEtatReportDms(criteres);
			//List<TaEtatMouvementDms> listeEtatFin = calculEtatFinDms(criteres);


			//rassembler les entitées de même famille pour en créer une seule
			//avec report, mouvement et solde fin mois

			for (ITaEtatDms taEtat : listeEtatReport ) {
				int rang =((TaEtatMouvementDms)taEtat).existeArticleUnite(taEtat,(listeEtatMouvement));
				if(rang==-1){
					((TaEtatMouvementDms)taEtat).setQte1Report(((TaEtatMouvementDms)taEtat).getQte1());
					((TaEtatMouvementDms)taEtat).setQte2Report(((TaEtatMouvementDms)taEtat).getQte2());
					((TaEtatMouvementDms)taEtat).setQte1(BigDecimal.valueOf(0));
					((TaEtatMouvementDms)taEtat).setQte2(BigDecimal.valueOf(0));
					listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
				}
				else
				{
					TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
					if(((TaEtatMouvementDms)taEtat).getQte1()!=null && tmp.getQte1Report()!=null)
						tmp.setQte1Report(tmp.getQte1Report().add(((TaEtatMouvementDms)taEtat).getQte1()));
					if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2Report()!=null)
						tmp.setQte2Report(tmp.getQte2Report().add(((TaEtatMouvementDms)taEtat).getQte2()));
				}
			}
			listeEtatFinalMouvement.addAll(listeEtatMouvement);
			
			for (TaEtatMouvementDms taEtat : listeEtatFinalMouvement) {
				if(taEtat.getQte1()==null)taEtat.setQte1(new BigDecimal(0));
				if(taEtat.getQte1Report()==null)taEtat.setQte1Report(new BigDecimal(0));
				taEtat.setQte1Fin(taEtat.getQte1Report().add(taEtat.getQte1()));
				if(taEtat.getQte2()==null)taEtat.setQte2(new BigDecimal(0));
				if(taEtat.getQte2Report()==null)taEtat.setQte2Report(new BigDecimal(0));
				taEtat.setQte2Fin(taEtat.getQte2Report().add(taEtat.getQte2()));
			}
		}
		else listeEtatFinalMouvement.addAll(listeEtatFinalMouvementLocal);
		return listeEtatFinalMouvement;
	}
	
	
	
	
	public List<TaEtatMouvementDms> calculEtatReportDms(TaEtatMouvementDms criteres){
		//LinkedList<SWTStocks> listObject = new LinkedList<SWTStocks>();
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		

		listeEtatTemp.clear();

		listeEtatTemp.clear();
		listeEtatTemp =((TaLFactureDAO)taLDocumentServiceFacture).selectSumReportDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatReport.add(taEtat);
			}
		
		listeEtatTemp.clear();
		listeEtatTemp =((TaLAvoirDAO)taLDocumentServiceAvoir).selectSumReportDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatReport.add(taEtat);
			}
		
		
		for (TaEtatMouvementDms taEtat : listeEtatReport) {
			int rang =taEtat.existeArticleUnite(taEtat,listeEtatFinalReport);
			if(rang==-1)
				listeEtatFinalReport.add(taEtat);
			else {
				TaEtatMouvementDms tmp = listeEtatFinalReport.get(rang);
				if(taEtat.getQte1()!=null)
					tmp.setQte1(tmp.getQte1().add(taEtat.getQte1()));
				if(taEtat.getQte2()!=null && tmp.getQte2()!=null)
					tmp.setQte2(tmp.getQte2().add(taEtat.getQte2()));
			}
		}
		
		return listeEtatFinalReport;
	}

	
	
	public List<TaEtatMouvementDms> calculEtatFinDms(TaEtatMouvementDms criteres){
		//LinkedList<SWTStocks> listObject = new LinkedList<SWTStocks>();
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		

		listeEtatTemp.clear();

		//TaSumMouvDocument
		listeEtatTemp.clear();
		listeEtatTemp =((TaLFactureDAO)taLDocumentServiceFacture).selectSumSyntheseMouvDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFin.add(taEtat);
			}
		
		listeEtatTemp.clear();
		listeEtatTemp =((TaLAvoirDAO)taLDocumentServiceAvoir).selectSumSyntheseMouvDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFin.add(taEtat);
			}
		
		
		for (TaEtatMouvementDms taEtat : listeEtatFin) {
			int rang =taEtat.existeArticleUnite(taEtat,listeEtatFinalFin);
			if(rang==-1)
				listeEtatFinalFin.add(taEtat);
			else {
				TaEtatMouvementDms tmp = listeEtatFinalFin.get(rang);
				if(taEtat.getQte1()!=null)
					tmp.setQte1(tmp.getQte1().add(taEtat.getQte1()));
				if(taEtat.getQte2()!=null && tmp.getQte2()!=null)
					tmp.setQte2(tmp.getQte2().add(taEtat.getQte2()));
			}
		}
		
		return listeEtatFinalFin;
	}

}
