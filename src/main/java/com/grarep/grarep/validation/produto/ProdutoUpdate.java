package com.grarep.grarep.validation.produto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProdutoUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ProdutoUpdate {
        String message() default "Erro de validação";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
