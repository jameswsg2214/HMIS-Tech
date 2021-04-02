package com.hmis_tn.lims.ui.lmis.lmisNewOrder.model.response.createEncounterResponse

data class CreateEncounterResponseContents(
    val encounter: Encounter? = null,
    val encounterDoctor: EncounterDoctor? = null
)