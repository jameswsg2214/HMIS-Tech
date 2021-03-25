package com.hmis_tn.lims.ui.lmis.lmisTest.model.response.testMethodResponse

data class ResponseTestMethod(
    var req: String? = "",
    var responseContents: List<ResponseTestMethodContent?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)