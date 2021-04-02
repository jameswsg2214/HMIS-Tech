package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.searcPatientListResponse

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.ResponseContentslmisorder

data class NewLmisOrderModule(
    var message: String? = "",
    var responseContents: List<ResponseContentslmisorder?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)