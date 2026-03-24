package binary.kortexvtreadskotlin.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "allowed")
@PropertySource(value = ["classpath:allowed-paths.yaml"], factory = YamlPropertySourceFactory::class)
class AllowedPathsProperties {
    lateinit var paths: Map<String, PathConfig>
}