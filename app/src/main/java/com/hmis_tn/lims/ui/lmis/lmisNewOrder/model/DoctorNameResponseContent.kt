package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model

data class DoctorNameResponseContent(
    val first_name: String = "",
    val last_name: Any = Any(),
    val middle_name: Any = Any(),
    val role_uuid: Int = 0,
    val salutation_uuid: String = "",
    val title_name: String = "",
    val user_name: String = "",
    val uuid: Int = 0
)