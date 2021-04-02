package com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui

import java.text.FieldPosition

interface ClearLmisFavParticularPositionListener {
//    fun sendRefresh()

    fun ClearFavParticularPosition(position: Int)
    fun ClearAllData()

    fun checkanduncheck(position: Int,isSelect:Boolean)

    fun drugIdPresentCheck(drug_id: Int): Boolean
    fun clearDataUsingDrugid(drug_id: Int)

    fun favouriteID(favouriteID : Int)
}

