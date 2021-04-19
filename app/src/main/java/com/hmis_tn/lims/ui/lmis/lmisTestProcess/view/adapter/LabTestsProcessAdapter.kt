package com.hmis_tn.lims.ui.lmis.lmisTestProcess.view.adapter

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
import com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter.LabTestAdapter
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.row_lab_test_process.view.*
import kotlinx.android.synthetic.main.row_lab_test_process.view.checkbox
import kotlinx.android.synthetic.main.row_lab_test_process.view.mainLinearLayout
import kotlinx.android.synthetic.main.row_lab_test_process.view.patient_info
import kotlinx.android.synthetic.main.row_lab_test_process.view.print
import kotlinx.android.synthetic.main.row_lab_test_process.view.status
import kotlinx.android.synthetic.main.row_lab_test_process.view.test_name
import kotlinx.android.synthetic.main.row_lab_test_process.view.tv1
import kotlinx.android.synthetic.main.row_lab_test_process.view.tv2
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.*

class LabTestsProcessAdapter(context: Context, private var labTestList: ArrayList<LabTestresponseContent?>?) :
    RecyclerView.Adapter<LabTestsProcessAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    var isTablet: Boolean? = false
    var utils:Utils? = null
    var selectAllCheckbox: Boolean? = false
    private var isLoadingAdded = false
    private var SelectedLabData: ArrayList<LabTestresponseContent?>? = ArrayList()
    private var onSelectAllListener: OnSelectAllListener? = null

    private var onPrintClickListener: OnPrintClickListener? = null
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

/*
        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            status = view.findViewById<View>(R.id.status) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            checkbox = view.findViewById(R.id.checkbox)
            sampleid = view.findViewById(R.id.sample_id)
            testMethod = view.findViewById(R.id.testMethod)
            test_name = view.findViewById(R.id.test_name)

            print = view.findViewById(R.id.print)

        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_test_process, parent, false)
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //orderNumString = labTestList[position].toString()

        if(isTablet!!) {
            val labAllData = labTestList!![position]
            holder.itemView.patient_info.text =
                labAllData!!.first_name + "/" + labAllData!!.ageperiod + "/" + labAllData.gender_name
            holder.itemView.dateText.text = labAllData.order_request_date
            holder.itemView.status.text = labAllData.order_status_name
            holder.itemView.sample_id.text = labAllData.order_number.toString()
            holder.itemView.testMethod.text = labAllData.test_method_name

            holder.itemView.test_name.text = labAllData.test_name

            holder.itemView.checkbox.isChecked = labAllData.is_selected!!

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

            if (this.labTestList!![position]!!.auth_status_uuid == 4) {

                holder.itemView.print.visibility = View.VISIBLE
            }

            holder.itemView.print.setOnClickListener {

                onPrintClickListener?.onPrintClick(this.labTestList!![position]!!.uuid!!)

                //     this.labTestList!![position]!!.uuid

            }


            if (labTestList!![position]!!.order_status_uuid == 7 && labTestList!![position]!!.auth_status_uuid == 4 || labTestList!![position]!!.order_status_uuid == 2) {
                if (labTestList!![position]!!.order_status_uuid == 2) {

                    holder.itemView.status?.setText(labTestList!![position]!!.order_status_name)
                } else {

                    holder.itemView.status?.setText(labTestList!![position]!!.auth_status_name)
                }

                labTestList!![position]!!.checkboxdeclardtatus = false
            } else {
                labTestList!![position]!!.checkboxdeclardtatus = true
            }

            if (labTestList!![position]!!.checkboxdeclardtatus == false) {
                holder.itemView.checkbox.isEnabled = false
                holder.itemView.status?.setTextColor(Color.parseColor("#FF0000"))
            } else {

                holder.itemView.checkbox.isEnabled = true
                holder.itemView.status?.setTextColor(Color.parseColor("#000000"))
            }

            holder.itemView.checkbox.setOnClickListener {

                val myCheckBox = it as CheckBox

                val responseLabTestContent = labTestList!![position]

                if (myCheckBox.isChecked) {

                    labTestList!![position]!!.is_selected = true

                    SelectedLabData!!.add(responseLabTestContent)

                } else {

                    labTestList!![position]!!.is_selected = false

                    SelectedLabData!!.remove(responseLabTestContent)

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

                    SelectedLabData!!.add(responseLabTestContent)



                } else {

                    SelectedLabData!!.remove(responseLabTestContent)


                }


                if(SelectedLabData?.size==labTestList?.size){


                    onSelectAllListener?.onSelectAll(true)

                }
                else{
                    onSelectAllListener?.onSelectAll(false)
                }
            }


            holder.itemView.checkbox.isChecked = labTestList!![position]!!.is_selected!!

            if (labTestList!![position]!!.auth_status_uuid == null ) {

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


    fun getSelectedCheckData(): ArrayList<LabTestresponseContent?>? {

        return SelectedLabData
    }

    fun clearAll() {
        this.labTestList?.clear()
        this.SelectedLabData?.clear()
        notifyDataSetChanged()
    }

    fun setData(setContents : List<LabTestresponseContent?>?){
        this.labTestList = setContents as ArrayList<LabTestresponseContent?>?
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return labTestList!!.size
    }
    fun addAll(responseContent: List<LabTestresponseContent?>?) {
        labTestList?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context

        utils= Utils(mContext)
        isTablet = utils!!.isTablet(mContext)
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(LabTestresponseContent())
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
       /* val position = labTestList!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labTestList!!.removeAt(position)
            notifyItemRemoved(position)
        }*/
    }

    fun selectAllCheckboxes(ischeckedBox : Boolean){

        for (i in labTestList!!.indices){

            if(ischeckedBox) {

                this!!.labTestList!![i]!!.is_selected = true
                selectAllCheckbox = true

                SelectedLabData!!.add(this!!.labTestList!![i])

                notifyDataSetChanged()

            }else{
                labTestList!![i]!!.is_selected = false
                selectAllCheckbox = false
                SelectedLabData!!.remove(this!!.labTestList!![i])
                notifyDataSetChanged()
            }

        }

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
