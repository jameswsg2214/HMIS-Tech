package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.labFavResponse


data class LabFavResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<LabFavModel?>? = ArrayList()
)