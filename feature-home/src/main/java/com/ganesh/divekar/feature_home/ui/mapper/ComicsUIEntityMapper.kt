package com.ganesh.divekar.feature_home.ui.mapper

import com.ganesh.divekar.feature_home.ui.models.ComicsUIModel
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.utils.Mapper

class ComicsUIEntityMapper : Mapper<ComicsEntity, ComicsUIModel> {
    override fun from(model: ComicsUIModel): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): ComicsUIModel {
        return ComicsUIModel(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }

}