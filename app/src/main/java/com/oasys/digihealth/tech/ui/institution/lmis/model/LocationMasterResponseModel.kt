package com.oasys.digihealth.tech.ui.institution.lmis.model

data class LocationMasterResponseModel(
    var responseContents: List<LocationMaster> = listOf(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)