package binary.kortexvtreadskotlin.config


import binary.kortexvtreadskotlin.interceptor.PathValidationInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val pathValidationInterceptor: PathValidationInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(pathValidationInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/config/**")
    }
}