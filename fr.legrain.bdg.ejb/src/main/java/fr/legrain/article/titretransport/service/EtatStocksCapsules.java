package fr.legrain.article.titretransport.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.article.titretransport.dao.IReportStockCapsulesDAO;
import fr.legrain.article.titretransport.dao.IStockCapsulesDAO;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.bdg.article.titretransport.service.remote.IEtatStockCapsulesServiceRemote;
import fr.legrain.documents.dao.ILAvoirDAO;
import fr.legrain.documents.dao.ILBonlivDAO;
import fr.legrain.documents.dao.ILFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class EtatStocksCapsules implements IEtatStockCapsulesServiceRemote {
	
	@Inject private IStockCapsulesDAO dao ;
	@Inject private IReportStockCapsulesDAO reportDao;
	@Inject private ILBonlivDAO taLDocumentServiceBonliv;
	@Inject private ILFactureDAO taLDocumentServiceFacture;
	@Inject private ILAvoirDAO taLDocumentServiceAvoir;
	@Inject private IMouvementDAO taMouvementStockService ;
    
	private List<TaEtatStockCapsules>  listeEtatStock = new ArrayList<TaEtatStockCapsules>(0);
	List<TaEtatStockCapsules>  listeEtatStockFinal = new ArrayList<TaEtatStockCapsules>();
	
	
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres, boolean strict){
		return calculEtatStocks(criteres,reportDao.recupDerniereDateReportStock(),  strict, false);
	}
	
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres,Date dateDeb, boolean strict, boolean synthese){
		//LinkedList<SWTStocks> listObject = new LinkedList<SWTStocks>();
		List<TaEtatStockCapsules>  listeEtatStockTemp = new ArrayList<TaEtatStockCapsules>();
		
		listeEtatStock.clear();
		listeEtatStockFinal.clear();
		
		listeEtatStockTemp =dao.selectSumMouvEntrees(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		listeEtatStockTemp =	dao.selectSumMouvSorties(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}

		listeEtatStockTemp.clear();
		listeEtatStockTemp =	taMouvementStockService.selectSumMouvEntreesCRD(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
		listeEtatStockTemp.clear();
		listeEtatStockTemp =	taMouvementStockService.selectSumMouvSortiesCRD(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}

		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesEntree(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesSorties(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}


		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesEntree(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesSorties(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
	

		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesEntrees(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesSorties(criteres,dateDeb,strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
		
		if(criteres.getAvecReport()) {
		listeEtatStockTemp.clear();
		listeEtatStockTemp =reportDao.selectReportStocks(criteres);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		}
		
		for (TaEtatStockCapsules taEtatStock : listeEtatStock) {
			int rang = -1;
			if(synthese) {
				rang =existeArticle(taEtatStock);
			}
			//int rang =existeArticleUnite(taEtatStock);
			if(rang==-1)
				listeEtatStockFinal.add(taEtatStock);
			else {
				TaEtatStockCapsules tmp = listeEtatStockFinal.get(rang);
				if(tmp.getQteCapsuleStock()==null && taEtatStock.getQteCapsuleStock()!=null)tmp.setQteCapsuleStock(taEtatStock.getQteCapsuleStock());
				else
				if(taEtatStock.getQteCapsuleStock()!=null)
					tmp.setQteCapsuleStock(tmp.getQteCapsuleStock().add(taEtatStock.getQteCapsuleStock()));
			}
		}
		
		return listeEtatStockFinal;
	}
	
	
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres, boolean strict, boolean synthese){
		return calculEtatStocks(criteres,reportDao.recupDerniereDateReportStock(), strict,synthese);
	}
	
	
	public List<TaEtatStockCapsules> calculEtatStocksMouvements(TaEtatStockCapsules criteres, boolean strict){
		return calculEtatStocksMouvements(criteres,reportDao.recupDerniereDateReportStock(), strict);
	}
	
	public int existeArticleUnite(TaEtatStockCapsules taEtatStock){
		int i =0;
		for (TaEtatStockCapsules e : listeEtatStockFinal) {
			if(e.equalsPartiel(taEtatStock))
				return i;
			i++;
		}
		return -1;
	}
	
	public int existeArticle(TaEtatStockCapsules taEtatStock){
		int i =0;
		for (TaEtatStockCapsules e : listeEtatStockFinal) {
			if(e.equalsPartielCodeArticle(taEtatStock))
				return i;
			i++;
		}
		return -1;
	}
	
	public Date recupDerniereDateCalcul(){
		return reportDao.recupDerniereDateReportStock();	
	}
	
	public List<TaEtatStockCapsules> calculEtatStocksReport(TaEtatStockCapsules criteres){
		List<TaEtatStockCapsules>  listeEtatStockTemp = new ArrayList<TaEtatStockCapsules>();
		listeEtatStockTemp.clear();
		
		listeEtatStock.clear();
		listeEtatStockFinal.clear();
		
		listeEtatStockTemp =reportDao.selectReportStocks(criteres);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}

		
		for (TaEtatStockCapsules taEtatStock : listeEtatStock) {
			int rang =existeArticleUnite(taEtatStock);
			if(rang==-1)
				listeEtatStockFinal.add(taEtatStock);
			else {

			}
		}
		
		return listeEtatStockFinal;
	}

	@Override
	public List<TaEtatStockCapsules> calculEtatStocks(TaEtatStockCapsules criteres, Date dateDeb, boolean strict) {
		// TODO Auto-generated method stub
		return calculEtatStocks(criteres,dateDeb,  strict, false);
	}

	@Override
	public List<TaEtatStockCapsules> calculEtatStocksMouvements(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict) {
List<TaEtatStockCapsules>  listeEtatStockTemp = new ArrayList<TaEtatStockCapsules>();
		
		
		listeEtatStock.clear();
		listeEtatStockFinal.clear();
		
		if(criteres.getAvecReport())
			listeEtatStockTemp =dao.selectSumMouvEntrees(criteres,reportDao.recupDerniereDateReportStock(),strict);		
		else	listeEtatStockTemp =dao.selectSumMouvEntrees(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		if(criteres.getAvecReport())
		listeEtatStockTemp =	dao.selectSumMouvSorties(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =	dao.selectSumMouvSorties(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}

		
		if(criteres.getAvecReport())
		listeEtatStockTemp =taMouvementStockService.selectSumMouvEntreesCRD(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taMouvementStockService.selectSumMouvEntreesCRD(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		
		if(criteres.getAvecReport())
		listeEtatStockTemp =	taMouvementStockService.selectSumMouvSortiesCRD(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =	taMouvementStockService.selectSumMouvSortiesCRD(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
		listeEtatStockTemp.clear();
		
		if(criteres.getAvecReport())
		listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesEntree(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesEntree(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		if(criteres.getAvecReport())listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesSorties(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceBonliv.selectSumMouvDocumentStocksCapsulesSorties(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		

		listeEtatStockTemp.clear();
		
		if(criteres.getAvecReport())
		listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesEntree(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesEntree(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		if(criteres.getAvecReport())listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesSorties(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceFacture.selectSumMouvDocumentStocksCapsulesSorties(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
	
		
		listeEtatStockTemp.clear();
		if(criteres.getAvecReport())
			listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesEntrees(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesEntrees(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		listeEtatStockTemp.clear();
		
		if(criteres.getAvecReport())
			listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesSorties(criteres,reportDao.recupDerniereDateReportStock(),strict);
		else listeEtatStockTemp =taLDocumentServiceAvoir.selectSumMouvDocumentStocksCapsulesSorties(criteres,criteres.getDateStockDeb(),strict);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		
		
		if(criteres.getAvecReport()) {
		listeEtatStockTemp.clear();
		listeEtatStockTemp =reportDao.selectReportStocks(criteres);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}
		}
		
		for (TaEtatStockCapsules taEtatStock : listeEtatStock) {
			int rang =-1;
			if(rang==-1)
				listeEtatStockFinal.add(taEtatStock);
			else {

			}
		}
		

		return listeEtatStockFinal;
	}

	@Override
	public Date recupDerniereDateReportStock() {
		// TODO Auto-generated method stub
		return reportDao.recupDerniereDateReportStock();
	}

	@Override
	public void recalculReport(List<TaEtatStockCapsules> listeReports, Date dateFin) {
		// TODO Auto-generated method stub
		reportDao.recalculReport(listeReports, dateFin);
	}
	
	

	@Override
	public List<TaEtatStockCapsules> calculEtatStocksPourReport(TaEtatStockCapsules criteres, Date dateDeb) {

		List<TaEtatStockCapsules>  listeEtatStockTemp = new ArrayList<TaEtatStockCapsules>();
		
		
		listeEtatStock.clear();
		listeEtatStockFinal.clear();
		
		listeEtatStockTemp =reportDao.selectReportStocks(criteres);
		if(listeEtatStockTemp!=null)
			for (TaEtatStockCapsules taStock : listeEtatStockTemp) {
				listeEtatStock.add(taStock);
			}

		
		for (TaEtatStockCapsules taEtatStock : listeEtatStock) {
			int rang = -1;

			rang =existeArticle(taEtatStock);

			if(rang==-1)
				listeEtatStockFinal.add(taEtatStock);
			else {
				TaEtatStockCapsules tmp = listeEtatStockFinal.get(rang);
				if(tmp.getQteCapsuleStock()==null && taEtatStock.getQteCapsuleStock()!=null)tmp.setQteCapsuleStock(taEtatStock.getQteCapsuleStock());
				else
				if(taEtatStock.getQteCapsuleStock()!=null)
					tmp.setQteCapsuleStock(tmp.getQteCapsuleStock().add(taEtatStock.getQteCapsuleStock()));
			}
		}
		
		return listeEtatStockFinal;
	}
	

}
