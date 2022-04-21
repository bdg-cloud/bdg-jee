package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaTLigne;

public interface ILigneDocumentTiers {
	
	public void setLegrain(boolean legrain);
	
	public int getIdLDocument();
	
	public TaTLigne getTaTLigne(); 
	public void setTaTLigne(TaTLigne taTLigne);

	public IDocumentTiers getTaDocument(); 
	public void setTaDocumentGeneral(IDocumentTiers  taDocument); 
	
	public TaArticle getTaArticle();
	public void setTaArticle(TaArticle taArticle); 
	
	public Integer getNumLigneLDocument();
	public void setNumLigneLDocument(Integer numLigneLFacture);

	public String getLibLDocument();
	public void setLibLDocument(String libLFacture);
	
	public BigDecimal getQteLDocument(); 
	public void setQteLDocument(BigDecimal qteLFacture);
	
	public BigDecimal getQte2LDocument();
	public void setQte2LDocument(BigDecimal qte2LFacture);

	public String getU1LDocument();
	public void setU1LDocument(String u1LFacture);

	public String getU2LDocument();
	public void setU2LDocument(String u2LFacture); 

	public BigDecimal getPrixULDocument();
	public void setPrixULDocument(BigDecimal prixULFacture); 

	public BigDecimal getTauxTvaLDocument();
	public void setTauxTvaLDocument(BigDecimal tauxTvaLFacture);
	
	public String getCompteLDocument();
	public void setCompteLDocument(String compteLFacture);
	
	public String getCodeTvaLDocument();
	public void setCodeTvaLDocument(String codeTvaLFacture);
	
	public String getCodeTTvaLDocument();
	public void setCodeTTvaLDocument(String codeTTvaLFacture);

	public BigDecimal getMtHtLDocument();
	public void setMtHtLDocument(BigDecimal mtHtLFacture);
	
	public BigDecimal getMtTtcLDocument();
	public void setMtTtcLDocument(BigDecimal mtTtcLFacture);
	
	public BigDecimal getRemTxLDocument();
	public void setRemTxLDocument(BigDecimal remTxLFacture);
	
	public BigDecimal getRemHtLDocument();
	public void setRemHtLDocument(BigDecimal remHtLFacture);

	public BigDecimal getTxRemHtDocument();
	public void setTxRemHtDocument(BigDecimal txRemHtFacture);

	public BigDecimal getMtHtLApresRemiseGlobaleDocument();
	public void setMtHtLApresRemiseGlobaleDocument(BigDecimal mtHtLApresRemiseGlobaleDocument);
	
	public BigDecimal getMtTtcLApresRemiseGlobaleDocument();
	public void setMtTtcLApresRemiseGlobaleDocument(BigDecimal mtTtcLApresRemiseGlobaleDocument);
	
	public String getQuiCree() ;
	public void setQuiCree(String quiCreelApporteur) ;

	public Date getQuandCree() ;
	public void setQuandCree(Date quandCreeLApporteur);

	public String getQuiModif();
	public void setQuiModif(String quiModifLApporteur) ;

	public Date getQuandModif() ;
	public void setQuandModif(Date quandModifLApporteur) ;
	
	public String getIpAcces();
	public void setIpAcces(String ipAcces);

	public String getVersion() ;
	public void setVersion(String version);
	
	public TaLot getTaLot();
	public void setTaLot(TaLot taLot);

	public boolean getGenereLigne() ;
	public void setGenereLigne(boolean genereLigne) ;

	
	public BigDecimal getQteGenere() ;
	public void setQteGenere(BigDecimal qteGenere) ;

	public String getUniteGenere();
	public void setUniteGenere(String uniteGenere);
	

	public String getNumlotGenere();
	public void setNumlotGenere(String numlotGenere);
	
	public Boolean getAbonnement();
	public void setAbonnement(Boolean abonnement);
	
	public List<TaLigneALigneSupplementDTO> getListeSupplement();
	public void setListeSupplement(List<TaLigneALigneSupplementDTO> listeSupplement);
	
	public List<TaLigneALigneSupplementDTO> getListeLigneAIntegrer();
	public void setListeLigneAIntegrer(List<TaLigneALigneSupplementDTO> listeLigneAIntegrer);

//	public TaEtat getTaEtat();
//	public void setTaEtat(TaEtat etat);
	
	public TaREtatLigneDocument getTaREtatLigneDocument();
	public void setTaREtatLigneDocument(TaREtatLigneDocument rEtatLigneDocument);
	
	public Set<TaLigneALigne> getTaLigneALignes();
	public void setTaLigneALignes(Set<TaLigneALigne> taLigneALignes);
	
	public Set<TaREtatLigneDocument> getTaREtatLigneDocuments() ;
	public void setTaREtatLigneDocuments(Set<TaREtatLigneDocument> taREtatLigneDocuments);

	public void addREtatLigneDoc(TaEtat etatLigneOrg);
	
	public Set<TaHistREtatLigneDocument> getTaHistREtatLigneDocuments();
	public void setTaHistREtatLigneDocuments(Set<TaHistREtatLigneDocument> taHistREtatLigneDocuments);
	
	public Set<TaLigneALigneEcheance> getTaLigneALignesEcheance();
	public void setTaLigneALignesEcheance(Set<TaLigneALigneEcheance> taLigneALignes);

	public void setTaEntrepot(TaEntrepot findById);

	public void setEmplacementLDocument(String emplacement);

	public TaEntrepot getTaEntrepot();

	public String getEmplacementLDocument();
	
	
	public String getUSaisieLDocument(); 
	public void setUSaisieLDocument(String uSaisieLDocument);
	

	public BigDecimal getQteSaisieLDocument();
	public void setQteSaisieLDocument(BigDecimal qteSaisieLDocument);
}
