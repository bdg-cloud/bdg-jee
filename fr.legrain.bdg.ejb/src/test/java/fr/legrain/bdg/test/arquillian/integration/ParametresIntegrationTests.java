package fr.legrain.bdg.test.arquillian.integration;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@RunWith(Categories.class)
@SuiteClasses( {
	//***** Articles
	UniteTest.class,
	//FamilleArticleTest.class,
	//***** Tiers
	TypeAdresseTest.class, 
	TypeCiviliteTest.class,
	TypeEntiteTest.class,
	TypeTelephoneTest.class,
	TypeTiersTest.class,
	TypeWebTest.class,	
	TypeEmailTest.class,
	FamilleTiersTest.class, 
	//***** Solstyce
	EntrepotTest.class, 
	TypeFabricationTest.class,
	TypeReceptionTest.class,
	TypeCodeBarreTest.class,
	GroupeControleTest.class
	})
public class ParametresIntegrationTests {
//	//Internet devbdg.work
////	public static final String URL = "http://demo.devbdg.work/login/login.xhtml";
//	
//	//Dev eclipse local Nicolas
////	public static final String URL = "http://dev.demo.promethee.biz:8080/solstyce/login/login.xhtml";
////	public static final String BASE_URL = "http://dev.demo.promethee.biz:8080/solstyce/";
//	
//	//jboss arquillian
//	public static final String URL = "http://dev.demo.promethee.biz:8180/login/login.xhtml";
//	public static final String BASE_URL = "http://dev.demo.promethee.biz:8180/";
	
}
