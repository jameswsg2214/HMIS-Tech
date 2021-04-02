package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList

data class DietTemplateDetails(
    val diet_category_code: String,
    val diet_category_id: Int,
    val diet_category_name: String,
    val diet_code: String,
    val diet_display_order: Int,
    val diet_frequency_code: String,
    val diet_frequency_id: Int,
    val diet_frequency_name: String,
    val diet_id: Int,
    val diet_name: String,
    val quantity: Int,
    val template_details_displayorder: Int,
    val template_details_uuid: Int
)