package fr.legrain.autorisations.ejb.lib.osgi;

public class EJBLookupName {

	/**
	 * 
	 * @param appName - The app name is the EAR name of the deployed EJB without .ear
        suffix. Since we haven't deployed the application as a .ear, the app
        name for us will be an empty string.
        <p>Ex : "fr.legrain.bdg.ear" si le nom de l'EAR est fr.legrain.bdg.ear.ear<br>
		"test-app" si le nom de l'appli dans application.xml est test-app<br>
		"" vide si pas d'EAR
		</p>
	 * @param moduleName - The module name is the JAR name of the deployed EJB without the
        .jar suffix.
        <p>
        Ex : "fr.legrain.bdg.ejb"
        </p>
        
	 * @param distinctName - AS7 allows each deployment to have an (optional) distinct name.
        This can be an empty string if distinct name is not specified.
	 * @param beanName - The EJB bean implementation class name
	 <p>
	 	Ex : "TaTCiviliteService" ou TaTCiviliteBean.class.getSimpleName() si la classe est accessible
	 </p>
	 * 
	 * @param interfaceName - Fully qualified remote interface name
	 <p>
	 	Ex : ITaTCiviliteServiceRemote.class.getName()
	 </p>
	 * 
	 * @return - The look up string name
	 */
	@SuppressWarnings("unused")
	public static String getLookupName(String appName,String moduleName,
			String distinctName,String beanName,final String interfaceName ) {
		
		String name = "ejb:" + appName + "/" + moduleName + "/" +
				distinctName    + "/" + 
				beanName + "!" + interfaceName;
		
		/*
		The app name is the EAR name of the deployed EJB without .ear
        suffix. Since we haven't deployed the application as a .ear, the app
        name for us will be an empty string 
		String appName = "fr.legrain.bdg.ear";
		//String appName = "test-app"; //nom de l'appli dans application.xml
		//String appName = "";

		The module name is the JAR name of the deployed EJB without the
        .jar suffix.
		String moduleName = "fr.legrain.bdg.ejb";

		AS7 allows each deployment to have an (optional) distinct name.
        This can be an empty string if distinct name is not specified.
		String distinctName = "";

		// The EJB bean implementation class name
		//String beanName = TaTCiviliteBean.class.getSimpleName();
		String beanName = "TaTCiviliteService";

		// Fully qualified remote interface name
		final String interfaceName = ITaTCiviliteServiceRemote.class.getName();

		// Create a look up string name
		String name = "ejb:" + appName + "/" + moduleName + "/" +
				distinctName    + "/" + 
				beanName + "!" + interfaceName;
		 */
		
		System.err.println(name);
		
		return name;
	}
	
	public static String getLookupName(String moduleName,
			String beanName,final String interfaceName ) {
		
		String appName = "fr.legrain.autorisations.ear";
		String distinctName = "";
		
		return getLookupName(appName,moduleName,distinctName,beanName,interfaceName);
	}
	
	public static String getLookupName(String beanName,final String interfaceName ) {
		
		String moduleName = "fr.legrain.autorisations.ejb";
		
		return getLookupName(moduleName,beanName,interfaceName);
	}

}
