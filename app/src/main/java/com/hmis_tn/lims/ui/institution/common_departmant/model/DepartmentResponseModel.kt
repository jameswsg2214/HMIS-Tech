package com.hmis_tn.lims.ui.institution.common_departmant.model

data class DepartmentResponseModel(
    var msg: String? = "",
    var responseContents: List<DepartmentResponseContent?>? = listOf(),
    var statusCode: Int? = 0
)