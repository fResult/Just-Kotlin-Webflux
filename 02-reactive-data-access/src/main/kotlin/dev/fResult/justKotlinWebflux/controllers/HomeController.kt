package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.reactive.result.view.Rendering
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicLong

@Controller
class HomeController(
  private val database: MutableMap<Long, Employee> = mutableMapOf(
    1L to Employee(1L, "John Wick", "Assassin"),
    2L to Employee(2L, "John Constantine", "Exorcist"),
    3L to Employee(3L, "Johnny Mnemonic", "Data Courier"),
  ),
  private val idGenerator: AtomicLong = AtomicLong(database.size.toLong())
) {
  @GetMapping
  fun index(): Mono<Rendering> = Flux.fromIterable(database.values).collectList().map {
    Rendering.view("index")
      .modelAttribute("employees", it)
      .modelAttribute("newEmployee", Employee("", ""))
      .build()
  }

  @PostMapping("/new-employee")
  fun newEmployee(@ModelAttribute newEmployee: Mono<Employee>): Mono<String> = newEmployee
    .doOnNext { employee ->
      val id = idGenerator.incrementAndGet()
      database[id] = Employee(id, employee.name, employee.role)
    }.thenReturn("redirect:/")
}
