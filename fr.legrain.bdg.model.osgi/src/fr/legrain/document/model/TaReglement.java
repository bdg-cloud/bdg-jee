package fr.legrain.document.model;

// Generated Apr 7, 2009 3:27:23 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersComplet;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.lib.data.ChangeModeEvent;
import fr.legrain.lib.data.ChangeModeListener;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;


@SqlResultSetMapping(name="TaReglementDTO",
classes = {
    @ConstructorResult(
            targetClass = TaReglementDTO.class,
            columns = {

                @ColumnResult(name = "id_Document",type = int.class),
                @ColumnResult(name = "code_Document",type = String.class),
                @ColumnResult(name = "date_Document",type = Date.class),
                @ColumnResult(name = "libelle_Document",type = String.class),
                @ColumnResult(name = "code_Tiers",type = String.class),
                @ColumnResult(name = "nom_Tiers",type = String.class),
                @ColumnResult(name = "date_Liv_Document",type = Date.class),
                @ColumnResult(name = "date_Export",type = Date.class),
                @ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
                @ColumnResult(name = "affectationTotale",type = BigDecimal.class),
                @ColumnResult(name = "resteARegler",type = BigDecimal.class)
            })    
})

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_reglement")
//@SequenceGenerator(name = "gen_ta_reglement", sequenceName = "num_id_reglement", allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATEEXPORT_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f  join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and  f.dateExport =:date order by f.dateDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_TIERS_AND_DATE_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f  join f.taTiers tiers  where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin order by f.codeDocument"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_EXPORT_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f  join f.taTiers tiers  where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin  and f.dateExport is not null order by f.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.dateDocument, a.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin order by f.dateDocument, f.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_TIERS_AND_CODE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_DTO, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f  join f.taTiers tiers  where f.taTiers.codeTiers like :codeTiers and f.codeDocument between :codeDeb and :codeFin  order by f.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_PARDATE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is null order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_NON_EXPORT, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is null order by a.dateDocument, a.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_NON_EXPORT, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateExport is null order by a.dateDocument, a.codeDocument"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_NON_EXPORT_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin  and f.dateExport is null order by  f.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_NON_EXPORT_LIGHT, query="select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument,f.dateLivDocument, f.libelleDocument, tiers.codeTiers, tiers.nomTiers,f.dateExport,f.netTtcCalc,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) from TaReglement f join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.codeDocument between :codeDeb and :codeFin and f.dateExport is null order by f.dateDocument, f.codeDocument"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_TIERS_AND_DATE_DTO, query=" select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument, f.dateLivDocument, f.libelleDocument, t.codeTiers, "
				+ "t.nomTiers,f.dateExport,f.netTtcCalc,cast(0.0 as big_decimal),cast(null as date),(select coalesce(sum(r.affectation),0)from TaRReglement r where r.taReglement=f )"
				+ " ,coalesce(tp.idTPaiement,0),tp.codeTPaiement,tp.libTPaiement,cb.idCompteBanque,cb.codeBIC,cb.iban,"
				+ "(select coalesce(count(r),0)from TaRReglement r where r.taReglement=f),ac.codeDocument,p.codeDocument,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) "
				+ " from TaReglement f  join f.taTiers t left join f.taTPaiement tp left join f.taCompteBanque cb left join f.taAcompte ac left join f.taPrelevement p where t.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin order by f.dateDocument, f.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_TIERS_DTO, query=" select new fr.legrain.document.dto.TaReglementDTO(f.idDocument, f.codeDocument, f.dateDocument, f.dateLivDocument, f.libelleDocument, t.codeTiers, "
				+ "t.nomTiers,f.netTtcCalc,cast(0.0 as big_decimal),(select coalesce(sum(r.affectation),0)from TaRReglement r where r.taReglement=f )"
				+ " ,coalesce(tp.idTPaiement,0),tp.codeTPaiement,tp.libTPaiement,cb.idCompteBanque,cb.codeBIC,cb.iban,"
				+ "(select coalesce(count(r),0)from TaRReglement r where r.taReglement=f),ac.codeDocument,p.codeDocument,f.dateExport,f.dateVerrouillage, mad.accessibleSurCompteClient, mad.envoyeParEmail, mad.imprimePourClient,(select a.taDocument.codeDocument from TaLRemise a join a.taReglement r where r=f)as codeRemise) "
				+ " from TaReglement f  join f.taTiers t  left join f.taMiseADisposition mad  join f.taTPaiement tp left join f.taCompteBanque cb left join f.taAcompte ac left join f.taPrelevement p where t.codeTiers like :codeTiers order by f.dateDocument desc, f.codeDocument desc"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_EXPORT, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is not null order by a.dateDocument, a.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_EXPORT, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateExport is not null order by a.dateDocument, a.codeDocument"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_NON_VERROUILLE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateVerrouillage is null order by a.dateDocument, a.codeDocument"),		
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_NON_VERROUILLE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateExport is null order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATE_VERROUILLE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateVerrouillage is not null order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_DATEEXPORT, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateExport =:date order by a.dateDocument, a.codeDocument"), 
		@NamedQuery(name=TaReglement.QN.FIND_BY_CODE_VERROUILLE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateVerrouillage is not null order by a.dateDocument, a.codeDocument"),
		@NamedQuery(name=TaReglement.QN.FIND_BY_TIERS_AND_DATE, query="select a from TaReglement a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.dateDocument, a.codeDocument"),
		
//		// Renvoi les totaux de l'ensemble des devis groupé par jour sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_JOUR_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"
					+ " order by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des devis groupé par mois sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_MOIS_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(month from f.dateDocument),extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(month from f.dateDocument),extract(year from f.dateDocument)"
					+ " order by extract(month from f.dateDocument),extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des devis groupé par année sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_ANNEE_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(year from f.dateDocument),coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(year from f.dateDocument)"
					+ " order by extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des factures sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_TOTAL_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"),
		@NamedQuery(name=TaReglement.QN.SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " cast(0.0 as big_decimal) ,cast(0.0 as big_decimal),"
				+ " coalesce(sum(f.netTtcCalc),0)-coalesce(sum(a.netTtcCalc),0)) "
				+ " from TaReglement f ,TaAvoir a "
				+ " where a.taTiers=f.taTiers and f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"),
		
		// Renvoi les totaux de l'ensemble des factures payées sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_TOTAL_LIGHT_PERIODE_PAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " cast(0.0 as big_decimal),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  left join f.taRReglements rr    "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"
					+" group by true"
					+"  having coalesce(sum(rr.affectation),0)>= coalesce(sum(f.netTtcCalc),0) "),
//		// Renvoi les totaux de l'ensemble des factures pour un tiers sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " cast(0.0 as big_decimal),cast(0.0 as big_decimal),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers "),
		
		//
		@NamedQuery(name=TaReglement.QN.SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument), "
				+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),cast(0.0 as big_decimal)) "
				+ " from TaReglement f  "
				+ " where f.dateDocument between :dateDebut and :dateFin  "
				+ " and f.taTiers.codeTiers like :codeTiers"
				+ " and f.taEtat is null "
				+ " and coalesce((f.netTtcCalc),0)<=(" 
				+"	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr    "  
				+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
				+ " group by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"
				+ " order by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"),		
		@NamedQuery(name=TaReglement.QN.SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " extract(month from f.dateDocument),extract(year from f.dateDocument), "
				+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),cast(0.0 as big_decimal)) "
				+ " from TaReglement f  "
				+ " where f.dateDocument between :dateDebut and :dateFin  "
				+ " and f.taTiers.codeTiers like :codeTiers"
				+ " and f.taEtat is null "
				+ " and coalesce((f.netTtcCalc),0)<=(" 
				+"	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr    "  
				+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
				+ " group by extract(month from f.dateDocument),extract(year from f.dateDocument)"
				+ " order by extract(month from f.dateDocument),extract(year from f.dateDocument)"),
		@NamedQuery(name=TaReglement.QN.SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netTtcCalc),0),cast(0.0 as big_decimal),cast(0.0 as big_decimal)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and coalesce((f.netTtcCalc),0)<=(" 
					+"	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr    "  
					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
					+ " group by extract(year from f.dateDocument)"
					+ " order by extract(year from f.dateDocument)"),
		// Renvoi le total à relancer car date échéance dépassée du ca ht, des affectations des règlements+des affectations des avoirs, et du reste à règler des factures sur la période dateDebut à dateFin
		@NamedQuery(name=TaReglement.QN.SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " cast(0.0 as big_decimal),cast(0.0 as big_decimal), coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaReglement f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and coalesce((f.netTtcCalc),0)<=(" 
					+"	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr     "  
					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
					+ " group by true")	,	
		// Renvoi les articles de l'ensemble des Proforma sur la période datedebut à datefin
		@NamedQuery(name=TaReglement.QN.FIND_ARTICLES_PAR_TIERS_PAR_MOIS, 
			query="select new fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO("
					+ " c.codeTiers, c.nomTiers,d.nomEntreprise,a.codeDocument,a.dateDocument,e.codeArticle,e.libellecArticle,b.mtHtLDocument,b.mtTtcLDocument) "
					+ " from TaReglement a join a.taRReglements rr join rr.taFacture fs join fs.lignes b join fs.taTiers c  left join c.taEntreprise d join b.taArticle e  "
					+ " where a.dateDocument between :dateDebut and :dateFin"),
		
		// Renvoi les articles de l'ensemble des Proforma transformé sur la période datedebut à datefin
		@NamedQuery(name=TaReglement.QN.FIND_ARTICLES_PAR_TIERS_TRANSFORME, 
			query="select new fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO("
					+ " c.codeTiers, c.nomTiers,d.nomEntreprise,a.codeDocument,a.dateDocument,e.codeArticle,e.libellecArticle,b.mtHtLDocument,b.mtTtcLDocument) "
					+ " from TaReglement a join a.taRReglements rr join rr.taFacture fs join fs.lignes b join fs.taTiers c  left join c.taEntreprise d join b.taArticle e  "
					+ " where a.dateDocument between :dateDebut and :dateFin "
					+ " and a.taEtat is null "  
					+ " and coalesce((a.netTtcCalc),0)<=("   
					+ "	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr     "  
					+ "	where f3.idDocument=a.idDocument group by f3.idDocument)"),
		
		// Renvoi les articles de l'ensemble des Proforma non transformé sur la période datedebut à datefin
		@NamedQuery(name=TaReglement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME, 
			query="select new fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO("
					+ "c.codeTiers, c.nomTiers,d.nomEntreprise,a.codeDocument,a.dateDocument,e.codeArticle,e.libellecArticle,b.mtHtLDocument,b.mtTtcLDocument) "
					+ "from TaReglement a join a.taRReglements rr join rr.taFacture fs join fs.lignes b join fs.taTiers c  left join c.taEntreprise d join b.taArticle e  "
					+ "where a.dateDocument between :dateDebut and :dateFin "
					+ " and a.taEtat is null "  
					+ " and not exists(select rr from TaRReglement rr "  
					+" join rr.taReglement f2  where f2.idDocument=a.idDocument)"),
		
		// Renvoi les articles de l'ensemble des Proforma non transformé sur la période datedebut à datefin
		@NamedQuery(name=TaReglement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER, 
			query="select new fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO("
					+ "c.codeTiers, c.nomTiers,d.nomEntreprise,a.codeDocument,a.dateDocument,e.codeArticle,e.libellecArticle,b.mtHtLDocument,b.mtTtcLDocument) "
					+ "from TaReglement a join a.taRReglements rr join rr.taFacture fs join fs.lignes b join fs.taTiers c  left join c.taEntreprise d join b.taArticle e  "
					+ "where a.dateDocument between :dateDebut and :dateFin   "
					+ " and a.taEtat is null "  
					+" and exists(select rr from TaRReglement rr " + 
					" join rr.taReglement f2 where f2.idDocument=a.idDocument)" 
					+ " and coalesce((a.netTtcCalc),0)>("   
					+ "	select coalesce(sum(rr.affectation),0) from TaReglement f3  left join f3.taRReglements rr     "  
					+ "	where f3.idDocument=a.idDocument group by f3.idDocument)"),
})
@NamedNativeQueries({
	@NamedNativeQuery(name=TaReglement.QN.FIND_ALL_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Liv_Document,res.date_export,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler "
					+ "from(  select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" (s1.affectation)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation))as resteARegler " + 
					" from ta_reglement f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,cast(0.0 as numeric),cast(0.0 as numeric),coalesce(f.net_Ttc_Calc,0),s1.affectation" + 
					" order by f.date_Document DESC, f.code_Document DESC)as res ", resultSetMapping = "TaReglementDTO"),
	@NamedNativeQuery(name=TaReglement.QN.FIND_PAYE_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Liv_Document,res.date_export,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,cast(0.0 as numeric),cast(0.0 as numeric),coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" (s1.affectation)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation))as resteARegler " + 
					" from ta_reglement f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)<=(" + 
					" select coalesce(sum(rr.affectation),0) from Ta_reglement f3  left join ta_R_Reglement rr on rr.id_reglement=f.id_document   " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,cast(0.0 as numeric),cast(0.0 as numeric),coalesce(f.net_Ttc_Calc,0),s1.affectation" + 
					" order by f.code_Document)as res ", resultSetMapping = "TaReglementDTO"),

	@NamedNativeQuery(name=TaReglement.QN.FIND_NON_PAYE_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Liv_Document,res.date_export,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" (s1.affectation)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation))as resteARegler " + 
					" from ta_reglement f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document  " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and  not exists(select rr from Ta_R_Reglement rr " + 
					" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,cast(0.0 as numeric),cast(0.0 as numeric),coalesce(f.net_Ttc_Calc,0),s1.affectation" + 
					" order by f.code_Document)as res ", resultSetMapping = "TaReglementDTO"),
	@NamedNativeQuery(name=TaReglement.QN.FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Liv_Document,res.date_export,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" (s1.affectation)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation))as resteARegler " + 
					" from ta_reglement f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
				" where f.date_Document between :dateDebut and :dateFin  " + 
				" and tiers.code_Tiers like :codeTiers" + 
				" and f.id_Etat is null " + 
				" and exists(select rr from Ta_R_Reglement rr " + 
				" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
				" and coalesce((f.net_Ttc_Calc),0)>(" + 
				" select coalesce(sum(rr.affectation),0) from Ta_Reglement f3  left join ta_R_Reglement rr on rr.id_Reglement=f.id_document   " + 
				" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
				" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, tiers.nom_Tiers,f.date_Liv_Document,f.date_Export,cast(0.0 as numeric),cast(0.0 as numeric),coalesce(f.net_Ttc_Calc,0),s1.affectation" + 
				" order by f.code_Document)as res ", resultSetMapping = "TaReglementDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select (0)as jour,(0)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document   " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " +  
					" where f.date_Document between :dateDebut and :dateFin  " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)>(" + 
					" select coalesce(sum(rr.affectation),0) from ta_reglement f3  left join ta_R_Reglement rr on rr.id_reglement=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by extract(year from f.date_Document),s1.affectation" + 
					" order by extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name = TaReglement.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document  " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" where f.date_Document between :dateDebut and :dateFin  " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and exists(select rr from Ta_R_Reglement rr " + 
			" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(rr.affectation),0) from Ta_reglement f3  left join ta_R_Reglement rr on rr.id_reglement=f.id_document    " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation" + 
			" order by extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER, 
	query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select extract(day from f.date_Document)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document  " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " +  
			" where f.date_Document between :dateDebut and :dateFin  " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and exists(select rr from Ta_R_Reglement rr " + 
			" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(rr.affectation),0) from Ta_reglement f3  left join ta_R_Reglement rr on rr.id_reglement=f.id_document   " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation" + 
			" order by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,(0)as mois,(0)as annee,true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document  " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " +  
			" where f.date_Document between :dateDebut and :dateFin " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and exists(select rr from Ta_R_Reglement rr " + 
			" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(rr.affectation),0) from Ta_reglement f3  left join ta_R_Reglement rr on rr.id_reglement=f.id_document    " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by s1.affectation)as res group by res.jour,res.mois,res.annee" ,resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select extract(day from f.date_Document)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document  " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and  not exists(select rr from Ta_R_Reglement rr " + 
					" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
					" group by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation" + 
					" order by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" where f.date_Document between :dateDebut and :dateFin  " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and  not exists(select rr from Ta_R_Reglement rr " + 
			" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
			" group by extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation" + 
			" order by extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select (0)as jour,(0)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document   " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and  not exists(select rr from Ta_R_Reglement rr " + 
					" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
					" group by extract(year from f.date_Document),s1.affectation" + 
					" order by extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaReglement.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE, 
	query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
	" select (0)as jour,(0)as mois,(0)as annee,true as typeReglement,coalesce(sum(f.net_Ttc_Calc),0)as mtHtCalc ,cast(0.0 as numeric)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
	" (s1.affectation)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation))as resteARegler from ta_reglement f " + 
	" join ta_tiers t on t.id_tiers=f.id_tiers " + 
	" join (" + 
	" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_reglement f left join ta_r_reglement rr on rr.id_reglement=f.id_document   " + 
	" group by f.id_document )as s1 on s1.id_document=f.id_document " +  
	" where f.date_Document between :dateDebut and :dateFin " + 
	" and t.code_Tiers like :codeTiers" + 
	" and f.id_Etat is null " + 
	" and not exists(select rr from Ta_R_Reglement rr \n" + 
	" join ta_Reglement f2 on f2.id_document=rr.id_reglement where f2.id_document=f.id_document)" + 
	" group by s1.affectation)as res group by res.jour,res.mois,res.annee" ,resultSetMapping = "DocumentChiffreAffaireDTO")
	
})

public class TaReglement extends SWTDocument implements ChangeModeListener,IDocumentTiers,IDocumentTiersComplet, java.io.Serializable,
Cloneable{

	private static final long serialVersionUID = -2211768655801607977L;
	
	public static final String TYPE_DOC = "Reglement";
	public static final String PATH_ICONE_COULEUR = "btn/roue-dentee.svg";
	public static final String PATH_ICONE_BLANC = "";
	public static final String PATH_ICONE_GRIS = "";
	
	public static class QN {
		public static final String FIND_BY_DATEEXPORT_LIGHT = "TaReglement.findDateExporteLight";     
		public static final String FIND_BY_TIERS_AND_DATE_LIGHT = "TaReglement.findTiersEntre2DateLight";
		public static final String FIND_BY_DATE_EXPORT_LIGHT = "TaReglement.findEntre2DateExporteLight";
		public static final String FIND_BY_DATE = "TaReglement.findEntre2Date";
		public static final String FIND_BY_DATE_LIGHT = "TaReglement.findEntre2DateLight";
		public static final String FIND_BY_TIERS_AND_CODE = "TaReglement.findEntre2CodeParCode";
		public static final String FIND_BY_DATE_PARDATE = "TaReglement.findEntre2DateParDate";
		public static final String FIND_BY_CODE = "TaReglement.findEntre2Code";
		public static final String FIND_BY_CODE_DTO= "TaReglement.findEntre2CodeDTO";
		public static final String FIND_BY_DATE_NON_EXPORT = "TaReglement.findEntre2DateNonExporte";
		public static final String FIND_BY_CODE_NON_EXPORT = "TaReglement.findEntre2CodeNonExporte";
		public static final String FIND_BY_DATE_NON_EXPORT_LIGHT = "TaReglement.findEntre2DateNonExporteLight";
		public static final String FIND_BY_CODE_NON_EXPORT_LIGHT = "TaReglement.findEntre2CodeNonExporteLight";
		public static final String FIND_BY_DATE_EXPORT = "TaReglement.findEntre2DateExporte";
		public static final String FIND_BY_CODE_EXPORT = "TaReglement.findEntre2CodeExporte";
		public static final String FIND_BY_TIERS_AND_DATE = "TaReglement.findTiersEntre2Date";
		public static final String FIND_BY_TIERS_AND_DATE_DTO = "TaReglement.findTiersEntre2DateDTO";
		public static final String FIND_BY_TIERS_DTO = "TaReglement.findTiersDTO";
		public static final String FIND_BY_DATE_NON_VERROUILLE = "TaReglement.findEntre2DateNonVerrouille";
		public static final String FIND_BY_CODE_NON_VERROUILLE = "TaReglement.findEntre2CodeNonVerrouille";
		public static final String FIND_BY_DATE_VERROUILLE = "TaReglement.findEntre2DateVerrouille";
		public static final String FIND_BY_CODE_VERROUILLE = "TaReglement.findEntre2CodeVerrouille";
		public static final String FIND_BY_DATEEXPORT = "TaReglement.findDateExporte";
		
		
		//rajout isa pour DashboardReglement
		public static final String FIND_ALL_LIGHT_PERIODE = "TaReglement.findAllLightPeriode";
		public static final String FIND_PAYE_LIGHT_PERIODE = "TaReglement.findFacturePayePeriodeDTO";
		public static final String FIND_NON_PAYE_LIGHT_PERIODE = "TaReglement.findFactureNonPayeDTO";		
		public static final String FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE = "TaReglement.findFactureNonPayeARelancerDTO";
		
		public static final String SUM_CA_JOUR_LIGTH_PERIODE = "TaReglement.caFactureJourPeriodeDTO";
		public static final String SUM_CA_MOIS_LIGTH_PERIODE = "TaReglement.caFactureMoisPeriodeDTO";
		public static final String SUM_CA_ANNEE_LIGTH_PERIODE = "TaReglement.caFactureAnneePeriodeDTO";
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE = "TaReglement.caFactureTotalPeriodeDTO";
		public static final String SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE = "TaReglement.caFactureTotalSansAvoirPeriodeDTO";

		public static final String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE = "TaReglement.resteAReglerFactureJourPeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE = "TaReglement.resteAReglerFactureMoisPeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE = "TaReglement.resteAReglerFactureAnneePeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE = "TaReglement.resteAReglerFactureTotalPeriodeDTO";
//
		public static final String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER = "TaReglement.resteAReglerFactureJourPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER = "TaReglement.resteAReglerFactureMoisPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER = "TaReglement.resteAReglerFactureAnneePeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER = "TaReglement.resteAReglerFactureTotalPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER2 = "1";
		
		public static final String SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE = "TaReglement.caFactureJourPeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE = "TaReglement.caFactureMoisPeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE = "TaReglement.caFactureAnneePeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE = "TaReglement.caFactureTotalPeriodeDTOTotalementPaye";
		
		
		public static final String SUM_CA_TOTAL_LIGHT_PERIODE_PAYE = "TaReglement.caFactureTotalPeriodeDTOPaye";
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE = "TaReglement.caTotalLightPeriodeParArticle";
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS = "TaReglement.caTotalLightPeriodeParTiers";
		
		
		public static final String FIND_ARTICLES_PAR_TIERS_PAR_MOIS = "TaReglement.articlesProformaMoisPeriode";
		public static final String FIND_ARTICLES_PAR_TIERS_TRANSFORME = "TaReglement.articlesProformaPeriodeDTOTransforme";
		public static final String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME = "TaReglement.articlesProformaPeriodeDTONonTransforme";
		public static final String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER = "TaReglement.articlesProformaPeriodeDTONonTransformeARelancer";
		
	}

//	private int id;
	private String version;
	private String codeDocument = "";
	private TaTiers taTiers;
	private Date dateDocument;
	private Date dateLivDocument; //dateEncaissement
	private Date dateExport;
	private TaTPaiement taTPaiement;
	private String libelleDocument;
	private TaCompteBanque taCompteBanque;
//	private Integer etat = 0;
//	private Integer export = 0;
	private BigDecimal netTtcCalc = new BigDecimal(0); //montantReglement
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	
	private Boolean  compteClient;
	private String refPaiement;
	private String service;
	private TaEtat taEtat;
	private TaAcompte taAcompte ;
	private TaPrelevement taPrelevement;
	private TaAvisEcheance taAvisEcheance;  
//	private TaEc id_echeancier ;
	private TaMiseADisposition taMiseADisposition;
	
	private Date dateVerrouillage;
	
	private Set<TaFacture> taFactures = new HashSet<TaFacture>(0);
	private Set<TaRReglement> taRReglements = new HashSet<TaRReglement>(0);
	private Set<TaRReglementLiaison> taRReglementLiaisons = new HashSet<TaRReglementLiaison>(0);
	@Transient	
	private BigDecimal resteAAffecter = new BigDecimal(0);
	
//	private Set<TaRDocument> taRDocuments = new HashSet<TaRDocument>(0);
    
//    @Transient
//    private List<TaReglement> taRReglements = new LinkedList<TaReglement>();
    
    @Transient
	private int etatDeSuppression = 0;
    
    @Transient
    protected Boolean utiliseUniteSaisie = true;
    
	public TaReglement() {
	}

	public TaReglement(String libelleDocument, BigDecimal netTtcCalc) {
	super();
	this.libelleDocument = libelleDocument;
	this.netTtcCalc = netTtcCalc;
}
	
//	public TaReglement(int idRAcompte) {
//		this.id = idRAcompte;
//	}
//
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_ta_reglement")
//	@Column(name = "id_reglement", unique = true, nullable = false)
//	@LgrHibernateValidated(champ = "id_reglement",table = "ta_reglement", champEntite="xxxxxx", clazz = TaReglement.class)
//	public int getId() {
//		return this.id;
//	}
//
//	public void setId(int idReglement) {
//		this.id = idReglement;
//	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}




	public void setVersion(String version) {
		this.version = version;
	}


	@Column(name = "code_document", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_document",table = "ta_reglement", champEntite="codeDocument", clazz = TaReglement.class)
	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeReglement) {
		this.codeDocument = codeReglement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tiers")
	@LgrHibernateValidated(champBd = "id_tiers",table = "ta_tiers", champEntite="taTiers.idTiers", clazz = TaTiers.class)
	public TaTiers getTaTiers() {
		return this.taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_document", length = 19)
	@LgrHibernateValidated(champBd = "date_document",table = "ta_reglement", champEntite="dateDocument", clazz = TaReglement.class)
	public Date getDateDocument() {
		return dateDocument;
	}

	public void setDateDocument(Date dateReglement) {
		this.dateDocument = dateReglement;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_liv_document", length = 19)
	@LgrHibernateValidated(champBd = "date_liv_document",table = "ta_reglement", champEntite="dateLivDocument", clazz = TaReglement.class)
	public Date getDateLivDocument() {
		return dateLivDocument;
	}

	public void setDateLivDocument(Date dateEncaissement) {
		this.dateLivDocument = dateEncaissement;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_t_paiement")
	@LgrHibernateValidated(champBd = "id_t_paiement",table = "ta_t_paiement", champEntite="taTPaiement.idTPaiement", clazz = TaTPaiement.class)
	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}

	@Column(name = "libelle_document", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "libelle_document",table = "ta_reglement", champEntite="libelleDocument", clazz = TaReglement.class)
	public String getLibelleDocument() {
		return libelleDocument;
	}

	public void setLibelleDocument(String libellePaiement) {
		this.libelleDocument = libellePaiement;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_compte_banque")
	@LgrHibernateValidated(champBd = "id_compte_banque",table = "ta_compte_banque", champEntite="taCompteBanque.idCompteBanque", clazz = TaCompteBanque.class)
	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}

	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
	}

//	@Column(name = "etat")
//	@LgrHibernateValidated(champBd = "etat",table = "ta_reglement", champEntite="etat", clazz = TaReglement.class)
//	public Integer getEtat() {
//		return etat;
//	}
//
//	public void setEtat(Integer etat) {
//		this.etat = etat;
//	}

//	@Column(name = "export")
//	@LgrHibernateValidated(champBd = "export",table = "ta_reglement", champEntite="export", clazz = TaReglement.class)
//	public Integer getExport() {
//		return export;
//	}
//
//	public void setExport(Integer export) {
//		this.export = export;
//	}
	


	@Column(name = "net_ttc_calc", precision = 15)
	@LgrHibernateValidated(champBd = "net_ttc_calc",table = "ta_reglement", champEntite="netTtcCalc", clazz = TaReglement.class)
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}

	public void setNetTtcCalc(BigDecimal montantReglement) {
		this.netTtcCalc = montantReglement;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taReglement", orphanRemoval=true)
	public Set<TaRReglement> getTaRReglements() {
		return taRReglements;
	}

	public void setTaRReglements(Set<TaRReglement> taRReglements) {
		this.taRReglements = taRReglements;
	}
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taReglement", orphanRemoval=true)
	public Set<TaRReglementLiaison> getTaRReglementLiaisons() {
		return taRReglementLiaisons;
	}

	public void setTaRReglementLiaisons(Set<TaRReglementLiaison> taRReglementLiaisons) {
		this.taRReglementLiaisons = taRReglementLiaisons;
	}
	

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeRDocument) {
		this.quiCree = quiCreeRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeRDocument) {
		this.quandCree = quandCreeRDocument;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifRDocument) {
		this.quiModif = quiModifRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifRDocument) {
		this.quandModif = quandModifRDocument;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	
	public void addRReglement(TaRReglement taReglement){
		if(!this.getTaRReglements().contains(taReglement)){
			this.getTaRReglements().add(taReglement);	
		}
	}
	public void removeReglement(TaRReglement taReglement){
		this.getTaRReglements().remove(taReglement);
	}
	
	public BigDecimal calculAffectationEnCoursReel(TaFacture taDocument){
		BigDecimal retour = BigDecimal.valueOf(0);

			//return getAffectation();
			Iterator<TaRReglement> ite = taRReglements.iterator();
			boolean trouve = false;
			TaRReglement taRReglement = null;
			while(ite.hasNext() && !trouve) {
				taRReglement = ite.next();
				if(taRReglement!=null && taRReglement.getTaFacture().getCodeDocument().equals(taDocument.getCodeDocument())) {
					retour = taRReglement.getAffectation();
					trouve = true;
				}
			}
		
		return retour;
	}
	
	public BigDecimal calculAffectationEnCours(TaFacture taDocument){
		BigDecimal retour = BigDecimal.valueOf(0);
		if(this.getIdDocument()==0) {
			//return getAffectation();
			Iterator<TaRReglement> ite = taRReglements.iterator();
			boolean trouve = false;
			TaRReglement taRReglement = null;
			while(ite.hasNext() && !trouve) {
				taRReglement = ite.next();
				if(taRReglement!=null && taRReglement.getTaFacture().getCodeDocument().equals(taDocument.getCodeDocument())) {
					retour = taRReglement.getAffectation();
					trouve = true;
				}
			}
		} else
			return BigDecimal.valueOf(0);
		return retour;
	}
	
	public BigDecimal calculAffectationEnCoursReel(TaTicketDeCaisse taDocument){
		BigDecimal retour = BigDecimal.valueOf(0);

			//return getAffectation();
			Iterator<TaRReglement> ite = taRReglements.iterator();
			boolean trouve = false;
			TaRReglement taRReglement = null;
			while(ite.hasNext() && !trouve) {
				taRReglement = ite.next();
				if(taRReglement!=null && taRReglement.getTaTicketDeCaisse().getCodeDocument().equals(taDocument.getCodeDocument())) {
					retour = taRReglement.getAffectation();
					trouve = true;
				}
			}
		
		return retour;
	}
	
	public BigDecimal calculAffectationEnCours(TaTicketDeCaisse taDocument){
		BigDecimal retour = BigDecimal.valueOf(0);
		if(this.getIdDocument()==0) {
			//return getAffectation();
			Iterator<TaRReglement> ite = taRReglements.iterator();
			boolean trouve = false;
			TaRReglement taRReglement = null;
			while(ite.hasNext() && !trouve) {
				taRReglement = ite.next();
				if(taRReglement!=null && taRReglement.getTaTicketDeCaisse().getCodeDocument().equals(taDocument.getCodeDocument())) {
					retour = taRReglement.getAffectation();
					trouve = true;
				}
			}
		} else
			return BigDecimal.valueOf(0);
		return retour;
	}
	
	
	public BigDecimal calculAffectationTotale(TaRReglement taRReglement){
		BigDecimal retour = BigDecimal.valueOf(0);
		for (TaRReglement taRReglementTmp : taRReglements) {
			if(taRReglement==null || taRReglement.getId()!=taRReglementTmp.getId())
				retour=retour.add(taRReglementTmp.getAffectation());
		}
		return retour;
	}
	public BigDecimal calculAffectationTotale(){
		BigDecimal retour = BigDecimal.valueOf(0);
		for (TaRReglement taRReglementTmp : taRReglements) {
			retour=retour.add(taRReglementTmp.getAffectation());
		}
		return retour;
	}

	@Transient
	public int getEtatDeSuppression() {
		return etatDeSuppression;
	}
	
	@Transient
	public void setEtatDeSuppression(int etatDeSuppression) {
		etatDeSuppression = etatDeSuppression;
	}
	
	@Transient
	public BigDecimal getResteAAffecter() {
		return getNetTtcCalc().subtract(calculAffectationTotale());
	}

	public void setResteAAffecter(BigDecimal resteAAffecter) {
		this.resteAAffecter = resteAAffecter;
	}

	@Override
	protected void afterAjoutLigne(SWTLigneDocument ligne) throws ExceptLgr {
		// TODO Auto-generated method stub
	}

	@Override
	protected void afterEnregistrerEntete() throws ExceptLgr {
		// TODO Auto-generated method stub
	}

	@Override
	protected void afterModifierEntete() throws ExceptLgr {
		// TODO Auto-generated method stub
	}

	@Override
	protected void afterRemoveLigne(SWTLigneDocument ligne) throws ExceptLgr {
		// TODO Auto-generated method stub
	}

	@Override
	protected void afterSupprimerEntete() throws ExceptLgr {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean beforeAjoutLigne(SWTLigneDocument ligne) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean beforeEnregistrerEntete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean beforeModifierEntete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean beforeRemoveLigne(SWTLigneDocument ligne) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean beforeSupprimerEntete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void reinitialiseNumLignes() {
		// TODO Auto-generated method stub
	}

	@Override
	public void changementMode(ChangeModeEvent evt) {
		// TODO Auto-generated method stub
	}
	
	


	//@Override
	public void calculeTvaEtTotaux() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codeDocument == null) ? 0 : codeDocument.hashCode());
		result = prime * result + ((dateDocument == null) ? 0 : dateDocument.hashCode());
		result = prime * result + ((dateExport == null) ? 0 : dateExport.hashCode());
//		result = prime * result + ((export == null) ? 0 : export.hashCode());
		result = prime * result + ((libelleDocument == null) ? 0 : libelleDocument.hashCode());
		result = prime * result + ((netTtcCalc == null) ? 0 : netTtcCalc.hashCode());
		result = prime * result + ((taAcompte == null) ? 0 : taAcompte.hashCode());
		result = prime * result + ((taEtat == null) ? 0 : taEtat.hashCode());
		result = prime * result + ((taTiers == null) ? 0 : taTiers.hashCode());
		return result;
	}

	@Override
	 @Transient
	public Set<TaRDocument> getTaRDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	 @Transient
	public void setTaRDocuments(Set<TaRDocument> taRDocuments) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public BigDecimal getTxRemHtDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public BigDecimal getTxRemTtcDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public void setTxRemHtDocument(BigDecimal txRemHtDocument) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public void setTxRemTtcDocument(BigDecimal txRemTtcDocument) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transient
	public Integer getTtc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transient
	public void setTtc(Integer accepte) {
		// TODO Auto-generated method stub
		
	}
	@Override
	@Transient
	public String getTypeDocument() {
		// TODO Auto-generated method stub
		return TYPE_DOC;
	}

	@Transient
	public Date getDateEchDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public BigDecimal getRegleCompletDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public BigDecimal getResteAReglerComplet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setDateEchDocument(Date dateEchDocument) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public void setRegleCompletDocument(BigDecimal regleCompletDocument) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public void setResteAReglerComplet(BigDecimal resteAReglerComplet) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public void setTypeDocument(String typeDocument) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean affectationMultiple(TaFacture taDocument){
		for (TaRReglement rReglement : getTaRReglements()) {
			if(rReglement.getTaFacture()!=null && taDocument!=null &&
					!rReglement.getTaFacture().getCodeDocument().equals(taDocument.getCodeDocument()))
				return true;
		}
		return false;
	}
	
	public boolean affectationMultiple(TaTicketDeCaisse taDocument){
		for (TaRReglement rReglement : getTaRReglements()) {
			if(rReglement.getTaTicketDeCaisse()!=null && taDocument!=null &&
					!rReglement.getTaTicketDeCaisse().getCodeDocument().equals(taDocument.getCodeDocument()))
				return true;
		}
		return false;
	}

	public void calculDateEcheanceAbstract(Integer report, Integer finDeMois){
//		calculDateEcheance(report,finDeMois);
	}

	@Transient
	public String getCommentaire() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setCommentaire(String commentaire) {
		// TODO Auto-generated method stub
		
	}



	
	
	@Transient
	public List<ILigneDocumentTiers> getLignesGeneral(){
		return this.lignes;
	}


	@Transient
	public boolean isLegrain() {
		// TODO Auto-generated method stub
		return false;
	}

	@Transient
	public BigDecimal getNetHtCalc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public IInfosDocumentTiers getTaInfosDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLegrain(boolean legrain) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setTaInfosDocument(IInfosDocumentTiers infosDocumentTiers) {
		
	}
	
	@XmlInverseReference(mappedBy="taReglement")
	@OneToMany(cascade = { CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "taReglement")
	public Set<TaFacture> getTaFactures() {
		return taFactures;
	}

	public void setTaFactures(Set<TaFacture> taFactures) {
		this.taFactures = taFactures;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL} , orphanRemoval=true )
	@JoinColumn(name = "id_mise_a_disposition")
	public TaMiseADisposition getTaMiseADisposition() {
		return taMiseADisposition;
	}

	public void setTaMiseADisposition(TaMiseADisposition taMiseADisposition) {
		this.taMiseADisposition = taMiseADisposition;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_export")
	public Date getDateExport() {
		return dateExport;
	}

	public void setDateExport(Date dateExport) {
		this.dateExport = dateExport;
	}

	@Transient
	public boolean isGestionTVA() {
		// TODO Auto-generated method stub
		return false;
	}

	@Transient
	public void setGestionTVA(boolean gestionTVA) {
		// TODO Auto-generated method stub
		
	}

	@Column(name = "compte_client")
	public Boolean getCompteClient() {
		return compteClient;
	}

	public void setCompteClient(Boolean compteClient) {
		this.compteClient = compteClient;
	}

	@Column(name = "ref_paiement")
	public String getRefPaiement() {
		return refPaiement;
	}

	public void setRefPaiement(String refPaiement) {
		this.refPaiement = refPaiement;
	}

	@Column(name = "service")
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_etat")
//	public TaEtat getTaEtat() {
//		return taEtat;
//	}
//
//	public void setTaEtat(TaEtat taEtat) {
//		this.taEtat = taEtat;
//	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_acompte")
	public TaAcompte getTaAcompte() {
		return taAcompte;
	}

	public void setTaAcompte(TaAcompte taAcompte) {
		this.taAcompte = taAcompte;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_prelevement")
	public TaPrelevement getTaPrelevement() {
		return taPrelevement;
	}

	public void setTaPrelevement(TaPrelevement taPrelevement) {
		this.taPrelevement = taPrelevement;
	}
	
	public boolean estDifferent(TaReglement old) {
		if(old.equalsTraca(this))return true;
		return false;
	}

	
	public boolean equalsTraca(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaReglement other = (TaReglement) obj;
		if (codeDocument == null) {
			if (other.codeDocument != null)
				return false;
		} else if (!codeDocument.equals(other.codeDocument))
			return false;
		if (dateDocument == null) {
			if (other.dateDocument != null)
				return false;
		} else if (!dateDocument.equals(other.dateDocument))
			return false;
		if (dateExport == null) {
			if (other.dateExport != null)
				return false;
		} else if (!dateExport.equals(other.dateExport))
			return false;
//		if (export == null) {
//			if (other.export != null)
//				return false;
//		} else if (!export.equals(other.export))
//			return false;
		if (libelleDocument == null) {
			if (other.libelleDocument != null)
				return false;
		} else if (!libelleDocument.equals(other.libelleDocument))
			return false;
		if (netTtcCalc == null) {
			if (other.netTtcCalc != null)
				return false;
		} else if (!netTtcCalc.equals(other.netTtcCalc))
			return false;
		if (taAcompte == null) {
			if (other.taAcompte != null)
				return false;
		} else if (!taAcompte.equals(other.taAcompte))
			return false;
		if (taEtat == null) {
			if (other.taEtat != null)
				return false;
		} else if (!taEtat.equals(other.taEtat))
			return false;
		if (taTiers == null) {
			if (other.taTiers != null)
				return false;
		} else if (!taTiers.equals(other.taTiers))
			return false;
		return true;
	}

	
	@Column(name = "date_verrouillage")
	public Date getDateVerrouillage() {
		return dateVerrouillage;
	}

	public void setDateVerrouillage(Date dateVerrouillage) {
		this.dateVerrouillage=dateVerrouillage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avis_echeance")
	public TaAvisEcheance getTaAvisEcheance() {
		return taAvisEcheance;
	}

	public void setTaAvisEcheance(TaAvisEcheance taAvisEcheance) {
		this.taAvisEcheance = taAvisEcheance;
	}


	

	@Transient
	public Boolean getGestionLot() {
		// TODO Auto-generated method stub
		return null;
	}
	@Transient
	public void setGestionLot(Boolean gestionLot) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public Integer getNbDecimalesQte() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setNbDecimalesQte(Integer nbDecimalesQte) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public Integer getNbDecimalesPrix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setNbDecimalesPrix(Integer nbDecimalesPrix) {
		// TODO Auto-generated method stub
		
	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_etat")
	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat",champEntite="TaEtat.idEtat", clazz = TaEtat.class)
	public TaEtat getTaEtat() {
		return this.taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}


	@Transient
	public Set<TaREtat> getTaREtats() {
		// TODO Auto-generated method stub
		return null;
	}
	@Transient
	public void setTaREtats(Set<TaREtat> taREtats) {
		// TODO Auto-generated method stub
		
	}
	@Transient
	public Set<TaHistREtat> getTaHistREtats() {
		// TODO Auto-generated method stub
		return null;
	}
	@Transient
	public void setTaHistREtats(Set<TaHistREtat> taHistREtats) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean gereStock() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addRReglementLiaison(TaRReglementLiaison taReglementLiaison){
		if(!this.getTaRReglementLiaisons().contains(taReglementLiaison)){
			this.getTaRReglementLiaisons().add(taReglementLiaison);	
		}
	}
	public void removeReglementLiaison(TaRReglementLiaison taReglementLiaison){
		this.getTaRReglementLiaisons().remove(taReglementLiaison);
	}

	@Transient
	public String getNumeroCommandeFournisseur() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transient
	public Boolean getUtiliseUniteSaisie() {
		return null;
	}
	@Transient
	public void setUtiliseUniteSaisie(Boolean utiliseUniteSaisie) {
		
	}
}
