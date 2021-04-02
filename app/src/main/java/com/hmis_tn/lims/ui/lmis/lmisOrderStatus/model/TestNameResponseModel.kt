package com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model

data class TestNameResponseModel(
    val responseContents: List<TestNameResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)