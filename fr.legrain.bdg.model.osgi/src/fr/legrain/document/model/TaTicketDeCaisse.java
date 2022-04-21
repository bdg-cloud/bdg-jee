package fr.legrain.document.model;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.document.dto.IDocumentCalcul;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersComplet;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.events.SWTModificationDocumentEvent;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.lib.data.ChangeModeEvent;
import fr.legrain.lib.data.ChangeModeListener;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

//@SqlResultSetMapping(name="TaTicketDeCaisseDTO",
//classes = {
//    @ConstructorResult(
//            targetClass = TaTicketDeCaisseDTO.class,
//            columns = {
//                @ColumnResult(name = "id_Document",type = int.class),
//                @ColumnResult(name = "code_Document",type = String.class),
//                @ColumnResult(name = "date_Document",type = Date.class),
//                @ColumnResult(name = "libelle_Document",type = String.class),
//                @ColumnResult(name = "code_Tiers",type = String.class),
//                @ColumnResult(name = "nom_Tiers",type = String.class),
//                @ColumnResult(name = "date_Ech_Document",type = Date.class),
//                @ColumnResult(name = "date_Export",type = Date.class),
//                @ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
//                @ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
//                @ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
//                @ColumnResult(name = "affectationTotale",type = BigDecimal.class),
//                @ColumnResult(name = "resteARegler",type = BigDecimal.class)
//            })    
//})

@SqlResultSetMappings({
	@SqlResultSetMapping(name="TaTicketDeCaisseDTO",
			classes = {
					@ConstructorResult(
							targetClass = TaFactureDTO.class,
							columns = {
									@ColumnResult(name = "id_Document",type = int.class),
									@ColumnResult(name = "code_Document",type = String.class),
									@ColumnResult(name = "date_Document",type = Date.class),
									@ColumnResult(name = "libelle_Document",type = String.class),
									@ColumnResult(name = "code_Tiers",type = String.class),
									@ColumnResult(name = "nom_Tiers",type = String.class),
									@ColumnResult(name = "date_Ech_Document",type = Date.class),
									@ColumnResult(name = "date_Export",type = Date.class),
									@ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
									@ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
									@ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
									@ColumnResult(name = "affectationTotale",type = BigDecimal.class),
									@ColumnResult(name = "resteARegler",type = BigDecimal.class)
							})    
	}),
	@SqlResultSetMapping(name="TaTicketDeCaisseDTORegroupementParFamille",
	classes = {
			@ConstructorResult(
					targetClass = TaTicketDeCaisseDTO.class,
					columns = {
							@ColumnResult(name = "type",type = String.class),
							@ColumnResult(name = "code_Famille",type = String.class),
							@ColumnResult(name = "id_Document",type = int.class),
							@ColumnResult(name = "code_Document",type = String.class),
							@ColumnResult(name = "date_Document",type = Date.class),
							@ColumnResult(name = "libelle_Document",type = String.class),
							@ColumnResult(name = "code_Tiers",type = String.class),
							@ColumnResult(name = "nom_Tiers",type = String.class),
							@ColumnResult(name = "date_Ech_Document",type = Date.class),
							@ColumnResult(name = "date_Export",type = Date.class),
							@ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
							@ColumnResult(name = "affectationTotale",type = BigDecimal.class),
							@ColumnResult(name = "resteARegler",type = BigDecimal.class)
					})    
	}),
	@SqlResultSetMapping(name="TaTicketDeCaisseDTORegroupementParTauxTva",
	classes = {
			@ConstructorResult(
					targetClass = TaTicketDeCaisseDTO.class,
					columns = {
							@ColumnResult(name = "type",type = String.class),
							@ColumnResult(name = "taux_Tva_L_Document",type = BigDecimal.class),
							@ColumnResult(name = "id_Document",type = int.class),
							@ColumnResult(name = "code_Document",type = String.class),
							@ColumnResult(name = "date_Document",type = Date.class),
							@ColumnResult(name = "libelle_Document",type = String.class),
							@ColumnResult(name = "code_Tiers",type = String.class),
							@ColumnResult(name = "nom_Tiers",type = String.class),
							@ColumnResult(name = "date_Ech_Document",type = Date.class),
							@ColumnResult(name = "date_Export",type = Date.class),
							@ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
							@ColumnResult(name = "affectationTotale",type = BigDecimal.class),
							@ColumnResult(name = "resteARegler",type = BigDecimal.class)
					})    
	}),	
	@SqlResultSetMapping(name="TaTicketDeCaisseDTORegroupementParTypePaiement",
	classes = {
			@ConstructorResult(
					targetClass = TaTicketDeCaisseDTO.class,
					columns = {
							@ColumnResult(name = "type",type = String.class),
							@ColumnResult(name = "code_T_Paiement",type = String.class),
							@ColumnResult(name = "id_Document",type = int.class),
							@ColumnResult(name = "code_Document",type = String.class),
							@ColumnResult(name = "date_Document",type = Date.class),
							@ColumnResult(name = "libelle_Document",type = String.class),
							@ColumnResult(name = "code_Tiers",type = String.class),
							@ColumnResult(name = "nom_Tiers",type = String.class),
							@ColumnResult(name = "date_Ech_Document",type = Date.class),
							@ColumnResult(name = "date_Export",type = Date.class),
							@ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
							@ColumnResult(name = "affectationTotale",type = BigDecimal.class),
							@ColumnResult(name = "resteARegler",type = BigDecimal.class)
					})    
	}),	
	@SqlResultSetMapping(name="TaTicketDeCaisseDTORegroupementParVendeur",
	classes = {
			@ConstructorResult(
					targetClass = TaTicketDeCaisseDTO.class,
					columns = {
							@ColumnResult(name = "type",type = String.class),
							@ColumnResult(name = "code_T_Paiement",type = String.class),
							@ColumnResult(name = "id_Document",type = int.class),
							@ColumnResult(name = "code_Document",type = String.class),
							@ColumnResult(name = "date_Document",type = Date.class),
							@ColumnResult(name = "libelle_Document",type = String.class),
							@ColumnResult(name = "code_Tiers",type = String.class),
							@ColumnResult(name = "nom_Tiers",type = String.class),
							@ColumnResult(name = "date_Ech_Document",type = Date.class),
							@ColumnResult(name = "date_Export",type = Date.class),
							@ColumnResult(name = "mt_Ht_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Tva_Calc",type = BigDecimal.class),
							@ColumnResult(name = "mt_Ttc_Calc",type = BigDecimal.class),
							@ColumnResult(name = "affectationTotale",type = BigDecimal.class),
							@ColumnResult(name = "resteARegler",type = BigDecimal.class)
					})    
	})		
	
})



@Entity
@EntityListeners(SwtDocumentListener.class)
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_ticket_caisse", uniqueConstraints = @UniqueConstraint(columnNames = "code_document"))
//@SequenceGenerator(name = "gen_ticket_caisse", sequenceName = "num_id_ticket_caisse", allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_LIGHT, query="select f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers where tiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin order by f.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_CODE_LIGHT, query="select f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers where tiers.codeTiers like :codeTiers and f.codeDocument like :codeDocument order by f.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_CODE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_PARDATE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.dateDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is null order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE_NON_EXPORT, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateVerrouillage is null  order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_PARDATE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is null order by a.dateDocument"), 
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_LIGHT, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc) from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin  and f.dateExport is null order by f.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE_NON_EXPORT_LIGHT, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc) from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.codeDocument between :codeDeb and :codeFin and f.dateExport is null order by f.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATEEXPORT, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateExport =:date order by a.codeDocument"), 
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc) from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers where f.taTiers.codeTiers like :codeTiers and f.dateDocument between :dateDeb and :dateFin  and f.dateExport is null order by f.dateDocument"), 
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_EXPORT, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateExport is not null order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE_EXPORT, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateExport is not null order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_TIERS, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE_POUR_COMPTE_CLIENT, query="select a from TaTicketDeCaisse a join a.taMiseADisposition mad where mad.accessibleSurCompteClient is not null and a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_ETAT_DATE, query="select a from TaTicketDeCaisse a where a.dateDocument between :dateDeb and :dateFin and a.taEtat.codeEtat = :codeEtat order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_ETAT_TIERS_DATE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin and a.taEtat.codeEtat = :codeEtat order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_NON_VERROUILLE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateVerrouillage is null order by a.codeDocument"),		
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE_NON_VERROUILLE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateVerrouillage is null  order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_DATE_VERROUILLE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.dateDocument between :dateDeb and :dateFin  and a.dateVerrouillage is not null order by a.codeDocument"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_CODE_VERROUILLE, query="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers and a.codeDocument between :codeDeb and :codeFin and a.dateVerrouillage is not null order by a.codeDocument"),
//		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc) from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers order by f.codeDocument"),

		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,infos.prenomTiers, "
				+ " infos.nomEntreprise,f.dateEchDocument,f.netHtCalc,f.netTvaCalc,f.netTtcCalc,f.dateExport,f.dateVerrouillage, mad.accessibleSurCompteClient, mad.envoyeParEmail, mad.imprimePourClient) from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers left join f.taMiseADisposition mad order by f.dateDocument DESC, f.codeDocument DESC"),
		// Vérifiez toutes les requêtes ci-dessous qui ont été copiés depuis devis
//		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_ALL_LIGHT_PERIODE, query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO("
//				+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc) "
//				+ " from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers "
//				+ " where f.dateDocument between :dateDebut and :dateFin   and f.taTiers.codeTiers like :codeTiers"
//				+ " order by f.codeDocument"),
//
//		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_PAYE_LIGHT_PERIODE, 
//		query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO("
//				+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc) "
//				+ " from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers   "
//				+ " where f.dateDocument between :dateDebut and :dateFin "
//				+ " and f.taTiers.codeTiers like :codeTiers"
//				+ " and f.taEtat is null "
//				+ " and coalesce((f.netTtcCalc),0)<=(" 
//				+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr left join f3.taRAvoirs ra   "  
//				+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
//				+ " order by f.codeDocument"),		
//		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_NON_PAYE_LIGHT_PERIODE, 
//			query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO("
//					+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc) "
//					+ " from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers  "
//					+ " where f.dateDocument between :dateDebut and :dateFin "
//					+ " and f.taTiers.codeTiers like :codeTiers"
//					+ " and f.taEtat is null "
//					+ " and coalesce((f.netTtcCalc),0)>(" 
//					+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr  left join f3.taRAvoirs ra   "  
//					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
//					+ " order by f.codeDocument"),
//		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE, 
//			query="select new fr.legrain.document.dto.TaTicketDeCaisseDTO("
//					+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc) "
//					+ " from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers  "
//					+ " where f.dateDocument between :dateDebut and :dateFin and f.dateEchDocument < :datejour"
//					+ " and f.taTiers.codeTiers like :codeTiers"
//					+ " and f.taEtat is null "
//					+ " and coalesce((f.netTtcCalc),0)>(" 
//					+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr   left join f3.taRAvoirs ra   "  
//					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
//					+ " order by f.codeDocument"),	

		
//		// Renvoi les totaux de l'ensemble des devis groupé par jour sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_JOUR_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"
					+ " order by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des devis groupé par mois sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_MOIS_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(month from f.dateDocument),extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(month from f.dateDocument),extract(year from f.dateDocument)"
					+ " order by extract(month from f.dateDocument),extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des devis groupé par année sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_ANNEE_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(year from f.dateDocument),coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.taTiers.codeTiers like :codeTiers"
					+ " group by extract(year from f.dateDocument)"
					+ " order by extract(year from f.dateDocument)"),
//		// Renvoi les totaux de l'ensemble des factures sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"),
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " coalesce(sum(f.netHtCalc),0)-coalesce(sum(a.netHtCalc),0) ,coalesce(sum(f.netTvaCalc),0)-coalesce(sum(a.netTvaCalc),0),"
				+ " coalesce(sum(f.netTtcCalc),0)-coalesce(sum(a.netTtcCalc),0)) "
				+ " from TaTicketDeCaisse f ,TaAvoir a "
				+ " where a.taTiers=f.taTiers and f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"),
		
		// Renvoi les totaux de l'ensemble des factures payées sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGHT_PERIODE_PAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  left join f.taRReglements rr    left join f.taRAvoirs ra "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"
					+" group by true"
					+"  having coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0)>= coalesce(sum(f.netTtcCalc),0) "),
//		// Renvoi les totaux de l'ensemble des factures pour un tiers sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers "),
//		// Renvoi les totaux de l'ensemble des factures pour un article sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
					+ "  from TaTicketDeCaisse f left join f.taTiers t left join f.lignes lf left join lf.taArticle art  "
					+ " where art.codeArticle = :codearticle and f.dateDocument between :dateDebut and :dateFin group by art.codeArticle"),
		
		
		
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument), "
				+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0), coalesce(sum(f.netTtcCalc),0)) "
				+ " from TaTicketDeCaisse f  "
				+ " where f.dateDocument between :dateDebut and :dateFin  "
				+ " and f.taTiers.codeTiers like :codeTiers"
				+ " and f.taEtat is null "
				+ " and coalesce((f.netTtcCalc),0)<=(" 
				+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr   left join f3.taRAvoirs ra   "  
				+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
				+ " group by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"
				+ " order by extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)"),		
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE, 
		query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
				+ " extract(month from f.dateDocument),extract(year from f.dateDocument), "
				+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0), coalesce(sum(f.netTtcCalc),0)) "
				+ " from TaTicketDeCaisse f  "
				+ " where f.dateDocument between :dateDebut and :dateFin  "
				+ " and f.taTiers.codeTiers like :codeTiers"
				+ " and f.taEtat is null "
				+ " and coalesce((f.netTtcCalc),0)<=(" 
				+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr   left join f3.taRAvoirs ra   "  
				+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
				+ " group by extract(month from f.dateDocument),extract(year from f.dateDocument)"
				+ " order by extract(month from f.dateDocument),extract(year from f.dateDocument)"),
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " extract(year from f.dateDocument),"
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0), coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and coalesce((f.netTtcCalc),0)<=(" 
					+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr   left join f3.taRAvoirs ra   "  
					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
					+ " group by extract(year from f.dateDocument)"
					+ " order by extract(year from f.dateDocument)"),
		// Renvoi le total à relancer car date échéance dépassée du ca ht, des affectations des règlements+des affectations des avoirs, et du reste à règler des factures sur la période dateDebut à dateFin
		@NamedQuery(name=TaTicketDeCaisse.QN.SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE, 
			query="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
					+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0), coalesce(sum(f.netTtcCalc),0)) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and coalesce((f.netTtcCalc),0)<=(" 
					+"	select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from TaTicketDeCaisse f3  left join f3.taRReglements rr   left join f3.taRAvoirs ra   "  
					+"	where f3.idDocument=f.idDocument group by f3.idDocument )"
					+ " group by true"),
		@NamedQuery(name=TaTicketDeCaisse.QN.FIND_BY_SESSION_CAISSE_COURANTE, query="select f from TaTicketDeCaisse f where f.taSessionCaisse is null")
		})


@NamedNativeQueries({
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.FIND_ALL_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Ech_Document,res.date_export,res.mt_Ht_Calc,res.mt_Tva_Calc,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0)as mt_Ht_Calc,coalesce(f.net_Tva_Calc,0)as mt_Tva_Calc,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteARegler " + 
					" from ta_ticket_caisse f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join ta_infos_ticket_caisse infos on infos.id_document=f.id_document " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" + 	
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0),coalesce(f.net_Tva_Calc,0),coalesce(f.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
					" order by f.code_Document)as res ", resultSetMapping = "TaTicketDeCaisseDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.FIND_PAYE_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Ech_Document,res.date_export,res.mt_Ht_Calc,res.mt_Tva_Calc,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0)as mt_Ht_Calc,coalesce(f.net_Tva_Calc,0)as mt_Tva_Calc,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteARegler " + 
					" from ta_ticket_caisse f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join ta_infos_ticket_caisse infos on infos.id_document=f.id_document " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" + 	
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)<=(" + 
					" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0),coalesce(f.net_Tva_Calc,0),coalesce(f.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
					" order by f.code_Document)as res ", resultSetMapping = "TaTicketDeCaisseDTO"),

	@NamedNativeQuery(name=TaTicketDeCaisse.QN.FIND_NON_PAYE_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Ech_Document,res.date_export,res.mt_Ht_Calc,res.mt_Tva_Calc,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0)as mt_Ht_Calc,coalesce(f.net_Tva_Calc,0)as mt_Tva_Calc,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteARegler " + 
					" from ta_ticket_caisse f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join ta_infos_ticket_caisse infos on infos.id_document=f.id_document " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" + 	
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and tiers.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)>(" + 
					" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0),coalesce(f.net_Tva_Calc,0),coalesce(f.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
					" order by f.code_Document)as res ", resultSetMapping = "TaTicketDeCaisseDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE, 
			query="select res.id_Document, res.code_Document, res.date_Document, res.libelle_Document, res.code_Tiers, res.nom_Tiers,res.date_Ech_Document,res.date_export,res.mt_Ht_Calc,res.mt_Tva_Calc,res.mt_Ttc_Calc,res.affectationTotale,res.resteARegler from( "+
					" select f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0)as mt_Ht_Calc,coalesce(f.net_Tva_Calc,0)as mt_Tva_Calc,coalesce(f.net_Ttc_Calc,0)as mt_Ttc_Calc,"+
					" coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(f.net_Ttc_Calc,0)-(s1.affectation+s2.affectationAvoir))as resteARegler " + 
					" from ta_ticket_caisse f join ta_tiers tiers on tiers.id_tiers=f.id_tiers " +
					" join ta_infos_ticket_caisse infos on infos.id_document=f.id_document " +
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
				" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
				" group by f.id_document )as s2 " + 
				" on s2.id_document=f.id_document" + 	
				" where f.date_Document between :dateDebut and :dateFin and f.date_Ech_Document < :datejour " + 
				" and tiers.code_Tiers like :codeTiers" + 
				" and f.id_Etat is null " + 
				" and coalesce((f.net_Ttc_Calc),0)>(" + 
				" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
				" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
				" group by f.id_Document, f.code_Document, f.date_Document, f.libelle_Document, tiers.code_Tiers, infos.nom_Tiers,f.date_Ech_Document,f.date_Export,coalesce(f.net_Ht_Calc,0),coalesce(f.net_Tva_Calc,0),coalesce(f.net_Ttc_Calc,0),s1.affectation,s2.affectationAvoir" + 
				" order by f.code_Document)as res ", resultSetMapping = "TaTicketDeCaisseDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select (0)as jour,(0)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" +  
					" where f.date_Document between :dateDebut and :dateFin and f.date_Ech_Document < :datejour " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)>(" + 
					" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
					" order by extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name = TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s2 " + 
			" on s2.id_document=f.id_document" +  
			" where f.date_Document between :dateDebut and :dateFin and f.date_Ech_Document < :datejour " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
			" order by extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER, 
	query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select extract(day from f.date_Document)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s2 " + 
			" on s2.id_document=f.id_document" +  
			" where f.date_Document between :dateDebut and :dateFin and f.date_Ech_Document < :datejour " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
			" order by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,(0)as mois,(0)as annee,true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s2 " + 
			" on s2.id_document=f.id_document" +  
			" where f.date_Document between :dateDebut and :dateFin and f.date_Ech_Document < :datejour " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by s1.affectation,s2.affectationAvoir)as res group by res.jour,res.mois,res.annee" ,resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select extract(day from f.date_Document)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" +  
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)>(" + 
					" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
					" order by extract(day from f.date_Document),extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
			" select (0)as jour,extract(month from f.date_Document)as mois,extract(year from f.date_Document)as annee,"+
			" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
			" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
			" join ta_tiers t on t.id_tiers=f.id_tiers " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
			" join (" + 
			" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
			" group by f.id_document )as s2 " + 
			" on s2.id_document=f.id_document" +  
			" where f.date_Document between :dateDebut and :dateFin  " + 
			" and t.code_Tiers like :codeTiers" + 
			" and f.id_Etat is null " + 
			" and coalesce((f.net_Ttc_Calc),0)>(" + 
			" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
			" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
			" group by extract(month from f.date_Document),extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
			" order by extract(month from f.date_Document),extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE, 
			query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
					" select (0)as jour,(0)as mois,extract(year from f.date_Document)as annee,"+
					" true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
					" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
					" join ta_tiers t on t.id_tiers=f.id_tiers " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
					" join (" + 
					" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
					" group by f.id_document )as s2 " + 
					" on s2.id_document=f.id_document" +  
					" where f.date_Document between :dateDebut and :dateFin " + 
					" and t.code_Tiers like :codeTiers" + 
					" and f.id_Etat is null " + 
					" and coalesce((f.net_Ttc_Calc),0)>(" + 
					" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
					" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
					" group by extract(year from f.date_Document),s1.affectation,s2.affectationAvoir" + 
					" order by extract(year from f.date_Document))as res group by res.jour,res.mois,res.annee", resultSetMapping = "DocumentChiffreAffaireDTO"),
	@NamedNativeQuery(name=TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE, 
	query = "select res.jour,res.mois,res.annee,true as typeReglement,sum(res.mtHtCalc)as mtHtCalc ,sum(res.mtTvaCalc)as mtTvaCalc, sum(res.mtTtcCalc)as mtTtcCalc,sum(res.affectationTotale)as affectationTotale,sum(res.resteARegler)as resteARegler from("+
	" select (0)as jour,(0)as mois,(0)as annee,true as typeReglement,coalesce(sum(f.net_Ht_Calc),0)as mtHtCalc ,coalesce(sum(f.net_Tva_Calc),0)as mtTvaCalc, coalesce(sum(f.net_Ttc_Calc),0)as mtTtcCalc," + 
	" (s1.affectation+s2.affectationAvoir)as affectationTotale,(coalesce(sum(f.net_Ttc_Calc),0)-(s1.affectation+s2.affectationAvoir))as resteARegler from ta_ticket_caisse f " + 
	" join ta_tiers t on t.id_tiers=f.id_tiers " + 
	" join (" + 
	" select f.id_document,(coalesce(sum(rr.affectation),0))as affectation from ta_ticket_caisse f left join ta_r_reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
	" group by f.id_document )as s1 on s1.id_document=f.id_document " + 
	" join (" + 
	" select f.id_document,(coalesce(sum(ra.affectation),0))as affectationAvoir from ta_ticket_caisse f left join ta_r_avoir ra on ra.id_ticket_caisse=f.id_document " + 
	" group by f.id_document )as s2 " + 
	" on s2.id_document=f.id_document" +  
	" where f.date_Document between :dateDebut and :dateFin " + 
	" and t.code_Tiers like :codeTiers" + 
	" and f.id_Etat is null " + 
	" and coalesce((f.net_Ttc_Calc),0)>(" + 
	" select coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0) from Ta_Facture f3  left join ta_R_Reglement rr on rr.id_ticket_caisse=f.id_document  left join ta_R_Avoir ra  on ra.id_ticket_caisse=f.id_document  " + 
	" where f3.id_Document=f.id_Document group by f3.id_Document )" + 
	" group by s1.affectation,s2.affectationAvoir)as res group by res.jour,res.mois,res.annee" ,resultSetMapping = "DocumentChiffreAffaireDTO"),	
})



public class TaTicketDeCaisse extends SWTDocument implements ChangeModeListener, java.io.Serializable,
Cloneable,IDocumentTiers,IDocumentTiersComplet, IDocumentCalcul, IDocumentPayableCB{
	
	private static final long serialVersionUID = -4110377235072058281L;
	
	public static final String TYPE_DOC = "TicketDeCaisse";
	public static final String PATH_ICONE_COULEUR = "dashboard/facture.svg";
	public static final String PATH_ICONE_BLANC = "dashboard/facture-blanc.png";
	public static final String PATH_ICONE_GRIS = "";
	public static class QN {
		public static final String FIND_BY_DATE = "TaTicketDeCaisse.findEntre2Date";
		public static final String FIND_BY_DATE_LIGHT = "TaTicketDeCaisse.findEntre2DateLight";
		public static final String FIND_BY_DATE_PARDATE = "TaTicketDeCaisse.findEntre2DateParDate";
		public static final String FIND_BY_TIERS_AND_CODE = "TaTicketDeCaisse.findEntre2CodeParCode";
		public static final String FIND_BY_TIERS_AND_CODE_LIGHT = "TaTicketDeCaisse.findEntre2CodeParCodeLight";
		public static final String FIND_BY_CODE = "TaTicketDeCaisse.findEntre2Code";
		public static final String FIND_BY_DATE_NON_EXPORT = "TaTicketDeCaisse.findEntre2DateNonExporte";
		public static final String FIND_BY_CODE_NON_EXPORT = "TaTicketDeCaisse.findEntre2CodeNonExporte";
		public static final String FIND_BY_DATE_NON_EXPORT_PARDATE="TaTicketDeCaisse.findEntre2DateNonExporteParDate";
		public static final String FIND_BY_DATE_EXPORT = "TaTicketDeCaisse.findEntre2DateExporte";
		public static final String FIND_BY_CODE_EXPORT = "TaTicketDeCaisse.findEntre2CodeExporte";
		public static final String FIND_BY_DATE_NON_VERROUILLE = "TaTicketDeCaisse.findEntre2DateNonVerrouille";
		public static final String FIND_BY_CODE_NON_VERROUILLE = "TaTicketDeCaisse.findEntre2CodeNonVerrouille";
		public static final String FIND_BY_DATE_VERROUILLE = "TaTicketDeCaisse.findEntre2DateVerrouille";
		public static final String FIND_BY_CODE_VERROUILLE = "TaTicketDeCaisse.findEntre2CodeVerrouille";
		public static final String FIND_BY_TIERS = "TaTicketDeCaisse.findTiers";
		public static final String FIND_BY_TIERS_AND_DATE = "TaTicketDeCaisse.findTiersEntre2Date";
		public static final String FIND_BY_TIERS_AND_DATE_POUR_COMPTE_CLIENT = "TaTicketDeCaisse.findTiersEntre2DateCompteClient";
		public static final String FIND_BY_ETAT_DATE = "TaTicketDeCaisse.findEtatDate";
		public static final String FIND_BY_ETAT_TIERS_DATE = "TaTicketDeCaisse.findTiersEtat";
		public static final String FIND_ALL_LIGHT = "TaTicketDeCaisse.findAllLight";
		public static final String FIND_ALL_LIGHT_LISTE = "TaTicketDeCaisse.findAllLightListe";
		public static final String FIND_BY_DATE_NON_EXPORT_LIGHT = "TaTicketDeCaisse.findEntre2DateNonExporteLight";
		public static final String FIND_BY_CODE_NON_EXPORT_LIGHT = "TaTicketDeCaisse.findEntre2CodeNonExporteLight";
		public static final String FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT="TaTicketDeCaisse.findEntre2DateNonExporteParDateLight";
		public static final String FIND_BY_DATEEXPORT = "TaTicketDeCaisse.findDateExporte";
//		public static final String FIND_BY_DATE_NON_VERROULLE_LIGHT = "TaTicketDeCaisse.findEntre2DateNonVerrouilleLight";
//		public static final String FIND_BY_CODE_NON_VERROUILLE_LIGHT = "TaTicketDeCaisse.findEntre2CodeNonVerrouilleLight";
		
		public static final String FIND_BY_SESSION_CAISSE_COURANTE = "TaTicketDeCaisse.findBySessionCaisseCourante";

		
		public static final String FIND_ALL_LIGHT_PERIODE = "TaTicketDeCaisse.findAllLightPeriode";
		public static final String FIND_PAYE_LIGHT_PERIODE = "TaTicketDeCaisse.findFacturePayePeriodeDTO";
		public static final String FIND_NON_PAYE_LIGHT_PERIODE = "TaTicketDeCaisse.findFactureNonPayeDTO";		
		public static final String FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE = "TaTicketDeCaisse.findFactureNonPayeARelancerDTO";

		/*Isa*/
		public static final String SUM_CA_JOUR_LIGTH_PERIODE = "TaTicketDeCaisse.caFactureJourPeriodeDTO";
		public static final String SUM_CA_MOIS_LIGTH_PERIODE = "TaTicketDeCaisse.caFactureMoisPeriodeDTO";
		public static final String SUM_CA_ANNEE_LIGTH_PERIODE = "TaTicketDeCaisse.caFactureAnneePeriodeDTO";
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE = "TaTicketDeCaisse.caFactureTotalPeriodeDTO";
		public static final String SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE = "TaTicketDeCaisse.caFactureTotalSansAvoirPeriodeDTO";

		public static final String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE = "TaTicketDeCaisse.resteAReglerFactureJourPeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE = "TaTicketDeCaisse.resteAReglerFactureMoisPeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE = "TaTicketDeCaisse.resteAReglerFactureAnneePeriodeDTO";
		public static final String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE = "TaTicketDeCaisse.resteAReglerFactureTotalPeriodeDTO";
//
		public static final String SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER = "TaTicketDeCaisse.resteAReglerFactureJourPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER = "TaTicketDeCaisse.resteAReglerFactureMoisPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER = "TaTicketDeCaisse.resteAReglerFactureAnneePeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER = "TaTicketDeCaisse.resteAReglerFactureTotalPeriodeDTOARelancer";
		public static final String SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER2 = "1";
		
		public static final String SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE = "TaTicketDeCaisse.caFactureJourPeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE = "TaTicketDeCaisse.caFactureMoisPeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE = "TaTicketDeCaisse.caFactureAnneePeriodeDTOTotalementPaye";
		public static final String SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE = "TaTicketDeCaisse.caFactureTotalPeriodeDTOTotalementPaye";
		/*fin Isa*/
		
		public static final String SUM_CA_TOTAL_LIGHT_PERIODE_PAYE = "TaTicketDeCaisse.caFactureTotalPeriodeDTOPaye";
		
		
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS = "TaTicketDeCaisse.caTotalLightPeriodeParTiers";
		public static final String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE = "TaTicketDeCaisse.caTotalLightPeriodeParArticle";
	}


//	private int idDocument;
	private String version;
	
	@Transient
	private TaRReglement taRReglement;
	@Transient
	private TaTPaiement taTPaiement;
	private Boolean gestionLot = false;
	
	private TaTiers taTiers;
	//private TaCPaiement taCPaiement;
	private String codeDocument;
	private Date dateDocument;
	private Date dateEchDocument;
	private Date dateLivDocument;
	private Date dateExport;
	private String libelleDocument;
	private TaGrMouvStock taGrMouvStock;
	
	protected Integer nbDecimalesPrix;
	protected Integer nbDecimalesQte;
	
	@Transient
	private BigDecimal regleDocument = new BigDecimal(0);
	
	private BigDecimal remHtDocument = new BigDecimal(0);
	private BigDecimal txRemHtDocument = new BigDecimal(0);
	private BigDecimal remTtcDocument = new BigDecimal(0);
	private BigDecimal txRemTtcDocument = new BigDecimal(0);
	private Integer nbEDocument = 0;
	private Integer ttc = 0;
//	private Integer export = 0;
	private String commentaire;
//	private String	quiCree;
//	private Date quandCreeDocument;
//	private String quiModifDocument;
//	private Date quandModifDocument;
	private String ipAcces;
//	private Integer versionObj;
	private Date dateVerrouillage;
	private TaEtat taEtat;
	private TaInfosTicketDeCaisse taInfosDocument;
	
	@Transient
//	private Boolean miseADispo=false;
	private TaMiseADisposition taMiseADisposition;
	
	private TaReglement taReglement;
	
	private TaSessionCaisse taSessionCaisse;
	private TaUtilisateur taUtilisateurVendeur;
	
	private Set<TaRDocument> taRDocuments = new HashSet<TaRDocument>(0);
	private Set<TaRAcompte> taRAcomptes = new HashSet<TaRAcompte>(0);
	private Set<TaRAvoir> taRAvoirs = new HashSet<TaRAvoir>(0);
	private Set<TaRReglement> taRReglements = new HashSet<TaRReglement>(0);
	private Set<TaREtat> taREtats = new HashSet<TaREtat>(0);
	private Set<TaHistREtat> taHistREtats = new HashSet<TaHistREtat>(0);
	@Transient
	private ArrayList<LigneTva> lignesTVA = null; //ensemble des lignes de tva du document
	@Transient
	private boolean gestionTVA = true;
	@Transient
	static Logger logger = Logger.getLogger(TaTicketDeCaisse.class.getName());


	private BigDecimal mtTtcCalc = new BigDecimal(0);//après remise ht

	private BigDecimal mtTtcAvantRemiseGlobaleCalc = new BigDecimal(0);

	private BigDecimal mtHtCalc = new BigDecimal(0);//avant remise

	private BigDecimal mtTvaCalc = new BigDecimal(0);//avant remise (champ non rempli actuellement)

	private BigDecimal netTtcCalc = new BigDecimal(0);//après escompte mais 
	                                           //devra prendre valeur avant escompte 

	private BigDecimal netHtCalc = new BigDecimal(0); //après remise ht

	private BigDecimal netTvaCalc = new BigDecimal(0);//après remise ht

	private BigDecimal netAPayer = new BigDecimal(0); //remplace le netTTcCalc après escompte
	
	@Transient
	private BigDecimal remTtcIntermediaireDocument = new BigDecimal(0);
	


	@Transient
	private BigDecimal resteARegler = new BigDecimal(0);

	
	private BigDecimal acomptes = new BigDecimal(0);

	@Transient
	private BigDecimal avoirs = new BigDecimal(0);
	@Transient
	private BigDecimal avoirsComplet = new BigDecimal(0);
	@Transient
	private String libellePaiement ;
	
	@Transient
	private boolean legrain = false;
	
	@Transient
	private BigDecimal regleCompletDocument = new BigDecimal(0);
	@Transient
	private BigDecimal resteAReglerComplet = new BigDecimal(0);
	@Transient
	private String typeDocument;
	
	protected Boolean utiliseUniteSaisie = true;

	private Set<TaRReglementLiaison> taRReglementLiaisons = new HashSet<TaRReglementLiaison>(0);

//	private String tgOperation;
	
	
	public TaTicketDeCaisse(boolean legrain) {
		this.legrain = legrain;
		lignes = new ArrayList<TaLTicketDeCaisse>(0);
		lignesTVA = new ArrayList<LigneTva>();
//		setTaRReglement(new TaRReglement());
//		getTaRReglement().setTaFacture(this);
	}

	public TaTicketDeCaisse() {
		lignes = new ArrayList<TaLTicketDeCaisse>(0);
		lignesTVA = new ArrayList<LigneTva>();
//		setTaRReglement(new TaRReglement());
//		getTaRReglement().setTaFacture(this);
	}

	public TaTicketDeCaisse(int idFacture) {
		this.idDocument = idFacture;
		lignes = new ArrayList<TaLTicketDeCaisse>(0);
		lignesTVA = new ArrayList<LigneTva>();
//		setTaRReglement(new TaRReglement());
//		getTaRReglement().setTaFacture(this);
	}

	public TaTicketDeCaisse(String oldCodeFacture) {
		super(oldCodeFacture);
		legrain = true;
		lignes = new ArrayList<TaLTicketDeCaisse>(0);
		this.lignesTVA = new ArrayList<LigneTva>();
		//passage ejb
//		this.modeDocument = EnumModeObjet.C_MO_INSERTION;
//		setTaRReglement(new TaRReglement());
//		getTaRReglement().setTaFacture(this);
	}

	public TaTicketDeCaisse(int idFacture, TaTPaiement taTPaiement, TaTiers taTiers,
			TaCPaiement taCPaiement,  String codeFacture,
			Date dateFacture, Date dateEchFacture, Date dateLivFacture,
			String libelleFacture, BigDecimal regleFacture,
			BigDecimal remHtFacture, BigDecimal txRemHtFacture,
			BigDecimal remTtcFacture, BigDecimal txRemTtcFacture,
			Integer nbEFacture, Integer ttc, Integer export, String commentaire,
			String quiCreeFacture, Date quandCreeFacture,
			String quiModifFacture, Date quandModifFacture, String ipAcces,
			Integer versionObj, List<TaLTicketDeCaisse> TaLTicketDeCaisses,
			TaInfosTicketDeCaisse taInfosFactures, Set<TaRDocument> taRDocuments, Set<TaRAcompte> taRAcomptes) {
		this.idDocument = idFacture;
		this.taTPaiement = taTPaiement;
		this.taTiers = taTiers;
		//this.taCPaiement = taCPaiement;
		this.codeDocument = codeFacture;
		this.dateDocument = dateFacture;
		this.dateEchDocument = dateEchFacture;
		this.dateLivDocument = dateLivFacture;
		this.libelleDocument = libelleFacture;
		this.regleDocument = regleFacture;
		this.remHtDocument = remHtFacture;
		this.txRemHtDocument = txRemHtFacture;
		this.remTtcDocument = remTtcFacture;
		this.txRemTtcDocument = txRemTtcFacture;
		this.nbEDocument = nbEFacture;
		this.ttc = ttc;
//		this.export = export;
		this.commentaire = commentaire;
		this.quiCree = quiCreeFacture;
		this.quandCree = quandCreeFacture;
		this.quiModif = quiModifFacture;
		this.quandModif = quandModifFacture;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
		this.lignes = TaLTicketDeCaisses;
		this.taInfosDocument = taInfosFactures;
		this.taRDocuments = taRDocuments;
		this.taRAcomptes = taRAcomptes;
//		setTaRReglement(new TaRReglement());
//		getTaRReglement().setTaFacture(this);
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_ticket_caisse")
//	@Column(name = "id_document", unique = true, nullable = false)
//	@LgrHibernateValidated(champ = "id_document",table = "ta_ticket_caisse",clazz = TaTicketDeCaisse.class)
//	public int getIdDocument() {
//		return this.idDocument;
//	}
//
//	public void setIdDocument(int idFacture) {
//		this.idDocument = idFacture;
//	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_t_paiement")
	@LgrHibernateValidated(champBd = "id_t_paiement",table = "ta_t_paiement",champEntite="TaTPaiement.idTPaiement", clazz = TaTPaiement.class)
//	@Transient
	public TaTPaiement getTaTPaiement() {
		return this.taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		if(this.taTPaiement==null||taTPaiement==null ||
				!this.taTPaiement.equals(taTPaiement)){
			this.taTPaiement = taTPaiement;
		}
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tiers")
	@LgrHibernateValidated(champBd = "id_tiers",table = "ta_tiers",champEntite="TaTiers.idTiers", clazz = TaTiers.class)
	public TaTiers getTaTiers() {
		return this.taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		//if(taRAcomptes.size()==0 || rechercheSiMemeTiers(taTiers)) // => Passage EJB
			this.taTiers = taTiers;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL} , orphanRemoval=true )
	@JoinColumn(name = "id_reglement")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_reglement",champEntite="TaReglement.idDocument", clazz = TaReglement.class)
	@XmlTransient
	public TaReglement getTaReglement() {
		return taReglement;
	}

//	public void setTaReglementAndCalcul(TaReglement taReglement) {
//		this.setTaReglement(taReglement);
//		this.calculRegleDocument();
//	}
	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
	}
	

	public boolean rechercheSiMemeTiers(TaTiers taTiers){
		for (TaRAcompte acompte : taRAcomptes) {
			if(acompte.getTaAcompte()!=null && acompte.getTaAcompte().getTaTiers()!=null)
				if (!acompte.getTaAcompte().getTaTiers().equals(taTiers))
					return false;
		}
		return true;
	}
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_c_paiement")
//	@LgrHibernateValidated(champ = "id_c_paiement",table = "ta_c_paiement",clazz = TaCPaiement.class)
//	public TaCPaiement getTaCPaiement() {
//		return this.taCPaiement;
//	}
//
//	public void setTaCPaiement(TaCPaiement taCPaiement) {
//		this.taCPaiement = taCPaiement;
//	}


	@Column(name = "code_document", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_document",table = "ta_ticket_caisse",champEntite="codeDocument", clazz = TaTicketDeCaisse.class)
	public String getCodeDocument() {
		return this.codeDocument;
	}

	public void setCodeDocument(String codeFacture) {
		this.codeDocument = codeFacture;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_document", length = 19)
	@LgrHibernateValidated(champBd = "date_document",table = "ta_ticket_caisse",champEntite="dateDocument", clazz = TaTicketDeCaisse.class)
	public Date getDateDocument() {
		return this.dateDocument;
	}

	public void setDateDocument(Date dateFacture) {
		if(this.oldDate==null)this.oldDate=dateFacture;
		else
		if(this.dateDocument==null||
				this.dateDocument.compareTo(dateFacture)!=0)
			this.oldDate=this.dateDocument;
		this.dateDocument = dateFacture;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_ech_document", length = 19)
	@LgrHibernateValidated(champBd = "date_ech_document",table = "ta_ticket_caisse",champEntite="dateEchDocument", clazz = TaTicketDeCaisse.class)
	public Date getDateEchDocument() {
		return this.dateEchDocument;
	}

	public void setDateEchDocument(Date dateEchFacture) {
		this.dateEchDocument = dateEchFacture;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_liv_document", length = 19)
	@LgrHibernateValidated(champBd = "date_liv_document",table = "ta_ticket_caisse",champEntite="dateLivDocument", clazz = TaTicketDeCaisse.class)
	public Date getDateLivDocument() {
		return this.dateLivDocument;
	}

	public void setDateLivDocument(Date dateLivFacture) {
		this.dateLivDocument = dateLivFacture;
	}

	@Column(name = "libelle_document")
	@LgrHibernateValidated(champBd = "libelle_document",table = "ta_ticket_caisse",champEntite="libelleDocument", clazz = TaTicketDeCaisse.class)
	public String getLibelleDocument() {
		return this.libelleDocument;
	}

	public void setLibelleDocument(String libelleFacture) {
		this.libelleDocument = libelleFacture;
	}

	//@Column(name = "regle_document", precision = 15)
	@LgrHibernateValidated(champBd = "regle_document",table = "ta_ticket_caisse",champEntite="regleDocument", clazz = TaTicketDeCaisse.class)
	@Transient
	public BigDecimal getRegleDocument() {
		if(getTaRReglement()!=null)
		return this.getTaRReglement().getAffectation();
		else
			//return BigDecimal.valueOf(0);
		return this.regleDocument;
	}

	public void setRegleDocument(BigDecimal regleFacture) {
		//if(regleFacture!=null && this.regleDocument.compareTo(regleFacture)!=0){
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		this.regleDocument=calculSommeReglementsIntegresEcran();		
		
//		if(getTaRReglement()!=null)
//			getTaRReglement().setAffectation(this.regleDocument);
//		else
//			this.regleDocument = calculSommeReglementsNonIntegres();		
			if(legrain) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaFactureService
				calculTotaux();
				
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
//		}
	}

	@Column(name = "rem_ht_document", precision = 15)
	@LgrHibernateValidated(champBd = "rem_ht_document",table = "ta_ticket_caisse",champEntite="remHtDocument", clazz = TaTicketDeCaisse.class)
	public BigDecimal getRemHtDocument() {
		return this.remHtDocument;
	}

	public void setRemHtDocument(BigDecimal remHtFacture) {
//		if(this.remHtDocument.compareTo(remHtFacture)!=0){
			this.remHtDocument = remHtFacture;
//		}
	}

	@Column(name = "tx_rem_ht_document", precision = 15)
	@LgrHibernateValidated(champBd = "tx_rem_ht_document",table = "ta_ticket_caisse",champEntite="txRemHtDocument", clazz = TaTicketDeCaisse.class)
	public BigDecimal getTxRemHtDocument() {
		return this.txRemHtDocument;
	}

	public void setTxRemHtDocument(BigDecimal txRemHtFacture) {
		if(txRemHtFacture==null)txRemHtFacture=new BigDecimal(0);
		if(this.txRemHtDocument!=null && this.txRemHtDocument.compareTo(txRemHtFacture)!=0){
			this.txRemHtDocument = txRemHtFacture;
			if(legrain) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaFactureService
				calculeTvaEtTotaux();
				
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}

//	@AccessType(value="field")
	@Column(name = "rem_ttc_document", precision = 15)
	@LgrHibernateValidated(champBd = "rem_ttc_document",table = "ta_ticket_caisse",champEntite="remTtcDocument", clazz = TaTicketDeCaisse.class)
	public BigDecimal getRemTtcDocument() {
		return this.remTtcDocument;
	}

	public void setRemTtcDocument(BigDecimal remTtcFacture) {
//		if(this.remTtcDocument.compareTo(remTtcFacture)!=0){	
			this.remTtcDocument = remTtcFacture;
//		}
	}

//	@AccessType(value="field")
	@Column(name = "tx_rem_ttc_document", precision = 15)
	@LgrHibernateValidated(champBd = "tx_rem_ttc_document",table = "ta_ticket_caisse",champEntite="txRemTtcDocument", clazz = TaTicketDeCaisse.class)
	public BigDecimal getTxRemTtcDocument() {
		return this.txRemTtcDocument;
	}

	public void setTxRemTtcDocument(BigDecimal txRemTtcFacture) {
		if(txRemTtcFacture==null)txRemTtcFacture=new BigDecimal(0);
		if(this.txRemTtcDocument!=null && this.txRemTtcDocument.compareTo(txRemTtcFacture)!=0){		
			this.txRemTtcDocument = txRemTtcFacture;
			if(legrain) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaFactureService
				calculeTvaEtTotaux();
				
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this,"txRemTtcDocument"));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}

	@Column(name = "nb_e_document")
	@LgrHibernateValidated(champBd = "nb_e_document",table = "ta_ticket_caisse",champEntite="nbEDocument", clazz = TaTicketDeCaisse.class)
	public Integer getNbEDocument() {
		return this.nbEDocument;
	}

	public void setNbEDocument(Integer nbEFacture) {
		this.nbEDocument = nbEFacture;
	}

	@Column(name = "ttc")
	@LgrHibernateValidated(champBd = "ttc",table = "ta_ticket_caisse",champEntite="ttc", clazz = TaTicketDeCaisse.class)
	public Integer getTtc() {
		return this.ttc;
	}

	public void setTtc(Integer ttc) {
		this.ttc = ttc;
	}

//	@Column(name = "export")
//	@LgrHibernateValidated(champBd = "export",table = "ta_ticket_caisse",champEntite="export", clazz = TaTicketDeCaisse.class)
//	public Integer getExport() {
//		return this.export;
//	}
//
//	public void setExport(Integer export) {
//		this.export = export;
//	}

	@Column(name = "commentaire", length = 2000)
	@LgrHibernateValidated(champBd = "commentaire",table = "ta_ticket_caisse",champEntite="commentaire", clazz = TaTicketDeCaisse.class)
	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		if(this.commentaire==null||(commentaire!=null &&
				this.commentaire.compareTo(commentaire)!=0)){
			this.commentaire = commentaire;		
			if(legrain) {
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_etat")
//	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat",champEntite="TaEtat.idEtat", clazz = TaEtat.class)
//	public TaEtat getTaEtat() {
//		return this.taEtat;
//	}
//
//	public void setTaEtat(TaEtat taEtat) {
//		this.taEtat = taEtat;
//	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCreeDocument() {
//		return this.quiCree;
//	}
//
//	public void setQuiCreeDocument(String quiCreeFacture) {
//		this.quiCree = quiCreeFacture;
//	}

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCreeDocument() {
//		return this.quandCree;
//	}
//
//	public void setQuandCreeDocument(Date quandCreeFacture) {
//		this.quandCree = quandCreeFacture;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModifDocument() {
//		return this.quiModifDocument;
//	}
//
//	public void setQuiModifDocument(String quiModifFacture) {
//		this.quiModifDocument = quiModifFacture;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModifDocument() {
//		return this.quandModifDocument;
//	}
//
//	public void setQuandModifDocument(Date quandModifFacture) {
//		this.quandModifDocument = quandModifFacture;
//	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

//	@Version
//	@Column(name = "version_obj")
//	public Integer getVersionObj() {
//		return this.versionObj;
//	}
//
//	public void setVersionObj(Integer versionObj) {
//		this.versionObj = versionObj;
//	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taDocument", orphanRemoval=true)
	@OrderBy("numLigneLDocument")
	@Fetch(FetchMode.SUBSELECT)
	@Valid
	public List<TaLTicketDeCaisse> getLignes() {
		return this.lignes;
	}

	public void setLignes(List<TaLTicketDeCaisse> taLTicketDeCaisses) {
		this.lignes = taLTicketDeCaisses;
	}

//	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taDocument")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval=true, optional=false)
	@JoinColumn(name = "id_infos_document")
	public TaInfosTicketDeCaisse getTaInfosDocument() {
		return this.taInfosDocument;
	}

	public void setTaInfosDocument(TaInfosTicketDeCaisse taInfosFactures) {
		this.taInfosDocument = taInfosFactures;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taTicketDeCaisse", orphanRemoval=true)
	@XmlTransient
	public Set<TaRDocument> getTaRDocuments() {
		return this.taRDocuments;
	}

	public void setTaRDocuments(Set<TaRDocument> taRDocuments) {
		this.taRDocuments = taRDocuments;
	}

	@OneToMany( fetch = FetchType.EAGER, mappedBy = "taTicketDeCaisse", orphanRemoval=true)
	public Set<TaRAvoir> getTaRAvoirs() {
		return taRAvoirs;
	}

	public void setTaRAvoirs(Set<TaRAvoir> taRAvoirs) {
		this.taRAvoirs = taRAvoirs;
	}
	
	@OneToMany( fetch = FetchType.EAGER, mappedBy = "taTicketDeCaisse", orphanRemoval=true)
	@XmlTransient //plus utilisé
	public Set<TaRAcompte> getTaRAcomptes(){
		return this.taRAcomptes;
	}

	public void setTaRAcomptes(Set<TaRAcompte> taRAcomptes) {
		this.taRAcomptes = taRAcomptes;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "taTicketDeCaisse", orphanRemoval=true)
	public Set<TaRReglement> getTaRReglements() {
		return taRReglements;
	}

	public void setTaRReglements(Set<TaRReglement> taRReglements) {
		this.taRReglements = taRReglements;
	}
	
	
	/**
	 * Initialisation des propriétés de la facture en fonction du tiers
	 */
	public void changementDeTiers() {
//		if(this.taTiers!=null && this.taTiers.getTaTTvaDoc()!=null && 
//				this.taTiers.getTaTTvaDoc().getCodeTTvaDoc()!=null){
//			if(!this.taTiers.getTaTTvaDoc().getCodeTTvaDoc().equals("F")
//					//				||this.taTiers.getTaTTvaDoc().getCodeTTvaDoc().equals("UE")
//					//				||this.taTiers.getTaTTvaDoc().getCodeTTvaDoc().equals("HUE")
//			)
//				setGestionTVA(false);
//			else setGestionTVA(true);
//		}else
//			setGestionTVA(true);
	}

	

	
	/*
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 * *********************************************************************************************************************************
	 */
//	public Integer typeLigne(String CodeTypeLigne) throws SQLException{		
//		Integer idTLigne;
//		return  idTLigne =LibConversion.stringToInteger(ibApplication.selectCleEtrangere(Const.C_NOM_TA_T_LIGNE,
//				Const.C_ID_T_LIGNE, Variant.STRING, Const.C_CODE_T_LIGNE, CodeTypeLigne));		
//	}

	@Override
	protected boolean beforeAjoutLigne(SWTLigneDocument ligne) {
		// TODO Raccord de méthode auto-généré
		return true;
	}

	@Override
	protected void afterAjoutLigne(SWTLigneDocument ligne) throws ExceptLgr {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		calculeTvaEtTotaux();
		
		reinitialiseNumLignes();
	}

	@Override
	protected boolean beforeRemoveLigne(SWTLigneDocument ligne) {
		// TODO Raccord de méthode auto-généré
		return true;
	}

	@Override
	protected void afterRemoveLigne(SWTLigneDocument ligne) throws ExceptLgr {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		calculeTvaEtTotaux();
		
		reinitialiseNumLignes();
	}

	@Transient
	public boolean isGestionTVA() {
		return gestionTVA;
	}

	public void setGestionTVA(boolean gestionTVA) {
		this.gestionTVA = gestionTVA;
	}

	@Transient
	public ArrayList<LigneTva> getLignesTVA() {
		return lignesTVA;
	}

	public void setLignesTVA(ArrayList<LigneTva> lignesTVA) {
		this.lignesTVA = lignesTVA;
	}

	@Override
	protected boolean beforeEnregistrerEntete() {
		// TODO Raccord de méthode auto-généré
		return false;
	}

	@Override
	protected void afterEnregistrerEntete() throws ExceptLgr {
		// TODO Raccord de méthode auto-généré

	}

	@Override
	protected boolean beforeModifierEntete() {
		// TODO Raccord de méthode auto-généré
		return false;
	}

	@Override
	protected void afterModifierEntete() throws ExceptLgr {
		// TODO Raccord de méthode auto-généré

	}

	@Override
	protected boolean beforeSupprimerEntete() {
		// TODO Raccord de méthode auto-généré
		return false;
	}

	@Override
	protected void afterSupprimerEntete() throws ExceptLgr {
		// TODO Raccord de méthode auto-généré

	}



	public void changementMode(ChangeModeEvent evt) {
		// TODO Raccord de méthode auto-généré
		switch (evt.getNouveauMode()) {
		case C_MO_CONSULTATION:
			break;
		case C_MO_EDITION:
			//S'il n'existe pas déjà, charger un objet swtArticle pour la ligne
			break;
		case C_MO_INSERTION:
//			S'il n'existe pas déjà, charger un objet swtArticle pour la ligne
			break;
		case C_MO_SUPPRESSION:
			break;
		}

	}
	
	//@Transient
	@Column(name = "mt_ttc_calc", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ttc_calc",table = "ta_ticket_caisse",champEntite="mtTtcCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getMtTtcCalc() {
		return mtTtcCalc;
	}

	public void setMtTtcCalc(BigDecimal mtTtcCalc) {
		this.mtTtcCalc = LibCalcul.arrondi(mtTtcCalc);
	}
	
	//@Transient
	@Column(name = "mt_ht_calc", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ht_calc",table = "ta_ticket_caisse",champEntite="mtHtCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getMtHtCalc() {
		return mtHtCalc;
	}

	public void setMtHtCalc(BigDecimal mtHtCalc) {
		this.mtHtCalc = LibCalcul.arrondi(mtHtCalc);
	}
	
	//@Transient
	@Column(name = "mt_tva_calc", precision = 15)
	@LgrHibernateValidated(champBd = "mt_tva_calc",table = "ta_ticket_caisse",champEntite="mtTvaCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getMtTvaCalc() {
		return mtTvaCalc;
	}

	public void setMtTvaCalc(BigDecimal mtTvaCalc) {
		this.mtTvaCalc = LibCalcul.arrondi(mtTvaCalc);
	}
	
	//@Transient
	@Column(name = "net_ttc_calc", precision = 15)
	@LgrHibernateValidated(champBd = "net_ttc_calc",table = "ta_ticket_caisse",champEntite="netTtcCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}

	public void setNetTtcCalc(BigDecimal netTtcCalc) {
		this.netTtcCalc = LibCalcul.arrondi(netTtcCalc);
	}
	
	//@Transient
	@Column(name = "net_ht_calc", precision = 15)
	@LgrHibernateValidated(champBd = "net_ht_calc",table = "ta_ticket_caisse",champEntite="netHtCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getNetHtCalc() {
		return netHtCalc;
	}

	public void setNetHtCalc(BigDecimal netHtCalc) {
		this.netHtCalc = LibCalcul.arrondi(netHtCalc);
	}
	
	//@Transient
	@Column(name = "net_tva_calc", precision = 15)
	@LgrHibernateValidated(champBd = "net_tva_calc",table = "ta_ticket_caisse",champEntite="netTvaCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getNetTvaCalc() {
		return netTvaCalc;
	}

	public void setNetTvaCalc(BigDecimal netTvaCalc) {
		this.netTvaCalc = LibCalcul.arrondi(netTvaCalc);
	}
	
	//@Transient
	@Column(name = "net_a_payer", precision = 15)
	@LgrHibernateValidated(champBd = "net_a_payer",table = "ta_ticket_caisse",champEntite="netTtcCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getNetAPayer() {
		return netTtcCalc;
		//return netAPayer;
	}

	public void setNetAPayer(BigDecimal netAPayer) {
		this.netAPayer = LibCalcul.arrondi(netAPayer);
	}
	
	//@Transient
	@Column(name = "mt_ttc_avt_rem_globale_calc", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ttc_avt_rem_globale_calc",table = "ta_ticket_caisse",champEntite="mtTtcAvantRemiseGlobaleCalc", clazz = TaTicketDeCaisse.class)
	public BigDecimal getMtTtcAvantRemiseGlobaleCalc() {
		return mtTtcAvantRemiseGlobaleCalc;
	}

	public void setMtTtcAvantRemiseGlobaleCalc(
			BigDecimal mtTtcAvantRemiseGlobaleCalc) {
		this.mtTtcAvantRemiseGlobaleCalc = mtTtcAvantRemiseGlobaleCalc;
	}



	//@Column(name = "reste_a_regler", precision = 15)
	@LgrHibernateValidated(champBd = "reste_a_regler",table = "ta_ticket_caisse",champEntite="resteARegler", clazz = TaTicketDeCaisse.class)
	@Transient
	public BigDecimal getResteAReglerEcran() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		this.resteARegler= netTtcCalc.subtract(getAcomptes().add(getAvoirs()).add(calculSommeReglementsIntegresEcran()));
		
		return resteARegler;
	}

	@Transient
	public BigDecimal getResteARegler() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		this.resteARegler= netTtcCalc.subtract(getAcomptes().add(getAvoirs()).add(calculSommeReglementsIntegres()));
		
		return resteARegler;
	}

	public void setResteARegler(BigDecimal resteARegler) {
		this.resteARegler = resteARegler;
	}

	@Column(name = "acomptes", precision = 15)
	@LgrHibernateValidated(champBd = "acomptes",table = "ta_ticket_caisse",champEntite="", clazz = TaTicketDeCaisse.class)
	public BigDecimal getAcomptes() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		calculSommeAcomptes();
		
		if(acomptes==null)return new BigDecimal(0);
		else return acomptes;
	}
	
	
	public void setAcomptes(BigDecimal acomptes) {
		//Correction bug #1259
		if(acomptes==null || this.acomptes==null || this.acomptes.compareTo(acomptes)!=0){
//		if(this.acomptes.compareTo(acomptes)!=0){
			this.acomptes = acomptes;
			if(legrain) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaFactureService
				calculTotaux();
				
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}


//	@Column(name = "avoirs", precision = 15)
//	@LgrHibernateValidated(champ = "avoirs",table = "ta_ticket_caisse",clazz = TaTicketDeCaisse.class)
	@Transient
	public BigDecimal getAvoirs() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		return calculSommeAvoirIntegres();
//		return this.avoirs;
		
//		 return this.avoirs;
	}
	
	public void setAvoirs(BigDecimal avoirs) {
		//Correction bug #1259
		if(avoirs==null || this.avoirs==null || this.avoirs.compareTo(avoirs)!=0){
//		if(this.acomptes.compareTo(acomptes)!=0){
			this.avoirs = avoirs;
			if(legrain) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaFactureService
				calculTotaux();
				
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}
	
	@Transient
	public BigDecimal getAvoirsComplet() {
		return calculSommeAvoir();
		//return this.avoirsComplet;
	}
	
	public void setAvoirsComplet(BigDecimal avoirsComplet) {
		this.avoirsComplet = avoirsComplet;
	}

	
//	@Column(name = "libelle_paiement", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_paiement",table = "ta_ticket_caisse",champEntite="libellePaiement", clazz = TaTicketDeCaisse.class)
	@Transient
	public String getLibellePaiement() {
		if(getTaRReglement()!=null&& getTaRReglement().getTaReglement()!=null)
			return getTaRReglement().getTaReglement().getLibelleDocument();
		else
		return "";
	}

	@Transient
	public void setLibellePaiement(String libellePaiement) {
		if(getLibellePaiement()==null||libellePaiement==null ||
				getLibellePaiement().compareTo(libellePaiement)!=0){
			getTaRReglement().getTaReglement().setLibelleDocument(libellePaiement);
			//this.libellePaiement = libellePaiement;		
			if(legrain) {
				try {
					fireModificationDocument(new SWTModificationDocumentEvent(this));
				} catch (Exception e) {
					logger.error("",e);
				}
			}
		}
	}		

	@Transient
	@XmlTransient
	public TaRReglement getTaRReglement() {
		return taRReglement;
	}

	public void setTaRReglement(TaRReglement taRReglement) {
		this.taRReglement = taRReglement;
	}

	//passage ejb => dans TaFactureService
	/**
	 * Repartir le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva() {
		
		BigDecimal tvaLigne = new BigDecimal(0); //Montant de TVA de la ligne du document courante
		BigDecimal totalTemp = new BigDecimal(0); //Somme des montants HT des lignes du document (mis à jour au fil des iterations)

		boolean derniereLignePourTVA = false;

		for (Object ligne : lignes) {
			if(((TaLTicketDeCaisse)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLTicketDeCaisse)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && txRemHtDocument!=null)
			setRemHtDocument(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLTicketDeCaisse ligne : getLignes()) {
			if(txRemHtDocument!=null && txRemHtDocument.signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(ttc==1){
					((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
							.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtHtLDocument()
							.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument());	
				}
			}
		}
		
		
		//pour chaque ligne/code TVA
		for (LigneTva ligneTva : lignesTVA) { 

			if (ligneTva.getMtTva()!=null) {
				int lignepasse=1;
				BigDecimal tvaTmp = ligneTva.getMtTva(); //montant total de la TVA pour cette ligne/code TVA décrémenter du montant de TVA des lignes du documents deja traite
				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());
				BigDecimal tvaCalcule = new BigDecimal(0);
				
				//TaLTicketDeCaisse derniereLigneFactureAvecMontantDifferentDeZero = null;
				derniereLignePourTVA = false;

				//pour chaque ligne du document
				for (Object ligne : lignes) {
					//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						//si le code TVA de la ligne correspond à celui traite (boucle superieure)
						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							tvaLigne = prorataMontantTVALigne((TaLTicketDeCaisse)ligne, ligneTva);
							
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							if(tvaTmp.compareTo(resteTVA(ligneTva))==0 && !derniereLignePourTVA) {
								//Le reste de TVA a traiter correspond a la difference d'arrondi,
								//les lignes de documents suivantes (s'il en reste) ont un montant HT nul
								//c'est donc la derniere ligne sur laquelle on peut mettre de la TVA => on ajoute le reliquat
								tvaLigne = tvaLigne.add(tvaTmp);
								derniereLignePourTVA = true;
							}
							totalTemp = totalTemp.add(((TaLTicketDeCaisse)ligne).getMtHtLDocument());

							//===Correction des totaux après remise de la ligne du document
							if(txRemHtDocument!=null && txRemHtDocument.signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									if(ttc==1){
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
												.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument().divide(BigDecimal.valueOf(1).add(
												 (((TaLTicketDeCaisse)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
											)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
										
									}else{
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtHtLDocument()
												.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
										tvaCalcule = (((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument().
										multiply(((TaLTicketDeCaisse)ligne).getTauxTvaLDocument().divide(new BigDecimal(100),MathContext.DECIMAL128))).setScale(2,BigDecimal.ROUND_HALF_UP);
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument().add(tvaCalcule));	
									}
//									((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()
//											.multiply(txRemHtDocument).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}

							} else {
								if(ttc==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLTicketDeCaisse)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLTicketDeCaisse)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							if(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
								ttcTmp =  ttcTmp.subtract(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument());
							if(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
								htTmp =  htTmp.subtract(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument());

							lignepasse++;
						}
					}
					setRemHtDocument(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));						

////					setRemHtDocument(getRemHtDocument().add(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100)))));						

				}
			}

		}


	}
	
	/**
	 * Calcule le montant de TVA d'une ligne du document par rapport au montant total de TVA pour un code TVA donnee
	 * @param ligne - 
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal prorataMontantTVALigne(TaLTicketDeCaisse ligne, LigneTva ligneTva) {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		if (ligneTva.getMontantTotalHt().signum()==0) 
			tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
		else {
//			if  (lignepasse>= ligneTva.getNbLigneDocument()) //si c'est la deniere ligne, on prend tout ce qui reste
//				tvaLigne = tvaTmp;
//			else {
				if(ttc==1){ //si saisie TTC
					if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
						tvaLigne=BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.
								arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				else{
					if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
						tvaLigne =BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTva().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.
								arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
//			}
		}
		return tvaLigne;
	}
	
	/**
	 * Calcule le montant de TVA d'une ligne du document par rapport au montant total de TVA pour un code TVA donnee <b>avant remise</b>
	 * @param ligne
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal prorataMontantTVALigneAvantRemise(TaLTicketDeCaisse ligne, LigneTva ligneTva) {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		if (ligneTva.getMontantTotalHt().signum()==0) 
			tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
		else {
//			if  (lignepasse>= ligneTva.getNbLigneDocument()) 
//				tvaLigne = tvaAvantRemiseTmp;
//			else {
				if(ttc==1){
					if(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()).signum()<=0)
						tvaLigne=BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				else{
					if(LibCalcul.arrondi(ligneTva.getMontantTotalHt()).signum()<=0)
						tvaLigne =BigDecimal.valueOf(0);
					else
						tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHt()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
//			}
		}
		return tvaLigne;
	}
	
	/**
	 * Calcule le montant de TVA qui reste après répartion de la TVA sur les lignes au prorata du monant HT.
	 * Ce montant de TVA restant de 1 ou 2 centimes provient des arrondis successifs.
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal resteTVAAvantRemise(LigneTva ligneTva) {
		BigDecimal resteTVA = ligneTva.getMtTva();
		for (Object ligne : lignes) {
			//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
			if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
				//si le code TVA de la ligne correspond à celui traite (boucle superieure)
				if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
					resteTVA = resteTVA.subtract(prorataMontantTVALigneAvantRemise(((TaLTicketDeCaisse)ligne),ligneTva));
				}
			}
		}
		return resteTVA;
	}
	
	/**
	 * Calcule le montant de TVA qui reste après répartion de la TVA sur les lignes au prorata du monant HT.
	 * Ce montant de TVA restant de 1 ou 2 centimes provient des arrondis successifs.
	 * @param ligneTva
	 * @return
	 */
	public BigDecimal resteTVA(LigneTva ligneTva) {
		BigDecimal resteTVA = ligneTva.getMtTva();
		for (Object ligne : lignes) {
			//si c'est une ligne "normale" (ligne HT et non une ligne de commentaire ou autre)
			if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
				//si le code TVA de la ligne correspond à celui traite (boucle superieure)
				if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
					resteTVA = resteTVA.subtract(prorataMontantTVALigne(((TaLTicketDeCaisse)ligne),ligneTva));
				}
			}
		}
		return resteTVA;
	}


	
	public void dispatcherTvaAvantRemise() {
		BigDecimal tvaLigne = new BigDecimal(0);
		
		boolean derniereLignePourTVA = false;

		for (LigneTva ligneTva : lignesTVA) {
			if (ligneTva.getMtTvaAvantRemise()!=null) {
				int lignepasse=1;
				BigDecimal tvaAvantRemiseTmp = ligneTva.getMtTvaAvantRemise();
				
				derniereLignePourTVA = false;

				for (Object ligne : lignes) {
					if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)&& 
							((TaLTicketDeCaisse)ligne).getMtHtLDocument()!=null && ((TaLTicketDeCaisse)ligne).getMtHtLDocument().signum()!=0) {
						if(((TaLTicketDeCaisse)ligne).getCodeTvaLDocument()!=null&&((TaLTicketDeCaisse)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
//							if (ligneTva.getMontantTotalHt().signum()==0) 
//								tvaLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
//							else {
//								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
//									tvaLigne = tvaAvantRemiseTmp;
//								else {
//									if(ttc==1){
//										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()).signum()<=0)
//											tvaLigne=BigDecimal.valueOf(0);
//										else
//											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtc()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
//									}
//									else{
//										if(LibCalcul.arrondi(ligneTva.getMontantTotalHt()).signum()<=0)
//											tvaLigne =BigDecimal.valueOf(0);
//										else
//											tvaLigne = (ligneTva.getMtTvaAvantRemise().multiply(((TaLTicketDeCaisse)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHt()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
//									}
//								}
//							}
							tvaLigne = prorataMontantTVALigneAvantRemise((TaLTicketDeCaisse)ligne, ligneTva);
							
							tvaAvantRemiseTmp =  tvaAvantRemiseTmp.subtract(tvaLigne);
							
							if(tvaAvantRemiseTmp.compareTo(resteTVA(ligneTva))==0) {
								//Le reste de TVA a traiter correspond a la difference d'arrondi,
								//les lignes de documents suivantes (s'il en reste) ont un montant HT nul
								//c'est donc la derniere ligne sur laquelle on peut mettre de la TVA => on ajoute le reliquat
								tvaLigne = tvaLigne.add(tvaAvantRemiseTmp);
								derniereLignePourTVA = true;
							}

							if(ttc==1)
								((TaLTicketDeCaisse)ligne).setMtHtLDocument(((TaLTicketDeCaisse)ligne).getMtTtcLDocument().subtract(tvaLigne));
							else
								((TaLTicketDeCaisse)ligne).setMtTtcLDocument(((TaLTicketDeCaisse)ligne).getMtHtLDocument().add(tvaLigne));

							lignepasse++;
						}
					}

				}
			}
		}
	}
	

	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument() {
		for (Object ligne : lignes) {
			((TaLTicketDeCaisse)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal() {
		calculMontantLigneDocument();
		calculLignesTva();
		dispatcherTvaAvantRemise();
		dispatcherTva();
	}

	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva() {
		Map<String,BigDecimal> montantTotalHt = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtc = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalHtAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtcAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVA = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVAAvantRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> tauxTVA = new HashMap<String,BigDecimal>();
		Map<String,Integer> nbLigne = new HashMap<String,Integer>();
		Map<String,String> libelleLignesTVA = new HashMap<String,String>();
		String codeTVA = null;
//		TaTvaDAO taTvaDAO = new TaTvaDAO();
		
		/*
		 * calcul de la TVA different en fonction de la propriete TTC
		 */
		BigDecimal ttcLigne = null;
		BigDecimal htLigne = null;
		for (Object ligne : lignes) {
			//en commentaire pour ne pas refaire les calculs pendants les editions, 
			//((TaLTicketDeCaisse)ligne).calculMontant();
			codeTVA = ((TaLTicketDeCaisse)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLTicketDeCaisse)ligne).getMtTtcLDocument();
				htLigne = ((TaLTicketDeCaisse)ligne).getMtHtLDocument();
				if(montantTotalHt.containsKey(codeTVA)) {
					montantTotalTtc.put(codeTVA,montantTotalTtc.get(codeTVA).add(ttcLigne));
					montantTotalHt.put(codeTVA,montantTotalHt.get(codeTVA).add(htLigne));
					montantTotalTtcAvecRemise.put(codeTVA,montantTotalTtcAvecRemise.get(codeTVA).add(ttcLigne));
					montantTotalHtAvecRemise.put(codeTVA,montantTotalHtAvecRemise.get(codeTVA).add(htLigne));
					nbLigne.put(codeTVA,nbLigne.get(codeTVA)+1);
				} else {
					montantTotalTtc.put(codeTVA,ttcLigne);
					montantTotalHt.put(codeTVA,htLigne);
					montantTotalTtcAvecRemise.put(codeTVA,ttcLigne);
					montantTotalHtAvecRemise.put(codeTVA,htLigne);
					tauxTVA.put(codeTVA,((TaLTicketDeCaisse)ligne).getTauxTvaLDocument());
					nbLigne.put(codeTVA,1);
					libelleLignesTVA.put(codeTVA, ((TaLTicketDeCaisse)ligne).getLibTvaLDocument());
//					libelleLignesTVA.put(codeTVA, ((TaLTicketDeCaisse)ligne).getLibLDocument());
				}
			}
		}

		for (String codeTva : montantTotalTtc.keySet()) {
			//les 2 maps ont les meme cles
			BigDecimal mtTtcTotal = montantTotalTtc.get(codeTva);
			BigDecimal mtHtTotal = montantTotalHt.get(codeTva);
			BigDecimal tva =null;
			//traitement tva avant remise
			if (ttc==1) {
				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
				mtTVAAvantRemise.put(codeTva, tva);
			} else {
				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
				mtTVAAvantRemise.put(codeTva, tva );
			}
			//traitement remise
			if(txRemHtDocument!=null && txRemHtDocument.signum()>0) {
//				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(     mtTtcTotal.multiply(   txRemHtDocument.divide(new BigDecimal(100))  )       ));
//				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract(    mtHtTotal.multiply( (txRemHtDocument.divide(new BigDecimal(100))))     ) ) ;
				BigDecimal valeurInterTTC=mtTtcTotal.multiply(   txRemHtDocument.divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(valeurInterTTC )) ;
				BigDecimal valeurInterHT=mtHtTotal.multiply( txRemHtDocument.divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract( valeurInterHT )) ;
				montantTotalTtcAvecRemise.put(codeTva, mtTtcTotal);
				montantTotalHtAvecRemise.put(codeTva, mtHtTotal);
			} 
			//traitement tva après remise
			if (ttc==1) {
				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
				mtTVA.put(codeTva, tva);
				montantTotalHtAvecRemise.put(codeTva, mtTtcTotal.subtract(tva));
			} else {
				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
				mtTVA.put(codeTva, tva );
				montantTotalTtcAvecRemise.put(codeTva, mtHtTotal.add(tva));
			}
		}

		lignesTVA.clear();
		for (String codeTva : mtTVA.keySet()) {
			LigneTva ligneTva = new LigneTva();
			ligneTva.setCodeTva(codeTva);
			ligneTva.setTauxTva(tauxTVA.get(codeTva));
			ligneTva.setMtTva(mtTVA.get(codeTva));
			ligneTva.setMtTvaAvantRemise(mtTVAAvantRemise.get(codeTva));
			ligneTva.setMontantTotalHt(montantTotalHt.get(codeTva));
			ligneTva.setMontantTotalTtc(montantTotalTtc.get(codeTva));
			ligneTva.setMontantTotalHtAvecRemise(montantTotalHtAvecRemise.get(codeTva));
			ligneTva.setMontantTotalTtcAvecRemise(montantTotalTtcAvecRemise.get(codeTva));
//			ligneTva.setLibelle(taTvaDAO.findByCode(codeTva).getLibelleTva());
			ligneTva.setLibelle(libelleLignesTVA.get(codeTva));
			ligneTva.setNbLigneDocument(nbLigne.get(codeTva));
			lignesTVA.add(ligneTva);
		}
		
		//dispatcherTva();
	}
	
	
	public BigDecimal calculSommeAvoirIntegres(){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && (taRAvoir.getEtat()&IHMEtat.integre)!=0 && 
					(taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		//setAvoirs(sommeAvoir);
		return sommeAvoir;
	}
	
	public BigDecimal calculSommeAvoir(){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && (taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		//setAvoirs(sommeAvoir);
		return sommeAvoir;
	}
	public BigDecimal calculSommeAvoir(TaRAvoir avoirEnCours){
		BigDecimal sommeAvoir = new BigDecimal(0);
		for (TaRAvoir taRAvoir : getTaRAvoirs()) {
			if(taRAvoir.getTaAvoir()!=null && taRAvoir.getId()!=avoirEnCours.getId() && (taRAvoir.getEtat()&IHMEtat.suppression)==0)
				sommeAvoir=sommeAvoir.add(taRAvoir.getAffectation());
		}
		return sommeAvoir;
	}
	public BigDecimal calculSommeAcomptes(TaRAcompte acompteEnCours){
		BigDecimal sommeAcompte = new BigDecimal(0);
		for (TaRAcompte taRAcompte : getTaRAcomptes()) {
			if(taRAcompte.getTaAcompte()!=null && taRAcompte.getId()!=acompteEnCours.getId() && !taRAcompte.isEtatDeSuppression())
				sommeAcompte=sommeAcompte.add(taRAcompte.getAffectation());
		}
		return sommeAcompte;
	}
	
	public void calculSommeAcomptes(){
		BigDecimal sommeAcompte = new BigDecimal(0);
		for (TaRAcompte taRAcompte : getTaRAcomptes()) {
			if(taRAcompte.getTaAcompte()!=null && !taRAcompte.isEtatDeSuppression())
				sommeAcompte=sommeAcompte.add(taRAcompte.getAffectation());
		}
		setAcomptes(sommeAcompte);
	}
	
	public BigDecimal calculSommeReglementsIntegresEcran(){
		Integer nbReglement=0;
		BigDecimal sommeReglements = new BigDecimal(0);
		if(this.getTaReglement()!=null){
			sommeReglements=this.getTaReglement().getNetTtcCalc();
			nbReglement=1;
		}else 
			if(getTaRReglements()!=null && getTaRReglements().size()>0){			
		for (TaRReglement taRReglement : getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null && (taRReglement.getEtat()&IHMEtat.integre)!=0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				sommeReglements=sommeReglements.add(taRReglement.getAffectation());
			nbReglement++;
		}
		if(taRReglement!=null && !getTaRReglements().contains(taRReglement)&& 
				!multiReglement())//((taRReglement.getEtatDeSuppression()&IHMEtat.multiple)==0)
			if(taRReglement.getAffectation()!=null){
				sommeReglements=sommeReglements.add(taRReglement.getAffectation());
				nbReglement++;
			}
		}
		logger.debug(sommeReglements);
		logger.debug(nbReglement);	
		return sommeReglements;
}
	
	public BigDecimal calculSommeReglementsIntegres(){
		Integer nbReglement=0;
		BigDecimal sommeReglements = new BigDecimal(0);
		if(this.getTaReglement()!=null){
			sommeReglements=this.getTaReglement().getNetTtcCalc();
			nbReglement=1;
		}else{
			for (TaRReglement taRReglement : getTaRReglements()) {
				if(taRReglement.getTaTicketDeCaisse()!=null && taRReglement.getEtat()!=null && (taRReglement.getEtat()&IHMEtat.integre)!=0 
						&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
					sommeReglements=sommeReglements.add(taRReglement.getAffectation());
				nbReglement++;
			}
		}
		logger.debug(sommeReglements);
		logger.debug(nbReglement);
		return sommeReglements;
	}
	public Boolean aDesReglementsIndirects(){
		for (TaRReglement taRReglement : getTaRReglements()) {
			if(taRReglement.getTaTicketDeCaisse()!=null 
					&& (taRReglement.getEtat()&IHMEtat.integre)==0 
					&& ((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
				return true;
		}
		return false;
	}
	
	public Boolean aDesAvoirsIndirects(){
		for (TaRAvoir taRavoir : getTaRAvoirs()) {
			if(taRavoir.getTaTicketDeCaisse()!=null 
					&& (taRavoir.getEtat()&IHMEtat.integre)==0 
					&& ((taRavoir.getEtat()&IHMEtat.suppression)==0))
				return true;
		}
		return false;
	}
	
	public BigDecimal calculSommeReglementsComplet(){
		BigDecimal sommeReglements = new BigDecimal(0);
		if(this.getTaReglement()!=null){
			sommeReglements=this.getTaReglement().getNetTtcCalc();
		}else{
			for (TaRReglement taReglement : getTaRReglements()) {
				if(taReglement.getTaTicketDeCaisse()!=null 
						//					&& (taReglement.getTaReglement().getEtat()&IHMEtat.integre)==0 
						&& ((taReglement.getEtatDeSuppression()&IHMEtat.suppression)==0))
					//if(taReglement.getAffectation()!=null)
					sommeReglements=sommeReglements.add(taReglement.getAffectation());
			}
		}
		return sommeReglements;
	}
	
	public BigDecimal calculSommeReglementsComplet(TaRReglement taRReglementEnCours){
		BigDecimal sommeReglements = new BigDecimal(0);
		if(this.getTaReglement()!=null){
			sommeReglements=this.getTaReglement().getNetTtcCalc();
		}else{
			for (TaRReglement taReglement : getTaRReglements()) {
				if(taReglement.getTaTicketDeCaisse()!=null 
						//					&& (taReglement.getTaReglement().getEtat()&IHMEtat.integre)==0 
						&& ((taReglement.getEtatDeSuppression()&IHMEtat.suppression)==0) 
						&& taRReglementEnCours.getId()!=taReglement.getId())
					//if(taReglement.getAffectation()!=null)
					sommeReglements=sommeReglements.add(taReglement.getAffectation());
			}
		}
		return sommeReglements;
	}
	
	public BigDecimal calculRegleDocumentComplet(){
//		setRegleDocument(calculSommeReglementsIntegres().add(calculSommeReglementsNonIntegres()));
		regleCompletDocument=calculSommeReglementsComplet().add(getAcomptes().add(calculSommeAvoir()));
		setResteAReglerComplet(getNetTtcCalc().subtract(regleCompletDocument));
		return this.regleCompletDocument;
	}
	
	public void calculRegleDocument(){
		if(this.getTaReglement()!=null){
			setRegleDocument(calculSommeReglementsIntegresEcran().add(getAcomptes().add(getAvoirs())));
			setResteARegler(getNetTtcCalc().subtract(regleDocument));
		}else{
			setRegleDocument(calculSommeReglementsIntegresEcran().add(getAcomptes().add(getAvoirs())));
			setResteARegler(getNetTtcCalc().subtract(getRegleDocument()));
		}
	}
	public BigDecimal calculResteAReglerComplet(){
//		setRegleCompletDocument(calculSommeReglementsComplet());
		resteAReglerComplet=getNetTtcCalc().subtract(calculSommeReglementsComplet().add(getAcomptes().add(calculSommeAvoir())));
		return resteAReglerComplet;
	}
	public boolean multiReglement(){
		int nb=0;
		for (TaRReglement taReglement : getTaRReglements()) {
			if (((taReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
					&& (taReglement.getEtat()&IHMEtat.integre)!=0)
			{
				nb++;
			}
		}		
		return nb>1;
	}
	
	public boolean multiReglementReel(){
		int nb =0;
		nb=getTaRReglements().size()+getTaRAvoirs().size();
		return nb>1;
	}
	
	public boolean reglementExistant(){
		boolean retour=false;
		for (TaRReglement rr : getTaRReglements()) {
			retour=rr.getId()!=0;
			if(retour)return retour;
		};
		for (TaRAvoir rr : getTaRAvoirs()) {
			retour=rr.getId()!=0;
			if(retour)return retour;
		};
		return retour;
	}
	
	@Transient
	public BigDecimal getRegleCompletDocument() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		regleCompletDocument = calculSommeReglementsComplet().add(getAcomptes().add(calculSommeAvoir()));
		setResteAReglerComplet(getNetTtcCalc().subtract(regleCompletDocument).subtract(getAcomptes()).subtract(calculSommeAvoir()));		
		
		return regleCompletDocument;
	}

	@Transient
	public BigDecimal getRegleCompletDocument(TaRReglement taRReglementEnCours) {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		regleCompletDocument = calculSommeReglementsComplet(taRReglementEnCours).add(getAcomptes().add(calculSommeAvoir()));
		setResteAReglerComplet(getNetTtcCalc().subtract(regleCompletDocument).subtract(getAcomptes()).subtract(calculSommeAvoir()));		
		
		return regleCompletDocument;
	}
	
	@Transient
	public BigDecimal getRegleCompletDocument(TaRAvoir taRAvoirEnCours) {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		regleCompletDocument = calculSommeAvoir(taRAvoirEnCours).add(getAcomptes().add(calculSommeReglementsComplet()));
		setResteAReglerComplet(getNetTtcCalc().subtract(regleCompletDocument).subtract(getAcomptes()).subtract(calculSommeReglementsComplet()));		
		
		return regleCompletDocument;
	}
	
	public void setRegleCompletDocument(BigDecimal regleCompletDocument) {
		this.regleCompletDocument = regleCompletDocument;
	}
	
	
	@Transient
	public BigDecimal getResteAReglerComplet() {
		
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
		//passage ejb => dans TaFactureService
		resteAReglerComplet = getNetTtcCalc().subtract(calculSommeReglementsComplet().add(getAcomptes().add(calculSommeAvoir())));
		
		return resteAReglerComplet;
	}

	public void setResteAReglerComplet(BigDecimal resteAReglerComplet) {
		this.resteAReglerComplet = resteAReglerComplet;
	}
	
	//passage ejb => dans TaFactureService
//	public void affecteReglementFacture(String typePaiementDefaut) throws Exception{
//		setTaRReglement(creeReglement(null,null,true,typePaiementDefaut));
//		getTaRReglement().setEtatDeSuppression(IHMEtat.insertion);
////		getTaRReglement().setEtat(IHMEtat.integre);
//		if(!multiReglement()){
//			for (TaRReglement taReglement : getTaRReglements()) {
//				if (((taReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
//						&& (taReglement.getTaReglement().getEtat()&IHMEtat.integre)!=0)
//					setTaRReglement(taReglement);				
//			}			
//		}else{
//			setTaRReglement(creeReglement(null,null,false,typePaiementDefaut));
//			taRReglement.getTaReglement().setTaTPaiement(null);
//			taRReglement.setAffectation(calculSommeReglementsIntegresEcran());
//			taRReglement.getTaReglement().setLibelleDocument("Réglements multiples");
//			taRReglement.setEtatDeSuppression(IHMEtat.multiple);
//			taRReglement.getTaReglement().setEtat(0);
//		}
////		mettreAJourDateEcheanceReglement(taRReglement.getTaReglement());
//		taRReglement.getTaReglement().setTaTiers(this.taTiers);
//		taRReglement.setTaFacture(this);
//		
//	}
	
	public void modifieLibellePaiementMultiple(){
		String libelleValide="";
		for (TaRReglement taRReglement : getTaRReglements()) {
			if (((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)&& (taRReglement.getEtat()&IHMEtat.integre)!=0)
			{
				if(!multiReglement() && (taRReglement.getTaReglement().getLibelleDocument()!=null&&
						taRReglement.getTaReglement().getLibelleDocument().equals("Multiples réglements")))
					libelleValide="";
				else if(!multiReglement())libelleValide=taRReglement.getTaReglement().getLibelleDocument();
				else libelleValide="Multiples réglements";
			}
		}
		if(getTaRReglement()!=null && getTaRReglement().getTaReglement()!=null) 
			getTaRReglement().getTaReglement().setLibelleDocument(libelleValide);		
	}
	
	public void modifieTypePaiementMultiple(){
		TaTPaiement taTPaiement=null;
		for (TaRReglement taRReglement : getTaRReglements()) {
			if (((taRReglement.getEtatDeSuppression()&IHMEtat.suppression)==0)
					&& (taRReglement.getEtat()&IHMEtat.integre)!=0)
			{
				if(!multiReglement()) taTPaiement=taRReglement.getTaReglement().getTaTPaiement();
			}
		}
		if(getTaRReglement()!=null && getTaRReglement().getTaReglement()!=null) 
			getTaRReglement().getTaReglement().setTaTPaiement(taTPaiement);
	}
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux() {
		
//			    MT_TVA Numeric(15,2),
			setMtHtCalc(new BigDecimal(0));
			setNetHtCalc(new BigDecimal(0));
			setMtTtcCalc(new BigDecimal(0));
			setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : lignes) {
				if(((TaLTicketDeCaisse)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H) && ((TaLTicketDeCaisse)ligne).getTaArticle()!=null) {
					if(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument()!=null) {
						setNetHtCalc(getNetHtCalc().add(((TaLTicketDeCaisse)ligne).getMtHtLApresRemiseGlobaleDocument()));
					}if(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						setMtTtcCalc(getMtTtcCalc().add(((TaLTicketDeCaisse)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLTicketDeCaisse)ligne).getMtHtLDocument()!=null)
						setMtHtCalc(getMtHtCalc().add(((TaLTicketDeCaisse)ligne).getMtHtLDocument()));
					if(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()!=null)
						setMtTtcAvantRemiseGlobaleCalc(getMtTtcAvantRemiseGlobaleCalc().add(((TaLTicketDeCaisse)ligne).getMtTtcLDocument()));
				}
				
			}
			setRemHtDocument(getMtHtCalc().subtract(getNetHtCalc())); // passage ejb 3/8/2016
			setNetTvaCalc(getMtTtcCalc().subtract(getNetHtCalc()));
			BigDecimal tva = new BigDecimal(0);
			for (LigneTva ligneTva : lignesTVA) {
				tva = tva.add(ligneTva.getMtTva());
			}
			if(tva.compareTo(getNetTvaCalc())!=0) {
				logger.error("Montant de la TVA incorrect : "+getNetTvaCalc()+" ** "+tva);
			}
			BigDecimal tvaAvantRemise = new BigDecimal(0);
			for (LigneTva ligneTva : lignesTVA) {
				tvaAvantRemise = tvaAvantRemise.add(ligneTva.getMtTvaAvantRemise());
			}
			setMtTvaCalc(tvaAvantRemise);
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getRemTtcFacture().divide(new BigDecimal(100)))));
			setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP)));
//			setNetTtcCalc(getMtTtcAvantRemiseGlobaleCalc().subtract(getMtTtcAvantRemiseGlobaleCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100)))));
			
			/*
			 * remise HT déjà calculée dans dispatcherTva()
			 */
			setRemTtcDocument(getMtTtcCalc().subtract(getNetTtcCalc()).setScale(2,BigDecimal.ROUND_HALF_UP));
			calculSommeAcomptes();
			//calculSommeAvoirIntegres();
			//modifié suite à changement écran le 23/04/2010 par isa
			//setNetAPayer(getNetTtcCalc().subtract(getRegleDocument()));
			setResteARegler(getNetTtcCalc().subtract(getRegleDocument()).subtract(getAcomptes()).subtract(calculSommeAvoirIntegres()));
			
	}
	
	public void calculeTvaEtTotaux(){
		calculTvaTotal();
		calculTotaux();
	}
	

//	public void removeTousRAcomptes() throws Exception{
//		List<TaRAcompte> listeTemp=new LinkedList<TaRAcompte>();
//		for (TaRAcompte element : getTaRAcomptes()) {
//			listeTemp.add(element);
//			element.getTaAcompte().removeRAcompte(element);
//		} 
//
//		for (TaRAcompte element : listeTemp) {
//			removeRAcompte(element);
//		}
//	}
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("qteLFacture")
				|| evt.getPropertyName().equals("qte2LFacture")
				|| evt.getPropertyName().equals("prixULFacture")
				|| evt.getPropertyName().equals("remTxLFacture")
				){
			
			System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
			//passage ejb => dans TaFactureService
			calculeTvaEtTotaux();
			
			try {
				fireModificationDocument(new SWTModificationDocumentEvent(this));
			} catch (Exception e) {
				logger.error("",e);
			}
		}
	}
	

	public void setLegrain(boolean legrain) {
		this.legrain = legrain;
		for (TaLTicketDeCaisse ligne : getLignes()) {
			ligne.setLegrain(legrain);
		}		
	}

	@Override
	protected void reinitialiseNumLignes() {
//		for (Object ligne : lignes) {
//			((TaLTicketDeCaisse)ligne).setNumLigneLDocument(
//					((TaLTicketDeCaisse) ligne).getNUM_LIGNE());
//		}
		
	}

	
//	public TaFacture clone() {
//		TaFacture doc = new TaFacture(true);
//		try {
//			//doc = (TaFacture)super.clone();
//			doc.setIdDocument(0);
//			doc.setVersion(version);
//			doc.setTaTPaiement(taTPaiement);
//			doc.setTaTiers(taTiers);
////			doc.setTaCPaiement(taCPaiement);
//			doc.setCodeDocument("");
//			doc.setDateDocument(dateDocument);
//			doc.setDateEchDocument(dateEchDocument);
//			doc.setDateLivDocument(dateLivDocument);
//			doc.setLibelleDocument("");
//			doc.setRegleDocument(regleDocument);
//			doc.setRemHtDocument(remHtDocument);
//			doc.setTxRemHtDocument(txRemHtDocument);
//			doc.setRemTtcDocument(remTtcDocument);
//			doc.setTxRemTtcDocument(txRemTtcDocument);
//			doc.setNbEDocument(nbEDocument);
//			doc.setTtc(ttc);
//			doc.setExport(export);
//			doc.setCommentaire(commentaire);
//			doc.setQuiCreeDocument(quiCreeDocument);
//			doc.setQuandCreeDocument(quandCreeDocument);
//			doc.setQuiModifDocument(quiModifDocument);
//			doc.setQuandModifDocument(quandModifDocument);
//			doc.setIpAcces(ipAcces);
//			doc.setVersionObj(versionObj);
//			
//			for (TaLTicketDeCaisse ligne : getLignes()) {
//				TaLTicketDeCaisse temp =ligne.clone(); 
//				temp.setTaDocument(doc);
//				doc.addLigne(temp);
//			}
//			TaInfosFacture infos = getTaInfosDocument().clone();
//			infos.setTaDocument(doc);
//			doc.setTaInfosDocument(infos);
//		} catch(Exception cnse) {
//			logger.error("",cnse);
//		}
//		// on renvoie le clone
//		return doc;
//	}


	//passage ejb => dans TaFactureService
	public void addRReglement(TaRReglement taReglement){
		if(!this.getTaRReglements().contains(taReglement)){
			//taReglement.setTaFacture(this);
			taReglement.setTaTicketDeCaisse(this);
			this.getTaRReglements().add(taReglement);	
		}
		//calculSommeAcomptes();
	}
	public void removeReglement(TaRReglement taRReglement){
		this.getTaRReglements().remove(taRReglement);
		
//		calculSommeAcomptes();
	}

//	public void removeTousRReglements(EntityManager em) throws Exception{
//		List<TaRReglement> listeTemp=new LinkedList<TaRReglement>();
//		for (TaRReglement element : getTaRReglements()) {
//			listeTemp.add(element);
////			removeReglement(element);
//			element.getTaReglement().removeReglement(element);
//			if(element.getTaReglement().getTaRReglements().size()==0)
//			{
//				new TaReglementDAO().supprimer(element.getTaReglement());
//			}
//		} 
//
//		for (TaRReglement element : listeTemp) {
//			removeReglement(element);
//		}
////		calculSommeAcomptes();
//	}
	

//	public void removeTousRAcomptes(EntityManager em) throws Exception{
//		List<TaRAcompte> listeTemp=new LinkedList<TaRAcompte>();
//		for (TaRAcompte element : getTaRAcomptes()) {
//			listeTemp.add(element);
//			element.getTaAcompte().removeRAcompte(element);
//		} 
//
//		for (TaRAcompte element : listeTemp) {
//			removeRAcompte(element);
//		}
//	}
	public void removeTousRAvoirs(EntityManager em) throws Exception{
		List<TaRAvoir> listeTemp=new LinkedList<TaRAvoir>();
		for (TaRAvoir element : getTaRAvoirs()) {
			listeTemp.add(element);
			element.getTaAvoir().removeRAvoir(element);
		} 

		for (TaRAvoir element : listeTemp) {
			removeRAvoir(element);
		}
	}
	
	public void gestionReglement(TaRReglement taRReglement){
		if(taRReglement.getAffectation()==null
				//||taReglement.getAffectation().compareTo(BigDecimal.valueOf(0))==0
				||(taRReglement.getEtat()&IHMEtat.integre)==0){
			removeReglement(taRReglement);
		}else
			if((taRReglement.getEtat()&IHMEtat.integre)!=0)
				addRReglement(taRReglement);
	}

	//passage ejb => dans TaFactureService
	public void addRAcompte(TaRAcompte taRAcompte){
		if(!this.getTaRAcomptes().contains(taRAcompte))
			this.getTaRAcomptes().add(taRAcompte);	
		calculSommeAcomptes();
	}
	public void removeRAvoir(TaRAvoir taRAvoir){
		this.getTaRAvoirs().remove(taRAvoir);
		calculSommeAvoir();
	}

	public void addRAvoir(TaRAvoir taRAvoir){
		if(!this.getTaRAvoirs().contains(taRAvoir))
			this.getTaRAvoirs().add(taRAvoir);	
		setAvoirs(calculSommeAvoir());
		
	}
	public void removeRAcompte(TaRAcompte taRAcompte){
		this.getTaRAcomptes().remove(taRAcompte);
		calculSommeAcomptes();
	}

	@Transient
	public String getTypeDocument() {
		return TYPE_DOC;
	}


	public void setTypeDocument(String typeDocument) {
		this.typeDocument=typeDocument;
	}

//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((codeDocument == null) ? 0 : codeDocument.hashCode());
//		return result;
//	}


	//passage ejb => dans TaFactureService
//	public TaRReglement creeReglement(EntityManager Em,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut) throws Exception{
//		if(Em==null)new TaRReglementDAO().getEntityManager();
////		EntityManager Em=new TaRReglementDAO().getEntityManager();
//		if(taRReglement!=null){
//			taRReglement.setTaFacture(this);
//			taRReglement.getTaReglement().setDateDocument(this.getDateDocument());
//			taRReglement.getTaReglement().setDateLivDocument(this.getDateEchDocument());
//			taRReglement.getTaReglement().setTaCompteBanque(new TaCompteBanqueDAO().findByTiersEntreprise(taRReglement.getTaReglement().getTaTPaiement()));
////			taReglement.setEtat(IHMEtat.integre);
//		}else{
//			Boolean devientMultiReglement=reglementExiste() &&
//				getTaRReglement().getTaReglement().getTaRReglements().size()>0 && integrer;
//			Integer report=0;
//			Integer finDeMois=0;
//			TaTPaiement typePaiement=null;
//			TaRReglement taRReglementTmp = new TaRReglement();
//			TaReglement taReglement = new TaReglement();
//			taRReglementTmp.setTaFacture(this);
//			taRReglementTmp.setTaReglement(taReglement);		
//			taRReglementTmp.getTaReglement().setDateDocument(this.getDateDocument());
//			taRReglementTmp.getTaReglement().setDateLivDocument(this.getDateEchDocument());
//			if(!devientMultiReglement && getTaRReglement()!=null && getTaRReglement().getTaReglement()!=null && 
//					getTaRReglement().getTaReglement().getTaTPaiement()!=null){
//				typePaiement=getTaRReglement().getTaReglement().getTaTPaiement();
//				report=getTaRReglement().getTaReglement().getTaTPaiement().getReportTPaiement();
//				finDeMois=getTaRReglement().getTaReglement().getTaTPaiement().getFinMoisTPaiement();
//			}				
//			else
//			if(this.taTiers!=null && this.taTiers.getTaTPaiement()!=null ){
//				typePaiement=this.taTiers.getTaTPaiement();
//					report=this.taTiers.getTaTPaiement().getReportTPaiement();
//					finDeMois=this.taTiers.getTaTPaiement().getFinMoisTPaiement();
//			}
//			
//			else {
//				TaTPaiementDAO taTPaiementDAO = new TaTPaiementDAO();
//				if (typePaiementDefaut == null || typePaiementDefaut=="")
//					typePaiementDefaut="C";
//				try {
//					typePaiement = taTPaiementDAO
//							.findByCode(typePaiementDefaut);
//
//				} catch (Exception e) {
//				}
//				//typePaiement=new TaTPaiementDAO(Em).findByCode("C");
//			}
//			if(taRReglementTmp.getTaReglement().getNetTtcCalc().signum()>0){	
//			taRReglementTmp.getTaReglement().setTaTPaiement(typePaiement);
//			if(taRReglementTmp.getTaReglement().getLibelleDocument()==null || 
//					taRReglementTmp.getTaReglement().getLibelleDocument().equals(""))
//			taRReglementTmp.getTaReglement().setLibelleDocument(typePaiement.getLibTPaiement());
//			}
//			//taRReglementTmp.getTaReglement().setDateLivDocument(this.getDateEchDocument());
//			//taRReglementTmp.getTaReglement().setDateLivDocument(calculDateEcheance(report,finDeMois,typePaiement));
//			
//			
//			taRReglementTmp.getTaReglement().setTaCompteBanque(new TaCompteBanqueDAO().findByTiersEntreprise(typePaiement));
//			taRReglementTmp.getTaReglement().setTaTiers(this.taTiers);
//			taRReglementTmp.setEtatDeSuppression(IHMEtat.insertion);
//			taRReglement=taRReglementTmp;
//		}
//		taRReglement.getTaReglement().addRReglement(taRReglement);
//		if((taRReglement.getTaReglement().getEtat()&IHMEtat.multiple)==0 && integrer)	
//			taRReglement.getTaReglement().setEtat(IHMEtat.integre);
//		else 
//			taRReglement.getTaReglement().setEtat(0);
//			List<String> listeCodes = new LinkedList<String>();
//			for (TaRReglement reglement : this.getTaRReglements()) {
//				listeCodes.add(reglement.getTaReglement().getCodeDocument());
//			}
//			if(taRReglement.getTaReglement().getCodeDocument()==null ||taRReglement.getTaReglement().getCodeDocument().equals(""))
//				taRReglement.getTaReglement().setCodeDocument(new TaReglementDAO().genereCode(listeCodes));
//		return taRReglement;
//	}

	@Transient
	public BigDecimal getRemTtcIntermediaireDocument() {
		return mtTtcAvantRemiseGlobaleCalc.subtract(mtTtcCalc);
	}

//	@Transient
//	public List<ILigneDocumentTiers> calculNbReelLigneImpression(int coupure,ArrayList<ILigneDocumentTiers>ligneInitiales){
//		int nbLigne=0;
//		int rangImpression=0;
//		LinkedList<ILigneDocumentTiers> listeFinale=new LinkedList<ILigneDocumentTiers>();
//		LinkedList<ILigneDocumentTiers> liste2=new LinkedList<ILigneDocumentTiers>();
//		for (Object ligne : ligneInitiales) {
//			liste2.clear();
//			liste2=((SWTLigneDocument)ligne).creeLigneImpression_indirect(coupure,rangImpression);
//			for (ILigneDocumentTiers ligneFinale : liste2) {
//				listeFinale.add(ligneFinale);
//			}
//			rangImpression++;
//			
//			//nbLigne=nbLigne+((TaLTicketDeCaisse)ligne).calculNbLigneLibelle(coupure);
//		}
//		return listeFinale;
//	}
	
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		TaTicketDeCaisse doc = new TaTicketDeCaisse(true);
		try {
			doc.setIdDocument(0);
			doc.setVersion(version);
			//doc.setTaTPaiement(taTPaiement);
			doc.setTaTiers(taTiers);
			//doc.setTaCPaiement(taCPaiement);
			doc.setCodeDocument("");
			doc.setDateDocument(dateDocument);
			doc.setDateEchDocument(dateEchDocument);
			doc.setDateLivDocument(dateLivDocument);
			doc.setLibelleDocument("");

			doc.setRemHtDocument(remHtDocument);
			doc.setTxRemHtDocument(txRemHtDocument);
			doc.setRemTtcDocument(remTtcDocument);
			doc.setTxRemTtcDocument(txRemTtcDocument);
			doc.setNbEDocument(nbEDocument);
			doc.setTtc(ttc);
			doc.setDateExport(dateExport);
//			doc.setExport(export);
			doc.setCommentaire(commentaire);
			doc.setQuiCree(quiCree);
			doc.setQuandCree(quandCree);
			doc.setQuiModif(quiModif);
			doc.setQuandModif(quandModif);
			doc.setIpAcces(ipAcces);
			doc.setVersionObj(versionObj);
			
			doc.setNbDecimalesPrix(nbDecimalesPrix);
			doc.setNbDecimalesQte(nbDecimalesQte);
			

			for (TaLTicketDeCaisse ligne : getLignes()) {
				TaLTicketDeCaisse temp =ligne.clone(); 
				temp.setTaDocument(doc);
				doc.addLigne(temp);
			}
			TaInfosTicketDeCaisse infos = getTaInfosDocument().clone();
			infos.setTaDocument(doc);
			doc.setTaInfosDocument(infos);
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		// on renvoie le clone
		return doc;
	}
	
	//passage ejb => dans TaFactureService
//	@Override
//	public void calculDateEcheanceAbstract(Integer report, Integer finDeMois) {
//		// TODO Auto-generated method stub
//		calculDateEcheanceAbstract(report,finDeMois,null);
//	}
//	public Date calculDateEcheanceAbstract(Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
//
//		return calculDateEcheance(report,finDeMois,taTPaiement);
//	}
//	public Date calculDateEcheance(Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
//		TaTCPaiementDAO taTCPaiementDAO = new TaTCPaiementDAO();
//		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_FACTURE);
//		TaCPaiement conditionDoc = null;
//		TaCPaiement conditionTiers = null;
//		TaCPaiement conditionSaisie = null;
//		TaCPaiement conditionTPaiement = null;
//		
//		if(typeCP!=null) conditionDoc = typeCP.getTaCPaiement();
//		if(getTaTiers()!=null) conditionTiers = getTaTiers().getTaCPaiement();
//		if(taTPaiement!=null){
//			conditionTPaiement=new TaCPaiement();
//			conditionTPaiement.setReportCPaiement(taTPaiement.getReportTPaiement());
//			conditionTPaiement.setFinMoisCPaiement(taTPaiement.getFinMoisTPaiement());
//		}
//		if(report!=null || finDeMois!=null) { 
//			conditionSaisie = new TaCPaiement();
//			if(report!=null)
//				conditionSaisie.setReportCPaiement(report);
//			if(finDeMois!=null)
//				conditionSaisie.setFinMoisCPaiement(finDeMois);
//		}
//		
//		//on applique toute les conditions par ordre décroissant de priorité, la derniere valide est conservée
//		Date nouvelleDate = getDateDocument();
//		if(conditionDoc!=null) {
//			nouvelleDate = conditionDoc.calculeNouvelleDate(getDateDocument());
//		}
//		if(conditionTPaiement!=null){
//			nouvelleDate = conditionTPaiement.calculeNouvelleDate(getDateDocument());
//		}
//		if(conditionTiers!=null) {
//			nouvelleDate = conditionTiers.calculeNouvelleDate(getDateDocument());
//		}
//		if(conditionSaisie!=null) {
//			nouvelleDate = conditionSaisie.calculeNouvelleDate(getDateDocument());
//		}
//		setDateEchDocument(nouvelleDate);
//		return nouvelleDate;
//	}
//
//
//	public LinkedList<TaRReglement> rechercheSiDocumentContientTraite(String typeTraite){
//		LinkedList<TaRReglement> listeTraite =new LinkedList<TaRReglement>();
//		for (TaRReglement rReglement : getTaRReglements()) {
//			if(rReglement.getTaReglement().getTaTPaiement()!=null){
//				if(rReglement.getTaReglement().getTaTPaiement().getCodeTPaiement().equals(typeTraite)
//						&& (rReglement.getTaReglement().getEtat()&IHMEtat.integre)!=0 )
//					listeTraite.add(rReglement);				
//			}
//		}
//		return listeTraite;
//	}

	public boolean reglementExiste(){
		return taRReglement!=null && taRReglement.getTaReglement()!=null ;
	}

	public boolean reglementRempli(){
		for (TaRReglement elem : getTaRReglements()) {
			if(elem.getAffectation().signum()!=0)
				return true;
		}
		return false;
	}
	
	public void mettreAJourDateEcheanceReglements(){
		if(!multiReglement()){
		for (TaRReglement rReglement : getTaRReglements()) {
			if(rReglement.getTaReglement()!=null){
				if(  !rReglement.getTaReglement().affectationMultiple(this))
					rReglement.getTaReglement().setDateLivDocument(this.getDateEchDocument());
				}
			}
		}
	}
	

	public boolean contientReglementAffectationMultiple(){
		for (TaRReglement rReglement : getTaRReglements()) {
			if(rReglement.getTaReglement()!=null && !rReglement.getTaReglement().affectationMultiple(this)){
				if(  rReglement.getTaReglement().affectationMultiple(this))
					return true;
				}
			}
		return false;
	}
	public void mettreAJourDateEcheanceReglement(TaReglement reglement){
		if(!multiReglement() && !reglement.affectationMultiple(this))
			reglement.setDateLivDocument(this.getDateEchDocument());
	}


	
	
	@Transient
	public List<ILigneDocumentTiers> getLignesGeneral(){
		return this.lignes;
	}
	
	@JsonIgnore
	public void setTaInfosDocument(IInfosDocumentTiers infosDocumentTiers) {
		this.taInfosDocument = (TaInfosTicketDeCaisse) infosDocumentTiers;
	}
	
	@Transient
	public boolean isLegrain() {
		// TODO Auto-generated method stub
		return legrain;
	}
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true , mappedBy ="taTicketDeCaisse")
	//Attention si FetchType.EAGER => à cause d'autre EAGER, génère des outter join et duplique les lignes,
	//si EAGER il faut donc gérer ce problème ou remplacer la List des lignes par un Set
	@JoinColumn(name = "id_gr_mouv_stock")
//	@Fetch(FetchMode.SUBSELECT)
	@LgrHibernateValidated(champBd = "id_gr_mouvement_stock",table = "ta_gr_mouv_stock", champEntite="taGrMouvStock.idGrMouvStock", clazz = TaGrMouvStock.class)
	@XmlTransient //peut etre à modifier si on doit utiliser les mouvements dans les web services
	public TaGrMouvStock getTaGrMouvStock() {
		return taGrMouvStock;
	}
	public void setTaGrMouvStock(TaGrMouvStock taGrMouvStock) {
		this.taGrMouvStock = taGrMouvStock;
	}

	



	public Object cloneCentralisation() throws CloneNotSupportedException {
		TaTicketDeCaisse doc = new TaTicketDeCaisse(false);
		try {
			doc.setIdDocument(0);
			doc.setVersion(version);
			//doc.setTaTPaiement(taTPaiement);
			doc.setTaTiers(taTiers);
			//doc.setTaCPaiement(taCPaiement);
			doc.setCodeDocument("");
			doc.setDateDocument(dateDocument);
			doc.setDateEchDocument(dateEchDocument);
			doc.setDateLivDocument(dateLivDocument);
			doc.setLibelleDocument("");

			doc.setRemHtDocument(remHtDocument);
			doc.setTxRemHtDocument(txRemHtDocument);
			doc.setRemTtcDocument(remTtcDocument);
			doc.setTxRemTtcDocument(txRemTtcDocument);
			doc.setNbEDocument(nbEDocument);
			doc.setTtc(ttc);
			doc.setDateExport(dateExport);
//			doc.setExport(export);
			doc.setCommentaire(commentaire);
			doc.setQuiCree(quiCree);
			doc.setQuandCree(quandCree);
			doc.setQuiModif(quiModif);
			doc.setQuandModif(quandModif);
			doc.setIpAcces(ipAcces);
			doc.setVersionObj(versionObj);
			
			doc.setNbDecimalesPrix(nbDecimalesPrix);
			doc.setNbDecimalesQte(nbDecimalesQte);
			

			TaInfosTicketDeCaisse infos = getTaInfosDocument().clone();
			infos.setTaDocument(doc);
			doc.setTaInfosDocument(infos);
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		// on renvoie le clone
		return doc;
	}

	public TaLTicketDeCaisse contientMemeParametreCompte(TaLTicketDeCaisse ligneCompare){
		for (TaLTicketDeCaisse obj : getLignes()) {
			if(obj.getCompteLDocument()!=null && obj.getCodeTvaLDocument()!=null && obj.getTauxTvaLDocument()!=null && obj.getCodeTTvaLDocument()!=null
					&& obj.getCompteLDocument().equals(ligneCompare.getCompteLDocument())
					&& obj.getCodeTvaLDocument().equals(ligneCompare.getCodeTvaLDocument())
					&& LibConversion.bigDecimalToString(obj.getTauxTvaLDocument()).equals(LibConversion.bigDecimalToString(ligneCompare.getTauxTvaLDocument()))
					&& obj.getCodeTTvaLDocument().equals(ligneCompare.getCodeTTvaLDocument()))
			{
				return obj;	
			}
		}
		return null;
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
	@LgrHibernateValidated(champBd = "date_export",table = "ta_ticket_caisse",champEntite="dateExport", clazz = TaTicketDeCaisse.class)
	public Date getDateExport() {
		return dateExport;
	}

	public void setDateExport(Date dateExport) {
		this.dateExport = dateExport;
	}

	@Transient
	public Boolean getMiseADispo() {
		if(taMiseADisposition!=null)
		return taMiseADisposition.estMisADisposition();
		return false;
	}

	
	@Column(name = "date_verrouillage")
	public Date getDateVerrouillage() {
		return dateVerrouillage;
	}

	public void setDateVerrouillage(Date dateVerrouillage) {
		this.dateVerrouillage=dateVerrouillage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_session_caisse")
	public TaSessionCaisse getTaSessionCaisse() {
		return taSessionCaisse;
	}

	public void setTaSessionCaisse(TaSessionCaisse taSessionCaisse) {
		this.taSessionCaisse = taSessionCaisse;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vendeur")
	public TaUtilisateur getTaUtilisateurVendeur() {
		return taUtilisateurVendeur;
	}

	public void setTaUtilisateurVendeur(TaUtilisateur taUtilisateurVendeur) {
		this.taUtilisateurVendeur = taUtilisateurVendeur;
	}
	
	@Column(name = "gestion_lot")
	public Boolean getGestionLot() {
		return gestionLot;
	}

	public void setGestionLot(Boolean gestionLot) {
		this.gestionLot = gestionLot;
	}


	
	
	@Column(name = "nb_decimales_qte")
	public Integer getNbDecimalesQte() {
		return nbDecimalesQte;
	}

	public void setNbDecimalesQte(Integer nbDecimalesQte) {
		this.nbDecimalesQte = nbDecimalesQte;
	}

	@Column(name = "nb_decimales_prix")
	public Integer getNbDecimalesPrix() {
		return nbDecimalesPrix;
	}

	public void setNbDecimalesPrix(Integer nbDecimalesPrix) {
		this.nbDecimalesPrix = nbDecimalesPrix;
	}


	@PrePersist
	public void PrePersist() throws Exception{
		verifInfoDocument();
	}
	
	@PreUpdate
	public void PreUpdate() throws Exception{
		verifInfoDocument();
	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_etat")
	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat",champEntite="TaEtat.idEtat", clazz = TaEtat.class)
	public TaEtat getTaEtat() {
		return this.taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
//		this.taEtat = taEtat;
	}

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taTicketDeCaisse", orphanRemoval = true)
	public Set<TaREtat> getTaREtats() {
		return this.taREtats;
	}

	public void setTaREtats(Set<TaREtat> taREtats) {
		this.taREtats = taREtats;
	}
	

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taTicketDeCaisse", orphanRemoval = true)
	public Set<TaHistREtat> getTaHistREtats() {
		return this.taHistREtats;
	}

	public void setTaHistREtats(Set<TaHistREtat> taHistREtats) {
		this.taHistREtats = taHistREtats;
	}
	
	
	public void addREtat(TaEtat taEtat) {
		TaHistREtat hist=new TaHistREtat();
		TaREtat rEtat=getTaREtat();
		if(rEtat!=null) {
			hist.setTaEtat(getTaREtat().getTaEtat());
			hist.setTaTicketDeCaisse(this);
			this.getTaHistREtats().add(hist);
		}else rEtat=new TaREtat();
		
		rEtat.setTaEtat(taEtat);
		rEtat.setTaTicketDeCaisse(this);
		this.setTaREtat(rEtat);
		this.getTaREtats().add(rEtat);
	}

	@Override
	public Boolean gereStock() {
		// TODO Auto-generated method stub
		 return this.getTaGrMouvStock()!=null;
	}


	
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "taTicketDeCaisse", orphanRemoval=true)
	public Set<TaRReglementLiaison> getTaRReglementLiaisons() {
		return taRReglementLiaisons;
	}

	public void setTaRReglementLiaisons(Set<TaRReglementLiaison> taRReglementLiaisons) {
		this.taRReglementLiaisons = taRReglementLiaisons;
	}
	
	
	public void addRReglementLiaison(TaRReglementLiaison taReglementLiaison){
		if(!this.getTaRReglementLiaisons().contains(taReglementLiaison)){
			taReglementLiaison.setTaTicketDeCaisse(this);
			this.getTaRReglementLiaisons().add(taReglementLiaison);	
		}
	}
	public void removeReglementLiaison(TaRReglementLiaison taRReglementLiaison){
		this.getTaRReglementLiaisons().remove(taRReglementLiaison);
	}

	@Transient
	public String getNumeroCommandeFournisseur() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Column(name = "utilise_unite_saisie")
	public Boolean getUtiliseUniteSaisie() {
		return utiliseUniteSaisie;
	}

	public void setUtiliseUniteSaisie(Boolean utiliseUniteSaisie) {
		this.utiliseUniteSaisie = utiliseUniteSaisie;
	}

}
