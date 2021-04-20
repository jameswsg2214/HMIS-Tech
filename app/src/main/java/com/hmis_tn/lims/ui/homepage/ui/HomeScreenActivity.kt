package com.hmis_tn.lims.ui.homepage.ui


import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hmis_tn.lims.R
import com.hmis_tn.lims.config.AppConstants
import com.hmis_tn.lims.config.AppPreferences
import com.hmis_tn.lims.databinding.ActivityHomeScreenBinding
import com.hmis_tn.lims.db.UserDetailsRoomRepository
import com.hmis_tn.lims.retrofitCallbacks.FragmentBackClick
import com.hmis_tn.lims.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.lims.ui.homepage.viewModel.HomeScreenViewModel
import com.hmis_tn.lims.ui.institution.common_departmant.view.fragment.DepartmentInstituteDialogFragment
import com.hmis_tn.lims.ui.institution.lmis.view.fragment.LabInstituteDialogFragment
import com.hmis_tn.lims.ui.institution.rmis.view.fragment.RadiologyInstituteDialogFragment
import com.hmis_tn.lims.ui.lmis.lmisNewOrder.ui.LimsNewOrderFragment
import com.hmis_tn.lims.ui.lmis.lmisOrderStatus.ui.OrderStatusFragment
import com.hmis_tn.lims.ui.lmis.lmisResultDispatch.ui.ResultDispatchFragment
import com.hmis_tn.lims.ui.lmis.lmisTest.view.fragment.LabTestFragment
import com.hmis_tn.lims.ui.lmis.lmisTestApprovel.view.fragment.LabTestApprovalFragment
import com.hmis_tn.lims.ui.lmis.lmisTestProcess.view.fragment.LabTestProcessFragment
import com.hmis_tn.lims.ui.lmis.sampleDispatch.view.fragment.SampleDispatchFragment
import com.hmis_tn.lims.ui.login.model.SimpleResponseModel
import com.hmis_tn.lims.ui.login.view.LoginActivity
import com.hmis_tn.lims.ui.settings.ui.ChangePasswordFragemnt
import com.hmis_tn.lims.ui.settings.ui.LanguagesDialogFragemnt
import com.hmis_tn.lims.utils.Utils
import kotlinx.android.synthetic.main.activity_main_land_screen.*
import kotlinx.android.synthetic.main.land_layout.*
import kotlinx.android.synthetic.main.land_layout.view.*
import kotlinx.android.synthetic.main.navigation_layout.*
import retrofit2.Response

class HomeScreenActivity : AppCompatActivity() ,LanguagesDialogFragemnt.OnLanguageProcessListener,
    FragmentBackClick {

    private var backPressed: Long = 0
    var binding: ActivityHomeScreenBinding? = null

    private var viewModel: HomeScreenViewModel? = null

    var appPreferences: AppPreferences? = null

    var name:String?=null

    private var customdialog: Dialog?=null

    var registercheck:Boolean?=null
    var LmisCheck:Boolean?=null
    var rmisCheck:Boolean?=null

    var ipCheck:Boolean?=null


    private var utils: Utils? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var emrStartDate: String = ""
    var emrEndDate: String = ""
    var emrGender: String = ""
    var emrSession: String = ""

    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_land_screen)

        if (Utils(this).isTablet(this)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen)

        viewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
            HomeScreenViewModel::class.java)

        binding!!.lifecycleOwner = this
        binding?.viewModel = viewModel


        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)
        setSupportActionBar(homeLayoutInclude?.toolbar)


        val toggle = object :
            ActionBarDrawerToggle(
                this,
                drawerLayout, homeLayoutInclude?.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {}
        drawerLayout?.addDrawerListener(toggle)

        toggle.toolbarNavigationClickListener =
            View.OnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }
        homeLayoutInclude?.toolbar?.setNavigationIcon(R.drawable.ic_hamburger_icon)


        LmisCheck= appPreferences?.getBoolean(AppConstants.LMISCHECK)
        registercheck= appPreferences?.getBoolean(AppConstants.REGISTRATIONCHECK)
        rmisCheck= appPreferences?.getBoolean(AppConstants.CHECK_RMIS)
        ipCheck= appPreferences?.getBoolean(AppConstants.CHECK_IPMANAGEMENT)

        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        var departmentname=appPreferences?.getString(AppConstants.DEPARTMENT_NAME)

        userNameTextView?.text = ""+userDataStoreBean?.title?.name+userDataStoreBean?.first_name+"\n"+"($departmentname)"
        utils = Utils(this)


        setLanguage()


        languages?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = LanguagesDialogFragemnt()
            dialog.show(ft, "Tag")
        }

        if(this.LmisCheck!!){

            lab.visibility=View.VISIBLE

        }
        else{

            lab.visibility=View.GONE

        }

        if(this.registercheck!!){

            registration.visibility=View.VISIBLE

        }
        else{

            registration.visibility=View.GONE

        }



        if(this.rmisCheck!!){

            radilogy.visibility=View.VISIBLE

        }
        else{

            radilogy.visibility=View.GONE
        }


        registration?.setOnClickListener {

            if (registerList?.visibility == View.GONE) {


                registerList?.visibility = View.VISIBLE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE

                settingLayout?.visibility= View.GONE


                add_image?.setImageResource(R.drawable.ic_minus_white)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)

                settings_image?.setImageResource(R.drawable.ic_add)

                val covidCheck=appPreferences?.getBoolean(AppConstants.COVIDREGISTER)

                if(covidCheck!!){

                    covid_reg?.visibility = View.VISIBLE

                }
                else{

                    covid_reg?.visibility = View.GONE
                }

                val quickcheck=appPreferences?.getBoolean(AppConstants.QUICKREGISTER)

                if(quickcheck!!){

                    quickReg?.visibility = View.VISIBLE

                }
                else{

                    quickReg?.visibility = View.GONE

                }


                detailedRegistration?.visibility=View.VISIBLE



            } else {
                registerList?.visibility = View.GONE
                add_image?.setImageResource(R.drawable.ic_add)


            }
        }

        lab?.setOnClickListener {

            if (lmisList?.visibility == View.VISIBLE) {

                lmisList?.visibility=View.GONE

                lab_image?.setImageResource(R.drawable.ic_add)

            } else {
                registerList?.visibility = View.GONE
                lmisList?.visibility=View.VISIBLE
                rmislayout?.visibility=View.GONE
                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)
                lab_image?.setImageResource(R.drawable.ic_minus_white)
                rad_image?.setImageResource(R.drawable.ic_add)
                settings_image?.setImageResource(R.drawable.ic_add)


                val labtest=appPreferences?.getBoolean(AppConstants.LABTEST)

                if(labtest!!){

                    lab_test?.visibility = View.VISIBLE
                }
                else{

                    lab_test?.visibility = View.GONE
                }

                val labApprovel=appPreferences?.getBoolean(AppConstants.LABAPPROVEL)

                if(labApprovel!!){

                    lab_approvl?.visibility = View.VISIBLE

                }
                else{

                    lab_approvl?.visibility = View.GONE

                }

                val labProcess=appPreferences?.getBoolean(AppConstants.LABPROCESS)

                if(labProcess!!){

                    lab_process?.visibility = View.VISIBLE

                }
                else{

                    lab_process?.visibility = View.GONE

                }

                val labNew=appPreferences?.getBoolean(AppConstants.LABNEWORDER)

                if(labNew!!){

                    new_order.visibility=View.VISIBLE

                }
                else{

                    new_order.visibility=View.GONE
                }

                val labSampleDispatch=appPreferences?.getBoolean(AppConstants.LABSAMPLEDISPATCH)

                if(labSampleDispatch!!){

                    sample_dispatch.visibility=View.VISIBLE

                }
                else{

                    sample_dispatch.visibility=View.GONE
                }


                val labResultDispatch=appPreferences?.getBoolean(AppConstants.LABRESULTDISPATCH)

                if(labResultDispatch!!){

                    result_dispatch.visibility=View.VISIBLE

                }
                else{

                    result_dispatch.visibility=View.GONE
                }

                val labOderStatus=appPreferences?.getBoolean(AppConstants.LABORDERSTATUS)

                if(labOderStatus!!){

                    order_status.visibility=View.VISIBLE

                }
                else{

                    order_status.visibility=View.GONE
                }


            }
        }


        radilogy?.setOnClickListener {

            if(rmislayout?.visibility==View.GONE){


                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.VISIBLE


                settingLayout?.visibility= View.GONE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_minus_white)


                settings_image?.setImageResource(R.drawable.ic_add)



                val radNewOrderCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TECH)

                val radTestCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TEST)

                val radProcessCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TESTPROCESS)

                val radResultCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_DISPATCH)

                val radorderCheck=appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_ORDER)

                val radDashboard=true//appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_DASHBORD)
                val rmisapprovel= appPreferences?.getBoolean(AppConstants.ACTIVITY_CHECK_RMIS_TESTAPPROVAL)

                if(rmisapprovel!!){

                    rad_approvl?.visibility = View.VISIBLE

                }
                else{

                    rad_approvl?.visibility = View.GONE
                }


                if(radNewOrderCheck!!){

                    rad_new_order?.visibility = View.VISIBLE

                }
                else{

                    rad_new_order?.visibility = View.GONE
                }

                if(radDashboard){

                    rad_dashboard?.visibility = View.VISIBLE
                }
                else{
                    rad_dashboard?.visibility = View.GONE
                }

                if(radTestCheck!!){

                    rad_test?.visibility = View.VISIBLE

                }
                else{

                    rad_test?.visibility = View.GONE
                }

                if(radProcessCheck!!){

                    rad_process?.visibility = View.VISIBLE

                }
                else{

                    rad_process?.visibility = View.GONE
                }

                if(radResultCheck!!){
                    rad_result_dispatch?.visibility = View.VISIBLE

                }
                else{

                    rad_result_dispatch?.visibility = View.GONE

                }
                if(radorderCheck!!){

                    rad_order_status?.visibility = View.VISIBLE

                }
                else{

                    rad_order_status?.visibility = View.GONE
                }


            } else {

                rmislayout?.visibility = View.GONE

                rad_image?.setImageResource(R.drawable.ic_add)


            }


        }




        settings.setOnClickListener {

            if (settingLayout.visibility == View.VISIBLE) {

                settingLayout?.visibility= View.GONE

                settings_image.setImageResource(R.drawable.ic_add)

            } else {


                registerList?.visibility = View.GONE

                lmisList?.visibility=View.GONE

                rmislayout?.visibility=View.GONE


                settingLayout?.visibility= View.VISIBLE

                add_image?.setImageResource(R.drawable.ic_add)

                lab_image?.setImageResource(R.drawable.ic_add)

                rad_image?.setImageResource(R.drawable.ic_add)


                settings_image.setImageResource(R.drawable.ic_minus_white)

                changePassword.visibility = View.VISIBLE
                languages.visibility = View.VISIBLE


            }
        }

        //Application
/*

        application_institution?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val app_institution = InstitutionFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, app_institution)
            fragmentTransaction.commit()
        }

        ipwardmasterlayout?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op = WardMasterFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
*/

        changePassword?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = ChangePasswordFragemnt()
            dialog.show(ft, "Tag")
        }


        covid_reg?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
/*
            val op=QuickRegistrationActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()*/
        }

        quickReg?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
/*
            val op=QuickRegistrationNew()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
         //   fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()*/
        }

        OPCorrection?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
         /*   val op= OpCorrectionFragement()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()*/
        }

        detailedRegistration?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

   /*         val op=DetailedRegistration()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)

            fragmentTransaction.commit()*/
        }



        lab_test?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op=LabTestFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }
        lab_approvl?.setOnClickListener {


            drawerLayout!!.closeDrawer(GravityCompat.START)


            val op=LabTestApprovalFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        lab_process?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)

            val op=LabTestProcessFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()


        }

        new_order?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= LimsNewOrderFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }
        sample_dispatch?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= SampleDispatchFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }
        result_dispatch?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= ResultDispatchFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }

        order_status?.setOnClickListener {

            drawerLayout!!.closeDrawer(GravityCompat.START)
            val op= OrderStatusFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()

        }


        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)

        name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)

     /*   userDetailsRoomRepository = UserDetailsRoomRepository(application!!)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        */
        landofficeName!!.text = ""+userDataStoreBean?.title?.name+userDataStoreBean?.first_name+" / "+name


        iv_change_institue?.setOnClickListener {

            val logintype=appPreferences?.getString(AppConstants.LOGINTYPE)

            if(logintype==AppConstants.LABINCHARGE) {

                val ft = supportFragmentManager.beginTransaction()
                val dialog = LabInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else  if((logintype==AppConstants.PHYSICIANASSISTANT || logintype==AppConstants.PHYSICIANTRAINEE ) || (logintype== AppConstants.HELPDESK || logintype == AppConstants.GENDRALCODE)) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = DepartmentInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

            else  if(logintype==AppConstants.RADINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = RadiologyInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()

                val dialog = DepartmentInstituteDialogFragment()
               // val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }

/*
            if(type!=AppConstants.LABINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = InstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()
                val dialog = SelectInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }*/
        }

        landofficeName?.setOnClickListener {

            val logintype=appPreferences?.getString(AppConstants.LOGINTYPE)

            if(logintype==AppConstants.LABINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()

                val dialog = LabInstituteDialogFragment()

                dialog.show(ft, "Tag")
            }
            else  if((logintype==AppConstants.PHYSICIANASSISTANT || logintype==AppConstants.PHYSICIANTRAINEE ) || (logintype== AppConstants.HELPDESK || logintype == AppConstants.GENDRALCODE)) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = DepartmentInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else  if(logintype==AppConstants.RADINCHARGE) {
                val ft = supportFragmentManager.beginTransaction()
                val dialog = RadiologyInstituteDialogFragment()
                dialog.show(ft, "Tag")
            }
            else{
                val ft = supportFragmentManager.beginTransaction()

                val dialog = DepartmentInstituteDialogFragment()

                dialog.show(ft, "Tag")
            }

        }


        val land= appPreferences?.getString(AppConstants.LANDSCREEN)

        val landurl= appPreferences?.getString(AppConstants.LANDURL)


        if(land!! == AppConstants.LABAPPROVELCODE){

            val op=LabTestApprovalFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.LABPROCESSCODE){

            val op=LabTestProcessFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.LABTESTCODE){

            val op=LabTestFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
/*

        if(land!! == AppConstants.LABAPPROVELCODE){

            val op=LabTestApprovalActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.COVIDREGISTERCODE){

            val op=QuickRegistrationActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.QUICKREGISTERCODE){

            var op=QuickRegistrationNew()

            var bundle:Bundle= Bundle()

            bundle.putInt("PIN",0)

            op.arguments=bundle

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.LABPROCESSCODE){

            val op=LabTestProcessActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }
        else if(land == AppConstants.LABTESTCODE){

            val op=LabTestActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.ACTIVITY_CODE_NURSE_CONFIG || land == AppConstants.BEDMANNGEMENT ){

            val op=NurseEmrWorkFlowActivity()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.commit()
        }

        else if(land == AppConstants.LABNEWORDERCODE){

           */
/* val op=DashBoardActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()*//*

        }

        else if(land == AppConstants.ACTIVITY_IP_WARD_MASTER){

            val op=WardMasterFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.EMRDASHBOARDCODE){

            val op=DashBoardActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
  //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.HELPDESKTICKETS){

            val op=UserTicketsListFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        else if(land == AppConstants.ACTIVITY_RMIS_TECH){

            val op=RMISNewOrderFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.ACTIVITY_RMIS_TESTAPPROVAL){

            val op=RmisTestApprovalActivity()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        if(land == AppConstants.ACTIVITY_BEDSTHW){

            val op=BedStatusHospitalWiseFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else if(land == AppConstants.ACTIVITY_IP_ADMISSION){

            val op=AdmissionListFragment()

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.landfragment, op)
            //          fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        else{


            if(landurl!! ==AppConstants.OPROUTEURL){

                val op= OutPatientFragment()

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, op)
                fragmentTransaction.addToBackStack("home")
                fragmentTransaction.commit()
            }
            else if(landurl ==AppConstants.IPROUTEURL){

                val op= InpatientFragment()

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.landfragment, op)
                fragmentTransaction.commit()
            }
            else{


           }
        }
*/
        changePassword?.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val dialog = ChangePasswordFragemnt()
            dialog.show(ft, "Tag")
        }

        logout?.setOnClickListener {
            drawerLayout!!.closeDrawer(GravityCompat.START)

            customdialog = Dialog(this)
            customdialog!! .requestWindowFeature(Window.FEATURE_NO_TITLE)
            customdialog!! .setCancelable(false)
            customdialog!! .setContentView(R.layout.logout_dialog)
            val closeImageView = customdialog!! .findViewById(R.id.closeImageView) as ImageView
            closeImageView.setOnClickListener {

                customdialog?.dismiss()
            }

            val yesBtn = customdialog!! .findViewById(R.id.yes) as CardView

            val noBtn = customdialog!! .findViewById(R.id.no) as CardView

            yesBtn.setOnClickListener {

                viewModel?.LogoutSession(appPreferences?.getInt(AppConstants.FACILITY_UUID)!!,LoginSeasionRetrofitCallBack)


                appPreferences?.saveInt(AppConstants.LAB_UUID, 0)

                appPreferences?.saveString(AppConstants.OTHER_DEPARTMENT_UUID, "")

                startActivity(Intent(this, LoginActivity::class.java))

                finishAffinity()



                customdialog!!.dismiss()

            }
            noBtn.setOnClickListener {
                customdialog!! .dismiss()


            }
            customdialog!! .show()
        }


    }

    private fun setLanguage() {


        val language = appPreferences?.getString(AppConstants.LANGUAGE)
    /*    if(language?.equals("Tamil")!!)
        {

            val Str_register: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.registration, this)!!
            val Str_Quick_reg : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.quickreg, this)!!
            val Str_covidreg : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.covidreg, this)!!


            val Str_lmis : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.lmis, this)!!

            val Str_new_order : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.neworder, this)!!
            val Str_labtest : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labtesttext, this)!!
            val Str_test_process : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.testprocess, this)!!
            val Str_test_approval : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.testapproval, this)!!
            val Str_sample_dispatch : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.samplediaptch, this)!!
            val Str_result_dispatch : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.resultdispath, this)!!
            val Str_orderstatus : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.orderstatus, this)!!
            /////////////
            val Str_labreport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labreport, this)!!

            val Str_consolidate_report : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.consolidatereport, this)!!
            val Str_districtwisepatientreport: String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.districtwisepatientreport, this)!!
            val Str_districtwisetestreport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.districtwisetestreport, this)!!
            val Str_labwisereport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labwisereport, this)!!
            val Str_labtestwisereport : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.labtestwisereport, this)!!

            ///////
            val Str_labsettings : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.action_settings, this)!!
            val Str_change_password : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.change_password, this)!!
            val Str_languages : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.languages, this)!!

            val Str_logout : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.logout, this)!!

            val Str_emr : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.emr, this)!!

            val Str_tutorial : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.tutorial, this)!!

            val Str_videotutorial : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.videoTutorial, this)!!
            val Str_usermanual : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.userManual, this)!!

            val Str_helpDesk : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.helpDesk, this)!!

            val Str_tickets : String =
                utils?.getLocaleStringResource(Locale("ta"), R.string.tickets, this)!!



*//*


            binding?.navigationItem?.registrationText!!.text = Str_register
            binding?.navigationItem?.lmistext!!.text = Str_lmis
            binding?.navigationItem?.labreporttext!!.text = Str_labreport
            binding?.navigationItem?.settingstext!!.text = Str_labsettings
            binding?.navigationItem?.logouttext!!.text = Str_logout

            binding?.navigationItem?.quickRegText?.text = Str_Quick_reg
            binding?.navigationItem?.covidregtext?.text = Str_covidreg

            //lmis
            binding?.navigationItem?.newordertext?.text = Str_new_order
            binding?.navigationItem?.labtest?.text = Str_labtest
            binding?.navigationItem?.textprocesstext?.text = Str_test_process
            binding?.navigationItem?.testapprovaltext?.text = Str_test_approval
            binding?.navigationItem?.sampledispathtext?.text = Str_sample_dispatch
            binding?.navigationItem?.resultdispathtext?.text = Str_result_dispatch
            binding?.navigationItem?.orderstatustext?.text = Str_orderstatus
            //

            binding?.navigationItem?.consolidatedReportText?.text = Str_consolidate_report
            binding?.navigationItem?.districtWisePatienReportText?.text = Str_districtwisepatientreport
            binding?.navigationItem?.districtwisetestreporttext?.text = Str_districtwisetestreport
            binding?.navigationItem?.labwisereporttext?.text = Str_labwisereport
            binding?.navigationItem?.labtestwisereporttext?.text = Str_labtestwisereport
//////////////////

            binding?.navigationItem?.changepasswordtext?.text = Str_change_password
            binding?.navigationItem?.languagetext?.text = Str_languages

            binding?.navigationItem?.emrtext?.text = Str_emr

            //tutorial
            binding?.navigationItem?.tutorialtext?.text = Str_tutorial
            binding?.navigationItem?.videoTutorialtext?.text = Str_videotutorial
            binding?.navigationItem?.userManualtext?.text = Str_usermanual

            //Help Desk
            binding?.navigationItem?.helpDesktext?.text = Str_helpDesk
            binding?.navigationItem?.ticketstext?.text = Str_tickets
*//*
        }
        else if(language.equals("English"))
        {
            val Str_register: String =
                utils?.getLocaleStringResource(Locale("en"), R.string.registration, this)!!
            val Str_lmis : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.lmis, this)!!
            val Str_labreport : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.labreport, this)!!
            val Str_tutorial : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.tutorial, this)!!
            val Str_helpDesk : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.helpDesk, this)!!
            val Str_labsettings : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.action_settings, this)!!
            val Str_logout : String =
                utils?.getLocaleStringResource(Locale("en"), R.string.logout, this)!!
*//*
            binding?.navigationItem?.registrationText!!.text = Str_register
            binding?.navigationItem?.lmistext!!.text = Str_lmis
            binding?.navigationItem?.labreporttext!!.text = Str_labreport
            binding?.navigationItem?.tutorialtext!!.text = Str_tutorial
            binding?.navigationItem?.helpDesktext!!.text = Str_helpDesk
            binding?.navigationItem?.settingstext!!.text = Str_labsettings
            binding?.navigationItem?.logouttext!!.text = Str_logout*//*
        }
*/

    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).addToBackStack("").commit()
    }

    fun replaceFragmentNoBack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).commit()
    }

    fun replaceFragmentbundledata(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.landfragment, fragment).commit()
    }


    override fun onRefreshLanguage() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
/*        if (selectedFragment is DashBoardActivity) {
            if (backPressed + 2000 > System.currentTimeMillis())
                finish()
            else {
                Toast.makeText(this@HomeScreenActivity, "Press once again to exit", Toast.LENGTH_SHORT)
                    .show()
            }
            backPressed = System.currentTimeMillis()
        } else {
            supportFragmentManager.popBackStack()
        }*/
    }

    override fun setSelectedFragment(fragment: Fragment?) {
        this.selectedFragment = fragment
    }



    val LoginSeasionRetrofitCallBack = object : RetrofitCallback<SimpleResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<SimpleResponseModel?>) {

            if (responseBody?.body()?.statusCode == 200) {


            }

        }

        override fun onBadRequest(errorBody: Response<SimpleResponseModel?>) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {

        }
    }


}
