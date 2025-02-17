package dev.fResult.justKotlinWebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Webflux2Application

fun main(args: Array<String>) {
  runApplication<Webflux2Application>(*args)
}
