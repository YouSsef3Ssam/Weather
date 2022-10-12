package com.youssef.weather.framework.datasources.local.mappers

interface EntityMapper <Entity, Response>{

    fun mapFromEntity(entity: Entity): Response

    fun mapToEntity(response: Response): Entity
}