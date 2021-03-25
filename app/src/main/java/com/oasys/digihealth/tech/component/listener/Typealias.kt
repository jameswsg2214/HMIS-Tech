package com.oasys.digihealth.tech.component.listener


import java.io.File


typealias OnCalendarDaysSelected = (day: String) -> Unit

typealias OnDownloadPDFSuccess = (file: File?) -> Unit

typealias OnClickedYes<R> = (R) -> Unit

typealias OnClickedNo<R> = (R) -> Unit
