package com.ziemniak.webcli.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Checks if two fields have equal value
 */
@Documented
@Constraint(validatedBy = EqualFieldsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualFields {
	String first();

	String second();

	String message() default "ok";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
