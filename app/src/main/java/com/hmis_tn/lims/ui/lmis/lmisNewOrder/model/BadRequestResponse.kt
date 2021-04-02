package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model

data class BadRequestResponse(
    var code: Int? = 0,
    var existingDetails: ExistingDetails? = ExistingDetails(),
    var message: String? = ""
)