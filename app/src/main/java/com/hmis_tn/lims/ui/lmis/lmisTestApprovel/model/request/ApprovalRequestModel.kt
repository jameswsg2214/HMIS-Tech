package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.request

import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.Row

data class ApprovalRequestModel(
    var Id: List<Int> = listOf(),
    var auth_status_uuid: Int = 0,
    var details: ArrayList<Row> = ArrayList()
)