package com.oasys.digihealth.tech.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel

data class LabTestSpinnerResponseModel(
    val responseContents: List<LabTestSpinnerResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)