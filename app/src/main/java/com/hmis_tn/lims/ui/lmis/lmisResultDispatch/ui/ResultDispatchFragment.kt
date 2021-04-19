package com.hmis_tn.lims.ui.lmis.lmisResultDispatch.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.FragmentResultDispatchBinding
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.utils.Utils
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model.ResponseContentsResultDispatch
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.model.ResponseResultDispatch
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.request.RequestDispatchSearch
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.request.Requestpdf
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.viewmodel.ResultDispatchViewModel
import com.hmis_tn.lims.utils.FileHelper
import okhttp3.ResponseBody

import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ResultDispatchFragment : Fragment() {

    var binding: FragmentResultDispatchBinding? = null
    var utils: Utils? = null
    private var viewModel: ResultDispatchViewModel? = null
    var appPreferences: AppPreferences? = null
    var listPrint:ArrayList<ResponseContentsResultDispatch?> = ArrayList()

    private var mAdapter: ResultDispatchAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var toDate: String = ""
    private var fromDateRev: String = ""
    private var toDateRev: String = ""
    private var isLoadingPaginationAdapterCallback: Boolean = false
    var cal = Calendar.getInstance()

    private var startDate: String = ""
    private var endDate: String = ""
    /////Pagination

    private var currentPage = 0
    private var pageSize = 10
    private var isTablet = false
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES: Int = 0
    private var facility_id: Int = 0

    private var Labuuid: Int = 0
    private var pinnumber: String = ""
    private var searchUsingOrderNo: String = ""
    private var qucik_search: String = ""

    var downloadZipFileTask: DownloadZipFileTask? =
        null
    private var destinationFile: File?=null

    private  var printListUUid:ArrayList<Int> = ArrayList()
    private  var printpatentUUid:ArrayList<String> = ArrayList()

    companion object {
        const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101

    }


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_result_dispatch,
                container,
                false
            )

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(ResultDispatchViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())
        isTablet = utils!!.isTablet(requireContext())
        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)

        }
        binding?.drawerLayout?.drawerElevation = 0f
        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.sampleDispatchrecycleview!!.layoutManager = linearLayoutManager
        mAdapter = ResultDispatchAdapter(requireContext(), ArrayList())
        binding?.sampleDispatchrecycleview!!.adapter = mAdapter
        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        utils = Utils(requireContext())
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        Labuuid = appPreferences?.getInt(AppConstants.LAB_UUID)!!
        utils = Utils(requireContext())

        qucik_search = ""
        pinnumber = ""
        searchUsingOrderNo = ""

        labResultDispatchListAPI(pageSize, currentPage)

        initViews()
        listeners()

        return binding!!.root
    }

    private fun initViews() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        binding?.calendarEditText!!.setText("""${formatter.format(Date())}-${formatter.format(Date())}""")

        //startDate = utils!!.getAgedayDifferent(1)+ "T23:59:59.000Z"

        //endDate = sdf.format(Date())+"T23:59:59.000Z"
    }

    private fun listeners() {
        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
                binding?.advanceSearchLayout?.visibility = View.GONE


            } else {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE


            }
        }

        binding?.searchButton!!.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString().isEmpty()) {

                startDate = fromDateRev + "T00:01:00.000Z"
                endDate = toDateRev + "T23:59:59.000Z"
            }

            qucik_search = binding?.qucikSearch!!.text.trim().toString()
            pinnumber = binding?.patientDetailsPin!!.text.trim().toString()
            searchUsingOrderNo = binding?.searchUsingOrderNo!!.text.trim().toString()

            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

            mAdapter!!.clearAll()

            pageSize = 10

            currentPage = 0

            labResultDispatchListAPI(pageSize, currentPage)
        }

        mAdapter!!.setOnPrintClickListener(object : ResultDispatchAdapter.OnPrintClickListener {
            override fun onPrintClick(
                responseContent: ResponseContentsResultDispatch?,
                checkBox: Boolean
            ) {

                if (checkBox) {


                    if (printpatentUUid.size != 0) {

                        if (printpatentUUid.contains(responseContent!!.patient_order_detail!!.vw_patient_info!!.uhid!!)) {

                            printpatentUUid.add(responseContent!!.patient_order_detail!!.vw_patient_info!!.uhid!!)

                            printListUUid.add(responseContent!!.patient_order_detail!!.patient_order_test_details_uuid!!)

                        } else {

                            Toast.makeText(requireContext(), "Different PIN and order Number Not possible to print", Toast.LENGTH_SHORT).show()

                            mAdapter!!.clearCheckBox()

                            printListUUid.clear()
                            printpatentUUid.clear()

                        }

                    } else {

                        printpatentUUid.add(responseContent!!.patient_order_detail!!.vw_patient_info!!.uhid!!)

                        printListUUid.add(responseContent!!.patient_order_detail!!.patient_order_test_details_uuid!!)


                    }

                } else {

                    listPrint!!.remove(responseContent)

                    printpatentUUid.add(responseContent!!.patient_order_detail!!.vw_patient_info!!.uhid!!)

                    printListUUid.add(responseContent!!.patient_order_detail!!.patient_order_test_details_uuid!!)

                }

            }
        })


        binding?.print?.setOnClickListener {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runTimePermission()

            } else {

                val requestpdf : Requestpdf = Requestpdf()
                requestpdf.Id= printListUUid


                //    listOf(listPrint[0]!!.patient_order_detail?.patient_order_test_details_uuid!!)
                viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)

            }


        }

        mAdapter!!.setOnItemClickListener(object :
            ResultDispatchAdapter.OnItemClickListener {
            override fun onItemClick(
                responseContent: ResponseContentsResultDispatch?,
                position: Int
            ) {
         /*       val bundle = Bundle()
                bundle.putInt(
                    "pdfid",
                    responseContent?.patient_order_detail?.patient_order_test_details_uuid!!
                )
                val labTechnicianFragment = ResultPDFViewerActivity()
                labTechnicianFragment.arguments = bundle//passing data to fragment
                val fragmentManager: FragmentManager? = fragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, labTechnicianFragment)
                fragmentTransaction.commit()*/
            }
        })

        binding?.calendarEditText!!.setOnClickListener {

            Toast.makeText(context, "Select Start Date", Toast.LENGTH_LONG).show()
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    Toast.makeText(context, "Select End Date", Toast.LENGTH_LONG).show()

                    fromDate = String.format(
                        "%02d",
                        dayOfMonth
                    ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year

                    fromDateRev = year.toString() + "-" + String.format(
                        "%02d",
                        monthOfYear + 1
                    ) + "-" + String.format(
                        "%02d",
                        dayOfMonth
                    )

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    val dateoickDialog = DatePickerDialog(
                        requireContext(),
                        DatePickerDialog.OnDateSetListener { view, year1, month1, dayOfMonth1 ->

                            toDate = String.format(
                                "%02d",
                                dayOfMonth1
                            ) + "-" + String.format("%02d", month1 + 1) + "-" + year1

                            toDateRev = year1.toString() + "-" + String.format(
                                "%02d",
                                month1 + 1
                            ) + "-" + String.format(
                                "%02d",
                                dayOfMonth1
                            )

                            binding?.calendarEditText!!.setText(fromDate + "-" + toDate)

                        },
                        mYear!!,
                        mMonth!!,
                        mDay!!
                    )

                    dateoickDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

                    dateoickDialog.datePicker.minDate = cal.timeInMillis

                    dateoickDialog.show()


                }, mYear!!, mMonth!!, mDay!!
            )

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

            datePickerDialog.show()

        }

        binding?.sampleDispatchrecycleview?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (!isLoadingPaginationAdapterCallback) {
                        isLoadingPaginationAdapterCallback = true
                        currentPage += 1
                        if (currentPage <= TOTAL_PAGES) {
                            // Toast.makeText(requireContext(),""+currentPage,Toast.LENGTH_LONG).show()
                            resultDispatchSeacondAPI(pageSize, currentPage)
                        }

                    }
                }
            }
        })

        binding?.clear?.setOnClickListener {
            clearAllFields()

            initViews()
        }
    }

    private fun clearAllFields() {
        binding?.qucikSearch?.setText("")
        binding?.patientDetailsPin?.setText("")
        binding?.searchUsingOrderNo?.setText("")
        binding?.calendarEditText?.setText("")
    }

    private fun resultDispatchSeacondAPI(
        pageSize: Int,
        currentPage: Int
    ) {

        //customProgressDialog!!.show()
        val requestResultdiapatch = RequestDispatchSearch()
        requestResultdiapatch.pageNo = currentPage
        requestResultdiapatch.paginationSize = pageSize
        requestResultdiapatch.search = ""
        requestResultdiapatch.sortField = "modified_date"
        requestResultdiapatch.sortOrder = "DESC"
        requestResultdiapatch.search = qucik_search
        requestResultdiapatch.order_number = searchUsingOrderNo
        requestResultdiapatch.uhid = pinnumber
        requestResultdiapatch.fromDate = startDate
        requestResultdiapatch.toDate = endDate
        viewModel?.getresultdispatchsecond(
            requestResultdiapatch,
            secondresultdispatchResponseRetrofitCallback
        )


    }

    private fun labResultDispatchListAPI(
        pageSize: Int,
        currentPage: Int
    ) {
        //customProgressDialog!!.show()
        val requestResultdiapatch = RequestDispatchSearch()
        requestResultdiapatch.pageNo = currentPage
        requestResultdiapatch.paginationSize = pageSize
        requestResultdiapatch.search = ""
        requestResultdiapatch.sortField = "modified_date"
        requestResultdiapatch.sortOrder = "DESC"
        requestResultdiapatch.search = qucik_search
        requestResultdiapatch.order_number = searchUsingOrderNo
        requestResultdiapatch.uhid = pinnumber
        requestResultdiapatch.fromDate = startDate
        requestResultdiapatch.toDate = endDate

        viewModel?.getresultdispatch(requestResultdiapatch, resultdispatchResponseRetrofitCallback)
    }

    val resultdispatchResponseRetrofitCallback = object : RetrofitCallback<ResponseResultDispatch> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseResultDispatch?>) {
            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                binding?.resultDispatchCount?.text = "Result Dispatch Count " + responseBody.body()?.totalRecords
                TOTAL_PAGES =
                    Math.ceil(responseBody.body()!!.totalRecords!!.toDouble() / 10).toInt()
                if (responseBody.body()!!.responseContents!!.isNotEmpty()) {
                    isLoadingPaginationAdapterCallback = false
                    mAdapter!!.addAll(responseBody.body()!!.responseContents)
                    if (currentPage < TOTAL_PAGES) {
                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter!!.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                    } else {
                        binding?.progressbar!!.visibility = View.GONE
                        mAdapter!!.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                    }

                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
                if (responseBody.body()!!.totalRecords!! < 11) {
                    binding?.progressbar!!.visibility = View.GONE
                }

            } else {

                binding?.resultDispatchCount?.text = "Result Dispatch Count 0 "
                Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show()
            }
        }

        override fun onBadRequest(errorBody: Response<ResponseResultDispatch?>) {
            isLoadingPaginationAdapterCallback = false
            val gson = GsonBuilder().create()
            val responseModel: ResponseResultDispatch
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ResponseResultDispatch::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.msg!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>?) {
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            isLoadingPaginationAdapterCallback = false
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure?:"")
        }

        override fun onEverytime() {
            isLoadingPaginationAdapterCallback = false
            binding?.progressbar!!.visibility = View.GONE

        }


    }


    val secondresultdispatchResponseRetrofitCallback =
        object : RetrofitCallback<ResponseResultDispatch> {
            override fun onSuccessfulResponse(responseBody: Response<ResponseResultDispatch?>) {
                if (responseBody.body()?.responseContents!!.isNotEmpty()) {
                    binding?.progressbar!!.visibility = View.GONE
                    mAdapter!!.removeLoadingFooter()
                    isLoading = false
                    isLoadingPaginationAdapterCallback = false
                    mAdapter?.addAll(responseBody.body()!!.responseContents)

                    println("testing for two  = $currentPage--$TOTAL_PAGES")

                    if (currentPage < TOTAL_PAGES) {

                        binding?.progressbar!!.visibility = View.VISIBLE
                        mAdapter?.addLoadingFooter()
                        isLoading = true
                        isLastPage = false
                        println("testing for four  = $currentPage--$TOTAL_PAGES")
                    } else {
                        isLastPage = true
                        binding?.progressbar!!.visibility = View.GONE
//                    visitHistoryAdapter.removeLoadingFooter()
                        isLoading = false
                        isLastPage = true
                        println("testing for five  = $currentPage--$TOTAL_PAGES")
                    }

                } else {
                    binding?.progressbar!!.visibility = View.GONE
                    println("testing for six  = $currentPage--$TOTAL_PAGES")
                    mAdapter?.removeLoadingFooter()
                    isLoading = false
                    isLastPage = true
                }
            }

            override fun onBadRequest(errorBody: Response<ResponseResultDispatch?>) {
                isLoadingPaginationAdapterCallback = false
                mAdapter?.removeLoadingFooter()
                isLoading = false
                isLastPage = true

            }

            override fun onServerError(response: Response<*>?) {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onForbidden() {
                viewModel!!.progress.value = View.GONE
                utils!!.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String?) {
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, failure?:"")
            }

            override fun onEverytime() {
                binding?.progressbar!!.visibility = View.GONE
            }
        }




    private fun runTimePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        }

        else{
            val requestpdf : Requestpdf = Requestpdf()
            requestpdf.Id= printListUUid


                //listOf(listPrint.get(0)!!.patient_order_detail?.patient_order_test_details_uuid)
            viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)
            return
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            Log.i("", "" + grantResults)

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // now, you have permission go ahead
                val requestpdf : Requestpdf = Requestpdf()
                requestpdf.Id= printListUUid

              //      listOf(listPrint[0]?.patient_order_detail?.patient_order_test_details_uuid)
                viewModel?.GetPDFdownload(requestpdf, GetPDFRetrofitCallback)

            } else {

                getCustomDialog()
            }

        }

    }

    private fun getCustomDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(context)
        // set message of alert dialog
        dialogBuilder.setMessage("App need this permission")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                runTimePermission()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Permission!!")
        // show alert dialog
        alert.show()
    }

    val GetPDFRetrofitCallback = object : RetrofitCallback<ResponseBody> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseBody?>) {
            //you can now get your file in the InputStream
            downloadZipFileTask = DownloadZipFileTask()
            downloadZipFileTask!!.execute(responseBody?.body())


        }

        override fun onBadRequest(errorBody: Response<ResponseBody?>) {

//            Log.e("badreq",response.toString())
//            val gson = GsonBuilder().create()
            /*var mError = SampleErrorResponse()
            try {
                mError = gson.fromJson(response!!.errorBody()!!.string(), SampleErrorResponse::class.java)



            } catch (e: IOException) { // handle failure to read error
            }*/
        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(failure: String?) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }
    @Suppress("DEPRECATION")
    inner class DownloadZipFileTask :
        AsyncTask<ResponseBody?, Pair<Int?, Long?>?, String?>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }
        fun doProgress(progressDetails: Pair<Int?, Long?>?) {
            publishProgress(progressDetails)
        }

        override fun onPostExecute(result: String?) {
//            binding?.progressbar!!.setVisibility(View.GONE);
/*
            binding?.pdfView!!.fromFile(destinationFile)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onPageError { page, _ ->
                    Toast.makeText(
                        context,
                        "Error at page: $page", Toast.LENGTH_LONG
                    ).show()
                }
                .load()
            Toast.makeText(
                context,
                "Storage path: $destinationFile", Toast.LENGTH_LONG
            ).show()*/


            destinationFile?.let { FileHelper(context).showNotification(it) }


        }
        override fun doInBackground(vararg params: ResponseBody?): String? {
            saveToDisk(params[0]!!, "${listPrint[0]!!.patient_order_detail!!.vw_patient_info!!.first_name}.pdf")
            return null
        }}
    private fun saveToDisk(body: ResponseBody, filename: String) {
        try {
            destinationFile = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            val inputstream : InputStream? = body.byteStream()
            val os: OutputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            //read from is to buffer
            while (inputstream!!.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputstream.close()
            //flush OutputStream to write any buffered data to file
            os.flush()
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}



