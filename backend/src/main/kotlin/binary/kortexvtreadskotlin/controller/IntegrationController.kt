package binary.kortexvtreadskotlin.controller

import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class IntegrationController(
    private val allowedPathsProperties: AllowedPathsProperties
) {

    @RequestMapping("/{category}/{segment}")
    fun handleRequest(
        @PathVariable category: String,
        @PathVariable segment: String,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        val config = allowedPathsProperties.routes[category]?.get(segment)
            ?: return ResponseEntity.notFound().build()

        val responseBody = mutableMapOf<String, Any>(
            "message" to config.message,
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        )
        config.targetUrl?.let { responseBody["targetUrl"] = it }

        return ResponseEntity.status(config.httpStatus).body(responseBody)
    }
}