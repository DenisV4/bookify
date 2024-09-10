package example.bookify.server.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or hasRole('USER') and #id == principal.id and #request.roles == null")
public @interface AdminOrTargetUser {
}
