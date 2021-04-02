package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.fetchEncountersResponse

data class FectchEncounterResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: List<FetchEncounterResponseContent?>? = null
)