package com.oasys.digihealth.tech.ui.login.model

data class ChangePasswordOTPResponseModel(
    val msg: String? = "",
    val responseContents: Otp? = Otp(),
    val status: String? = ""
)