package example.bookify.server.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Value("${app.date-format:dd-MM-yyyy}")
    private String dateFormat;

    @Value("${app.date-time-format:dd-MM-yyyy HH:mm:ss}")
    private String dateTimeFormat;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .simpleDateFormat(dateTimeFormat)
                .serializers(
                        new LocalDateSerializer(DateTimeFormatter.ISO_DATE),
                        new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME)
                )
                .deserializers(
                        new LocalDateDeserializer(DateTimeFormatter.ISO_DATE),
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME)
                );
    }
}
