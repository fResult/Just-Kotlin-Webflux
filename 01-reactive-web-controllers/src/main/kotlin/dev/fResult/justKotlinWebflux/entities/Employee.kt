package dev.fResult.justKotlinWebflux.entities

data class Employee(val id: Long?, val name: String, val role: String) {
  init {
    require(name.isNotBlank()) { "Name must not be blank" }
    require(role.isNotBlank()) { "Role must not be blank" }
  }

  constructor(name: String, role: String) : this(null, name, role)
}
