package com.cheng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cheng.common.CFieldType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CField {
	CFieldType type() default CFieldType.STRING;
	String name() default "";
	int length() default 255;
	CConstraints constaints() default @CConstraints;

}
