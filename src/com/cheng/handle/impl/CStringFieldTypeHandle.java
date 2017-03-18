package com.cheng.handle.impl;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import com.cheng.annotation.CField;
import com.cheng.handle.CTypeHandle;
import com.cheng.util.CommonUtil;

public class CStringFieldTypeHandle implements CTypeHandle {

	@Override
	public void createField(StringBuffer sb, CField detailField, Field field) {
		String fieldName = CommonUtil.getFieldName(detailField.name(),field);
		sb.append("'"+fieldName+"'");
		sb.append(" VARCHAR(");
		int length = detailField.length();
		sb.append(length);
		sb.append(")");
	}

	@Override
	public <T> void queryAll(CField detailField,T object, Field field, ResultSet resultSet) {
		try {
			String fieldName = CommonUtil.getFieldName(detailField.name(), field);
			String result = resultSet.getString(fieldName);
			field.setAccessible(true);
			field.set(object, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}




}
