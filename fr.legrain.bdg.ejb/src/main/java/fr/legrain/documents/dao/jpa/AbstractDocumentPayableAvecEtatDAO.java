package fr.legrain.documents.dao.jpa;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dashboard.dto.CountDocumentParTypeRegrouptement;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;

/**
 * Home object for domain model class TaTicketDeCaisse.
 * @see fr.legrain.documents.dao.TaTicketDeCaisse
 * @author Hibernate Tools
 */


public abstract class  AbstractDocumentPayableAvecEtatDAO	<Entity,InfosEntity,LigneEntity> extends AbstractDocumentAvecEtatDAO<Entity,InfosEntity,LigneEntity>{


	static Logger logger = Logger.getLogger(AbstractDocumentPayableAvecEtatDAO.class);
	


	//  !!!!! déjà dans super  !!!!!!!!!!!!!
//	public String SUM_CA_JOUR_LIGTH_PERIODE ; 
//	public String SUM_CA_MOIS_LIGTH_PERIODE  ;
//	public String SUM_CA_ANNEE_LIGTH_PERIODE  ;
//	public String SUM_CA_TOTAL_LIGTH_PERIODE  ;
	//  !!!!! déjà dans super  !!!!!!!!!!!!!

	//Correspond à NonTransforme
	public String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE  ;
	public String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE  ;
	public String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE  ;
	public String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE  ;
	
	//Correspond à NonTransformeARelancer
	public String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER  ;
	public String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER  ;
	public String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER  ;
	public String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER  ;
	
	////Correspond à Transforme
	public String SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE  ;
	public String SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE  ;
	public String SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE  ;
	public String SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE  ;
	
	
	
	public String FIND_ARTICLES_PAR_TIERS_PAR_MOIS ;
	public String FIND_ARTICLES_PAR_TIERS_TRANSFORME ;
	public String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME ;
	public String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER ;
	
	public String FIND_ALL_LIGHT_PERIODE ;
	public String FIND_PAYE_LIGHT_PERIODE ;
	public String FIND_NON_PAYE_LIGHT_PERIODE  ;		
	public String FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE  ;


	public String SUM_CA_TOTAL_LIGHT_PERIODE_PAYE ;
	
	public String FIND_ALL_LIGHT_PERIODE_AVEC_ETAT;
	public String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_AVEC_ETAT;
	public String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_AVEC_ETAT;
	public String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_AVEC_ETAT;
	public String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_AVEC_ETAT;

	
	abstract public String getRequeteSommeAffecte(String prefixe) ;
	abstract public String getRequeteSommeAffecteJPQL(String prefixe) ;
	abstract public String getRequeteAffectationReglement() ;
	abstract public String getRequeteAffectationAvoir() ;
	abstract public String getRequeteAffectationReglementJPQL() ;
	abstract public String getRequeteAffectationAvoirJPQL() ;
	abstract public String getRequeteTypePaiement(String prefixe);
	abstract public String getRequeteTypePaiementSQL(String prefixe);
	
	
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
//	private String nameEntity;
//	private Entity entity;
//	@Inject ITPaiementDAO daoTpaiement;
	@Inject ITvaDAO daoTva;
	
	
	public void initialiseRequetes() {
		super.initialiseRequetes();

		SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE=
				"select "
				+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
					+ " from "+nameEntity+" doc  "
					+ " where doc.dateDocument between :dateDebut and :dateFin "
					+ " and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "
				+ " and coalesce((doc.netTtcCalc),0)<=("+getRequeteSommeAffecteJPQL("doc") +")"
				+ " group by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
				+ " order by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";

		SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE=
				"select "
				+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
					+ " from "+nameEntity+" doc  "
					+ " where doc.dateDocument between :dateDebut and :dateFin "
					+ " and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "
				+ " and coalesce((doc.netTtcCalc),0)<=("+getRequeteSommeAffecteJPQL("doc") +")"
				+ " group by extract(month from doc.dateDocument),extract(year from doc.dateDocument)"
				+ " order by extract(month from doc.dateDocument),extract(year from doc.dateDocument)";

		SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE=
				"select "
				+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
					+ " from "+nameEntity+" doc  "
					+ " where doc.dateDocument between :dateDebut and :dateFin "
					+ " and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "
				+ " and coalesce((doc.netTtcCalc),0)<=("+getRequeteSommeAffecteJPQL("doc") +")"
				+ " group by extract(year from doc.dateDocument)"
				+ " order by extract(year from doc.dateDocument)";
		// Renvoi le total à relancer car date échéance dépassée du ca ht, des affectations des règlements+des affectations des avoirs, et du reste à règler des factures sur la période dateDebut à dateFin

		SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE=				
				"select "
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
				+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
					+ " from "+nameEntity+" doc  "
					+ " where doc.dateDocument between :dateDebut and :dateFin "
					+ " and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "
				+ " and coalesce(doc.netTtcCalc,0)<=("+getRequeteSommeAffecteJPQL("doc") +")"
				+ " group by true";


		SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "+getRequeteARelancer()    
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by extract(year from doc.date_Document) )as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "+getRequeteARelancer()    
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by extract(month from doc.date_Document),extract(year from doc.date_Document))as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "+getRequeteARelancer()  
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document))as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_annee+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "+getRequeteARelancer()    
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by s1.affectation,s2.affectationAvoir)as res group by res.jour,res.mois,res.annee" ;

		SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document))as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin  "  
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by extract(month from doc.date_Document),extract(year from doc.date_Document))as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir" 
						+" order by extract(year from doc.date_Document))as res group by res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE = 
				"select res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_annee+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by s1.affectation,s2.affectationAvoir)as res group by res.jour,res.mois,res.annee";	
	

		
		FIND_ARTICLES_PAR_TIERS_PAR_MOIS=
				"select "
						+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
						taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
						taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
						
						taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" ,"+

						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art left join art.taFamille fam  "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " order by doc.codeDocument";

		// Renvoi les articles de l'ensemble des Facture transformé sur la période dateDebut à dateFin
		FIND_ARTICLES_PAR_TIERS_TRANSFORME=
				"select "
						+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
						taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
						taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null "
						+ " and coalesce((doc.netTtcCalc),0)<=("+getRequeteSommeAffecteJPQL("doc") +")"
						+ " order by doc.codeDocument";

		FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME=
				"select "
						+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
						+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
						+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
						+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
						+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
						+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
						+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null "
						+ " and coalesce((doc.netTtcCalc),0)>("+getRequeteSommeAffecteJPQL("doc") +")"
						+ " order by doc.codeDocument";


		// Renvoi les articles de l'ensemble des Facture non transformé sur la période dateDebut à dateFin
		FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER=
				"select "+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
						+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
						+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
						+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
						+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
						+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
						+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin "+getRequeteARelancerJPQL()  
						+ " and doc.taEtat is null  and tiers.codeTiers like :codeTiers "  
						+ " and coalesce((doc.netTtcCalc),0)>("+getRequeteSommeAffecteJPQL("doc") +")"
						+ " order by doc.codeDocument";

		FIND_ALL_LIGHT_PERIODE=
				"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
						+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
						
	    				+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
	    				+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
		
						+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
						+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
						+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
						+" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers " 
						+" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and tiers.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,infos.prenom_Tiers, infos.nom_Entreprise,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
						+" order by doc.date_Document DESC, doc.code_Document  DESC";

		FIND_PAYE_LIGHT_PERIODE=
				"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
						+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
						
						+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
						
						+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
						+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
						+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet " + 
						" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers " +
						" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " +
						" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document " + 
						" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document" + 	
						" where doc.date_Document between :dateDebut and :dateFin " + 
				" and tiers.code_Tiers like :codeTiers" + 
				" and doc.id_Etat is null"+  
				" and coalesce((doc.net_Ttc_Calc),0)<=("+getRequeteSommeAffecte("doc") +")"+
				" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers, infos.prenom_Tiers, infos.nom_Entreprise,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
				" order by doc.date_Document DESC, doc.code_Document DESC";

		FIND_NON_PAYE_LIGHT_PERIODE=
				"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
						+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
						
						+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
						
						
						+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
						+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
						+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet " + 
						" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers " +
						" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " +
						" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document " + 
						" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document" + 	
						" where doc.date_Document between :dateDebut and :dateFin " + 
				" and tiers.code_Tiers like :codeTiers" + 
				" and doc.id_Etat is null"+  
				" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"+
				" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers, infos.prenom_Tiers, infos.nom_Entreprise, doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
				" order by doc.date_Document DESC, doc.code_Document DESC";

		FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE=
				"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
						+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
						
						+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "

						+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
						+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
						+" (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
						+" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers " 
						+" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
						+" where doc.date_Document between :dateDebut and :dateFin "+getRequeteARelancer()    
						+" and tiers.code_Tiers like :codeTiers"  
						+" and doc.id_Etat is null "  
						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,infos.prenom_Tiers, infos.nom_Entreprise,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
						+" order by doc.date_Document DESC, doc.code_Document DESC";


		SUM_CA_TOTAL_LIGHT_PERIODE_PAYE=
				"select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " coalesce(sum(doc.netHtCalc),0),coalesce(sum(doc.netTvaCalc),0),coalesce(sum(doc.netTtcCalc),0)) "
				+ " from "+nameEntity+" doc  "
				+ " where doc.dateDocument between :dateDebut and :dateFin  "
				+ " and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "
				+ " and coalesce((doc.netTtcCalc),0)<=("+getRequeteSommeAffecteJPQL("doc") +")";
		

		
		
		
		
		/////////////****************** AVEC ETAT ******************/////////////////////////
		
		FIND_ALL_LIGHT_PERIODE_AVEC_ETAT=
				"select "
						+"case  when "+getRequeteARelancer().replaceFirst("and", "")+" then true else false   END as relancer,"
						+documentDTO.retournChampAndAlias("et","identifiant")+", "
						+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
						+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
						+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
						
	    				+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
	    				+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
		
						+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
						+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
						+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
						+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
						+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
						+" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
						+ " join ta_R_Etat re on re.id_r_etat=doc.id_r_etat "
						+ " join ta_Etat et on et.id_etat=re.id_etat " 
						+" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and tiers.code_Tiers like :codeTiers"  
						+"  and et.identifiant like :etat "  
//						+" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,infos.prenom_Tiers, infos.nom_Entreprise,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
						+" order by doc.date_Document DESC, doc.code_Document  DESC";

		
		
		SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_AVEC_ETAT = 
				"select res.identifiant, res.relancer,res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, "
				+ "sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+"et.identifiant  as identifiant, "
						+"case  when "+getRequeteARelancer().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ documentChiffreAffaireDTO.retournExtractDayAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+ " join ta_R_Etat re on re.id_r_etat=doc.id_r_etat "
						+ " join ta_Etat et on et.id_etat=re.id_etat " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+"  and et.identifiant like :etat " 
//						+" and coalesce((doc.net_Ttc_Calc),0)>(" +getRequeteSommeAffecte("doc") +")"  
						+" group by et.identifiant,relancer,extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by relancer,extract(day from doc.date_Document),extract(month from doc.date_Document),extract(year from doc.date_Document))"
						+ " as res group by res.identifiant,relancer,res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_AVEC_ETAT = 
				"select res.identifiant,res.relancer,res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, "
				+ "sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+"et.identifiant  as identifiant, "
						+"case  when "+getRequeteARelancer().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+ " join ta_R_Etat re on re.id_r_etat=doc.id_r_etat "
						+ " join ta_Etat et on et.id_etat=re.id_etat " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin  "  
						+" and t.code_Tiers like :codeTiers"  
						+"  and et.identifiant like :etat "  
//						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by et.identifiant,relancer,extract(month from doc.date_Document),extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir"  
						+" order by relancer,extract(month from doc.date_Document),extract(year from doc.date_Document))"
						+ "as res group by res.identifiant,res.relancer,res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_AVEC_ETAT = 
				"select res.identifiant,res.relancer,res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, "
				+ "sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+"et.identifiant  as identifiant, "
						+"case  when "+getRequeteARelancer().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAliasSQLNatif("doc","date_Document")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+ " join ta_R_Etat re on re.id_r_etat=doc.id_r_etat "
						+ " join ta_Etat et on et.id_etat=re.id_etat " 
						+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+"  and et.identifiant like :etat "   
//						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by et.identifiant,relancer,extract(year from doc.date_Document),s1.affectation,s2.affectationAvoir" 
						+" order by relancer,extract(year from doc.date_Document))"
						+ "as res group by res.identifiant,res.relancer,res.jour,res.mois,res.annee";

		SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_AVEC_ETAT = 
				"select  res.identifiant,res.relancer,res.jour as jour ,res.mois as mois ,res.annee as annee,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, "
				+ "sum(res.mtTtcCalc)as mtTtcCalc,sum(res.reglementComplet)as reglementComplet,sum(res.resteAReglercomplet)as resteAReglercomplet from( "
						+"select "
						+"et.identifiant  as identifiant, "
						+"case  when "+getRequeteARelancer().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_jour+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_mois+","
						+ "cast('0' as varchar) as "+documentChiffreAffaireDTO.f_annee+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ht_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Tva_Calc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "net_Ttc_Calc")+","
						+" (s1.affectation+s2.affectationAvoir)as reglementComplet, "
						+" (coalesce(sum(doc.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteAReglercomplet "
						+" from "+nameEntitySQL+" doc  "
						+" join ta_tiers t on t.id_tiers=doc.id_tiers "  
						+ " join ta_R_Etat re on re.id_r_etat=doc.id_r_etat "
						+ " join ta_Etat et on et.id_etat=re.id_etat " 
						+" left join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
						+" left join (" +getRequeteAffectationAvoir()+")as s2 on s2.id_document=doc.id_document"   
						+" where doc.date_Document between :dateDebut and :dateFin "  
						+" and t.code_Tiers like :codeTiers"  
						+"  and et.identifiant like :etat "  
//						+" and coalesce((doc.net_Ttc_Calc),0)>("+getRequeteSommeAffecte("doc") +")"
						+" group by  et.identifiant,relancer,s1.affectation,s2.affectationAvoir)"
						+ "as res group by  res.identifiant,res.relancer,res.jour,res.mois,res.annee";	
		
		
	

		
	
		
	}
	
	
	public AbstractDocumentPayableAvecEtatDAO(){
		entity=newDocument();
		nameEntity=entity.getClass().getName();
	}
	
	
	private Object newGenerics(int postion) {
		try {
			return ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[postion]).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Entity newDocument() {
		return (Entity) newGenerics(0);
	}
	
	
	
	public List<DocumentChiffreAffaireDTO> countDocumentParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");
		List<DocumentChiffreAffaireDTO> l ;
		l=super.countDocumentParTypeRegroupement(debut, fin, codeTiers, typeRegroupement, valeurRegroupement);
		if(l!=null )return l;//si type regroupement trouvé dans classe mere
		if(codeTiers==null)codeTiers="%";
		Query query = null;
		String requete="";
		boolean execute=true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			requete="select  "
					+"count(doc) as "+documentChiffreAffaireDTO.f_count+","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
					+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+",";
			
			
			 switch(typeRegroupement)
		        {
		            case Const.PAR_TYPE_PAIEMENT:
						requete=documentChiffreAffaireDTO.retournChampAndAlias("tp", "codeTPaiement")+""
								+" from "+nameEntity+" doc "
								+" join doc.lignes ldoc "
								+ " join doc.taRReglements rr join rr.taReglement reg join reg.taTPaiement tp "+             	
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and tp.codeTPaiement like :valeur " + 
		    					" group by tp.codeTPaiement" + 
		    					" order by tp.codeTPaiement";
		            break;
		            case Const.PAR_VENDEUR:
		            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
							requete=documentChiffreAffaireDTO.retournChampAndAlias("ut", "username")+""
								+" from "+nameEntity+" doc"+ 		            		
								" join doc.lignes ldoc "+
								" join doc.taUtilisateurVendeur ut "+
		    					" join doc.lignes ldoc " + 		            			
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and ut.username like :valeur " + 
		    					" group by ut.username" + 
		    					" order by ut.username";
		            	}
		            break;
		            default:execute=false;break;
		}

			 if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			 }
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");
		List<DocumentChiffreAffaireDTO> l ;
		l=super.countDocumentParTypeRegroupement(debut, fin, codeTiers, typeRegroupement, valeurRegroupement);
		if(l!=null )return l;//si type regroupement trouvé dans classe mere
		if(codeTiers==null)codeTiers="%";
		if(codeEtat==null ||codeEtat.equals("%")) {
			List<TaEtat> listEtat =serviceEtat.selectAll();
			codeEtat=new LinkedList<String>();
			for (TaEtat taEtat : listEtat) {
				((List<String>)codeEtat).add(taEtat.getCodeEtat());
			}
		}
		Query query = null;
		String requete="";
		boolean execute=true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			requete="select  "
					+"count(doc) as "+documentChiffreAffaireDTO.f_count+","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
					+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+",";
			
			
			 switch(typeRegroupement)
		        {
		            case Const.PAR_TYPE_PAIEMENT:
						requete+=documentChiffreAffaireDTO.retournChampAndAlias("tp", "codeTPaiement")+""
								+" from "+nameEntity+" doc"+
								" join doc.lignes ldoc "+
		    					" join doc.taEtat etat "
		    					+ " join doc.taRReglements rr join rr.taReglement reg join reg.taTPaiement tp "+
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and etat.codeEtat in :codeEtat "+
		    					" and tp.codeTPaiement like :valeur " + 
		    					" group by tp.codeTPaiement" + 
		    					" order by tp.codeTPaiement";
		            break;
		            case Const.PAR_VENDEUR:
		            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
							requete+=documentChiffreAffaireDTO.retournChampAndAlias("ut", "username")+""
								+" from "+nameEntity+" doc"+
								" join doc.lignes ldoc "+
		    					" join doc.taEtat etat "+
		    					" join doc.taUtilisateurVendeur ut "+
		    					" join doc.lignes ldoc " + 		            			
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and etat.codeEtat in :codeEtat "+
		    					" and ut.username like :valeur " + 
		    					" group by ut.username" + 
		    					" order by ut.username";
		            	}
		            break;
		            default:execute=false;break;
		}

			 if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeEtat",codeEtat);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			 }
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


//	
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
//		logger.debug("getting nombre document dans periode");
//		
//		if(codeTiers==null)codeTiers="%";
//		Query query = null;
//		String requete="";
//	    boolean execute = true;
//		try {
//			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
//			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
//			
////        	DocumentChiffreAffaireDTO d=new DocumentChiffreAffaireDTO();
////        	d.ajouteAlias(d.getCodeArticle());
//        	
//        	
//			 switch(typeRegroupement)
//		        {
//	            case Const.PAR_TIERS :
//	    			requete = "select count(doc) as count,t.codeTiers as codeTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//	    					" join doc.lignes ldoc " + 
//	    					" join doc.taTiers t " + 
//	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//	    					" group by t.codeTiers" + 
//	    					" order by t.codeTiers";
//	            break;
//	            case Const.PAR_ARTICLE :
//	    			requete = "select count(doc) as count,a.codeArticle as codeArticle,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//	    					" join doc.lignes ldoc " + 
//	    					" join ldoc.taArticle a " + 
//	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//	    					" and a.codeArticle like :valeur " + 
//	    					" group by a.codeArticle" + 
//	    					" order by a.codeArticle";
//	            break;	            
//		            case Const.PAR_FAMILLE_ARTICLE :
//		    			requete = "select count(doc) as count,fam.codeFamille as codeFamille,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" join ldoc.taArticle a " + 
//		    					" left join a.taFamille fam " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
//		    					" group by fam.codeFamille" + 
//		    					" order by fam.codeFamille";
//		            break;
//		            
//		            case Const.PAR_TAUX_TVA:
//		            	if(valeurRegroupement.equals("%")) {
//		            		valeurRegroupement=listeTauxTvaExistant();
//		            	}
//		    			requete = "select count(doc) as count,ldoc.tauxTvaLDocument as tauxTva,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" and (ldoc.tauxTvaLDocument is null or ldoc.tauxTvaLDocument in :valeur )" + 
//		    					" group by ldoc.tauxTvaLDocument" + 
//		    					" order by ldoc.tauxTvaLDocument";	            	
//		            break;
//		            case Const.PAR_TYPE_PAIEMENT:
//
//		            	requete=" select count(doc) as count,tp.codeTPaiement as codeTPaiement,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		            			" left join doc.taRReglements rr  "+
//		    					" left join rr.taReglement reg  "+
//		    					" left join reg.taTPaiement tp  "+
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
//		    					" and (tp.codeTPaiement is null or tp.codeTPaiement like :valeur )" + 
//		    					" group by tp.codeTPaiement" + 
//		    					" order by tp.codeTPaiement";
//		            break;
//		            case Const.PAR_FAMILLE_TIERS :
//
//		    			requete = "select count(doc) as count,fam.codeFamille as codeFamilleTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" join doc.taTiers t " + 
//		    					" join t.taFamilleTiers fam " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
//		    					" group by fam.codeFamille" + 
//		    					" order by fam.codeFamille";
//		            break;
//		            case Const.PAR_VENDEUR:
//		            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
//		            	requete=" select count(doc) as count,ut.username as vendeur,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.taUtilisateurVendeur ut "+
//		    					" join doc.lignes ldoc " + 		            			
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
//		    					" and ut.username like :valeur " + 
//		    					" group by ut.username" + 
//		    					" order by ut.username";
//		            	}else execute=false;
//		            break;
//		            default:execute=false;break;
//		}
//
//
//			 if(execute) {
//			query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
//			query.setParameter("codeTiers",codeTiers);
//			if(!typeRegroupement.equals(Const.PAR_TIERS))query.setParameter("valeur",valeurRegroupement);
//			
////			List<CountDocumentParTypeRegrouptement> l = query.getResultList();
//			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			return l;
//			 }
//			 return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	
	
	
	
	public List<CountDocumentParTypeRegrouptement> countArticlesParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre facture dans periode");

		
		if(codeTiers==null)codeTiers="%";
		Query query = null;
		String requete="";
		boolean execute=true;
		List<CountDocumentParTypeRegrouptement> l=null;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			requete =
			"select "
					+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
					taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
					taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLDocument")+","+
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLDocument")+" ";
			
			 switch(typeRegroupement)
		        {
		            case Const.PAR_FAMILLE_ARTICLE :
						requete+=","+taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" "
								+ " from "+nameEntity+" doc join doc.lignes b join doc.taTiers c join doc.taInfosDocument infos left join c.taEntreprise d "+
		    					" join ldoc.taArticle doc " + 
		    					" join doc.taFamille fam " + 
		    					" where doc.dateDocument between :dateDebut and :dateFin and dodoc.taTiers.codeTiers like :codeTiers "+
		    					" and fam.codeFamille like :valeur " + 
		    					" group by fam.codeFamille" + 
		    					" order by fam.codeFamille";
		            break;
		            case Const.PAR_TAUX_TVA:
						requete+=","+taArticlesParTiersDTO.retournChampAndAlias("b", "tauxTvaLDocument")+" "
								+ " from "+nameEntity+" doc join doc.lignes b join doc.taTiers c join doc.taInfosDocument infos left join c.taEntreprise d "+ 
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and ldoc.tauxTvaLDocument = :valeur " + 
		    					" group by b.tauxTvaLDocument" + 
		    					" order by b.tauxTvaLDocument";		            	
		            break;
		            case Const.PAR_TYPE_PAIEMENT:
						requete+=","+taArticlesParTiersDTO.retournChampAndAlias("tp", "codeTPaiement")+" "
								+ " from "+nameEntity+" doc join doc.lignes b join doc.taTiers c join doc.taInfosDocument infos left join c.taEntreprise d "+
		    					" join doc.taRReglements rr  "+
		    					" join rr.taReglement reg  "+
		    					" join reg.taTPaiement tp  "+
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and tp.codeTPaiement like :valeur " + 
		    					" group by tp.codeTPaiement" + 
		    					" order by tp.codeTPaiement";
		            break;

		            default:execute=false;break;
		}

			 if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(CountDocumentParTypeRegrouptement.class)).list();
			 }
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	

	

	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		Query query = null;
		String requete="";
		boolean execute = false;
		if(codeTiers==null)codeTiers="%";
		List<TaArticlesParTiersDTO> l=null;
		l=super.findArticlesParTiersParMoisParTypeRegroupement(debut, fin,codeTiers, typeRegroupement, valeurRegroupement, false);
		if(l!=null)return l;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			requete =
			"select "
					+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
					taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
					taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","+
					taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "+
					" from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos "
							+ " left join tiers.taEntreprise ent ";
			
			switch(typeRegroupement)
			{
			case Const.PAR_TYPE_PAIEMENT:
				if(entity.getClass().isInstance(TaTicketDeCaisse.class)
						||entity.getClass().isInstance(TaFacture.class)) {
				requete+=" join ldoc.taArticle art join doc.taRReglements rr join rr.taReglement reg  join reg.taTPaiment tp "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers " 
						+ " and tp.codeTPaiement like :valeur"
						+ " group by tp.codeTPaiement , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by tp.codeTPaiement,doc.codeDocument ";
				execute=true;
				}
				break;
			case Const.PAR_VENDEUR:
				if(entity.getClass().isInstance(TaTicketDeCaisse.class)) {
					requete+=" join ldoc.taArticle art "
							+ " join doc.taUtilisateurVendeur ut "			            			
							+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "  
							+ " and tp.codeTPaiement like :valeur"
							+ " group by ut.username , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
							+ " order by ut.username,doc.codeDocument ";	
					execute=true;
				}
				break;		            

			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("valeur", valeurRegroupement);

				l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
				//			List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	


	
	
//	/**
//	 * Classe permettant d'obtenir les factures non payées
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de facture non payées totalement
//	 */
//	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre facture non paye totalement");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete="select count(doc) "
//					+ " from "+nameEntity+" doc "					
//					+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers"
//					+ " and doc.taEtat is null "
//					+ " and exists("+getRequeteDocumentTransforme("doc")+")";
//
//
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("dateDebut", debut);
//			query.setParameter("dateFin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevisNonTranforme = (Long)query.getSingleResult();
//			result = nbDevisNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	public Query addScalarDocumentChiffreAffaireDTO(Query query ) {
		query.unwrap(SQLQuery.class)
		.addScalar(DocumentChiffreAffaireDTO.f_jour, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mois, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_annee, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_reglementComplet, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_resteAReglerComplet, BigDecimalType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
		return query;
	}
	
	
	public Query addScalarDocumentDTO(Query query ) {
		query.unwrap(SQLQuery.class)
        .addScalar(DocumentDTO.f_idDocument, IntegerType.INSTANCE)
        .addScalar(DocumentDTO.f_codeDocument, StringType.INSTANCE)
        .addScalar(DocumentDTO.f_dateDocument, DateType.INSTANCE)
        .addScalar(DocumentDTO.f_libelleDocument, StringType.INSTANCE)
        .addScalar(DocumentDTO.f_codeTiers, StringType.INSTANCE)
        .addScalar(DocumentDTO.f_nomTiers, StringType.INSTANCE)
        .addScalar(DocumentDTO.f_dateEchDocument, DateType.INSTANCE)
        .addScalar(DocumentDTO.f_dateExport, DateType.INSTANCE)
        .addScalar(DocumentDTO.f_netHtCalc, BigDecimalType.INSTANCE)
        .addScalar(DocumentDTO.f_netTvaCalc, BigDecimalType.INSTANCE)
        .addScalar(DocumentDTO.f_netTtcCalc, BigDecimalType.INSTANCE)
        .addScalar(DocumentDTO.f_reglementComplet, BigDecimalType.INSTANCE)
        .addScalar(DocumentDTO.f_resteAReglerComplet, BigDecimalType.INSTANCE)   
        .addScalar(DocumentDTO.f_prenomTiers, StringType.INSTANCE)
        .addScalar(DocumentDTO.f_nomEntreprise, StringType.INSTANCE)
    
        .setResultTransformer(Transformers.aliasToBean(DocumentDTO.class));
		return query;
	}


	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			switch (precision) {
			case 0:
				query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER);
				
				break;

			case 1:
				query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER);
			
				break;
			case 2:
				query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			addScalarDocumentChiffreAffaireDTO(query);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();

			
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
//		return null;	
	}


	 /**
		 * Classe permettant d'obtenir la listes des devis non transformés
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
		 * ne doivent pas dépasser par rapport à la date du jour 
		 * @return La requête renvoyée renvoi la liste des devis non transformés à relancer
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
			List<TaDevisDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			addScalarDocumentChiffreAffaireDTO(query);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
//			return null;
		}
		
		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
				int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE);
					
					break;

				case 1:
					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE);
					
					break;
				case 2:
					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE);
					
					break;

				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				addScalarDocumentChiffreAffaireDTO(query);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
//			return null;	
		}


		/**
		 * Classe permettant d'obtenir le reste à règler des factures non totalement réglée sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis non transformés sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				addScalarDocumentChiffreAffaireDTO(query);
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
//			return null;	
		}
		
		
		/**
		 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				
				List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
				
		}
		
		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createQuery(SUM_CA_ANNEE_LIGTH_PERIODE);
					
					break;

				case 1:
					query = entityManager.createQuery(SUM_CA_MOIS_LIGTH_PERIODE);
					
					break;
				case 2:
					query = entityManager.createQuery(SUM_CA_JOUR_LIGTH_PERIODE);
					
					break;

				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
				
		}

		/**
		 * Classe permettant d'obtenir le ca généré par les devis transformés sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis transformés sur la période éclaté en fonction de la précision 
		 * Jour Mois Année
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			try {
				switch (precision) {
				case 0:
					query = entityManager.createQuery(SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;

				case 1:
					query = entityManager.createQuery(SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;
				case 2:
					query = entityManager.createQuery(SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE);
					
					break;

				default:
					break;
				}
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
//			return null;	
		}

		 /**
		 * Classe permettant d'obtenir la listes des Factures non transformées
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
		 * ne doivent pas dépasser par rapport à la date du jour 
		 * @return La requête renvoyée renvoi la liste des Factures non transformées à relancer
		 */
		public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
			List<DocumentDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createNativeQuery(FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			addScalarDocumentDTO(query);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		}

		 /**
		 * Classe permettant d'obtenir les Factures non transformés
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
		 */
		public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
			List<DocumentDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Query query = entityManager.createNativeQuery(FIND_NON_PAYE_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			addScalarDocumentDTO(query);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		}

		 /**
		 * Classe permettant d'obtenir la liste des Factures transformés
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
		 */
		public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
			List<DocumentDTO> result = null;
			if(codeTiers==null)codeTiers="%";
			try {
			Query query = entityManager.createNativeQuery(FIND_PAYE_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			addScalarDocumentDTO(query);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		}
		
		
		@Override
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
//			return null;	
		}
		
		
		public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
			try {			
				if(codeTiers==null)codeTiers="%";

				/**RAJOUT YANN POUR  JM**/
//				+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
//				+documentDTO.retournChampAndAlias("tiers", "prenom_Tiers")+" , "
				FIND_ALL_LIGHT_PERIODE=
						"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
								+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
								+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
								
								+documentDTO.retournChampAndAlias("infos", "nom_Entreprise")+" , "
								+documentDTO.retournChampAndAlias("infos", "prenom_Tiers")+" , "
								
								+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
								+documentDTO.construitCoallesceAsAlias("doc", "net_Ht_Calc")+" , "
								+documentDTO.construitCoallesceAsAlias("doc", "net_Tva_Calc")+" , "
								+documentDTO.construitCoallesceAsAlias("doc", "net_Ttc_Calc")+" , "
								+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
								+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
								+" from "+nameEntitySQL+" doc join ta_tiers tiers on tiers.id_tiers=doc.id_tiers " 
								+" join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document " 
								+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
								+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
								+" where doc.date_Document between :dateDebut and :dateFin "  
								+" and tiers.code_Tiers like :codeTiers"    
								+" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers, infos.prenom_Tiers, infos.nom_Entreprise, doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
								+" order by doc.date_Document DESC, doc.code_Document DESC ";
				
//				Query query = entityManager.createNamedQuery(TaFacture.QN.FIND_ALL_LIGHT_PERIODE);
				Query query = entityManager.createNativeQuery(FIND_ALL_LIGHT_PERIODE);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
//				List<DocumentDTO> l = query.getResultList();
				query=addScalarDocumentDTO(query);
				List<DocumentDTO> l = query.getResultList();
				logger.debug("get successful");
				
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin,String codeTiers) {
			try {
				if(codeTiers==null)codeTiers="%";
				Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
//				List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
			try {
				if(codeTiers==null)codeTiers="%";
				Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
				Date dateJour = LibDate.dateDuJour();
				Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				for (Parameter<?> l : query.getParameters()) {
					if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
					if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
				}
				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
//				List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}	

		
		
		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin,String codeTiers) {
			try {
				if(codeTiers==null)codeTiers="%";
				Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_PAR_MOIS);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
//				List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		@Override
		public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin,String codeTiers) {
			try {
				if(codeTiers==null)codeTiers="%";
				Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_TRANSFORME);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
//				List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		
		/**
		 * Classe permettant d'obtenir le ca généré par les tickets de caisse sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param typeRegroupement map a une ligne représentant en cle le type de regroupement en valeur : la valeur du regroupement (ex parFamilleArticle , 'toto')
		 * @param regrouper sert à savoir si on doit détailler ou synthétiser les résultats
		 * @return La requête renvoyée renvoi le CA des tickets de caisse sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper) {
			try {
				Query query = null;
				String requete="";
				List<DocumentChiffreAffaireDTO> l;
				boolean execute = true;
				l=super.listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
				if(l!=null)return l;
				
				if(codeTiers==null)codeTiers="%";
				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
				switch(typeRegroupement)
				{
				case Const.PAR_TYPE_PAIEMENT:
					requete="select  "
							+documentChiffreAffaireDTO.retournChampAndAlias("tp", "codeTPaiement")+","
							+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
							+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
							+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
							+ " from "+nameEntity+" doc "
							+ " join doc.lignes ldoc "
							+ " join doc.taRReglements rr join rr.taReglement reg join reg.taTPaiement tp "
							+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
							+ " and tp.codeTPaiement like :valeur";
					if(regrouper)requete+=" group by  tp.codeTPaiement";	

					break;
				case Const.PAR_VENDEUR:
					if(entity.getClass().isInstance(TaTicketDeCaisse.class)) {
						requete="select  "
								+documentChiffreAffaireDTO.retournChampAndAlias("ut", "username")+","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
								+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""						
								+ " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taUtilisateurVendeur ut "
								+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers and "
								+ " ut.username like :valeur ";
						if(regrouper)requete+=" group by  ut.username";	
					}else execute=false;
					break;
				default:execute=false;break;
				}
				//			}



				if(execute) {
					query = entityManager.createQuery(requete);
					query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
					query.setParameter("dateFin", dateFin, TemporalType.DATE);
					query.setParameter("codeTiers",codeTiers);
					query.setParameter("valeur", valeurRegroupement);
					l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
					//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
					if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
						l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
					}
					logger.debug("get successful");
					return l;
				}
				return null;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}

		}

		
//		public List<DocumentChiffreAffaireDTO> chiffreAffaireParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
//			logger.debug("getting nombre document dans periode");
//			
//			if(codeTiers==null)codeTiers="%";
//			Query query = null;
//			String requete="";
//			boolean execute = true;
//			try {
//				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
//				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
//				
//				 switch(typeRegroupement)
//			        {
//		            case Const.PAR_TIERS :
//		    			requete = "select count(doc) as count,t.codeTiers as codeTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" join doc.taTiers t " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" group by t.codeTiers" + 
//		    					" order by t.codeTiers";
//		            break;
//		            case Const.PAR_ARTICLE :
//		    			requete = "select count(doc) as count,art.codeArticle as codeArticle,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" join ldoc.taArticle art " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" and art.codeArticle like :valeur " + 
//		    					" group by art.codeArticle" + 
//		    					" order by art.codeArticle";
//		            break;	 
//		            case Const.PAR_FAMILLE_TIERS :
//
//		    			requete = "select count(doc) as count,fam.codeFamille as codeFamilleTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//		    					" join doc.lignes ldoc " + 
//		    					" join doc.taTiers t " + 
//		    					" join t.taFamilleTiers fam " + 
//		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//		    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
//		    					" group by fam.codeFamille" + 
//		    					" order by fam.codeFamille";
//		            break;
//		            
//			            case Const.PAR_FAMILLE_ARTICLE :
//			    			requete = "select count(doc) as count,fam.codeFamille as codeFamille,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//			    					" join doc.lignes ldoc " + 
//			    					" join ldoc.taArticle art " + 
//			    					" left join art.taFamille fam " + 
//			    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//			    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
//			    					" group by fam.codeFamille" + 
//			    					" order by fam.codeFamille";
//			            break;
//			            
//						case Const.PAR_TAUX_TVA:
//			            	if(valeurRegroupement.equals("%")) {
//			            		valeurRegroupement=listeTauxTvaExistant();
//			            	}
//							if(valeurRegroupement instanceof String) {
//								valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
//							}
//			    			requete = "select count(doc) as count,ldoc.tauxTvaLDocument as tauxTva,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//			    					" join doc.lignes ldoc " + 
//			    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
//			    					" and (ldoc.tauxTvaLDocument is null or ldoc.tauxTvaLDocument in :valeur )" + 
//			    					" group by ldoc.tauxTvaLDocument" + 
//			    					" order by ldoc.tauxTvaLDocument";	            	
//			            break;
//			            case Const.PAR_TYPE_PAIEMENT:
//
//			            	requete=" select count(doc) as count,tp.codeTPaiement as codeTPaiement,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//			    					" join doc.lignes ldoc " + 
//			            			" left join doc.taRReglements rr  "+
//			    					" left join rr.taReglement reg  "+
//			    					" left join reg.taTPaiement tp  "+
//			    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
//			    					" and (tp.codeTPaiement is null or tp.codeTPaiement like :valeur )" + 
//			    					" group by tp.codeTPaiement" + 
//			    					" order by tp.codeTPaiement";
//			            break;
//			            case Const.PAR_VENDEUR:
//			            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
//			            	requete=" select count(doc) as count,ut.username as vendeur,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
//			    					" join doc.taUtilisateurVendeur ut "+
//			    					" join doc.lignes ldoc " + 		            			
//			    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
//			    					" and ut.username like :valeur " + 
//			    					" group by ut.username" + 
//			    					" order by ut.username";
//			            	}else execute=false;
//			            break;
//			            default:execute=false;break;
//			}
//
//
//				 if(execute) {
//				query = entityManager.createQuery(requete);
//				query.setParameter("dateDebut", debut);
//				query.setParameter("dateFin", fin);
//				query.setParameter("codeTiers",codeTiers);
//				if(!typeRegroupement.equals(Const.PAR_TIERS))query.setParameter("valeur",valeurRegroupement);
//				
//				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				return l;
//				 }
//				 return null;
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
//		}

		public List<DocumentDTO> findAllDTOPeriodeParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
			Query query = null;
			String requete="";
			String condition="";
			if(codeTiers==null)codeTiers="%";
			boolean execute = true;
			try {
				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
				requete =
						"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
								+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
								+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
								+documentDTO.construitSumCoallesceAsAlias("doc", DocumentDTO.f_net_Ht_Calc)+" , "
								+documentDTO.construitSumCoallesceAsAliasMtTvaSQL("doc", DocumentDTO.f_net_Tva_Calc)+" , "
								+documentDTO.construitSumCoallesceAsAlias("doc", DocumentDTO.f_net_Ttc_Calc)+" , "
								
//								+documentDTO.construitSumCoallesceAsAlias("ldoc", "mt_Ht_L_Document")+" , "
//								+documentDTO.construitSumCoallesceAsAliasMtTvaSQL("ldoc", DocumentDTO.f_mt_Tva_Calc)+" , "
//								+documentDTO.construitSumCoallesceAsAlias("ldoc", "mt_Ttc_L_Document")+" , "
								+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
								+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
								+" from "+nameEntitySQL+" doc "
										+ " join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document "
										+" join "+nameLignesEntitySQL+" ldoc on ldoc.id_document=doc.id_document" 
										+ " join ta_tiers tiers on tiers.id_tiers=doc.id_tiers ";
				
				
				switch(typeRegroupement)
				{
				case Const.PAR_ARTICLE :
					requete+=  " join ta_Article art on art.id_article=ldoc.id_article "; 
					condition= " and art.code_Article like :valeur";
					break;
				case Const.PAR_TIERS :
  
					condition= " and tiers.code_Tiers like :valeur";
					break;
				case Const.PAR_FAMILLE_ARTICLE :
					requete+= " join ta_Article art on art.id_article=ldoc.id_article "
							+ " join ta_Famille fam on fam.id_famille=art.id_famille ";
  
					condition= " and fam.code_Famille like :valeur";
					break;			
					
				case Const.PAR_FAMILLE_TIERS :
					requete+=" join ta_Famille famTiers on famTiers.id_famille=tiers.id_famille_tiers"; 
					condition= " and famTiers.code_Famille like :valeur";
					break;
				case Const.PAR_TAUX_TVA:
	            	if(valeurRegroupement.equals("%")) {
	            		valeurRegroupement=listeTauxTvaExistant();
	            	}
					if(valeurRegroupement instanceof String) {
						valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
					}
					condition= " and ldoc.taux_Tva_L_Document in :valeur";		            	
					break;		            
				case Const.PAR_TYPE_PAIEMENT :
					condition= " and exists("+getRequeteTypePaiementSQL("doc")+") ";
					break;
				case Const.PAR_VENDEUR :
	            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
					requete+=" join ta_utilisateur ut on ut.id=doc.id_vendeur"; 
					condition= " and ut.username like :valeur";
	            	}
					break;					
				default:execute=false;break;
				}

				requete+=" " 
				+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
				+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
				+" where doc.date_Document between :dateDebut and :dateFin "  
				+" and tiers.code_Tiers like :codeTiers ";
				requete+=condition;
				requete+=" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
				+" order by doc.code_Document  ";
				
				if(execute) {
					query = entityManager.createNativeQuery(requete);
					query.setParameter("dateDebut", debut, TemporalType.DATE);
					query.setParameter("dateFin", fin, TemporalType.DATE);
					query.setParameter("codeTiers", codeTiers);
					query.setParameter("valeur", valeurRegroupement);
					
					addScalarDocumentDTO(query);
					List<DocumentDTO> l = query.getResultList();
					
//					List<DocumentDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
					//			List<TaArticlesParTiersDTO> l = query.getResultList();
					logger.debug("get successful");
					return l;
				}
				return null;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
			Query query = null;
			String requete="";
			String condition="";
			boolean execute = true;
			if(codeTiers==null)codeTiers="%";
			String codeEtatTmp="%";
			//!!!! remplissage de la liste des codeEtat en mode SQL car requete en mode SQL
			if(codeEtat==null ||codeEtat.equals("%")) {
				List<TaEtat> listEtat =serviceEtat.selectAll();
				codeEtat="(";
				String virgule="";
				for (TaEtat taEtat : listEtat) {
					codeEtat+=virgule+"'"+taEtat.getCodeEtat()+"'";
					virgule=",";
				}
				codeEtat+=")";
			}
			try {
				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
				requete =
						"select "+documentDTO.retournChampAndAlias("doc", "id_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "code_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "libelle_Document")+" , "
								+documentDTO.retournChampAndAlias("tiers", "code_Tiers")+" , "
								+documentDTO.retournChampAndAlias("infos", "nom_Tiers")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Ech_Document")+" , "
								+documentDTO.retournChampAndAlias("doc", "date_Export")+" , "
								+documentDTO.construitSumCoallesceAsAlias("doc", DocumentDTO.f_net_Ht_Calc)+" , "
								+documentDTO.construitSumCoallesceAsAliasMtTvaSQL("doc", DocumentDTO.f_net_Tva_Calc)+" , "
								+documentDTO.construitSumCoallesceAsAlias("doc", DocumentDTO.f_net_Ttc_Calc)+" , "
								
//								+documentDTO.construitSumCoallesceAsAlias("ldoc", "mt_Ht_L_Document")+" , "
//								+documentDTO.construitSumCoallesceAsAliasMtTvaSQL("ldoc", DocumentDTO.f_mt_Tva_Calc)+" , "
//								+documentDTO.construitSumCoallesceAsAlias("ldoc", "mt_Ttc_L_Document")+" , "
								+" coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as reglementComplet,"
								+ " (coalesce(doc.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteAReglerComplet "  
								+" from "+nameEntitySQL+" doc "
										+ " join "+nameInfosEntitySQL+" infos on infos.id_document=doc.id_document "
										+" join "+nameLignesEntitySQL+" ldoc on ldoc.id_document=doc.id_document" 
										+ " join ta_tiers tiers on tiers.id_tiers=doc.id_tiers ";
				
				
				switch(typeRegroupement)
				{
				case Const.PAR_ARTICLE :
					requete+=  " join ta_Article art on art.id_article=ldoc.id_article "; 
					condition= " and art.code_Article like :valeur";
					break;
				case Const.PAR_TIERS :
  
					condition= " and tiers.code_Tiers like :valeur";
					break;
				case Const.PAR_FAMILLE_ARTICLE :
					requete+= " join ta_Article art on art.id_article=ldoc.id_article "
							+ " join ta_Famille fam on fam.id_famille=art.id_famille ";
  
					condition= " and fam.code_Famille like :valeur";
					break;			
					
				case Const.PAR_FAMILLE_TIERS :
					requete+=" join ta_Famille famTiers on famTiers.id_famille=tiers.id_famille_tiers"; 
					condition= " and famTiers.code_Famille like :valeur";
					break;
				case Const.PAR_TAUX_TVA:
	            	if(valeurRegroupement.equals("%")) {
	            		valeurRegroupement=listeTauxTvaExistant();
	            	}
					if(valeurRegroupement instanceof String) {
						valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
					}
					condition= " and ldoc.taux_Tva_L_Document in :valeur";		            	
					break;		            
				case Const.PAR_TYPE_PAIEMENT :
					condition= " and exists("+getRequeteTypePaiementSQL("doc")+") ";
					break;
				case Const.PAR_VENDEUR :
	            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
					requete+=" join ta_utilisateur ut on ut.id=doc.id_vendeur"; 
					condition= " and ut.username like :valeur";
	            	}
					break;					
				default:execute=false;break;
				}

				requete+=" " 
				+ " join ta_etat etat on etat.id_etat=doc.id_etat " 
				+" join (" +getRequeteAffectationReglement()+")as s1 on s1.id_document=doc.id_document "  
				+" join (" +getRequeteAffectationAvoir()+")as s2  on s2.id_document=doc.id_document"  	
				+" where doc.date_Document between :dateDebut and :dateFin "
				+ " and etat.code_Etat like :codeEtat " 
//				+ " and etat.code_Etat in :codeEtat "  
				+" and tiers.code_Tiers like :codeTiers";
				requete+=condition;
				requete+=" group by doc.id_Document, doc.code_Document, doc.date_Document, doc.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,doc.date_Ech_Document,doc.date_Export,coalesce(doc.net_Ht_Calc,0),coalesce(doc.net_Tva_Calc,0),coalesce(doc.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir"  
				+" order by doc.code_Document  ";
				
				if(execute) {
					query = entityManager.createNativeQuery(requete);
					query.setParameter("dateDebut", debut, TemporalType.DATE);
					query.setParameter("dateFin", fin, TemporalType.DATE);
					query.setParameter("codeTiers", codeTiers);
					query.setParameter("codeEtat", codeEtatTmp);
					query.setParameter("valeur", valeurRegroupement);

					addScalarDocumentDTO(query);
					List<DocumentDTO> l = query.getResultList();
//					List<DocumentDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
					//			List<TaArticlesParTiersDTO> l = query.getResultList();
					logger.debug("get successful");
					return l;
				}
				return null;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}

		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin, 
				int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
			
			Query query = null;
			List<DocumentChiffreAffaireDTO> l = null;
			if(codeTiers==null)codeTiers="%";
			String orderBy="";
			String groupBy="";
			String requete=	"select ";
			boolean execute=true;
			try {
				l=super.listeChiffreAffaireTotalJmaDTOParRegroupement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
				if(l!=null)return l;
				
				switch (precision) {
				case 0:
					requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
					groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument") ;
					orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument");
					break;

				case 1:
					requete+=documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+"," ;
					requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
					groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument") ;
					orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument");

					break;
				case 2:
					requete+=documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+"," ;
					requete+=documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+"," ;
					requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
					groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractDay("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument") ;
					orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractDay("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument");

					break;

				default:execute=false;break;
				}
				
			switch(typeRegroupement)
			{
			case Const.PAR_TYPE_PAIEMENT :
				requete+= listeChiffreAffaireTotalJmaDTOParTypePaiement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			case Const.PAR_VENDEUR :
            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
				requete+= listeChiffreAffaireTotalJmaDTOParVendeur(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
            	}
			default:break;
			}
			
			requete+= documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc");
			
			if(execute) {
				requete+=groupBy+orderBy;
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("valeur",valeurRegroupement);
				l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				logger.debug("get successful");
			}
			return l;
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}

		
		public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
			Query query = null;
			String requete="";
			boolean execute = false;
			if(codeEtat==null ||codeEtat.equals("%")) {
				List<TaEtat> listEtat =serviceEtat.selectAll();
				codeEtat=new LinkedList<String>();
				for (TaEtat taEtat : listEtat) {
					((List<String>)codeEtat).add(taEtat.getCodeEtat());
				}
			}
			try {
				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
				requete =
				"select "
						+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
						taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
						taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","+
						taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "+
						" from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent ";
				
				switch(typeRegroupement)
				{
				case Const.PAR_TYPE_PAIEMENT:
					if(entity.getClass().isInstance(TaTicketDeCaisse.class)
							||entity.getClass().isInstance(TaFacture.class)) {
					requete+=" join ldoc.taArticle art join doc.taRReglements rr join rr.taReglement reg  join reg.taTPaiment tp "
	    					+" join doc.taEtat etat "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
		   					+" and etat.codeEtat in :codeEtat "
							+ " and tp.codeTPaiement like :valeur"
							+ " group by tp.codeTPaiement , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
							+ " order by tp.codeTPaiement,doc.codeDocument ";
					execute=true;
					}
					break;
				case Const.PAR_VENDEUR:
					if(entity.getClass().isInstance(TaTicketDeCaisse.class)) {
						requete+=" join ldoc.taArticle art "
		    					+" join doc.taEtat etat "
								+ " join doc.taUtilisateurVendeur ut "			            			
								+ " where doc.dateDocument between :dateDebut and :dateFin "
			   					+" and etat.codeEtat in :codeEtat "
								+ " and tp.codeTPaiement like :valeur"
								+ " group by ut.username , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
								+ " order by ut.username,doc.codeDocument ";	
						execute=true;
					}
					break;		            

				default:execute=false;break;
				}

				if(execute) {
					query = entityManager.createQuery(requete);
					query.setParameter("dateDebut", debut, TemporalType.DATE);
					query.setParameter("dateFin", fin, TemporalType.DATE);
					query.setParameter("codeEtat",codeEtat);
					query.setParameter("valeur",valeurRegroupement);

					List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
					//			List<TaArticlesParTiersDTO> l = query.getResultList();
					logger.debug("get successful");
					return l;
				}
				return null;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}


		/**
		 * Classe permettant d'obtenir le ca généré par les tickets de caisse sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param typeRegroupement map a une ligne représentant en cle le type de regroupement en valeur : la valeur du regroupement (ex parFamilleArticle , 'toto')
		 * @param regrouper sert à savoir si on doit détailler ou synthétiser les résultats
		 * @return La requête renvoyée renvoi le CA des tickets de caisse sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement,boolean regrouper) {
			try {
				Query query = null;
				String requete="";
				String groupBy="";
				String orderBy="";
				boolean execute = true;
				List<DocumentChiffreAffaireDTO> l ;

				if(codeTiers==null)codeTiers="%";
				if(codeEtat==null ||codeEtat.equals("%")) {
					List<TaEtat> listEtat =serviceEtat.selectAll();
					codeEtat=new LinkedList<String>();
					for (TaEtat taEtat : listEtat) {
						((List<String>)codeEtat).add(taEtat.getCodeEtat());
					}
				}			
				if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
				if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
				
				requete="select  "
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+",";
						
				switch(typeRegroupement)
				{
				case Const.PAR_TIERS :
					requete=documentChiffreAffaireDTO.retournChampAndAlias("tiers", "codeTiers")
							+" from "+nameEntity+" doc"  
	    					+" join doc.taEtat etat "
							+" join doc.lignes ldoc "  
							+" join doc.taTiers tiers "  
							+" where doc.dateDocument between :dateDebut and :dateFin "
	    					+" and etat.codeEtat in :codeEtat "
							+ " and doc.taTiers.codeTiers like :codeTiers ";
					if(regrouper)requete+=" group by etat.codeEtat,tiers.codeTiers";				
					break;	
				case Const.PAR_ARTICLE :
					requete=documentChiffreAffaireDTO.retournChampAndAlias("art", "codeArticle")
							+ " from "+nameEntity+" doc  "
	    					+" join doc.taEtat etat "
							+ " join doc.lignes ldoc join ldoc.taArticle art  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
	    					+" and etat.codeEtat in :codeEtat "
							+ " and doc.taTiers.codeTiers like :codeTiers  ";
					if(regrouper)requete+=" group by etat.codeEtat,art.codeArticle";
					break;
				case Const.PAR_FAMILLE_TIERS :
					requete=" famTiers.codeFamille as "+documentChiffreAffaireDTO.f_codeFamilleTiers
							+" from "+nameEntity+" doc"  
	    					+" join doc.taEtat etat "
							+" join doc.lignes ldoc "  
							+" join doc.taTiers tiers "  
							+" join tiers.taFamilleTiers famTiers "  
							+" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
	    					+" and etat.codeEtat in :codeEtat "
							+" and (famTiers.codeFamille is null or famTiers.codeFamille like :valeur )"  
							+" group by famTiers.codeFamille"  ;
					if(regrouper)requete+=" group by etat.codeEtat,famTiers.codeFamille";				
					break;	
				case Const.PAR_FAMILLE_ARTICLE :
					requete=documentChiffreAffaireDTO.retournChampAndAlias("fam", "codeFamille")
							+ " from "+nameEntity+" doc "
	    					+" join doc.taEtat etat "
							+ " join doc.lignes ldoc join ldoc.taArticle art join art.taFamille fam "
							+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers  "
	    					+" and etat.codeEtat in :codeEtat "
							+ " and fam.codeFamille like :valeur ";
					if(regrouper)requete+=" group by etat.codeEtat,fam.codeFamille";
					break;
				case Const.PAR_TAUX_TVA:
	            	if(valeurRegroupement.equals("%")) {
	            		valeurRegroupement=listeTauxTvaExistant();
	            	}
					if(valeurRegroupement instanceof String) {
						valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
					}
					requete=documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")
							+ " from "+nameEntity+" doc "
	    					+" join doc.taEtat etat "
							+ " join doc.lignes ldoc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers  "
	    					+" and etat.codeEtat in :codeEtat "
							+ " and ldoc.tauxTvaLDocument in :valeur ";
					if(regrouper)requete+=" group by etat.codeEtat,ldoc.tauxTvaLDocument";			            	
					break;
				
	            case Const.PAR_TYPE_PAIEMENT:
					requete=documentChiffreAffaireDTO.retournChampAndAlias("tp", "codeTPaiement")+""
							+" from "+nameEntity+" doc"+             	
	    					" join doc.taEtat etat "+
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
	    					" and etat.codeEtat in :codeEtat "+
	    					" and exists("+getRequeteTypePaiement("doc")+") " ;
					if(regrouper)groupBy=" group by etat.codeEtat,tp.codeTPaiement";
					orderBy=" order by tp.codeTPaiement";
	            break;
	            case Const.PAR_VENDEUR:
	            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
						requete=documentChiffreAffaireDTO.retournChampAndAlias("ut", "username")+""
							+" from "+nameEntity+" doc"+ 		            		
	    					" join doc.taEtat etat "+
	    					" join doc.taUtilisateurVendeur ut "+
	    					" join doc.lignes ldoc " + 		            			
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
	    					" and etat.codeEtat in :codeEtat "+
	    					" and ut.username like :valeur " ;
						if(regrouper)groupBy=" group by etat.codeEtat,ut.username";
						orderBy=" order by ut.username";						
	            	}
	            break;
	            default:execute=false;break;
				}

				if(execute) {
					query = entityManager.createQuery(requete);
					query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
					query.setParameter("dateFin", dateFin, TemporalType.DATE);
					query.setParameter("codeTiers",codeTiers);
					query.setParameter("codeEtat",codeEtat);
					query.setParameter("valeur",valeurRegroupement);
					l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
					//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
					if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
						l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
					}
					logger.debug("get successful");
					return l;
				}
				return null;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}

		}

		
		
		
		/**
		 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @return La requête renvoyée renvoi le CA des devis sur la période 
		 */
		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers) {
			try {
				Query query = null;
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_AVEC_ETAT);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				query.setParameter("codeTiers","%");
				query.setParameter("codeTiers",codeTiers);
				
				List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
				
		}
		

}

