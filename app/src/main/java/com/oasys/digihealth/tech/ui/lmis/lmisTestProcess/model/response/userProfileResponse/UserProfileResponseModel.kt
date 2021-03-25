package com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.response.userProfileResponse

import com.oasys.digihealth.tech.ui.lmis.lmisTestProcess.model.response.userProfileResponse.UserProfileResponse

data class UserProfileResponseModel(
    var responseContents: List<UserProfileResponse> = listOf(),
    var msg: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)