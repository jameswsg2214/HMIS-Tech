package com.hmis_tn.lims.ui.login.model.login_response_model

data class LoginResponseContents(
    var activityDetails: List<ActivityDetail?>? = listOf(),
    var moduleDetails: List<ModuleDetail?>? = listOf(),
    var userDetails: UserDetails? = UserDetails()
)