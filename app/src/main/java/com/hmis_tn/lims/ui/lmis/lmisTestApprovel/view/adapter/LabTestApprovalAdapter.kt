package com.hmis_tn.lims.ui.lmis.lmisTestApprovel.view.adapter

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
import com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter.LabTestAdapter
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.model.response.LabTestApprovelResponse.LabTestApprovalresponseContent
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.row_lab_test_process_list.view.*
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.*
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.checkbox
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.mainLinearLayout
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.patient_info
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.print
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.radioGroup
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.status
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.tv1
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.tv2

class LabTestApprovalAdapter(context: Context, private var labTestApproval: ArrayList<LabTestApprovalresponseContent?>?) :
    RecyclerView.Adapter<LabTestApprovalAdapter.MyViewHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    var selectAllCheckbox: Boolean? = false
    var isTablet: Boolean = false
    private var isLoadingAdded = false
    private var RTLabData: ArrayList<LabTestApprovalresponseContent?>? = ArrayList()
    private  var positive_value:Int=0
    private  var negative_value:Int=0
    private  var equavel_value:Int=0
    private  var rejected_value:Int=0

    private var utils: Utils? = null
    private var onSelectAllListener: OnSelectAllListener? = null
    private var onPrintClickListener: OnPrintClickListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*

        var patient_info: TextView
        var status: TextView
        var dateText: TextView
        var test_name: TextView
        var testMethod: TextView
        var orderNo:TextView
        var mainLinearLayout: LinearLayout
        var checkbox : CheckBox
        var radioGroup : RadioGroup
        var radioButton1 : RadioButton
        var radioButton2 : RadioButton
        var radioButton3 : RadioButton
        var print:ImageView
        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            dateText = view.findViewById<View>(R.id.dateText) as TextView
            status = view.findViewById<View>(R.id.status) as TextView
            orderNo = view.findViewById<View>(R.id.orderNo) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            checkbox = view.findViewById(R.id.checkbox)
            radioGroup = view.findViewById(R.id.radioGroup)
            radioButton1 = view.findViewById(R.id.radioButton1)
            radioButton2 = view.findViewById(R.id.radioButton2)
            radioButton3 = view.findViewById(R.id.radioButton3)
            testMethod = view.findViewById(R.id.testMethod)
            test_name = view.findViewById(R.id.test_name)
            print = view.findViewById(R.id.print)
        }


        */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_lab_test_process_list, parent, false)
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //orderNumString = labTestApproval[position].toString()

        if(isTablet) {

            val labAllData = labTestApproval!![position]
            holder.itemView.patient_info.text =
                labAllData!!.first_name + "/" + labAllData!!.ageperiod + "/" + labAllData.gender_name
            holder.itemView.dateText.text = labAllData.order_request_date
            // holder.itemView.status.text = labAllData.order_status_name
            holder.itemView.testMethod.text = labAllData.test_method_name
            holder.itemView.checkbox.isChecked = labAllData.is_selected!!
            holder.itemView.orderNo.text = labAllData.order_number.toString()
            holder.itemView.testName.text = labAllData.test_name


            if (labTestApproval!![position]!!.order_status_uuid == 7 && labTestApproval!![position]!!.order_status_uuid != null) {

                holder.itemView.status.setText(labTestApproval!![position]!!.auth_status_name)
            } else {

                holder.itemView.status.setText(labTestApproval!![position]!!.order_status_name)
            }


            /*
       if(labTestApproval!![position]!!.order_status_uuid ==2)
        {

            holder.itemView..status.setText(labTestApproval!![position]!!.order_status_name)
        }

        */

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


            if (this.labTestApproval!![position]!!.auth_status_uuid == 4) {

                holder.itemView.print.visibility = View.VISIBLE
            }

            holder.itemView.print.setOnClickListener {

                onPrintClickListener?.onPrintClick(this.labTestApproval!![position]!!.uuid!!)

                //     this.labTestApproval!![position]!!.uuid

            }


            if (labTestApproval!![position]!!.test_method_uuid != 3 && labTestApproval!![position]!!.test_code == "COVID") {

                holder.itemView.radioGroup.visibility = View.VISIBLE

            } else {

                holder.itemView.radioGroup.visibility = View.GONE
            }



            if (labTestApproval!![position]!!.order_status_uuid == 7 && labTestApproval!![position]!!.auth_status_uuid == 4 || labTestApproval!![position]!!.order_status_uuid == 2) {

                if (labTestApproval!![position]!!.test_method_uuid != 3 && labTestApproval!![position]!!.test_code == "COVID") {

                    holder.itemView.radioGroup.visibility = View.VISIBLE

                }
                if (labTestApproval!![position]!!.order_status_uuid == 2) {

                    holder.itemView.status?.setText(labTestApproval!![position]!!.order_status_name)
                } else {

                    holder.itemView.status.setText(labTestApproval!![position]!!.auth_status_name)
                }

                labTestApproval!![position]!!.checkboxdeclardtatus = false

                when {

                    labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                        holder.itemView.radioButton1.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                        holder.itemView.radioButton2.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                        holder.itemView.radioButton3.isChecked = true
                    }
                }
            } else {
                labTestApproval!![position]!!.checkboxdeclardtatus = true

                holder.itemView.radioGroup.visibility = View.GONE

            }

            if (labTestApproval!![position]!!.checkboxdeclardtatus == false) {
                holder.itemView.radioButton1.isEnabled = false

                holder.itemView.radioButton2.isEnabled = false

                holder.itemView.radioButton3.isEnabled = false
                holder.itemView.checkbox.isEnabled = false

                holder.itemView.status?.setTextColor(Color.parseColor("#FF0000"))

                when {

                    labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                        holder.itemView.radioButton1.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                        holder.itemView.radioButton2.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                        holder.itemView.radioButton3.isChecked = true
                    }
                }

            } else {
                holder.itemView.radioButton1.isEnabled = false
                holder.itemView.radioButton2.isEnabled = false
                holder.itemView.radioButton3.isEnabled = false
                holder.itemView.checkbox.isEnabled = true
                holder.itemView.status?.setTextColor(Color.parseColor("#000000"))


                when {

                    labTestApproval!![position]!!.qualifier_uuid == 2 -> {

                        holder.itemView.radioButton1.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 1 -> {

                        holder.itemView.radioButton2.isChecked = true

                    }
                    labTestApproval!![position]!!.qualifier_uuid == 3 -> {

                        holder.itemView.radioButton3.isChecked = true
                    }
                }
            }


            holder.itemView.checkbox.setOnClickListener {

                val myCheckBox = it as CheckBox
                val responseLabTestContent = labTestApproval!![position]
                if (myCheckBox.isChecked) {

                    responseLabTestContent!!.is_selected = true

                    RTLabData!!.add(responseLabTestContent)

                    if (responseLabTestContent.test_method_uuid == 2 && responseLabTestContent.radioselectName == 0) {

                        Toast.makeText(
                            this.mContext,
                            "Please select one Result",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                } else {
                    holder.itemView.radioGroup.clearCheck()

                    labTestApproval!![position]!!.radioselectName = 0

                    responseLabTestContent!!.is_selected = false

                    RTLabData!!.remove(responseLabTestContent)

                }

            }
            if (selectAllCheckbox == true) {

                if (labTestApproval!![position]!!.checkboxdeclardtatus == false) {
                    holder.itemView.checkbox.isEnabled = false
                    holder.itemView.checkbox.isChecked = false
                } else {
                    holder.itemView.checkbox.isChecked = true
                    holder.itemView.checkbox.isEnabled = true
                }
            }


            if (this.labTestApproval!![position]!!.qualifier_uuid == 2) {

                holder.itemView.radioButton1.isChecked = true

            } else if (this.labTestApproval!![position]!!.qualifier_uuid == 1) {

                holder.itemView.radioButton2.isChecked = true
            } else if (this.labTestApproval!![position]!!.qualifier_uuid == 3) {

                holder.itemView.radioButton3.isChecked = true
            }


        }
        else
        {

            val labAllData = this.labTestApproval!![position]

            if(labTestApproval!![position]?.pattitle!=null && labTestApproval!![position]?.pattitle!="") {

                holder.itemView.tv1.text =""+
                        labTestApproval!![position]?.pattitle + labTestApproval!![position]!!.first_name + " / " + (labTestApproval!![position]?.gender_name?.get(
                    0
                ) ?: "") + " / " + labTestApproval!![position]?.ageperiod
            }
            else{

                holder.itemView.tv1.text =
                    labTestApproval!![position]!!.first_name + " / " + (labTestApproval!![position]?.gender_name?.get(
                        0
                    ) ?: "") + " / " + labTestApproval!![position]?.ageperiod

            }


            holder.itemView.checkbox.setOnClickListener {

                val myCheckBox = it as CheckBox
                val responseLabTestContent = labTestApproval!![position]
                if (myCheckBox.isChecked) {

                    responseLabTestContent!!.is_selected = true

                    RTLabData!!.add(responseLabTestContent)



                } else {

                    RTLabData!!.remove(responseLabTestContent)


                }


                if(RTLabData?.size==labTestApproval?.size){


                    onSelectAllListener?.onSelectAll(true)

                }
                else{
                    onSelectAllListener?.onSelectAll(false)
                }
            }


            holder.itemView.checkbox.isChecked = labTestApproval!![position]!!.is_selected!!

            if (labTestApproval!![position]!!.auth_status_uuid == null ) {

                holder.itemView.status.setText(labTestApproval!![position]!!.order_status_name)

            } else {

                holder.itemView.status.setText(labTestApproval!![position]!!.auth_status_name)
            }
            if(labAllData!!.sample_identifier!= null && labAllData!!.sample_identifier!=""){

                holder.itemView.tv2.text=
                    "${labTestApproval!![position]?.uhid} / ${labTestApproval!![position]?.order_number} / ${labTestApproval!![position]?.test_name} /  ${labTestApproval!![position]?.sample_identifier} /${labTestApproval!![position]?.location_name} / "+utils?.convertDateFormat(
                        labTestApproval!![position]?.order_request_date!!,
                        "yyyy-MM-dd HH:mm:ss",
                        "dd-MM-yyyy HH:mm"

                    )

            }
            else{

                holder.itemView.tv2.text=
                    "${labTestApproval!![position]?.uhid} / ${labTestApproval!![position]?.order_number} / ${labTestApproval!![position]?.test_name} / ${labTestApproval!![position]?.location_name} / "+utils?.convertDateFormat(
                        labTestApproval!![position]?.order_request_date!!,
                        "yyyy-MM-dd HH:mm:ss",
                        "dd-MM-yyyy HH:mm"
                    )

            }


        }


    }

    override fun getItemCount(): Int {
        return labTestApproval!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context
        utils= Utils(mContext)
        isTablet = utils!!.isTablet(mContext)
    }

    fun getSelectedCheckData(): ArrayList<LabTestApprovalresponseContent?>? {

        return RTLabData
    }



    fun selectAllCheckbox(ischeckedBox : Boolean){

        for (i in labTestApproval!!.indices){

            if(ischeckedBox) {

                labTestApproval!![i]!!.is_selected = true
                selectAllCheckbox = true

                RTLabData!!.add(labTestApproval!![i])

                notifyDataSetChanged()

            }else{
                labTestApproval!![i]!!.is_selected = false
                selectAllCheckbox = false
                RTLabData!!.remove(labTestApproval!![i])
                notifyDataSetChanged()
            }

        }

    }

    fun addAll(responseContent: List<LabTestApprovalresponseContent?>?) {


   /*     for(i in responseContent!!.indices){

            val check= this.labTestApproval!!.any{ it!!.order_number == responseContent[i]!!.order_number}

            if(!check){

                this.labTestApproval!!.add(responseContent[i]!!)

            }

        }
*/

        this.labTestApproval!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.RTLabData?.clear()
        this.labTestApproval?.clear()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(LabTestApprovalresponseContent())
    }
    fun add(r: LabTestApprovalresponseContent) {
        labTestApproval!!.add(r)
        notifyItemInserted(labTestApproval!!.size - 1)
    }
    fun getItem(position: Int): LabTestApprovalresponseContent? {
        return labTestApproval!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
      /*  val position = labTestApproval!!.size - 1
        val result = getItem(position)
        if (result != null) {
            labTestApproval!!.removeAt(position)
            notifyItemRemoved(position)
        }*/
    }

/*
    fun positiveValue():Int{
        val value=arrayOf(labTestApproval.qualifier_uuid.get("0")).count()

    }
*/


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