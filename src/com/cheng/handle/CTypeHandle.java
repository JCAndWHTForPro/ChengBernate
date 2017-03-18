package com.cheng.handle;

import java.lang.reflect.Field;
import java.sql.ResultSet;

import com.cheng.annotation.CField;

public interface CTypeHandle {

	public void createField(StringBuffer sb,CField detailField, Field field);

	public <T> void queryAll(CField detailField, T object,Field field, ResultSet resultSet);
}
