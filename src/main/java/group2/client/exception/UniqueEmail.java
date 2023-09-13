/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package group2.client.exception;

import group2.client.exception.UniqueEmailValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
/**
 *
 * @author Ann
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "Email must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
