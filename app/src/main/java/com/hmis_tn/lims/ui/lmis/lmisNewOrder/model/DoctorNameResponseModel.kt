package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model

data class DoctorNameResponseModel(
    val responseContents: ArrayList<DoctorNameResponseContent?> = ArrayList(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)