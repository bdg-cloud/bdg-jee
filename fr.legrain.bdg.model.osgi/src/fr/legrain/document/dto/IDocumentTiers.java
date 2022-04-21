package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Transient;

import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaHistREtat;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.tiers.model.TaTiers;

public interface IDocumentTiers {
	
	public void setLegrain(boolean legrain);
	
	public String getCodeDocument();
	public void setCodeDocument(String codeDocument);
	
	public int getIdDocument();
	public void setIdDocument(int idDocument);
	
	public Date getDateDocument();
	public void setDateDocument(Date dateDocument);
	
//	public Date getDateEchDocument();
//	public Date setDateEchDocument(Date dateEchDocument);
	
	public BigDecimal getTxRemHtDocument();
	public void setTxRemHtDocument(BigDecimal txRemHtDocument)	;
	
	public BigDecimal getTxRemTtcDocument();
	public void setTxRemTtcDocument(BigDecimal txRemTtcDocument);
	
	public Date getDateLivDocument();
	public void setDateLivDocument(Date dateLivDocument);
	
	public BigDecimal getNetHtCalc();
	
	public BigDecimal getNetTtcCalc();
	public void setNetTtcCalc(BigDecimal netTtcCalc);
	
	public BigDecimal getRegleCompletDocument();
	public BigDecimal getResteAReglerComplet();
	
	public String getLibelleDocument();
	public void setLibelleDocument(String libelleDocument);
	
	public Boolean getAccepte();
	public void setAccepte(Boolean accepte);
	
	public Integer getTtc();
	public void setTtc(Integer ttc);
	
	public Set<TaRDocument> getTaRDocuments();
	public void setTaRDocuments(Set<TaRDocument> taRDocuments);
	
	public TaTiers getTaTiers();
	public void setTaTiers(TaTiers taTiers);
	
	//passage ejb => dans TaDevisService
	public void calculeTvaEtTotaux();
//	public void calculDateEcheance();
	
	//passage ejb => dans TaDevisService
//	public void calculDateEcheanceAbstract(Integer report, Integer finDeMois);
	
	public void setRelationDocument(Boolean relation);
	public boolean getRelationDocument();
	
	public String getTypeDocument();
	
//	public void setExport(Integer export);
//	public Integer getExport();
	

	public String getCommentaire();
	public void setCommentaire(String commentaire);
//	public void setTypeDocument(String typeDocument);
	
	public List<ILigneDocumentTiers> getLignesGeneral();
//	public void setLignes(ILigneDocumentTiers lignes);
	public IInfosDocumentTiers getTaInfosDocument();
	public void setTaInfosDocument(IInfosDocumentTiers infosDocumentTiers);
	public boolean isLegrain();
	
	public boolean isGestionTVA() ;
	public void setGestionTVA(boolean gestionTVA);
	
//	public TaEtat getTaEtat();
//	public void setTaEtat(TaEtat taEtat);
	
	public Date getDateVerrouillage() ;
	public void setDateVerrouillage(Date dateVerrouillage);
	
	public Date getDateExport() ;
	public void setDateExport(Date dateExport);
	
	public TaMiseADisposition getTaMiseADisposition() ;
	public void setTaMiseADisposition(TaMiseADisposition taMiseADisposition) ;
	
	public Boolean getGestionLot();
	public void setGestionLot(Boolean gestionLot);
	
	public Integer getNbDecimalesQte();

	public void setNbDecimalesQte(Integer nbDecimalesQte);

	public Integer getNbDecimalesPrix() ;

	public void setNbDecimalesPrix(Integer nbDecimalesPrix);
	

	public TaREtat getTaREtat();
	public void setTaREtat(TaREtat taREtat);
	
	public Set<TaREtat> getTaREtats();
	public void setTaREtats(Set<TaREtat> taREtats);
	public Set<TaHistREtat> getTaHistREtats();
	public void setTaHistREtats(Set<TaHistREtat> taHistREtats);
	
	public Boolean gereStock();
	
	
	public Set<TaRReglementLiaison> getTaRReglementLiaisons();
	public void setTaRReglementLiaisons(Set<TaRReglementLiaison> taRReglementLiaisons);
	
	
	public String getNumeroCommandeFournisseur() ;
//	public void setNumeroCommandeFournisseur(String numeroCommandeFournisseur) ;
	
/*
	public String getQuiCreeDocument() ;
	public void setQuiCreeDocument(String quiCreeApporteur) ;

	public Date getQuandCreeDocument() ;
	public void setQuandCreeDocument(Date quandCreeApporteur);

	public String getQuiModifDocument();
	public void setQuiModifDocument(String quiModifApporteur) ;

	public Date getQuandModifDocument() ;
	public void setQuandModifDocument(Date quandModifApporteur) ;
	
	public String getIpAcces();
	public void setIpAcces(String ipAcces);

	public String getVersion() ;
	public void setVersion(String version);
*/
	
	public Boolean getUtiliseUniteSaisie();
	public void setUtiliseUniteSaisie(Boolean utiliseUniteSaisie);
}
