package com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui.refInterface

interface ClearTemplateParticularPositionListener {

    fun ClearTemplateParticularPosition(position: Int)

    fun ClearAllData()

    fun GetTemplateDetails()

    fun updatestaus(favitem:Int,position: Int,select:Boolean)

    fun checkanduncheck(dataList: ArrayList<Int>)
}