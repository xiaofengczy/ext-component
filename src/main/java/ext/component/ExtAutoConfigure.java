package ext.component;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ExtWebFluxConfig.class)
public class ExtAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    ExtWebFluxConfig exampleService (){
        return  new ExtWebFluxConfig();
    }

}

