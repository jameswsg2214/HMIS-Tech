package com.hmis_tn.lims.ui.lmis.lmisTest.model.request.orderRequest

data class WorkOrderAttachment(
    var attachment_name: String? = null,
    var file_path: String? = null,
    var uuid: Int? = null,
    var work_order_detail_uuid: Int? = null
)