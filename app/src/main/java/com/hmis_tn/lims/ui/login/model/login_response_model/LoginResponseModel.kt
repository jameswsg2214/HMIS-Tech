package com.hmis_tn.lims.ui.login.model.login_response_model


data class LoginResponseModel(
    var req: String? = "",
    var responseContents: LoginResponseContents? = LoginResponseContents(),
    var msg:String?="",
    var statusCode: Int? = 0
)