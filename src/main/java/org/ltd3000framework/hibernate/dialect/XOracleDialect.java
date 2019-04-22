package org.ltd3000framework.hibernate.dialect;

import java.sql.Types;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

public class XOracleDialect extends Oracle10gDialect {
	public XOracleDialect() {
		super();
		//注册新类型
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.DECIMAL, StandardBasicTypes.DOUBLE.getName());
		registerHibernateType(Types.NCLOB, StandardBasicTypes.STRING.getName());

	}

}