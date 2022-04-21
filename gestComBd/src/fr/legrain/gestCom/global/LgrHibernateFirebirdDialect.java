package fr.legrain.gestCom.global;

import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;

public class LgrHibernateFirebirdDialect extends FirebirdDialect{

	public LgrHibernateFirebirdDialect() {
		super();
		registerFunction("substring", new SQLFunctionTemplate(new org.hibernate.type.StringType(),"substring(?1 from ?2 for ?3)"));

	}

}
