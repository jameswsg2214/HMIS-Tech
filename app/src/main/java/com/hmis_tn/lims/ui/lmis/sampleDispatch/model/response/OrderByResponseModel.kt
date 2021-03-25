package com.hmis_tn.lims.ui.lmis.sampleDispatch.model.response

import com.hmis_tn.lims.ui.lmis.sampleDispatch.model.response.OrderByResponseContent

data class OrderByResponseModel(
    val responseContents: List<OrderByResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)