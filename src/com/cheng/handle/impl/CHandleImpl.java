package com.cheng.handle.impl;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.sql.DataSource;

import com.cheng.annotation.CConstraints;
import com.cheng.annotation.CField;
import com.cheng.annotation.CTable;
import com.cheng.common.CFieldType;
import com.cheng.handle.CHandle;
import com.cheng.handle.CTypeHandle;
import com.cheng.util.CommonUtil;

public class CHandleImpl implements CHandle{
	
	
	
	public static EnumMap<CFieldType, CTypeHandle> fieldHandleMap = new EnumMap<>(CFieldType.class);
	
	static{
		fieldHandleMap.put(CFieldType.INTEGER, new CIntegerFieldTypeHandle());
		fieldHandleMap.put(CFieldType.STRING, new CStringFieldTypeHandle());
	}
	
	private DataSource dataSource;
	
	public CHandleImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void create(Class<?> clazz) {
		CTable table = clazz.getAnnotation(CTable.class);
		if(table == null){
			return;
		}
		StringBuffer createBuffer = new StringBuffer("CREATE TABLE ");
		String tableName = CommonUtil.getTableName(table.name(), clazz);
		List<String> keyList = new ArrayList<>();
		createBuffer.append(tableName);
		createBuffer.append("(");
		for(Field field : clazz.getDeclaredFields()){
			CField detailField = field.getAnnotation(CField.class);
			CFieldType type = detailField.type();
			CHandleImpl.fieldHandleMap.get(type).createField(createBuffer, detailField, field);
			this.dealConstraints(detailField.constaints(), createBuffer);
			this.dealKeyList(detailField.constaints(),CommonUtil.getFieldName(detailField.name(), field),keyList);
			createBuffer.append(",");
		}
		this.componentKey(createBuffer,keyList);
		createBuffer = createBuffer.deleteCharAt(createBuffer.length()-1);
		createBuffer.append(")");
		//TODO ¥Ú”°sql
		System.out.println(createBuffer.toString());
		CommonUtil.excuteSql(createBuffer.toString(), dataSource);
		
		
	}
	private void componentKey(StringBuffer createBuffer, List<String> keyList) {
		if(!keyList.isEmpty()){
			createBuffer.append("PRIMARY KEY (");
			for(int i = 0;i<keyList.size();i++){
				String key = keyList.get(i);
				createBuffer.append("'"+key+"'");
				if(i!=keyList.size()-1){
					createBuffer.append(",");
				}
			}
			createBuffer.append("),");
		}
		
	}

	private void dealKeyList(CConstraints constaints, String fieldName, List<String> keyList) {
		if(constaints.primaryKey()){
			keyList.add(fieldName);
		}
		
	}

	private void dealConstraints(CConstraints constraints,StringBuffer sb){
		if(!constraints.allowNull()){
			sb.append(" NOT NULL");
		}
		if(constraints.unique()){
			sb.append(" UNIQUE");
		}
	}


	@Override
	public void update(Class<?> clazz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> List<T> queryAll(Class<T> clazz) {
		List<T> resultList = new ArrayList<>();
//		resultList.add(clazz.newInstance());
		CTable table = clazz.getAnnotation(CTable.class);
		if(table ==null){
			return null;
		}
		String tableName = CommonUtil.getTableName(table.name(), clazz);
		StringBuffer queryAllBuffer = new StringBuffer("SELECT * FROM ");
		queryAllBuffer.append(tableName);
		queryAllBuffer.append(" WHERE 1=1");
		ResultSet resultSet = CommonUtil.exceteQuerySql(queryAllBuffer.toString(), dataSource);
		try {
			while(resultSet.next()){
				T object = clazz.newInstance();
				resultList.add(object);
				dealObject(object,resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	
	private <T> void dealObject(T object, ResultSet resultSet) {
		Class<?> clazz = object.getClass();
		for(Field field : clazz.getDeclaredFields()){
			CField detailField = field.getAnnotation(CField.class);
			if(detailField ==  null){
				continue;
			}
			CHandleImpl.fieldHandleMap.get(detailField.type()).queryAll(detailField,object,field,resultSet);
		}
		
	}

	@Override
	public <T> T queryById(Class<T> clazz, Object id) {
		return null;
	}

	@Override
	public void delete(Class<?> clazz) {
		// TODO Auto-generated method stub
		
	}


}
