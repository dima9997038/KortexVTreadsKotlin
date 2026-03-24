package binary.kortexvtreadskotlin.controller


import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = ["http://localhost:3000"])
class PathConfigController(
    private val allowedPathsProperties: AllowedPathsProperties
) {

    @GetMapping("/paths")
    fun getAllPaths() = allowedPathsProperties.paths
}