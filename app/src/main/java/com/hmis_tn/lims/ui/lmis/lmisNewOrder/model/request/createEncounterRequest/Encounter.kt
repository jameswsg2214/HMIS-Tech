package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest

data class Encounter(
    var admission_request_uuid: Int? = null,
    var admission_uuid: Int? = null,
    var appointment_uuid: Int? = null,
    var department_uuid: Int? = null,
    var discharge_type_uuid: Int? = null,
    var encounter_identifier: Int? = null,
    var encounter_priority_uuid: Int? = null,
    var encounter_status_uuid: Int? = null,
    var encounter_type_uuid: Int? = null,
    var facility_uuid: Int? = null,
    var patient_uuid: Int? = null
)