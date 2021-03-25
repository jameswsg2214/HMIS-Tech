package com.hmis_tn.lims.ui.lmis.lmisTest.model.request

import com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest.Header


data class DirectApprovelReq(
    var details: ArrayList<Header> = ArrayList()
)