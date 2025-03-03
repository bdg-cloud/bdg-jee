package fr.legrain.gestiondms.divers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.legrain.dms.dao.ITaEtatDms;
import fr.legrain.dms.dao.TaEtatMouvementDms;
import fr.legrain.documents.dao.TaLAvoirDAO;
import fr.legrain.documents.dao.TaLFactureDAO;
import fr.legrain.gestCom.Appli.AbstractApplicationDAO;
import fr.legrain.stocks.dao.TaStockDAO;


public class EtatDms {
//    private TaStockDAO dao = new TaStockDAO();
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

		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();
		//TaSumMouvDocument
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvDms(criteres);
		else
		listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumMouvementDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		if(synthese)listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvDms(criteres);
		else listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumMouvementDms(criteres);
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

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();
		//TaSumMouvDocument
		listeEtatTemp.clear();
		listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumReportDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatReport.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumReportDms(criteres);
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

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();
		//TaSumMouvDocument
		listeEtatTemp.clear();
		listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvDms(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFin.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvDms(criteres);
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
