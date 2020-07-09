package com.catCoder;

import java.lang.annotation.*;

/**
 * @author DELL
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
}
