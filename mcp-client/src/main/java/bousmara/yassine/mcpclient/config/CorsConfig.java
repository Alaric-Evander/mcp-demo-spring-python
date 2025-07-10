package bousmara.yassine.mcpclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins(

                        "http://localhost:8081",

                        "http://127.0.0.1:8081",

                        "http://192.168.1.5:8081"  // Add this line

                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Content-Type")
                .allowCredentials(true);

    }

}