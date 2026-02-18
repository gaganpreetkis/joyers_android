package com.joyersapp.feature.post.domain.usecase

import com.devscion.metaprobe.MetaProbe
import com.devscion.metaprobe.model.ProbedData
import com.joyersapp.feature.post.domain.model.LinkMetaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLinkMetadataUseCase @Inject constructor() {
    suspend operator fun invoke(url: String): Result<ProbedData> {
        return withContext(Dispatchers.IO) {
            val metaProbe = MetaProbe(url)
            metaProbe.probeLink() // Returns Result<ProbedData>
        }
    }
}

fun ProbedData.toLinkMetaData(): LinkMetaData {
    return LinkMetaData(
        title = this.title,
        description = this.description,
        icon = this.icon,
        image = this.image

    )
}