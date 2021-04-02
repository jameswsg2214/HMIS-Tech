package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest

data class EncounterDoctor(
    var department_uuid: Int? = null,
    var dept_visit_type_uuid: Int? = null,
    var doctor_uuid: Int? = null,
    var doctor_visit_type_uuid: Int? = null,
    var patient_uuid: Int? = null,
    var session_type_uuid: Int? = null,
    var speciality_uuid: Int? = null,
    var sub_deparment_uuid: Int? = null,
    var visit_type_uuid: Int? = null,
    var tat_start_time:String= "2020-04-28T07:14:43.753Z",
    var tat_end_time:String= "2020-04-28T07:14:43.753Z"

)