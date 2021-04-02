package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model

data class PrevLabLmisResponseModel(
    val message: String = "",
    val responseContents: List<PrevLabLmisResponseContent> = listOf(),
    val statusCode: Int = 0
)