package com.hmis_tn.lims.ui.lmis.lmisTest.model.request.SampleAcceptedRequest

data class Sample(
    var Id: Int = 0,
    var order_number: Int = 0,
    var order_status_uuid: Int = 0,
    var to_location_uuid: Int = 0
)