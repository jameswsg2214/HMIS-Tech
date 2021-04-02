package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.updateRequest

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.templateRequest.Headers


data class UpdateRequestModule(
    var existing_details: List<Any?>? = listOf(),
    var headers: Headers? = Headers(),
    var new_details: List<NewDetail?>? = listOf(),
    var removed_details: List<RemovedDetail?>? = listOf()
)