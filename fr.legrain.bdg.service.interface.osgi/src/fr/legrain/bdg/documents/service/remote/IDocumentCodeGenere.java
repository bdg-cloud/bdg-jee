package fr.legrain.bdg.documents.service.remote;

import java.util.Map;

public interface IDocumentCodeGenere {
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
}
