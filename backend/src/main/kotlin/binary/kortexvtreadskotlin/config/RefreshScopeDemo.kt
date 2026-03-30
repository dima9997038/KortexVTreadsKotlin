package binary.kortexvtreadskotlin.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.event.EventListener
import org.springframework.cloud.context.environment.EnvironmentChangeEvent
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

/**
 * Demonstrates runtime configuration refresh.
 *
 * How it works
 * ─────────────
 * 1. Change a property in the git config repo (e.g. kortex.feature-flag=true).
 * 2. POST to /actuator/refresh on this service:
 *      curl -X POST http://localhost:<port>/actuator/refresh
 * 3. Spring re-fetches the latest properties from the Config Server.
 * 4. Because this bean is @RefreshScope, it is destroyed and lazily
 *    re-created on next access — picking up the new @Value values.
 *
 * @ConfigurationProperties beans (like AllowedPathsProperties) are refreshed
 * automatically without needing @RefreshScope.
 */
@Service
@RefreshScope
class RefreshScopeDemo(

    /**
     * Sourced from the Config Server (git repo).
     * Default value is used when the config server is unreachable
     * or the property is not defined there.
     */
    @Value("\${kortex.feature-flag:false}")
    val featureFlag: Boolean,

    @Value("\${kortex.rate-limit:100}")
    val rateLimit: Int,

    @Value("\${kortex.greeting:Hello}")
    val greeting: String

) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun onRefresh() {
        // Called once on startup AND every time @RefreshScope re-creates this bean.
        log.info(
            "Config loaded → featureFlag=$featureFlag, rateLimit=$rateLimit, greeting='$greeting'"
        )
    }
}

/**
 * Listens to EnvironmentChangeEvent which fires AFTER the Environment is
 * updated but BEFORE @RefreshScope beans are re-created.
 *
 * Useful when you need to react to a specific property change — e.g. to
 * invalidate a cache, close a connection pool, or log an audit entry.
 */
@Service
class ConfigChangeListener(
    private val demo: RefreshScopeDemo        // lazily proxied — safe to inject
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun onEnvironmentChange(event: EnvironmentChangeEvent) {
        val changed = event.keys
        log.info("Config properties changed: $changed")

        if ("kortex.rate-limit" in changed) {
            // Example: reset rate-limiter state, update Bucket4j config, etc.
            log.info("Rate-limit changed to ${demo.rateLimit} — apply downstream effects here")
        }

        if ("kortex.feature-flag" in changed) {
            log.info("Feature flag is now ${demo.featureFlag}")
        }
    }
}