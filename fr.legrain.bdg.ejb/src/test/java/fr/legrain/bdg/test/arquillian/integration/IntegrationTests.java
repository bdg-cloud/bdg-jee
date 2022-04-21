package fr.legrain.bdg.test.arquillian.integration;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@RunWith(Categories.class)
@SuiteClasses( {
	LoginScreenGrapheneTest.class, 
	DeconnexionGrapheneTest.class, 
	InfosEntrepriseScreenGrapheneTest.class, 
	GoogleGrapheneTest.class
	})
public class IntegrationTests {
//	public static final String URL = "http://demo.devbdg.work/login/login.xhtml";
	
//	public static final String URL = "http://dev.demo.promethee.biz:8080/login/login.xhtml";
//	public static final String BASE_URL = "http://dev.demo.promethee.biz:8080/";
	
	public static final String URL = "http://dev.demo.promethee.biz:8180/login/login.xhtml";
	public static final String BASE_URL = "http://dev.demo.promethee.biz:8180/";
	
}
