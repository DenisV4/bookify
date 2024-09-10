package example.bookify.server.util;

import lombok.experimental.UtilityClass;

import java.util.StringJoiner;

@UtilityClass
public class ToStringUtil {

    public static String generateToString(Object obj) {
        var result = new StringJoiner(", ", obj.getClass().getSimpleName() + "(", ")");
        Class<?> currentClass = obj.getClass();

        while (currentClass != null) {
            for (var field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    var value = field.get(obj);
                    if (value != null) {
                        result.add(field.getName() + "=" + value);
                    }
                } catch (IllegalAccessException exc) {
                    throw new RuntimeException(exc);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return result.toString();
    }
}
