package binary.kortexvtreadskotlin.controller


import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import binary.kortexvtreadskotlin.config.PathConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/config")
class PathConfigController(
    private val allowedPathsProperties: AllowedPathsProperties
) {

    @GetMapping("/paths")
    fun getAllPaths(): Map<String, PathConfig> {
        return allowedPathsProperties.paths
    }
}