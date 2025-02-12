package dev.fResult.justKotlinWebflux

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/employees")
class EmployeeController {
  @GetMapping
  fun employees(): Flux<Employee> {
    return Flux.just(
      Employee("John Wick", "Assassin"),
      Employee("John Constantine", "Exorcist"),
      Employee("Johnny Mnemonic", "Data Courier")
    )
  }
}
