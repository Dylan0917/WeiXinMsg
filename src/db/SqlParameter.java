/*
 * The Spring Framework is published under the terms
 * of the Apache Software License.
 */

package db;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Object to represent a SQL parameter definition. Parameters may be anonymous,
 * in which case name is null. However all parameters must define a SQL type
 * constant from java.sql.Types.
 * 
 * @author Rod Johnson
 */
public class SqlParameter {

	public static final SqlParameter INTEGER = new SqlParameter(Types.INTEGER);

	public static final SqlParameter DOUBLE = new SqlParameter(Types.DOUBLE);

	public static final SqlParameter VARCHAR = new SqlParameter(Types.VARCHAR);

	public static final SqlParameter CHAR = new SqlParameter(Types.CHAR);

	public static final SqlParameter CLOB = new SqlParameter(Types.CLOB);

	public static final SqlParameter BLOB = new SqlParameter(Types.BLOB);

	public static final SqlParameter DECIMAL = new SqlParameter(Types.DECIMAL);

	public static final SqlParameter NUMERIC = new SqlParameter(Types.NUMERIC);

	public static final SqlParameter DATE = new SqlParameter(Types.DATE);

	/**
	 * 参数名称
	 */
	private String name;

	/** SQL type constant from java.sql.Types */
	private int type;

	/**
	 * used for types that are user-named like: STRUCT, DISTINCT, JAVA_OBJECT,
	 * and named array types.
	 */
	private String typeName;

	/**
	 * Add a new anonymous parameter
	 */
	public SqlParameter(int type) {
		this(null, type, null);
	}

	public SqlParameter(int type, String typeName) {
		this(null, type, typeName);
	}

	public SqlParameter(String name, int type) {
		this(name, type, null);
	}

	public SqlParameter(String name, int type, String typeName) {
		this.name = name;
		this.type = type;
		this.typeName = typeName;
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

	/**
	 * Convert a list of JDBC types, as defined in the java.sql.Types class, to
	 * a List of SqlParameter objects as used in this package
	 */
	public static List sqlTypesToAnonymousParameterList(int[] types) {
		if (types == null) {
			return Collections.EMPTY_LIST;
		}
		List l = new ArrayList(types.length);
		for (int i = 0; i < types.length; i++) {
			l.add(new SqlParameter(types[i]));
		}
		return l;
	}

}
