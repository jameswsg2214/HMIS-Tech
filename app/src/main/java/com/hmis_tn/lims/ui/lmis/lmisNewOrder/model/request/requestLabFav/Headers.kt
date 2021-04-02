package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav

data class Headers(
    var department_uuid: Int = 0,
    var display_order: String = "",
    var facility_uuid: String = "",
    var favourite_type_uuid: Int = 0,
    var is_public: Boolean = false,
    var revision: Boolean = false,
    var user_uuid: String = "",
    var is_active: Boolean = false,
    var ticksheet_type_uuid: Int = 0,
    var diagnosis_uuid: Int = 0,
    var lab_uuid: Int = 0

)