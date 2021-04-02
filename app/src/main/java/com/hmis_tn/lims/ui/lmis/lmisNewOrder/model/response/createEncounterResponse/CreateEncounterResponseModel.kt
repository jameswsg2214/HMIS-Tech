package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.createEncounterResponse

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.createEncounterResponse.CreateEncounterResponseContents

data class CreateEncounterResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: CreateEncounterResponseContents? = null
)