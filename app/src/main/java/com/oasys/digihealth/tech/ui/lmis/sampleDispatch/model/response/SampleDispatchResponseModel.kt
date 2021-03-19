package com.oasys.digihealth.tech.ui.lmis.sampleDispatch.model.response


data class SampleDispatchResponseModel(
    var responseContents: ArrayList<SampledispatchModel> = ArrayList(),
    var message: String = "",
    var statusCode: Int = 0
)