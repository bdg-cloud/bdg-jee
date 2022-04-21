package fr.legrain.bdg.rest;

import java.util.Map;
import java.util.Set;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

public class SwaggerReaderForMavenPlugin implements io.swagger.v3.oas.integration.api.OpenApiReader {

	/**
	 * L'implémentation de de ReaderListener est pour le bug 3060 ou 23
	 * qui fait que contrairement à la servlet qui génère le fichier json dynamiquement, 
	 * le plugin maven n'ajoute pas l'@ApplicationPath au chemin des méthodes.
	 * 
	 * https://github.com/swagger-api/swagger-core/issues/3060
	 * https://github.com/openapi-tools/swagger-maven-plugin/issues/23
	 */
//	@Override
//	public void beforeScan(Reader openApiReader, OpenAPI openAPI) {
//	}
//
//	@Override
//	public void afterScan(Reader openApiReader, OpenAPI openAPI) {
//		Paths paths = openAPI.getPaths();
//		Paths modifiedPaths = new Paths();
//
//		paths.forEach((path, pathItem) -> {
//			modifiedPaths.addPathItem(MyApplication.APPLICATION_PATH + path, pathItem);
//		});
//
//		openAPI.setPaths(modifiedPaths);
//
//	}

	@Override
	public OpenAPI read(Set<Class<?>> arg0, Map<String, Object> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(OpenAPIConfiguration arg0) {
		// TODO Auto-generated method stub
		
	}

}
