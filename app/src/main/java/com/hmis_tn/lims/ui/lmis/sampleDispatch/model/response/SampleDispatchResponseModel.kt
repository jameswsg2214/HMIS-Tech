package com.hmis_tn.lims.ui.lmis.sampleDispatch.model.response


data class SampleDispatchResponseModel(
    var responseContents: ArrayList<SampledispatchModel> = ArrayList(),
    var message: String = "",
    var statusCode: Int = 0
)