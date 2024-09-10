package example.bookify.server.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class BeanUtil {

    @SneakyThrows
    public void copyNonNullProperties(Object source, Object target) {
        Class<?> cls = source.getClass();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(source);

            if (value != null) {
                field.set(target, value);
            }
        }
    }
}
