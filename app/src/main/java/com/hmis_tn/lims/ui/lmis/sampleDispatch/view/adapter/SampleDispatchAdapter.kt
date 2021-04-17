package com.hmis_tn.lims.ui.lmis.sampleDispatch.view.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import kotlinx.android.synthetic.main.row_sample_dispatch_list.view.*

class SampleDispatchAdapter(context: Context, private var labTestApproval: ArrayList<LabTestresponseContent?>?) :
    RecyclerView.Adapter<SampleDispatchAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private var isTablet:Boolean = false
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    var utils:Utils?= null
    private var RTLabData: ArrayList<LabTestresponseContent?>? = ArrayList()
    var selectAllCheckbox: Boolean? = false
    private var onSelectAllListener: OnSelectAllListener? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*        var patient_info: TextView
        var status: TextView
        var sample_id: TextView
        var testName: TextView
        var dateetime: TextView
//        var assignedToOthers: TextView

        var mainLinearLayout: LinearLayout
        var selectAllCheckBox : CheckBox

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            status = view.findViewById<View>(R.id.statueText) as TextView
            sample_id = view.findViewById<View>(R.id.sample_id) as TextView
            testName = view.findViewById<View>(R.id.testName) as TextView
            dateetime = view.findViewById<View>(R.id.dateText) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
            selectAllCheckBox = view.findViewById(R.id.selectAllCheckBox)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_sample_dispatch_list, parent, false)
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {




        if(isTablet) {

            val labAllData = this!!.labTestApproval!![position]

            holder.itemView.statueText.setText(labTestApproval!![position]!!.order_status_name)

            holder.itemView.selectAllCheckBox.setOnClickListener {

                val myCheckBox = it as CheckBox
                val responseLabTestContent = labTestApproval!![position]
                if (myCheckBox.isChecked) {
                    responseLabTestContent!!.is_selected = true
                    RTLabData!!.add(responseLabTestContent)
                } else {
                    responseLabTestContent!!.is_selected = false
                    RTLabData!!.remove(responseLabTestContent)
                }
            }
            holder.itemView.testName.text = labAllData!!.test_name

            holder.itemView.sample_id.text = labAllData.sample_identifier

            holder.itemView.dateText.text = labAllData.order_request_date

            holder.itemView.patient_info.text =
                labAllData!!.first_name + "/" + labAllData!!.ageperiod + "/" + labAllData.gender_name

            holder.itemView.selectAllCheckBox.isChecked = labAllData.is_selected!!

            labTestApproval!![position]!!.checkboxdeclardtatus =
                labTestApproval!![position]!!.order_status_uuid != 2

            if (labTestApproval!![position]!!.is_selected!! == true) {
                holder.itemView.selectAllCheckBox.isChecked = true
                holder.itemView.selectAllCheckBox.isEnabled = true
            } else {
                holder.itemView.selectAllCheckBox.isChecked = false
                holder.itemView.selectAllCheckBox.isEnabled = false
            }

            if (selectAllCheckbox == true) {

                for (i in labTestApproval!!.indices) {
                    labTestApproval!![position]!!.is_selected =
                        labTestApproval!![position]!!.checkboxdeclardtatus != false
                }

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

            holder.itemView.selectAllCheckBox.isEnabled =
                labTestApproval!![position]!!.checkboxdeclardtatus != false


        }
        else{



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

            if (labTestApproval!![position]!!.order_status_uuid != 2) {

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
        isTablet= utils!!.isTablet(mContext)
    }


    fun clearAll() {

        this.labTestApproval?.clear()
        this.RTLabData?.clear()
        notifyDataSetChanged()
    }

    fun selectAllCheckbox(ischeckedBox : Boolean){
        for (i in labTestApproval!!.indices){
            if(ischeckedBox) {
                if (this!!.labTestApproval!![i]!!.order_status_uuid != 2) {
                    this!!.labTestApproval!![i]!!.is_selected = true
                    selectAllCheckbox = true
                    RTLabData!!.add(this!!.labTestApproval!![i])
                }
                else{
                    labTestApproval!![i]!!.is_selected = false
                    selectAllCheckbox = false
                    RTLabData!!.remove(this!!.labTestApproval!![i])
                }
            }else{
                labTestApproval!![i]!!.is_selected = false
                selectAllCheckbox = false
                RTLabData!!.remove(this!!.labTestApproval!![i])
            }}
        notifyDataSetChanged()}

    fun addAll(responseContent: List<LabTestresponseContent?>?) {
        this.labTestApproval!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(LabTestApprovalresponseContent())
    }
    fun add(r: LabTestresponseContent) {
        labTestApproval!!.add(r)
        notifyItemInserted(labTestApproval!!.size - 1)
    }
    fun getItem(position: Int): LabTestresponseContent? {
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

    fun getSelectedCheckData(): ArrayList<LabTestresponseContent?>? {
        return RTLabData
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