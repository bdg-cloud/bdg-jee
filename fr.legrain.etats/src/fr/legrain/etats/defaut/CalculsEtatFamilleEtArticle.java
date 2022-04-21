package fr.legrain.etats.defaut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.legrain.articles.dao.TaArticle;
import fr.legrain.articles.dao.TaArticleDAO;
import fr.legrain.articles.dao.TaFamille;
import fr.legrain.articles.dao.TaFamilleDAO;
import fr.legrain.articles.dao.TaTva;
import fr.legrain.dms.dao.ITaEtatDms;
import fr.legrain.dms.dao.TaEtatMouvementDms;
import fr.legrain.documents.dao.TaLAvoirDAO;
import fr.legrain.documents.dao.TaLFactureDAO;
import fr.legrain.gestCom.Appli.AbstractApplicationDAO;
import fr.legrain.stocks.dao.TaStockDAO;


public class CalculsEtatFamilleEtArticle {
	
	public static final int DETAIL = 1;
	public static final int SYNTHESE = 2;
	public static final int SYNTHESE_ET_ARTICLE = 3;
	
	private TaStockDAO dao = new TaStockDAO();
	private List<TaEtatMouvementDms> listeEtatMouvement = new ArrayList<TaEtatMouvementDms>(0);
	private List<TaEtatMouvementDms> listeEtatFinalMouvement = new ArrayList<TaEtatMouvementDms>();

	private List<TaEtatMouvementDms> listeEtatReport = new ArrayList<TaEtatMouvementDms>(0);
	private List<TaEtatMouvementDms> listeEtatFinalReport = new ArrayList<TaEtatMouvementDms>();

	private List<TaEtatMouvementDms> listeEtatFin = new ArrayList<TaEtatMouvementDms>(0);
	private List<TaEtatMouvementDms> listeEtatFinalFin = new ArrayList<TaEtatMouvementDms>();


	public List<TaEtatMouvementDms> calculEtatFamille(TaEtatMouvementDms criteres,int synthese){
		listeEtatFinalMouvement.clear();
		listeEtatMouvement.clear();
		
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		List<TaEtatMouvementDms>  listeEtatFinalMouvementLocal= new ArrayList<TaEtatMouvementDms>();

		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();
		
		listeEtatTemp.clear();
		if(synthese == SYNTHESE
				|| synthese == SYNTHESE_ET_ARTICLE)
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvEtatFamille(criteres);
		else
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumMouvementEtatFamille(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		if(synthese == SYNTHESE
				|| synthese == SYNTHESE_ET_ARTICLE)
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvEtatFamille(criteres);
		else 
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumMouvementEtatFamille(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}

		if(synthese == SYNTHESE
				|| synthese == SYNTHESE_ET_ARTICLE) {

			for (ITaEtatDms taEtat : listeEtatFinalMouvementLocal) {
				int rang = 0;
				if(synthese == SYNTHESE)
					rang = ((TaEtatMouvementDms)taEtat).existeFamilleUnite(taEtat,listeEtatMouvement);
				else if(synthese == SYNTHESE_ET_ARTICLE) {
					rang = ((TaEtatMouvementDms)taEtat).existeFamilleUniteArticle(taEtat,listeEtatMouvement);
				}
				if(rang==-1){
					listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
				}
				else{
					TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
					if(((TaEtatMouvementDms)taEtat).getQte1()!=null && tmp.getQte1()!=null)
						tmp.setQte1(tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()));
					if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2()!=null)
						tmp.setQte2(tmp.getQte2().add(((TaEtatMouvementDms)taEtat).getQte2()));

					if(((TaEtatMouvementDms)taEtat).getHt()!=null && tmp.getHt()!=null)
						tmp.setHt(tmp.getHt().add(((TaEtatMouvementDms)taEtat).getHt()));
					if(((TaEtatMouvementDms)taEtat).getTva()!=null && tmp.getTva()!=null)
						tmp.setTva(tmp.getTva().add(((TaEtatMouvementDms)taEtat).getTva()));
					if(((TaEtatMouvementDms)taEtat).getTtc()!=null && tmp.getTtc()!=null)
						tmp.setTtc(tmp.getTtc().add(((TaEtatMouvementDms)taEtat).getTtc()));
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


	public List<TaEtatMouvementDms> calculEtatArticle(TaEtatMouvementDms criteres,boolean synthese){
		listeEtatFinalMouvement.clear();
		listeEtatMouvement.clear();
		
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		List<TaEtatMouvementDms>  listeEtatFinalMouvementLocal= new ArrayList<TaEtatMouvementDms>();
		TaArticleDAO articleDao = new TaArticleDAO();
		TaArticle article = null;
		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();

		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvEtatArticle(criteres);
		else
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumMouvementEtatArticle(criteres);
//		debugList(listeEtatTemp);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvEtatArticle(criteres);
		else 
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumMouvementEtatArticle(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);

			}
		for (TaEtatMouvementDms taEtatMouvementDms : listeEtatFinalMouvementLocal) {
			if(taEtatMouvementDms.getCodeFamille()==null && article!=null && article.getTaFamille()!=null) {
				article=articleDao.findByCode(taEtatMouvementDms.getCodeArticle());
				taEtatMouvementDms.setCodeFamille(article.getTaFamille().getCodeFamille());	
			}
		}
		if(synthese){
			//Boolean retour=false;
			for (ITaEtatDms taEtat : listeEtatFinalMouvementLocal) {
				//			if(taEtat.getCodeArticle().equalsIgnoreCase("DIVERS"))
				//				retour=true;
				int rang =((TaEtatMouvementDms)taEtat).existeArticleUnite(taEtat,listeEtatMouvement);
				if(rang==-1){
					listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
				}
				else{
					TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
					if(((TaEtatMouvementDms)taEtat).getQte1()!=null && tmp.getQte1()!=null)
						tmp.setQte1(tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()));
					if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2()!=null)
						tmp.setQte2(tmp.getQte2().add(((TaEtatMouvementDms)taEtat).getQte2()));

					if(((TaEtatMouvementDms)taEtat).getHt()!=null && tmp.getHt()!=null)
						tmp.setHt(tmp.getHt().add(((TaEtatMouvementDms)taEtat).getHt()));
					if(((TaEtatMouvementDms)taEtat).getTva()!=null && tmp.getTva()!=null)
						tmp.setTva(tmp.getTva().add(((TaEtatMouvementDms)taEtat).getTva()));
					if(((TaEtatMouvementDms)taEtat).getTtc()!=null && tmp.getTtc()!=null)
						tmp.setTtc(tmp.getTtc().add(((TaEtatMouvementDms)taEtat).getTtc()));
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
	
	public List<TaEtatMouvementDms> calculEtatTiers(TaEtatMouvementDms criteres,boolean synthese, boolean groupByTiers){
		listeEtatFinalMouvement.clear();
		listeEtatMouvement.clear();
		
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		List<TaEtatMouvementDms>  listeEtatFinalMouvementLocal= new ArrayList<TaEtatMouvementDms>();
		TaArticleDAO articleDao = new TaArticleDAO();
		TaArticle article = null;
		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();

		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvEtatTiers(criteres);
		else
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumMouvementEtatTiers(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvEtatTiers(criteres);
		else 
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumMouvementEtatTiers(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);

			}
		for (TaEtatMouvementDms taEtatMouvementDms : listeEtatFinalMouvementLocal) {
			article=articleDao.findByCode(taEtatMouvementDms.getCodeArticle());
			if(article!=null && article.getTaFamille()!=null)
				taEtatMouvementDms.setCodeFamille(article.getTaFamille().getCodeFamille());	
		}
		if(synthese){
			//Boolean retour=false;
			for (ITaEtatDms taEtat : listeEtatFinalMouvementLocal) {
				//			if(taEtat.getCodeArticle().equalsIgnoreCase("DIVERS"))
				//				retour=true;
				int rang = 0;
				if(groupByTiers) {
					rang = ((TaEtatMouvementDms)taEtat).existeTiers(taEtat,listeEtatMouvement);
				} else {
					rang =((TaEtatMouvementDms)taEtat).existeTiersEtArticleUnite(taEtat,listeEtatMouvement);
				}
				if(rang==-1){
					listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
				}
				else{
					TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
					if(((TaEtatMouvementDms)taEtat).getQte1()!=null && tmp.getQte1()!=null)
						tmp.setQte1(tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()));
					if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2()!=null)
						tmp.setQte2(tmp.getQte2().add(((TaEtatMouvementDms)taEtat).getQte2()));

					if(((TaEtatMouvementDms)taEtat).getHt()!=null && tmp.getHt()!=null)
						tmp.setHt(tmp.getHt().add(((TaEtatMouvementDms)taEtat).getHt()));
					if(((TaEtatMouvementDms)taEtat).getTva()!=null && tmp.getTva()!=null)
						tmp.setTva(tmp.getTva().add(((TaEtatMouvementDms)taEtat).getTva()));
					if(((TaEtatMouvementDms)taEtat).getTtc()!=null && tmp.getTtc()!=null)
						tmp.setTtc(tmp.getTtc().add(((TaEtatMouvementDms)taEtat).getTtc()));
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
	
	public List<TaEtatMouvementDms> calculEtatFamilleTiers(TaEtatMouvementDms criteres,boolean synthese, boolean groupByTiers){
		listeEtatFinalMouvement.clear();
		listeEtatMouvement.clear();
		
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		List<TaEtatMouvementDms>  listeEtatFinalMouvementLocal= new ArrayList<TaEtatMouvementDms>();
		TaArticleDAO articleDao = new TaArticleDAO();
		TaArticle article = null;
		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();

		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumSyntheseMouvEtatFamilleTiers(criteres);
		else
			listeEtatTemp =((TaLFactureDAO)taLDocumentDAO).selectSumMouvementEtatFamilleTiers(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);
			}
		taLDocumentDAO = new TaLAvoirDAO();
		listeEtatTemp.clear();
		if(synthese)
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumSyntheseMouvEtatFamilleTiers(criteres);
		else 
			listeEtatTemp =((TaLAvoirDAO)taLDocumentDAO).selectSumMouvementEtatFamilleTiers(criteres);
		if(listeEtatTemp!=null)
			for (TaEtatMouvementDms taEtat : listeEtatTemp) {
				listeEtatFinalMouvementLocal.add(taEtat);

			}
		for (TaEtatMouvementDms taEtatMouvementDms : listeEtatFinalMouvementLocal) {
			article=articleDao.findByCode(taEtatMouvementDms.getCodeArticle());
			if(article!=null && article.getTaFamille()!=null)
				taEtatMouvementDms.setCodeFamille(article.getTaFamille().getCodeFamille());	
		}
		if(synthese){
			//Boolean retour=false;
			for (ITaEtatDms taEtat : listeEtatFinalMouvementLocal) {
				//			if(taEtat.getCodeArticle().equalsIgnoreCase("DIVERS"))
				//				retour=true;
				int rang = 0;
				if(groupByTiers) {
					rang = ((TaEtatMouvementDms)taEtat).existeTiers(taEtat,listeEtatMouvement);
				} else {
					rang =((TaEtatMouvementDms)taEtat).existeFamilleTiers(taEtat,listeEtatMouvement);
				}
				if(rang==-1){
					listeEtatMouvement.add((TaEtatMouvementDms)taEtat);
					//System.err.println(((TaEtatMouvementDms)taEtat).getCodeFamilleTiers()+" * "+((TaEtatMouvementDms)taEtat).getCodeTiers() +" --N-- "+((TaEtatMouvementDms)taEtat).getQte1());
				}
				else{
					TaEtatMouvementDms tmp = listeEtatMouvement.get(rang);
					//System.err.println(((TaEtatMouvementDms)taEtat).getCodeFamilleTiers()+" * "+((TaEtatMouvementDms)taEtat).getCodeTiers() +" ---- "+tmp.getQte1()+" add "+((TaEtatMouvementDms)taEtat).getQte1()+" ==>"+tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()) );
					if(((TaEtatMouvementDms)taEtat).getQte1()!=null && tmp.getQte1()!=null)
						tmp.setQte1(tmp.getQte1().add(((TaEtatMouvementDms)taEtat).getQte1()));
					if(((TaEtatMouvementDms)taEtat).getQte2()!=null && tmp.getQte2()!=null)
						tmp.setQte2(tmp.getQte2().add(((TaEtatMouvementDms)taEtat).getQte2()));

					if(((TaEtatMouvementDms)taEtat).getHt()!=null && tmp.getHt()!=null)
						tmp.setHt(tmp.getHt().add(((TaEtatMouvementDms)taEtat).getHt()));
					if(((TaEtatMouvementDms)taEtat).getTva()!=null && tmp.getTva()!=null)
						tmp.setTva(tmp.getTva().add(((TaEtatMouvementDms)taEtat).getTva()));
					if(((TaEtatMouvementDms)taEtat).getTtc()!=null && tmp.getTtc()!=null)
						tmp.setTtc(tmp.getTtc().add(((TaEtatMouvementDms)taEtat).getTtc()));
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
		
		//debugList(listeEtatFinalMouvement);
		
		return listeEtatFinalMouvement;
	}


	public List<TaEtatMouvementDms> calculEtatReportFamille(TaEtatMouvementDms criteres){
		listeEtatFinalMouvement.clear();
		listeEtatMouvement.clear();
		
		List<TaEtatMouvementDms>  listeEtatTemp = new ArrayList<TaEtatMouvementDms>();
		listeEtatTemp.clear();

		AbstractApplicationDAO taLDocumentDAO = null;
		taLDocumentDAO = new TaLFactureDAO();
		
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
	
	private void debugList(List<TaEtatMouvementDms> l) {
		System.out.println(l.get(0).toStringEnteteSQL());
		for (TaEtatMouvementDms taEtatMouvementDms : l) {
			System.out.println(taEtatMouvementDms.toStringSQL());
		}
	}
	
}
