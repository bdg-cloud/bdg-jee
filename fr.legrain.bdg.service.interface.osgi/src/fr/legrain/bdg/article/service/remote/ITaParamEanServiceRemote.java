package fr.legrain.bdg.article.service.remote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.ejb.Remote;

@Remote

public interface ITaParamEanServiceRemote {
	public Map<String, String>  decodeEan(String codeBarre) throws Exception;
	public String encodeEan(Map<String, Object> listeAiValeur) throws Exception;
	public String encodeEan(String ai,String valeur) throws Exception;
	public String encodeEan(String ai,Date valeur) throws Exception;
	public String encodeEan(String ai,BigDecimal valeur) throws Exception;
}
