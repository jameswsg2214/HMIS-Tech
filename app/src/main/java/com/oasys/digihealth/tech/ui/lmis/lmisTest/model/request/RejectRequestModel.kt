package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request

data class RejectRequestModel(
    var Id: ArrayList<Int> = ArrayList(),
    var reject_category_uuid: String = "",
    var reject_reason: String = ""
)