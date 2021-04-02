package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model

data class PrevLabLmisResponseContent(
    val created_date: String = "",
    val department_name: String = "",
    val department_uuid: Int = 0,
    val doctor_name: String = "",
    val doctor_uuid: Int = 0,
    val order_status: String = "",
    var checkboxdeclardtatus : Boolean?=false,
    val pod_arr_result: List<PodArrResult> = listOf()
)