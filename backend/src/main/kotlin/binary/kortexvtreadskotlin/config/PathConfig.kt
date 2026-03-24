package binary.kortexvtreadskotlin.config

import org.springframework.http.HttpStatus

data class PathConfig(
    val message: String,
    val httpStatus: HttpStatus,          // Spring автоматически преобразует строку из YAML в enum
    val allowedMethods: List<String>,    // список разрешённых HTTP-методов
    val targetUrl: String? = null        // опциональное поле, может отсутствовать
)