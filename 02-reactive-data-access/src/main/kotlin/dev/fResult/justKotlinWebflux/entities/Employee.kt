package dev.fResult.justKotlinWebflux.entities

import org.springframework.data.annotation.Id

data class Employee(@Id val id: Long?, val name: String, val role: String) {
  constructor(name: String, role: String) : this(null, name, role)
}
