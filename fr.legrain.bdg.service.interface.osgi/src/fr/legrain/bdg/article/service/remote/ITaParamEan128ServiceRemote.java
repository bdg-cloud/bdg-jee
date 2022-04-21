package fr.legrain.bdg.article.service.remote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaParamEan128DTO;
import fr.legrain.article.model.TaParamEan128;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote

public interface ITaParamEan128ServiceRemote extends IGenericCRUDServiceRemote<TaParamEan128,TaParamEan128DTO>,
IAbstractLgrDAOServer<TaParamEan128>,IAbstractLgrDAOServerDTO<TaParamEan128DTO>{

public static final String validationContext = "PARAM_EAN128";
public static final String ALPHANUMERIC = "alphanumeric";
public static final String NUMERIC = "numeric";
public static final String DATE = "date";
public static final String MOIS = "mois";
public static final String DECIMAL = "decimal";


public List<String> recupListIdentifiant();
public TaParamEan128 rechercheParam(String identifiant);

public Map<String, String>  decodeEan128(String codeBarre) throws Exception;

public String valeur(Map<String, String> listeRetour,String ai);


public String decodeEanSurembalage(Map<String, String> listeRetour);
public String decodeEanArticle(Map<String, String> listeRetour);
public String decodeCodeArticle(Map<String, String> listeRetour) ;
public String decodeLotFabrication(Map<String, String> listeRetour) ;
public String decodeQuantiteUnitaire(Map<String, String> listeRetour) ;
public String decodeQuantite(Map<String, String> listeRetour) ;
public Date decodeDDM(Map<String, String> listeRetour) ;
public Date decodeDLC(Map<String, String> listeRetour) ;
public BigDecimal decodePoidsEnKg(Map<String, String> listeRetour) ;
public BigDecimal decodeLongueurEnMetre(Map<String, String> listeRetour) ;
public BigDecimal decodeLargeurEnMetre(Map<String, String> listeRetour) ;
public BigDecimal decodeHauteurEnMetre(Map<String, String> listeRetour) ;
public BigDecimal decodeDurfaceEnMetreCarre(Map<String, String> listeRetour) ;
public BigDecimal decodeVolumeEnLitre(Map<String, String> listeRetour);



public String encodeEan128(String ai,BigDecimal valeur) throws Exception;
public String encodeEan128(String ai,Date valeur) throws Exception;
public String encodeEan128(String ai,String valeur) throws Exception;
public String encodeEan128(Map<String, Object> listeAiValeur)throws Exception;    
public String encodeEanSurembalage(String valeur)throws Exception;
public String encodeEanArticle(String valeur) throws Exception;
public String encodeCodeArticle(String valeur) throws Exception;
public String encodeLotFabrication(String valeur)throws Exception;
public String encodeQuantiteUnitaire(BigDecimal valeur)throws Exception;
public String encodeQuantite(BigDecimal valeur)throws Exception;
public String encodeDDM(Date valeur) throws Exception;
public String encodeDLC(Date valeur) throws Exception;
public String encodePoidsEnKg(BigDecimal valeur, String unite)throws Exception;
public String encodeLongueurEnMetre(BigDecimal valeur, String unite) throws Exception;
public String encodeLargeurEnMetre(BigDecimal valeur, String unite)throws Exception;
public String encodeHauteurEnMetre(BigDecimal valeur, String unite)throws Exception;
public String encodeSurfaceEnMetreCarre(BigDecimal valeur, String unite)throws Exception;
public String encodeVolumeEnLitre(BigDecimal valeur, String unite) throws Exception;

public String recupEan13SurGtin14(String gtin14)   throws Exception ;
}
