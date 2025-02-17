package dev.fResult.justKotlinWebflux.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("employees")
data class Employee(@Id val id: Long?, val name: String, val role: String) {
  constructor(name: String, role: String) : this(null, name, role)
}
