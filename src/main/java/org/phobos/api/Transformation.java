package org.phobos.api;

import java.lang.annotation.Repeatable;
import org.phobos.type.NoneNotNullEnum;
import org.phobos.type.NoneNotNullEnum2;

@Repeatable(Transformations.class)
public @interface Transformation {
   NoneNotNullEnum[] method() default {NoneNotNullEnum.None};

   Class<?> type() default Class.class;

   String value();

   Class<?>[] insertion() default {Class.class};

   NoneNotNullEnum2[] field() default {NoneNotNullEnum2.None};
}
