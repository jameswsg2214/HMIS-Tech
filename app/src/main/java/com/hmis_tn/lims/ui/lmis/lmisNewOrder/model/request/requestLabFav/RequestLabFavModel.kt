package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav.Detail
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.requestLabFav.Headers

data class RequestLabFavModel(
    var details: List<Detail> = listOf(),
    var headers: Headers = Headers()
)