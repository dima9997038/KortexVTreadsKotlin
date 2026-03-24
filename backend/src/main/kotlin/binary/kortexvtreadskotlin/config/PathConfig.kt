package binary.kortexvtreadskotlin.config

import org.springframework.http.HttpStatus

data class PathConfig(
    val message: String,
    val httpStatus: HttpStatus,
    val allowedMethods: List<String>,
    val targetUrl: String? = null      
)