package dev.fResult.justKotlinWebflux.controllers

import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableHypermediaSupport(type = [HypermediaType.HAL])
class HypermediaController {}
