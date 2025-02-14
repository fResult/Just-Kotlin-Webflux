package dev.fResult.justKotlinWebflux

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/employees")
class EmployeeController(
  // In-memory database
  private val database: MutableMap<Long, Employee> = mutableMapOf(
    1L to Employee(1L, "John Wick", "Assassin"),
    2L to Employee(2L, "John Constantine", "Exorcist"),
    3L to Employee(3L, "Johnny Mnemonic", "Data Courier"),
  ),
  private val idGenerator: AtomicLong = AtomicLong(database.size.toLong()),
) {
  @GetMapping
  fun employees(): Flux<Employee> {
    return Flux.fromIterable(database.values)
  }

  @PostMapping
  fun newEmployee(@RequestBody newEmployee: Mono<Employee>): Mono<Employee> {
    val id = idGenerator.incrementAndGet()

    return newEmployee.map { it.copy(id = id) }.doOnNext { database[id] = it }
  }
}
