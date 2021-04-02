package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.LabFavManageResponse

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.labFavResponse.LabMangeFavResponseContents

data class LabFavManageResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: LabMangeFavResponseContents = LabMangeFavResponseContents()
)