package com.ganesh.divekar.utils


interface Mapper<Entity, Model> {

    fun from(model: Model): Entity

    fun to(entity: Entity): Model

}
