package com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabApprovalResultResponse

import com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.Row

data class LabApprovalResultResponseContents(
    val count: Int = 0,
    val rows: ArrayList<Row> = ArrayList()
)