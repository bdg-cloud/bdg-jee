package fr.legrain.bdg.general.service.remote;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ITaGenCodeControlServiceRemote  {
//	public String genereCodeJPA() throws Exception;

	public String genereCodeJPA(String nomClass) throws Exception;
	public String genereCodeJPA(String nomClass, Map<String, String> params) throws Exception;
}
