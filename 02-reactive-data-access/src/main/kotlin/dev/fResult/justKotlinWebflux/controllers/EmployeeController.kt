package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.entities.Employee
import dev.fResult.justKotlinWebflux.repositories.EmployeeRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/employees")
class EmployeeController(private val employeeRepository: EmployeeRepository) {
  @GetMapping
  fun all(): Mono<ResponseEntity<List<Employee>>> = employeeRepository.findAll()
    .collectList().map { ResponseEntity.ok(it) }
}
