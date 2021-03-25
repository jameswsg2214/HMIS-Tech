package com.hmis_tn.lims.ui.login.model

data class Req(
    val otp: String? = "",
    val password: String? = "",
    val username: String? = ""
)