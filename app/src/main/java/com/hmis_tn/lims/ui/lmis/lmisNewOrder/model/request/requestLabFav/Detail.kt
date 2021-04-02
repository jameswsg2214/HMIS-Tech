package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav

data class Detail(
    var chief_complaint_uuid: Int = 0,
    var diagnosis_uuid: Int = 0,
    var display_order: String = "",
    var drug_frequency_uuid: Int = 0,
    var drug_instruction_uuid: Int = 0,
    var drug_route_uuid: Int = 0,
    var duration: Int = 0,
    var duration_period_uuid: Int = 0,
    var is_active: Boolean = false,
    var item_master_uuid: Int = 0,
    var revision: Boolean = false,
    var test_master_type_uuid: Int = 0,
    var test_master_uuid: Int? = null,
    var is_profile: Boolean = false,
    var profile_uuid: Int = 0,
    var vital_master_uuid: Int = 0,
    var diet_category_uuid: Int = 0,
    var diet_frequency_uuid: Int = 0,
    var diet_master_uuid: Int = 0,
    var speciality_sketch_uuid: Int = 0,
    var quantity: String = "",
    var treatment_kit_uuid: Int = 0
)