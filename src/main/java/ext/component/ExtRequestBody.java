package ext.component;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@EnableAutoConfiguration
public @interface ExtRequestBody {

}
