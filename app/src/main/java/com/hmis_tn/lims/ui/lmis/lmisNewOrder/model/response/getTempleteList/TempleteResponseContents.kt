package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getTempleteList.TemplatesLab

data class TempleteResponseContents(
    var templates_lab_list: List<TemplatesLab?>? = listOf(),
    var templates_radiology_list: List<TemplatesLab?>? = listOf(),
    var templates_list: List<TemplatesLab?>? = listOf()
)