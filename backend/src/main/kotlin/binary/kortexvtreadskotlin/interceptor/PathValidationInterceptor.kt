package binary.kortexvtreadskotlin.interceptor

import binary.kortexvtreadskotlin.config.AllowedPathsProperties
import binary.kortexvtreadskotlin.config.PathConfig
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class PathValidationInterceptor(
    private val allowedPathsProperties: AllowedPathsProperties
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val uri = request.requestURI

        if (!uri.startsWith("/api/path/")) {
            handleNotFound(request, response)
            return false
        }

        val key = uri.removePrefix("/api/path/").trim('/')
        val pathConfig = allowedPathsProperties.paths[key]

        return if (pathConfig != null) {
            handleConfiguredPath(request, response, pathConfig)
            false
        } else {
            handleNotFound(request, response)
            false
        }
    }

    private fun handleConfiguredPath(
        request: HttpServletRequest,
        response: HttpServletResponse,
        config: PathConfig
    ) {
        response.characterEncoding = "UTF-8"

        val method = request.method
        if (config.allowedMethods.isNotEmpty() && !config.allowedMethods.contains(method)) {
            response.status = HttpStatus.METHOD_NOT_ALLOWED.value()
            response.writer.write("{\"error\":\"Method $method not allowed for this path\"}")
            return
        }

        response.status = config.httpStatus.value()

        val responseBody = mutableMapOf<String, Any>(
            "message" to config.message,
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        )

        config.targetUrl?.let {
            responseBody["targetUrl"] = it
        }
        response.contentType = "application/json"
        response.writer.write(convertToJson(responseBody))
    }

    private fun handleNotFound(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        response.characterEncoding = "UTF-8"
        response.status = HttpStatus.NOT_FOUND.value()
        val responseBody = mapOf(
            "message" to "Путь не найден",
            "path" to request.requestURI,
            "timestamp" to System.currentTimeMillis()
        )
        response.contentType = "application/json"
        response.writer.write(convertToJson(responseBody))
    }

    private fun convertToJson(map: Map<String, Any>): String {
        return map.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) ->
            "\"$key\":${if (value is String) "\"$value\"" else value}"
        }
    }
}