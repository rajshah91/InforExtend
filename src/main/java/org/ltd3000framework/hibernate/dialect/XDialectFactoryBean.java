package org.ltd3000framework.hibernate.dialect;

import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.FactoryBean;

public class XDialectFactoryBean implements FactoryBean<XOracleDialect> {
	public static final String ORACLE = "oracle";
	private XOracleDialect dialect;
	private String dbType = "oracle";

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public XOracleDialect getObject() throws Exception {
		if ("oracle".equals(this.dbType)) {
			this.dialect = new XOracleDialect();
		}else {
			throw new Exception("未设置合适的数据库类型");
		}
		return this.dialect;
	}

	@Override
	public Class<?> getObjectType() {
		return Dialect.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}