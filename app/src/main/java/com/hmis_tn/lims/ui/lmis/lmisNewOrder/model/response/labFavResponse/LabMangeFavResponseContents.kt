package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.labFavResponse

data class LabMangeFavResponseContents(
    val details: List<FavResponseDetail> = listOf(),
    val headers: FavResponseHeaders = FavResponseHeaders()
)