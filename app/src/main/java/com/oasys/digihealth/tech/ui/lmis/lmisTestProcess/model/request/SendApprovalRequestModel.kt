package com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.request

data class SendApprovalRequestModel(
    var Id: ArrayList<Int> = ArrayList(),
    var comments: String = "",
    var doctor_Id: Int = 0
)