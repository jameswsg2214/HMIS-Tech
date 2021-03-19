package com.oasys.digihealth.tech.ui.lmis.sampleDispatch.model.request

data class DispatchReq(
    var comments: String = "",
    var dispatch_name: String = "",
    var mobile: String = "",
    var to_facility_id: Int = 0,
    var uuids: List<Int> = listOf()
)