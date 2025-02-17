package dev.fResult.justKotlinWebflux.entities

data class Employee(val id: Long?, val name: String, val role: String) {
  constructor(name: String, role: String) : this(null, name, role)
}
