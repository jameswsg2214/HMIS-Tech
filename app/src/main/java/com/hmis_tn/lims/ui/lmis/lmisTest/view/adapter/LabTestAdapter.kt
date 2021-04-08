package com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.lims.R
import com.hmis_tn.lims.ui.lmis.lmisTest.model.response.labTestResponse.LabTestresponseContent
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.*

class LabTestAdapter(
    context: Context,
    private var labTestList: ArrayList<LabTestresponseContent?>?
) :
    RecyclerView.Adapter<LabTestAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private val mContext: Context

    var orderNumString: String? = null

    var status: String? = null

    private var utils: Utils? = null

    var selectAllCheckbox: Boolean? = false

    private var onPrintClickListener: OnPrintClickListener? = null
    private var onSelectAllListener: OnSelectAllListener? = null

    private var RTLabData: ArrayList<LabTestresponseContent?>? = ArrayList()

    private var RapidLabData: ArrayList<LabTestresponseContent?>? = ArrayList()
    
    private var isTablet:Boolean=false

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext).inflate(R.layout.row_lmis_lab_test_list, parent, false)

        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        
        if(isTablet) {

            //orderNumString = labTestList[position].toString()
            holder.itemView.checkbox.tag = position

            val labAllData = this.labTestList!![position]

            holder.itemView.patient_info.text =
                labAllData?.pattitle
                    ?: "" + labAllData!!.first_name + "/" + labAllData.ageperiod + "/" + labAllData.gender_name

            holder.itemView.order_num.text = labAllData!!.order_number.toString()

            holder.itemView.status.text = labAllData.order_status_name

            holder.itemView.checkbox.isChecked = labAllData.is_selected!!

            holder.itemView.radioGroup.clearCheck()

            holder.itemView.test_name.text = labAllData.test_name

            if (this.labTestList!![position]!!.auth_status_uuid == 4) {

                holder.itemView.print.visibility = View.VISIBLE
            } else {

                holder.itemView.print.visibility = View.INVISIBLE

            }

            if (this.labTestList!![position]!!.test_method_uuid == 2) {

                holder.itemView.radioGroup.visibility = View.VISIBLE

            } else {
                holder.itemView.radioGroup.visibility = View.INVISIBLE
            }

            if (position % 2 == 0) {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.alternateRow
                    )
                )
            } else {
                holder.itemView.mainLinearLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.white
                    )
                )
            }

            holder.itemView.print.setOnClickListener {

                onPrintClickListener?.onPrintClick(this.labTestList!![position]!!.uuid!!)

                //     this.labTestList!![position]!!.uuid

            }


            if (labTestList!![position]!!.order_status_uuid == 7 && labTestList!![position]!!.auth_status_uuid == 4 || labTestList!![position]!!.order_status_uuid == 2) {
                if (labTestList!![position]!!.order_status_uuid == 2) {

                    holder.itemView.status.text = labTestList!![position]!!.order_status_name
                } else {

                    holder.itemView.status.text = labTestList!![position]!!.auth_status_name
                }

                labTestList!![position]!!.checkboxdeclardtatus = false
            } else {
                labTestList!![position]!!.checkboxdeclardtatus = true
            }

            if (labTestList!![position]!!.checkboxdeclardtatus == false) {
                holder.itemView.radiobutton1.isEnabled = false

                holder.itemView.radiobutton2.isEnabled = false

                holder.itemView.radiobutton3.isEnabled = false
                holder.itemView.checkbox.isEnabled = false
                holder.itemView.status.setTextColor(Color.parseColor("#FF0000"))

                if (labTestList!![position]!!.qualifier_uuid != null) {

                    when {

                        labTestList!![position]!!.qualifier_uuid == 2 -> {

                            holder.itemView.radiobutton1.isChecked = true

                        }
                        labTestList!![position]!!.qualifier_uuid == 1 -> {

                            holder.itemView.radiobutton2.isChecked = true

                        }
                        labTestList!![position]!!.qualifier_uuid == 3 -> {

                            holder.itemView.radiobutton3.isChecked = true
                        }
                    }

                }

            } else {
                holder.itemView.radiobutton1.isEnabled = true
                holder.itemView.radiobutton2.isEnabled = true
                holder.itemView.radiobutton3.isEnabled = true
                holder.itemView.checkbox.isEnabled = true
                holder.itemView.status.setTextColor(Color.parseColor("#000000"))
            }

            holder.itemView.checkbox.isChecked = labAllData.is_selected!!

            holder.itemView.checkbox.setOnClickListener {

                val myCheckBox = it as CheckBox
                val responseLabTestContent = labTestList!![position]
                if (myCheckBox.isChecked) {
                    responseLabTestContent!!.is_selected = true

                    RTLabData!!.add(responseLabTestContent)

                    if (responseLabTestContent.test_method_uuid == 2 && responseLabTestContent.radioselectName == 0) {

                        Toast.makeText(
                            this.mContext,
                            "Please select one Result",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                } else {
                    holder.itemView.radioGroup.clearCheck()

                    labTestList!![position]!!.radioselectName = 0

                    responseLabTestContent!!.is_selected = false

                    RTLabData!!.remove(responseLabTestContent)


                }

            }


            holder.itemView.radiobutton1.setOnClickListener {

                if (holder.itemView.radiobutton1.isChecked) {


                    val check = RTLabData!!.any { it!!.uuid == this.labTestList!![position]!!.uuid }

                    if (!check) {

                        this.labTestList!![position]!!.radioselectName = 1

                    } else {

                        for (i in RTLabData!!.indices) {

                            if (RTLabData!![i]!!.uuid == this.labTestList!![position]!!.uuid) {

                                RTLabData!![i]?.radioselectName = 1

                            }

                        }

                    }

                }

            }

            holder.itemView.radiobutton2.setOnClickListener {

                if (holder.itemView.radiobutton2.isChecked) {
                    val check = RTLabData!!.any { it!!.uuid == this.labTestList!![position]!!.uuid }

                    if (!check) {

                        this.labTestList!![position]!!.radioselectName = 2

                    } else {

                        for (i in RTLabData!!.indices) {

                            if (RTLabData!![i]!!.uuid == this.labTestList!![position]!!.uuid) {

                                RTLabData!![i]?.radioselectName = 2

                            }

                        }

                    }


                }

            }


            holder.itemView.radiobutton3.setOnClickListener {

                if (holder.itemView.radiobutton3.isChecked) {

                    val check = RTLabData!!.any { it!!.uuid == this.labTestList!![position]!!.uuid }

                    if (!check) {

                        this.labTestList!![position]!!.radioselectName = 3

                    } else {

                        for (i in RTLabData!!.indices) {

                            if (RTLabData!![i]!!.uuid == this.labTestList!![position]!!.uuid) {

                                RTLabData!![i]?.radioselectName = 3

                            }
                        }
                    }
                }
            }

            if (selectAllCheckbox == true) {

                if (labTestList!![position]!!.checkboxdeclardtatus == false) {
                    holder.itemView.checkbox.isEnabled = false
                    holder.itemView.checkbox.isChecked = false

                } else {
                    holder.itemView.checkbox.isChecked = true
                    holder.itemView.checkbox.isEnabled = true

                }
            }

        }

        else{

            val labAllData = this.labTestList!![position]

            if(labTestList!![position]?.pattitle!=null && labTestList!![position]?.pattitle!="") {

                holder.itemView.tv1.text =""+
                        labTestList!![position]?.pattitle + labTestList!![position]!!.first_name + " / " + (labTestList!![position]?.gender_name?.get(
                            0
                        ) ?: "") + " / " + labTestList!![position]?.ageperiod
            }
            else{

                holder.itemView.tv1.text =
                    labTestList!![position]!!.first_name + " / " + (labTestList!![position]?.gender_name?.get(
                            0
                        ) ?: "") + " / " + labTestList!![position]?.ageperiod

            }


            holder.itemView.checkbox.setOnClickListener {

                val myCheckBox = it as CheckBox
                val responseLabTestContent = labTestList!![position]
                if (myCheckBox.isChecked) {

                    responseLabTestContent!!.is_selected = true

                    RTLabData!!.add(responseLabTestContent)



                } else {

                    RTLabData!!.remove(responseLabTestContent)


                }


             if(RTLabData?.size==labTestList?.size){


                 onSelectAllListener?.onSelectAll(true)

                }
                else{
                    onSelectAllListener?.onSelectAll(false)
                }
            }


            holder.itemView.checkbox.isChecked = labTestList!![position]!!.is_selected!!

            if (labTestList!![position]!!.order_status_uuid != 2) {

                holder.itemView.status.setText(labTestList!![position]!!.order_status_name)

            } else {

                holder.itemView.status.setText(labTestList!![position]!!.auth_status_name)
            }
            if(labAllData!!.sample_identifier!= null && labAllData!!.sample_identifier!=""){

                holder.itemView.tv2.text=
                    "${labTestList!![position]?.uhid} / ${labTestList!![position]?.order_number} / ${labTestList!![position]?.test_name} /  ${labTestList!![position]?.sample_identifier} /${labTestList!![position]?.location_name} / "+utils?.convertDateFormat(
                        labTestList!![position]?.order_request_date!!,
                        "yyyy-MM-dd HH:mm:ss",
                    "dd-MM-yyyy HH:mm"

                )

            }
            else{

                holder.itemView.tv2.text=
                    "${labTestList!![position]?.uhid} / ${labTestList!![position]?.order_number} / ${labTestList!![position]?.test_name} / ${labTestList!![position]?.location_name} / "+utils?.convertDateFormat(
                        labTestList!![position]?.order_request_date!!,
                        "yyyy-MM-dd HH:mm:ss",
                        "dd-MM-yyyy HH:mm"
                    )

            }


        }
        

    }

    override fun getItemCount(): Int {
        return labTestList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
        isTablet = Utils(mContext).isTablet(mContext)
        utils= Utils(mContext)
    }

    fun getSelectedCheckData(): ArrayList<LabTestresponseContent?>? {

        return RTLabData
    }

    fun selectAllCheckbox(ischeckedBox: Boolean) {
        for (i in labTestList!!.indices) {
            if (ischeckedBox) {
                if (this.labTestList!![i]!!.order_status_uuid != 7) {
                    this.labTestList!![i]!!.is_selected = true
                    selectAllCheckbox = true
                    RTLabData!!.add(this.labTestList!![i])
                } else {
                    labTestList!![i]!!.is_selected = false
                    selectAllCheckbox = false
                    RTLabData!!.remove(this.labTestList!![i])
                }
            } else {
                labTestList!![i]!!.is_selected = false
                selectAllCheckbox = false
                RTLabData!!.remove(this.labTestList!![i])
            }
        }
        notifyDataSetChanged()
    }

    fun addAll(responseContent: List<LabTestresponseContent?>?) {
        this.labTestList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.labTestList?.clear()
        this.RTLabData?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        // add(LabTestresponseContent())
    }

    fun add(r: LabTestresponseContent) {
        labTestList!!.add(r)
        notifyItemInserted(labTestList!!.size - 1)
    }

    fun getItem(position: Int): LabTestresponseContent? {
        return labTestList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }


    interface OnPrintClickListener {
        fun onPrintClick(
            uuid: Int
        )
    }


    fun setOnPrintClickListener(onprintClickListener: OnPrintClickListener) {
        this.onPrintClickListener = onprintClickListener
    }

    interface OnSelectAllListener {
        fun onSelectAll(
            ischeck: Boolean
        )
    }


    fun setOnSelectAllListener(onSelectAllListener: OnSelectAllListener) {
        this.onSelectAllListener = onSelectAllListener
    }

}