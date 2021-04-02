package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.request.createEncounterRequest

data class CreateEncounterRequestModel(
    var encounter: Encounter? = null,
    var encounterDoctor: EncounterDoctor? = null
)