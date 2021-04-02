package com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.response.userProfileResponse

import com.hmis_tn.lims.ui.lmis.lmisTestProcess.model.response.userProfileResponse.UserProfileResponse

data class UserProfileResponseModel(
    var responseContents: List<UserProfileResponse> = listOf(),
    var msg: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)