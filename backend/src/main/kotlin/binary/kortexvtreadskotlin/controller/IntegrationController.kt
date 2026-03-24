package binary.kortexvtreadskotlin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class IntegrationController {

    @GetMapping("/path/{variable}")
    fun integrationRestGet(@PathVariable variable: String): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "status" to "success",
            "path" to variable,
            "timestamp" to System.currentTimeMillis()
        )
        return ResponseEntity.ok(response)
    }
}