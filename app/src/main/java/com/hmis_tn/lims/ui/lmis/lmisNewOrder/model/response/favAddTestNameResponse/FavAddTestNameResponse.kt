package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.favAddTestNameResponse

data class FavAddTestNameResponse(
    val message: String = "",
    val statusCode: Int = 0,
    val responseContents: List<FavAddTestNameResponseContent> = listOf(),
    val totalRecords: Int = 0
)