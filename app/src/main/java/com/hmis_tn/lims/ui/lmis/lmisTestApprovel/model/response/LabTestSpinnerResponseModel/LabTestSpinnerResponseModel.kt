package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestSpinnerResponseModel

data class LabTestSpinnerResponseModel(
    val responseContents: List<LabTestSpinnerResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)