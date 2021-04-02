package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getFavouriteList

import com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.getFavouriteList.FavouritesModel

data class FavouritesResponseModel(
    var code: Int? = null,
    var message: String? = null,
    var responseContentLength: Int? = null,
    var responseContents: ArrayList<FavouritesModel?>? = ArrayList()
)