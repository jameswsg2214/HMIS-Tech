package com.oasys.digihealth.tech.ui.login.model

data class Req(
    val otp: String? = "",
    val password: String? = "",
    val username: String? = ""
)