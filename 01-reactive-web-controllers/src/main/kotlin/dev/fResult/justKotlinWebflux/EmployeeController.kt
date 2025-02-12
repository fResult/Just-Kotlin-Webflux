package dev.fResult.justKotlinWebflux

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
  // In-memory database
  private val database: MutableMap<Long, Employee> = mutableMapOf(
    1L to Employee(1L, "John Wick", "Assassin"),
    2L to Employee(2L, "John Constantine", "Exorcist"),
    3L to Employee(3L, "Johnny Mnemonic", "Data Courier"),
  ),
) {
  @GetMapping
  fun employees(): Flux<Employee> {
    return Flux.fromIterable(database.values)
  }
}
