package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse

import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.Row

data class LabApprovalResultResponseContents(
    val count: Int = 0,
    val rows: ArrayList<Row> = ArrayList()
)