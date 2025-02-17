package dev.fResult.justKotlinWebflux.repositories

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface EmployeeRepository : ReactiveCrudRepository<Employee, Long>
