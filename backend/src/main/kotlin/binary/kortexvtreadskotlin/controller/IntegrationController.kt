package binary.kortexvtreadskotlin.controller

import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/path")
class IntegrationController(
    private val allowedPathsProperties: AllowedPathsProperties
) {

    @RequestMapping("/{resource}")
    fun handleRequest(
        @PathVariable resource: String,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        val config = allowedPathsProperties.path[resource]
            ?: return ResponseEntity.notFound().build()

        val response = mutableMapOf<String, Any>(
            "message" to config.message,
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        )
        config.targetUrl?.let { response["targetUrl"] = it }

        return ResponseEntity.status(config.httpStatus).body(response)
    }
}