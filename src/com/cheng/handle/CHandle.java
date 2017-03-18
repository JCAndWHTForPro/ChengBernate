package com.cheng.handle;

import java.util.List;

public interface CHandle {

	public void create(Class<?> clazz);
	public void update(Class<?> clazz);
	public <T> List<T> queryAll(Class<T> clazz);
	public <T> T queryById(Class<T> clazz,Object id);
	public void delete(Class<?> clazz);
}
