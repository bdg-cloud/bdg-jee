package fr.legrain.bdg.test.arquillian.integration;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

@RunWith(Arquillian.class)
public class GoogleGrapheneTest {
    private static final String WEBAPP_SRC = "src/main/webapp";
    
//    @Deployment(testable = false)
//    public static WebArchive createDeployment() {
//        return ShrinkWrap.create(WebArchive.class, "login.war")
//            .addClasses(Credentials.class, User.class, LoginController.class)
//            .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
//            .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
//            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
//            .addAsWebInfResource(
//                new StringAsset("<faces-config version=\"2.0\"/>"),
//                "faces-config.xml");
//    	return LoginScreenGrapheneTest.aa();
//    }
    
//    @Deployment(testable = false)
//    public static EnterpriseArchive createDeployment() {
//    	 EnterpriseArchive ear = ShrinkWrap.create(ZipImporter.class, "eee.ear")
//       		  .importFrom(new File("../dist/eee.ear")).as(EnterpriseArchive.class) ;
//
//       	WebArchive war = ear.getAsType(WebArchive.class, "/eee-war.war");
//       	war.addPackage("com.eee.testutils");
//       	return ear;
//    }
   
    
    @Drone
    private WebDriver browser;

//    @ArquillianResource
//    private URL deploymentUrl;
//    <systemPropertyVariables>
//    <arq.extension.graphene.xxx>http://www.google.com</arq.extension.graphene.xxx>
//</systemPropertyVariables>
//    private String url = "http://demo.devbdg.work/login/login.xhtml";
    private String url = "http://dev.demo.promethee.biz:8080/solstyce/login/login.xhtml";
    
    @FindBy(id = "lst-ib")                                  // 2. injects an element
    private WebElement texte;
//    
    @FindBy(name = "btnG")                                  // 2. injects an element
    private WebElement btn;
    
    @FindBy(partialLinkText = "[Legrain Informatique]")
    private WebElement lienLgr;
//    
//    @FindBy(id = "loginForm:id_btn_login")
//    private WebElement loginButton;
    
    
    
    @Test
    public void google() {
//    	URL url = new URL("http://www.google.com/");
    	
    	browser.get("http://www.google.com/");
//    	browser.get(url);
//        browser.get(deploymentUrl.toExternalForm() + "login.jsf");      // 1. open the tested page

    	texte.sendKeys("legrain informatique"); 
//    	user.sendKeys("demo9");// 3. control the page
//        password.sendKeys("demo");
//        
//        //loginButton.click();
        guardAjax(btn).click();
        
        waitAjax().until().element(lienLgr).is().present();
        
        guardHttp(lienLgr).click();

        
//        assertEquals("Welcome", facesMessage.getText().trim());
//        whoAmI.click();
//        waitAjax().until().element(signedAs).is().present();
//        assertTrue(signedAs.getText().contains("demo"));
    }
    
}