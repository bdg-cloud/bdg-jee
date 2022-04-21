package fr.legrain.import_agrigest.dao;

import fr.legrain.import_agrigest.sqlserver.util.SQLServerUtil;

public abstract class AbstractDAO {

    protected SQLServerUtil sqlServer = new SQLServerUtil();

	public SQLServerUtil getSqlServer() {
				return sqlServer;
	}
    
}
