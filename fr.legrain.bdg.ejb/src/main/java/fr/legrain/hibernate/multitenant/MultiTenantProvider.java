package fr.legrain.hibernate.multitenant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.HibernateException;
//import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
//import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
//import org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider;
//import org.hibernate.service.spi.ServiceRegistryAwareService;
//import org.hibernate.service.spi.ServiceRegistryImplementor;

public class MultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService  {

	private static final long serialVersionUID = 4368575201221677384L;
	
//	@Resource(mappedName="java:/BDGPostgresDS")
	private DataSource ds;
	
//	private C3P0ConnectionProvider connectionProvider = null;

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		
		try {
            final Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:/BDGPostgresDS");
        } catch (final NamingException e) {
            throw new RuntimeException(e);
        }
//		Map lSettings = serviceRegistry.getService(ConfigurationService.class).getSettings();
//		
//		connectionProvider = new C3P0ConnectionProvider();
//		connectionProvider.injectServices(serviceRegistry);
//		connectionProvider.configure(lSettings);
	}

	@SuppressWarnings("rawtypes")
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return ConnectionProvider.class.equals(unwrapType) || MultiTenantConnectionProvider.class.equals(unwrapType) || MultiTenantProvider.class.isAssignableFrom(unwrapType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException(unwrapType);
        }
    }

	@Override
	public Connection getAnyConnection() throws SQLException {
		final Connection connection = ds.getConnection();
		
//		Context initContext;
//		Connection connection = null;
//		try {
//			//DataSource ds = null;
//			initContext = new InitialContext();
//			ds = (DataSource) initContext.lookup("java:/BDGPostgresDS");
//			connection = ds.getConnection();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
		return connection;
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
//			connection.createStatement().execute("DEALLOCATE ALL"); 
			//https://github.com/pgjdbc/pgjdbc/issues/496   
			//http://docs.postgresql.fr/9.4/sql-deallocate.html
//			connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
			connection.createStatement().execute("set search_path='" + tenantIdentifier + "'");
		}
		catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		try {
			connection.createStatement().execute("SET SCHEMA 'public'");
		}
		catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [public]", e);
		}
		finally {
			connection.close();
//			connectionProvider.closeConnection(connection);
		}

	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		releaseAnyConnection(connection);
	}
}