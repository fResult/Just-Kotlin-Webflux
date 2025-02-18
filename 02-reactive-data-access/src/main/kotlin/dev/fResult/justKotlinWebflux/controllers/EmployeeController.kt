package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.dtos.EmployeeUpdateRequest
import dev.fResult.justKotlinWebflux.entities.Employee
import dev.fResult.justKotlinWebflux.repositories.EmployeeRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.net.URI

@RestController
@RequestMapping("/api/employees")
class EmployeeController(private val employeeRepository: EmployeeRepository) {
  @GetMapping
  fun all(): Mono<ResponseEntity<List<Employee>>> = employeeRepository.findAll()
    .collectList().map { ResponseEntity.ok(it) }

  @GetMapping("/{id}")
  fun byId(@PathVariable id: Long): Mono<ResponseEntity<Employee>> = employeeRepository.findById(id)
    .map { ResponseEntity.ok(it) }
    .switchIfEmpty(::toResponseNotFoundMono)

  @PostMapping
  fun create(@RequestBody body: Employee): Mono<ResponseEntity<Employee>> =
    // NOTE: This is working around because the body is not deserialized using Mono<Employee> in Kotlin (but Java works)
    Mono.just(body).flatMap(employeeRepository::save)
      .map { ResponseEntity.created(URI.create("/api/employees/${it.id}")).body(it) }

  @PatchMapping("/{id}")
  fun update(@PathVariable id: Long, @RequestBody body: EmployeeUpdateRequest): Mono<ResponseEntity<Employee>> =
    employeeRepository.findById(id)
      .flatMap {
        val employeeToUpdate = Employee(
          it.id,
          body.name ?: it.name,
          body.role ?: it.role
        )
        employeeRepository.save(employeeToUpdate)
      }
      .map { ResponseEntity.ok(it) }
      .switchIfEmpty(::toResponseNotFoundMono)

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: Long): Mono<ResponseEntity<Void>> =
    employeeRepository.deleteById(id)
      .map { ResponseEntity.noContent().build<Void>() }
      .switchIfEmpty(::toResponseNotFoundMono)

  private fun <T> toResponseNotFoundMono(): Mono<ResponseEntity<T>> {
    return Mono.just(ResponseEntity.notFound().build())
  }
}
