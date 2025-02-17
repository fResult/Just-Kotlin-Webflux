package dev.fResult.justKotlinWebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Webflux1Application

fun main(args: Array<String>) {
  runApplication<Webflux1Application>(*args)
}
