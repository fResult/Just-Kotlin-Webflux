package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.entities.Employee
import dev.fResult.justKotlinWebflux.repositories.EmployeeRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@RestController
@RequestMapping("/api/employees")
class EmployeeController(private val employeeRepository: EmployeeRepository) {
  @GetMapping
  fun all(): Mono<ResponseEntity<List<Employee>>> = employeeRepository.findAll()
    .collectList().map { ResponseEntity.ok(it) }

  @GetMapping("/{id}")
  fun byId(@PathVariable id: Long): Mono<ResponseEntity<Employee>> = employeeRepository.findById(id)
    .map {
      ResponseEntity.ok(it)
    }
    .switchIfEmpty { Mono.just(ResponseEntity.notFound().build()) }
}
