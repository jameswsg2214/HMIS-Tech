package com.hmis_tn.lims.ui.lmis.lmisOrderStatus.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.lims.R
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.model.OrderStatusResponseContent
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.row_lmis_lab_test_list.view.*
import kotlinx.android.synthetic.main.row_order_status_list.view.*
import kotlinx.android.synthetic.main.row_order_status_list.view.mainLinearLayout
import kotlinx.android.synthetic.main.row_order_status_list.view.patient_info
import kotlinx.android.synthetic.main.row_order_status_list.view.status
import kotlinx.android.synthetic.main.row_order_status_list.view.tv1
import kotlinx.android.synthetic.main.row_order_status_list.view.tv2


class OrderStatusAdapter(context: Context) :
    RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private var isTablet:Boolean = false

    private var utils: Utils?= null
    private val mContext: Context
    var orderNumString: String? = null
    var status: String? = null
    private var labTestList: ArrayList<OrderStatusResponseContent?>? = ArrayList()


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
 /*       var patient_info: TextView
        var pinNum: TextView
        var TestName: TextView
        var serialNum: TextView
        var order_status: TextView

        var mainLinearLayout: LinearLayout*/

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_order_status_list, parent, false)
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        if(isTablet) {

            holder.itemView.serialNum.text = (position + 1).toString()
            val labAllData = this!!.labTestList!![position]

            if (labAllData!!.pattitle != null) {

                holder.itemView.patient_info.text =
                    labAllData!!.pattitle + "" + labAllData!!.first_name + "/" + labAllData.ageperiod

            } else {

                holder.itemView.patient_info.text =
                    labAllData!!.first_name + " " + labAllData.ageperiod
            }

            holder.itemView.pinNum.text = labAllData!!.uhid
            holder.itemView.TestName.text = labAllData!!.test_name

            holder.itemView.order_status.text = labAllData!!.order_status_name


            if (labTestList!![position]!!.order_status_uuid == 7) {

                if (labTestList!![position]!!.order_status_uuid == 2) {

                    holder.itemView.order_status?.setText(labTestList!![position]!!.order_status_name)
                } else {

                    holder.itemView.order_status?.setText(labTestList!![position]!!.auth_status_name)
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


        }
        else{

            val labAllData = this!!.labTestList!![position]

            if(labTestList!![position]?.pattitle!=null && labTestList!![position]?.pattitle!="") {

                holder.itemView.tv1.text =
                    labAllData!!.pattitle + "" + labAllData!!.first_name + "/" + labAllData.ageperiod

            } else {

                holder.itemView.tv1.text =
                    labAllData!!.first_name + " " + labAllData.ageperiod
            }


            holder.itemView.tv2.text=
                "${labTestList!![position]?.uhid} / ${labTestList!![position]?.order_number} / ${labTestList!![position]?.test_name} /${labTestList!![position]?.from_facility_name} / "+utils?.convertDateFormat(
                    labTestList!![position]?.order_request_date!!,
                    "yyyy-MM-dd HH:mm:ss",
                    "dd-MM-yyyy HH:mm"

                )

            holder.itemView.status.text = labTestList!![position]!!.order_status_name


            if (labTestList!![position]!!.order_status_uuid == 7) {

                if (labTestList!![position]!!.order_status_uuid == 2) {

                    holder.itemView.status?.setText(labTestList!![position]!!.order_status_name)
                } else {

                    holder.itemView.status?.setText(labTestList!![position]!!.auth_status_name)
                }
            }


        }

    }

    override fun getItemCount(): Int {
        return labTestList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context

        utils= Utils(mContext)

        isTablet = utils!!.isTablet(mContext)
    }

    fun addAll(responseContent: List<OrderStatusResponseContent?>?) {
        this.labTestList?.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun add(r: OrderStatusResponseContent) {
        labTestList!!.add(r)
        notifyItemInserted(labTestList!!.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun getItem(position: Int): OrderStatusResponseContent? {
        return labTestList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

    }

    fun clearAll() {
        labTestList?.clear()
        notifyDataSetChanged()
    }


}