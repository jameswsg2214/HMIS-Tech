package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getDepaetmantList

data class FavAddAllDepatResponseModel(
    val req: String = "",
    val responseContents: List<FavAddAllDepatResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)