package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList

data class TemplatesLab(
    var lab_details: List<LabDetail?>? = listOf(),
    var radiology_details: List<LabDetail?>? = listOf(),
    var diet_details: List<DietTemplateDetails?>? = listOf(),
    var temp_details: TempDetails? = TempDetails(),
    var collapse:Boolean?=true
)