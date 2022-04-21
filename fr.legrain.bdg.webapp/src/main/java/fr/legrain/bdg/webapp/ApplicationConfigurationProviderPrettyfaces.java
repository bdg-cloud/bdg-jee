package fr.legrain.bdg.webapp;
//package fr.legrain.bdg.webapp;
//
//import javax.servlet.ServletContext;
//
//import org.ocpsoft.logging.Logger.Level;
//import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
//import org.ocpsoft.rewrite.config.Configuration;
//import org.ocpsoft.rewrite.config.ConfigurationBuilder;
//import org.ocpsoft.rewrite.config.Direction;
//import org.ocpsoft.rewrite.config.Log;
//import org.ocpsoft.rewrite.servlet.config.Domain;
//import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
//import org.ocpsoft.rewrite.servlet.config.Path;
//import org.ocpsoft.rewrite.servlet.config.Query;
//import org.ocpsoft.rewrite.servlet.config.Redirect;
//
///*
//Configuration de Prettyfaces pour l'URL Rewritting
//Attention : si <url-pattern>/* dans la config de prettyfaces dans le web.xml il passe avant le @WebFilter de la classe AuthFilter
//
//
//<listener>
//   <listener-class>org.ocpsoft.rewrite.servlet.impl.RewriteServletRequestListener</listener-class>
//</listener>
//
//<listener>
//   <listener-class>org.ocpsoft.rewrite.servlet.impl.RewriteServletContextListener</listener-class>
//</listener>
//
//<filter>
//   <filter-name>OCPsoft Rewrite Filter</filter-name>
//   <filter-class>org.ocpsoft.rewrite.servlet.RewriteFilter</filter-class>
//   <async-supported>true</async-supported>
//</filter>
//<filter-mapping>
//   <filter-name>OCPsoft Rewrite Filter</filter-name>
//   <url-pattern>/auth/*</url-pattern>
//   <dispatcher>FORWARD</dispatcher>
//   <dispatcher>REQUEST</dispatcher>
//   <dispatcher>INCLUDE</dispatcher>
//   <dispatcher>ASYNC</dispatcher>
//   <dispatcher>ERROR</dispatcher>
//</filter-mapping>
//
// */
//@RewriteConfiguration
//public class ApplicationConfigurationProviderPrettyfaces extends HttpConfigurationProvider
//{
//	@Override
//	public Configuration getConfiguration(ServletContext context) {
//		
//		return ConfigurationBuilder.begin()
//				.addRule()
//				.perform(Log.message(Level.INFO, "ApplicationConfigurationProvider - Prettyfaces- Rewrite is active."))
//
//				//https://dev.demo.promethee.biz:8443/auth/google_oauth2?state=aa
//
//
//				//http://bdg.cloud/auth/google_oauth2/callback=#{devBean.tenantInfo.tenantId} 
//				.addRule()
//				//         .when(Direction.isInbound().and(Path.matches("/book.php").and(Query.parameterExists("isbn"))))
//				//         .perform(Redirect.temporary(context.getContextPath() + "/book/{isbn}"))
//				.when(
//						Direction.isInbound()
//						.and(Path.matches("/auth/google_oauth2"))
//						.and(Query.parameterExists("state"))
//						//.and(Domain.matches("{d}"))
//						.and(Domain.matches("{pre}.{sub}.{d}.{tld}"))
//						//.and(Domain.matches("{sub}.{d}.{tld}"))
//								
//						)
//				.perform(Log.message(Level.INFO, "*/*/*/*/*/*/*/* {pre}.{sub}.{d}     "+"{state}.{d}.{tld}"+context.getContextPath()))
//				
//				//.perform(Redirect.temporary("{state}"+context.getContextPath() + "/book/{state}"))
//
//
//				;
//	}
//
//	@Override
//	public int priority() {
//		return 0;
//	}
//}