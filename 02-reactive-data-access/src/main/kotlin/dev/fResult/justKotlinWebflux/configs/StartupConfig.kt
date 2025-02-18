package dev.fResult.justKotlinWebflux.configs

import dev.fResult.justKotlinWebflux.entities.Employee
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.test.StepVerifier

@Configuration
class StartupConfig {
  @Bean
  fun initEmployees(template: R2dbcEntityTemplate): CommandLineRunner {
    return CommandLineRunner { _ ->
      template.databaseClient.sql(
        """
        CREATE TABLE IF NOT EXISTS employees (
          id IDENTITY NOT NULL PRIMARY KEY,
          name VARCHAR(255),
          role VARCHAR(255))
        """
      )
        .fetch()
        .rowsUpdated()
        .`as`(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete()

      val insertEmployeeToTable = insertEmployee(template)
      insertEmployeeToTable(Employee("John Wick", "Assassin"))
      insertEmployeeToTable(Employee("John Constantine", "Exorcist"))
      insertEmployeeToTable(Employee("Johnny Mnemonic", "Data Courier"))
    }
  }

  private fun insertEmployee(template: R2dbcEntityTemplate): (Employee) -> Unit = { employee ->
    template.insert(Employee::class.java)
      .using(employee)
      .`as`(StepVerifier::create)
      .expectNextCount(1)
      .verifyComplete()
  }
}
