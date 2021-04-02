package com.hmis_tn.lims.ui.lmis.lmisTest.model.request

data class Assigntoother(
    var facility_uuid: String = "",
    var id: Int = 0,
    var testname: String = "",
    var to_facility: Int = 0,
    var to_location_uuid: String = ""
)