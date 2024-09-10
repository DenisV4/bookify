package example.bookify.server.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@UtilityClass
public class FileUtil {

    public static ByteArrayOutputStream beansToCsv(Class<?> clazz, List<?> data)
            throws IOException, CsvFieldAssignmentException {

        try (var byteArrayOut = new ByteArrayOutputStream();
             var writer = new CSVWriter(new OutputStreamWriter(byteArrayOut))) {

            var fields = clazz.getDeclaredFields();
            var headers = getCsvHeaders(fields);

            writer.writeNext(headers.toArray(new String[0]));

            if (data != null) {
                for (Object obj : data) {
                    var line = new String[fields.length];

                    int lineIndex = 0;
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(CsvIgnore.class)) {
                            continue;
                        }

                        field.setAccessible(true);
                        try {
                            Object value = field.get(obj);
                            line[lineIndex++] = value != null ? value.toString() : "";
                        } catch (IllegalAccessException exc) {
                            throw new RuntimeException(exc);
                        }
                    }

                    writer.writeNext(line);
                }
            }

            writer.flush();
            return byteArrayOut;
        }
    }

    public static void zip(String fileName, ByteArrayOutputStream byteArrayOut, ZipOutputStream zipOut)
            throws IOException {

        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.write(byteArrayOut.toByteArray());
        zipOut.closeEntry();
    }

    private static List<String> getCsvHeaders(Field[] fields) {
        List<String> headers = new ArrayList<>();

        Arrays.stream(fields).forEach(field -> {
            if (!field.isAnnotationPresent(CsvIgnore.class)) {
                var annotation = field.getAnnotation(CsvBindByName.class);
                if (annotation != null) {
                    headers.add(annotation.column());
                } else {
                    headers.add(field.getName());
                }
            }
        });

        return headers;
    }
}
