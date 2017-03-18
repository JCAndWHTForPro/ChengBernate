package com.cheng.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class CommonUtil {
	public static String getFieldName(String name,Field field){
		if(name ==null || "".equals(name)){
			return field.getName().toUpperCase();
		}
		return name.toUpperCase();
	}
	public static String getTableName(String name,Class<?> clazz){
		if(name == null || "".equals(name)){
			return clazz.getName().toUpperCase();
		}
		return name.toUpperCase();
	}
	public static void excuteSql(String sql,DataSource dataSource){
		try(Connection conn = dataSource.getConnection();
			Statement stat = conn.createStatement()){
			stat.execute(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static ResultSet exceteQuerySql(String sql,DataSource dataSource){
		ResultSet result= null;
		try(Connection conn = dataSource.getConnection();
			Statement stat = conn.createStatement()){
			result = stat.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
}
