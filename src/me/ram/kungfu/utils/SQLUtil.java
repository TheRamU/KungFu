package me.ram.kungfu.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.ram.kungfu.database.TypeField;

public class SQLUtil {
	
	private String host;
	private String port;
	private String database;
	private String user;
	private String password;
	private String table;
    private Connection connection;
    private Statement statement;
    private TypeField[] fields;
    
	public SQLUtil (TypeField... fields) {
		this.fields = fields;
	}
	
	public void init(String host, String port, String database, String user, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
    public boolean connect() {
    	if (connection != null) {
    		close();
    	}
    	String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8&useSSL=false&autoReconnect=true";
    	try {
			connection  = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
		} catch (SQLException e) {
			return false;
		}
    	return true;
    }
    
    public void close() {
    	if (connection != null) {
        	try {
        		connection.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public void createTable(String table) {
    	this.table = table;
        try {
        	connection.prepareStatement("create table if not exists " + table + "(" + getFields() + ")").executeUpdate();
        	connection.prepareStatement("alter table " + table + " convert to character set utf8");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isExist(String key, String keydata) {
        try {
            return statement.executeQuery("select 1 from " + table + " where " + key + "='" + keydata + "'").next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void deleteData(String key, String keyData) {
        try {
            PreparedStatement preparedstatement = connection.prepareStatement("delete from " + table + " where " + key + "=?");
            preparedstatement.setString(1, keyData);
            preparedstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Object getData(String key, String keydata, String value) {
        try {
        	ResultSet resultSet = statement.executeQuery("select " + value + " from " + table + " where " + key + "='" + keydata + "'");
            if (resultSet.next()) {
                Object data = resultSet.getObject(1);
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void addData(String key, Object... data) {
        try {
            PreparedStatement preparedstatement = connection.prepareStatement("select * from " + table + " where " + key + "=?");
            preparedstatement.setObject(1, data[0]);
            ResultSet resultSet = preparedstatement.executeQuery();
            if (resultSet.next()) {
            	preparedstatement = connection.prepareStatement("update " + table + " " + getSettingSentence(data) + " where " + key + "=?");
            	preparedstatement.setObject(1, data[0]);
            	preparedstatement.executeUpdate();
            } else {
            	connection.prepareStatement("insert into " + table + " values(" + getInsertSentence(data) + ")").executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setData(String key, String keydata, String value, Object data) {
        try {
        	ResultSet resultSet = statement.executeQuery("select 1 from " + table + " where " + key + "='" + keydata + "'");
            if (!resultSet.next()) {
                statement.executeUpdate("insert into " + table + " values('" + keydata + "','" + data + "')");
            } else {
            	resultSet = statement.executeQuery("select " + value + " from " + table + " where " + key + "='" + keydata + "'");
                statement.executeUpdate("update " + table + " set " + value + "='" + data + "' where " + key + "='" + keydata + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String getFields() {
        String str = "";
        for (TypeField field : fields) {
        	str = str + ((str.length() > 0 ? "," : "") + field.getField() + " " + field.getType());
        }
        return str;
    }
    
    private String getSettingSentence(Object... data) {
    	String str = "";
    	for (int i = 0; i < fields.length; ++i) {
    		str = str + (str.length() > 0 ? " , " : "" ) + "set " + fields[i].getField() + "='" + data[i] + "'";
    	}
        return str;
    }
    
    private String getInsertSentence(Object... data) {
    	String str = "";
    	for (int i = 0; i < data.length; ++i) {
    		str = str + (str.length() > 0 ? "," : "" ) + "'" + data[i] + "'";
    	}
        return str;
    }
    
    public String getTable() {
		return table;
    }
    
    public void setTable(String table) {
    	this.table = table;
    }
    
    public Connection getConnection() {
		return connection;
    }
    
    public Statement getStatement() {
		return statement;
    }
    
    public ResultSet executeQuery(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
}
