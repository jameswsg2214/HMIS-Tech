package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.createEncounterResponse


data class EncounterErrorAPIClass(
    var code: Int? = 0,
    var message: String? = "",
    val existingDetails: ExisitingData = ExisitingData()
)