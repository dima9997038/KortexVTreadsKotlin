package binary.kortexvtreadskotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class KortexVTreadsKotlinApplication

fun main(args: Array<String>) {
    runApplication<KortexVTreadsKotlinApplication>(*args)
}
