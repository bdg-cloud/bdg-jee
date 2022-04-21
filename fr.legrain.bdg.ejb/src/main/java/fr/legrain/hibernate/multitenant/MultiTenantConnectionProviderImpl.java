//package fr.legrain.hibernate.multitenant;
//
////http://stackoverflow.com/questions/16213573/setting-up-a-multitenantconnectionprovider-using-hibernate-4-2-and-spring-3-1-1
//
//import java.beans.PropertyVetoException;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
//import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
//import org.hibernate.service.UnknownUnwrapTypeException;
//
//import com.mchange.v2.c3p0.ComboPooledDataSource;
//
//public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider  {
//
//
//    private static final long serialVersionUID = 8074002161278796379L;
//
//
////    private static Logger log = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class );
//
//    private ComboPooledDataSource cpds;
//    
//    private Map<String,ComboPooledDataSource> mapCpds = new HashMap<String,ComboPooledDataSource>();
//
//    private Properties properties;
//    private Properties props;
//
//    /**
//     * 
//     * Constructor. Initializes the ComboPooledDataSource based on the config.properties.
//     * 
//     * @throws PropertyVetoException
//     */
//    public MultiTenantConnectionProviderImpl() throws PropertyVetoException {
//    	System.out.println("Initializing Connection Pool!");
//        properties = new Properties();
//        String user = null;
//        String password = null;
//        String driver = null;
//        String url = null;
//        try {
//            //properties.load(MultiTenantConnectionProviderImpl.class.getResourceAsStream("/fr/legrain/hibernate/multitenant/config.properties"));
//            
//            String propertiesFileName = "bdg.properties";  
//
//			
//            props = new Properties();  
//            String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
//
//            
//            if(new File(path).exists()) { 
//            	File f = new File(path);
//            	props.load(new FileInputStream(f));  
//            	user = props.getProperty("jdbc.username");
//            	password = props.getProperty("jdbc.password");
//            	driver = props.getProperty("jdbc.driver");
//            	url = props.getProperty("jdbc.url");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//		  
//        //jdbc.url=jdbc:postgresql:demo
//        //jdbc.driver=org.postgresql.Driver
//        cpds = new ComboPooledDataSource("Example");
//        cpds.setDriverClass(driver!=null?driver:properties.getProperty("jdbc.driver"));
//        cpds.setJdbcUrl(url!=null?url:properties.getProperty("jdbc.url"));
//        cpds.setUser(user!=null?user:properties.getProperty("jdbc.username"));
//        //cpds.setPassword(PropertyUtil.getCredential("jdbc.password"));
//        cpds.setPassword(password!=null?password:properties.getProperty("jdbc.password"));
//        cpds.setConnectionCustomizerClassName("fr.legrain.hibernate.multitenant.IsolationLevelConnectionCustomizer");
//        System.out.println("Connection Pool initialised!");
//    }
//
//
//    @Override
//    public Connection getAnyConnection() throws SQLException {
////        log.debug("Get Default Connection:::Number of connections (max: busy - idle): {} : {} - {}",new int[]{cpds.getMaxPoolSize(),cpds.getNumBusyConnectionsAllUsers(),cpds.getNumIdleConnectionsAllUsers()});
//        if (cpds.getNumConnectionsAllUsers() == cpds.getMaxPoolSize()){
//        	 System.out.println("Maximum number of connections opened");
//        }
//        if (cpds.getNumConnectionsAllUsers() == cpds.getMaxPoolSize() && cpds.getNumIdleConnectionsAllUsers()==0){
//        	 System.out.println("Connection pool empty!");
//        }
//        return cpds.getConnection();
//    }
//
//    @Override
//    public Connection getConnection(String tenantIdentifier) throws SQLException {
//////        log.debug("Get {} Connection:::Number of connections (max: busy - idle): {} : {} - {}",new Object[]{tenantIdentifier, cpds.getMaxPoolSize(),cpds.getNumBusyConnectionsAllUsers(),cpds.getNumIdleConnectionsAllUsers()});
////        if (cpds.getNumConnectionsAllUsers() == cpds.getMaxPoolSize()){
////        	 System.out.println("Maximum number of connections opened");
////        }
////        if (cpds.getNumConnectionsAllUsers() == cpds.getMaxPoolSize() && cpds.getNumIdleConnectionsAllUsers()==0){
////        	 System.out.println("Connection pool empty!");
////        }
////        //return cpds.getConnection(tenantIdentifier, PropertyUtil.getCredential(tenantIdentifier));
////        //return cpds.getConnection(tenantIdentifier, properties.getProperty(tenantIdentifier));
////        cpds.getConnection().createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
//    	
//    	if(mapCpds.get(tenantIdentifier)==null) {
//    		ComboPooledDataSource cpds = new ComboPooledDataSource();
//            try {
//				//cpds.setDriverClass(properties.getProperty("jdbc.driver"));
//            	cpds.setDriverClass(props.getProperty("jdbc.driver"));
//			} catch (PropertyVetoException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//            //cpds.setJdbcUrl(properties.getProperty("jdbc.url")+"?searchpath="+tenantIdentifier);
////            cpds.setJdbcUrl(properties.getProperty("jdbc.url")+"?currentSchema="+tenantIdentifier);//9.4
//            cpds.setJdbcUrl(properties.getProperty("jdbc.url"));
//            cpds.setUser(properties.getProperty("jdbc.username"));
//            cpds.setPassword(properties.getProperty("jdbc.password"));
//            
//            cpds.setJdbcUrl(props.getProperty("jdbc.url"));
//            cpds.setUser(props.getProperty("jdbc.username"));
//            cpds.setPassword(props.getProperty("jdbc.password"));
//            cpds.setConnectionCustomizerClassName("fr.legrain.hibernate.multitenant.IsolationLevelConnectionCustomizer");
//            mapCpds.put(tenantIdentifier, cpds);
//    	}
//    	
//    	if (mapCpds.get(tenantIdentifier).getNumConnectionsAllUsers() == mapCpds.get(tenantIdentifier).getMaxPoolSize()){
//    		System.out.println("Maximum number of connections opened for "+tenantIdentifier);
//    	}
//    	if (mapCpds.get(tenantIdentifier).getNumConnectionsAllUsers() == mapCpds.get(tenantIdentifier).getMaxPoolSize() && mapCpds.get(tenantIdentifier).getNumIdleConnectionsAllUsers()==0){
//    		System.out.println("Connection pool empty! for "+tenantIdentifier);
//    	}
//    	Connection c = mapCpds.get(tenantIdentifier).getConnection();
//    	c.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
//    	return c;
//    	
//    	
//    	
//    	
//    	
//    	
//        //return cpds.getConnection();
//    }
//
//    @Override
//    public void releaseAnyConnection(Connection connection) throws SQLException {
//        connection.close();
//    }
//
//    @Override
//    public void releaseConnection(String tenantIdentifier, Connection connection){
//        try {
////        	connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
//            this.releaseAnyConnection(connection);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean supportsAggressiveRelease() {
//        return false;
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    public boolean isUnwrappableAs(Class unwrapType) {
//        return ConnectionProvider.class.equals( unwrapType ) || MultiTenantConnectionProvider.class.equals( unwrapType ) || MultiTenantConnectionProviderImpl.class.isAssignableFrom( unwrapType );
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T unwrap(Class<T> unwrapType) {
//        if ( isUnwrappableAs( unwrapType ) ) {
//            return (T) this;
//        }
//        else {
//            throw new UnknownUnwrapTypeException( unwrapType );
//        }
//    }
//}