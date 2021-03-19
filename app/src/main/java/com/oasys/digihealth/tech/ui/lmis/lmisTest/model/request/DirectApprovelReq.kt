package com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request

import com.oasys.digihealth.tech.ui.lmis.lmisTest.model.request.orderRequest.Header


data class DirectApprovelReq(
    var details: ArrayList<Header> = ArrayList()
)