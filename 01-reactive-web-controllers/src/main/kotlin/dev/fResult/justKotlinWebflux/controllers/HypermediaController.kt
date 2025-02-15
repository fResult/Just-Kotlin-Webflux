package dev.fResult.justKotlinWebflux.controllers

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
  fun employees(): Mono<CollectionModel<EntityModel<Employee>>> {
    val selfLinkMono = linkTo(methodOn(this::class.java).employees()).withSelfRel().toMono()

    return selfLinkMono.flatMap { selfLink ->
      Flux.fromIterable(database.keys).flatMap(::employee).collectList()
        .map { entityModels -> CollectionModel.of(entityModels, selfLink) }
    }
  }

  @GetMapping("/{id}")
  fun employee(@PathVariable id: Long): Mono<EntityModel<Employee>> {
    val selfLinkMono = linkTo(methodOn(this::class.java).employee(id)).withSelfRel().toMono()
    val aggregateRootLinkMono = linkTo(methodOn(this::class.java).employees()).withRel("employees").toMono()
    val linksMono = Mono.zip(selfLinkMono, aggregateRootLinkMono)

    return linksMono.map { links ->
      database[id]?.let { employee -> EntityModel.of(employee, links.t1, links.t2) }
    }
  }
}
