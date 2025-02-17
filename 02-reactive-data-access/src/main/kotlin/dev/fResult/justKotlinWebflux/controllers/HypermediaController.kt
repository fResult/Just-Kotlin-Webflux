package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.atomic.AtomicLong

/*
 * IMPORTANT NOTE:
 * The `@EnableHypermediaSupport` annotation only has to be used once.
 * We happen to be putting on our hypermedia controller for brevity in this book.
 * In a real application, it may be preferable to put it in the same class that has the `@SpringBootApplication` annotation.
 */
@RestController
@RequestMapping("/hypermedia/employees")
@EnableHypermediaSupport(type = [HypermediaType.HAL])
class HypermediaController(
  private val database: MutableMap<Long, Employee> = mutableMapOf(
    1L to Employee(1L, "John Wick", "Assassin"),
    2L to Employee(2L, "John Constantine", "Exorcist"),
    3L to Employee(3L, "Johnny Mnemonic", "Data Courier"),
  ),
  private val idGenerator: AtomicLong = AtomicLong(database.size.toLong())
) {
  @GetMapping
  fun allEmployees(): Mono<CollectionModel<EntityModel<Employee>>> {
    val selfLinkMono = linkTo(methodOn(this::class.java).allEmployees()).withSelfRel().toMono()

    return selfLinkMono.flatMap(::toEmployeeEntityModelCollectionMono)
  }

  @GetMapping("/{id}")
  fun employeeById(@PathVariable id: Long): Mono<EntityModel<Employee>> {
    val selfLinkMono = linkTo(methodOn(this::class.java).employeeById(id)).withSelfRel().toMono()
    val aggregateRootLinkMono = linkTo(methodOn(this::class.java).allEmployees()).withRel("employees").toMono()
    val linksMono = Mono.zip(selfLinkMono, aggregateRootLinkMono) { self, aggregateRoot -> self to aggregateRoot }

    return linksMono.flatMap(toEmployeeEntityModelMono(id))
  }

  private fun toEmployeeEntityModelCollectionMono(link: Link): Mono<CollectionModel<EntityModel<Employee>>> {
    val toEntityModelCollection: (List<EntityModel<Employee>>) -> CollectionModel<EntityModel<Employee>> =
      { entityModels -> CollectionModel.of(entityModels, link) }

    return Flux.fromIterable(database.keys)
      .flatMap(::employeeById).collectList()
      .map(toEntityModelCollection)
  }

  private fun toEmployeeEntityModelMono(id: Long): (Pair<Link, Link>) -> Mono<EntityModel<Employee>> = { linksPair ->
    val toEntityModel: (Employee) -> Mono<EntityModel<Employee>> = { employee ->
      Mono.just(EntityModel.of(employee, linksPair.first, linksPair.second))
    }

    database[id]?.let(toEntityModel)
      ?: Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with ID $id not found"))
  }
}
