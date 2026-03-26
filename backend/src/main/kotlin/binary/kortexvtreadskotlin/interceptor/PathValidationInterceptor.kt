package binary.kortexvtreadskotlin.interceptor

import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import binary.kortexvtreadskotlin.config.PathConfig
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class PathValidationInterceptor(
    private val allowedPathsProperties: AllowedPathsProperties,
    private val objectMapper: ObjectMapper
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val uri = request.requestURI

        // "/api/product/step" → ["api", "product", "step"]
        val parts = uri.trim('/').split("/")

        // must be exactly /api/{category}/{segment}
        if (parts.size < 3 || parts[0] != "api") {
            handleNotFound(request, response)
            return false
        }

        val category = parts[1]  // e.g. "product", "path"
        val segment  = parts[2]  // e.g. "step", "users"

        val pathConfig = allowedPathsProperties.routes[category]?.get(segment)

        if (pathConfig == null) {
            handleNotFound(request, response)
            return false
        }

        handleConfiguredPath(request, response, pathConfig)
        return false
    }

    private fun handleConfiguredPath(
        request: HttpServletRequest,
        response: HttpServletResponse,
        config: PathConfig
    ) {
        val method = request.method
        if (config.allowedMethods.isNotEmpty() && !config.allowedMethods.contains(method)) {
            writeJson(response, HttpStatus.METHOD_NOT_ALLOWED, mapOf(
                "error" to "Method $method not allowed for this path",
                "path" to request.requestURI,
                "timestamp" to System.currentTimeMillis()
            ))
            return
        }

        val responseBody = mutableMapOf<String, Any>(
            "message" to config.message,
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        )
        config.targetUrl?.let { responseBody["targetUrl"] = it }

        writeJson(response, config.httpStatus, responseBody)
    }

    private fun handleNotFound(request: HttpServletRequest, response: HttpServletResponse) {
        writeJson(response, HttpStatus.NOT_FOUND, mapOf(
            "message" to "Путь не найден",
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        ))
    }

    private fun writeJson(response: HttpServletResponse, status: HttpStatus, body: Map<String, Any>) {
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json;charset=UTF-8"
        response.status = status.value()
        response.writer.write(objectMapper.writeValueAsString(body))
    }
}