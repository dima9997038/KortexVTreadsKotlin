package binary.kortexvtreadskotlin.config

import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.io.IOException

class YamlPropertySourceFactory : PropertySourceFactory {
    @Throws(IOException::class)
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val yamlLoader = YamlPropertySourceLoader()
        val sources = yamlLoader.load(resource.resource.filename ?: "allowed-paths", resource.resource)
        return sources.first()
    }
}