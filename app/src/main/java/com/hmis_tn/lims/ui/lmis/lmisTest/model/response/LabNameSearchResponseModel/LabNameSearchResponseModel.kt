package com.hmis_tn.lims.ui.lmis.lmisTest.model.response.LabNameSearchResponseModel

data class LabNameSearchResponseModel(
    var responseContents: ArrayList<LabName> = ArrayList(),
    var status: String = "",
    var statusCode: Int = 0
)