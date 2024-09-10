package example.bookify.server.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class MessageConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            @NonNull
            public Locale resolveLocale(@NonNull HttpServletRequest request) {
                return Locale.ENGLISH;
            }
        };
    }

    @Bean
    public MessageSource bundleMessageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        return messageSource;
    }
}
