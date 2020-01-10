package com.ziemniak.webserv.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {
	private String first;
	private String second;
	@Override
	public void initialize(EqualFields constraintAnnotation) {
		first = constraintAnnotation.first();
		second = constraintAnnotation.second();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		try {
			Object v1 = getValue(object, first);
			Object v2 = getValue(object, second);
			if(v1 == null && v2 == null)
				return true;
			if(v1 != null)
				return v1.equals(v2);
			else
				return v2.equals(v1);
		} catch (Exception e) {
			return false;
		}
	}

	private Object getValue(Object object, String fieldName) throws Exception {
		Class<?> clazz = object.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}
}
