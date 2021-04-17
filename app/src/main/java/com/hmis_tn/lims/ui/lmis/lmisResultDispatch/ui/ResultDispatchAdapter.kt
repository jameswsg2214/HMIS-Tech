package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.lims.R
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model.ResponseContentsResultDispatch
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model.ResponseResultDispatch
import com.hmis_tn.lims.ui.lmis.lmisTest.view.adapter.LabTestAdapter
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.row_result_dispatch_list.view.*


class ResultDispatchAdapter(context: Context, private var resonseresultdispatchList: ArrayList<ResponseContentsResultDispatch?>?) :
    RecyclerView.Adapter<ResultDispatchAdapter.MyViewHolder>() {

    private val mLayoutInflater: LayoutInflater
    private var isLoadingAdded = false
    private var isTablet:Boolean = false
    private  var utils:Utils?= null
    private val mContext: Context
    private var onItemClickListener: OnItemClickListener? = null
    var orderNumString: String? = null
    private var onPrintClickListener: OnPrintClickListener? = null
    var status: String? = null
   class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
/*        var patient_info: TextView
        var TestName: TextView
        var orderedNUm: TextView
        var approvedBy: TextView
        var ic_print : ImageView

        var serialNum: TextView
        var mainLinearLayout: LinearLayout

        init {
            patient_info = view.findViewById<View>(R.id.patient_info) as TextView
            TestName = view.findViewById<View>(R.id.TestName) as TextView
            orderedNUm = view.findViewById<View>(R.id.orderedNUm) as TextView
            approvedBy = view.findViewById<View>(R.id.approvedBy) as TextView
            ic_print =  view.findViewById<View>(R.id.pdf) as ImageView
            serialNum = view.findViewById<View>(R.id.serialNum) as TextView
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayout = LayoutInflater.from(mContext)
            .inflate(R.layout.row_result_dispatch_list, parent, false)
        var recyclerView: RecyclerView
        return MyViewHolder(itemLayout)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        
        if(isTablet) {
            holder.itemView.serialNum.text = (position + 1).toString()

            val ResultDispathData = this.resonseresultdispatchList!![position]

            if (ResultDispathData!!.patient_order_detail?.vw_patient_info?.pattitle != null) {

                holder.itemView.patient_info.text =
                    ResultDispathData!!.patient_order_detail?.vw_patient_info?.pattitle + " " + ResultDispathData!!.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData!!.patient_order_detail?.vw_patient_info?.ageperiod

            } else {

                holder.itemView.patient_info.text =
                    ResultDispathData!!.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData!!.patient_order_detail?.vw_patient_info?.ageperiod
            }
            holder.itemView.TestName.text =
                ResultDispathData!!.patient_order_detail?.test_master!!.name.toString()
            holder.itemView.orderedNUm.text =
                ResultDispathData!!.patient_order_detail?.patient_order?.order_number.toString()
            holder.itemView.approvedBy.text = ResultDispathData!!.approved_details?.first_name.toString()

            holder.itemView.pdf.setOnClickListener {

                onItemClickListener?.onItemClick(ResultDispathData, position)

            }


            /*

        holder.itemView.patient_info.text = labAllData!!.title
        holder.itemView.pinNum.text=labAllData!!.genre
        holder.itemView.TestName.text = labAllData.year

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

        }

        else{


            val ResultDispathData = this.resonseresultdispatchList!![position]

            if (ResultDispathData!!.patient_order_detail?.vw_patient_info?.pattitle != null) {

                holder.itemView.tv1.text =
                    ResultDispathData!!.patient_order_detail?.vw_patient_info?.pattitle + " " + ResultDispathData!!.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData!!.patient_order_detail?.vw_patient_info?.ageperiod

            } else {

                holder.itemView.tv1.text =
                    ResultDispathData!!.patient_order_detail?.vw_patient_info?.first_name + "/" + ResultDispathData!!.patient_order_detail?.vw_patient_info?.ageperiod
            }


            holder.itemView.tv2.text=
                ResultDispathData!!.patient_order_detail?.test_master!!.name.toString()+" / " + ResultDispathData!!.patient_order_detail?.patient_order?.order_number.toString()+" / " + ResultDispathData!!.approved_details?.first_name.toString()+" / "+utils?.convertDateFormat(
                    ResultDispathData?.patient_order_detail!!.patient_order!!.order_request_date!!,
                    "yyyy-MM-dd HH:mm:ss",
                    "dd-MM-yyyy HH:mm"

                )


            holder.itemView.checkbox.setOnClickListener {

                var checkBox:CheckBox = it as CheckBox

                onPrintClickListener!!.onPrintClick(resonseresultdispatchList!![position]!!,checkBox.isChecked)


            }

        }
    }

    override fun getItemCount(): Int {
        return resonseresultdispatchList!!.size
    }

    init {
        mLayoutInflater = LayoutInflater.from(context)
        mContext = context

        utils= Utils(mContext)

        isTablet =utils!!.isTablet(mContext)
    }

    fun setData(responseContent: List<ResponseContentsResultDispatch?>?) {
        this.resonseresultdispatchList= responseContent as ArrayList<ResponseContentsResultDispatch?>?
        notifyDataSetChanged()
    }

    fun addAll(responseContent: List<ResponseContentsResultDispatch?>?) {
        this.resonseresultdispatchList!!.addAll(responseContent!!)
        notifyDataSetChanged()
    }

    fun clearAll() {
        this.resonseresultdispatchList?.clear()
        notifyDataSetChanged()
    }
    fun addLoadingFooter() {
        isLoadingAdded = true
//        add(ResponseContentsResultDispatch())
    }
    fun add(r: ResponseContentsResultDispatch) {
        resonseresultdispatchList!!.add(r)
        notifyItemInserted(resonseresultdispatchList!!.size - 1)
    }
    fun getItem(position: Int): ResponseContentsResultDispatch? {
        return resonseresultdispatchList!![position]
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
      /*  val position = resonseresultdispatchList!!.size - 1
        val result = getItem(position)
        if (result != null) {
            resonseresultdispatchList!!.removeAt(position)
            notifyItemRemoved(position)
        }*/
    }

    interface OnPrintClickListener {
        fun onPrintClick(
            responseContent: ResponseContentsResultDispatch?,
            checkBox: Boolean
        )
    }


    fun setOnPrintClickListener(onprintClickListener: OnPrintClickListener) {
        this.onPrintClickListener = onprintClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(responseContent: ResponseContentsResultDispatch?, position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


}