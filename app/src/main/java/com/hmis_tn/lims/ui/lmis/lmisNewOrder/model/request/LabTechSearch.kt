package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request

data class LabTechSearch(
    var is_default: Boolean = false,
    var lab_uuid: String? = null,
    var other_department_uuids: List<Int> = listOf(),
    var search: String = "",
    var sortField: String = "",
    var sortOrder: String = ""
)