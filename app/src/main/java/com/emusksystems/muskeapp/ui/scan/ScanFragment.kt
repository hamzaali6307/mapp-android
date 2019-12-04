package com.emusksystems.muskeapp.ui.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.emusksystems.muskeapp.R
import kotlinx.android.synthetic.main.fragment_scan.*


class ScanFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var codeScanner: CodeScanner
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val PERMISSION_REQUEST_CAMERA = 444

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        navController = Navigation.findNavController(view)
        initCodeScanner() //initialize code scanner and its callback
    }

    private fun initCodeScanner(){
        codeScanner = CodeScanner(activity!!, view_scanner)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            mHandler.post {
                showToast("Scan result: ${it.text}") //run on ui thread
                navController.navigate(R.id.action_scanFragment_to_signupFragment) //navigate to singup fragment
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            mHandler.post {
                showToast("Camera initialization error: ${it.message}")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(hasLocationPermission()){
            codeScanner.startPreview()
        }else{
            askLocationPermission()
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun hasLocationPermission():Boolean{
        return (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
    }


    private fun askLocationPermission(){
        // Permission is not granted
        // Should we show an explanation?
        // No explanation needed, we can request the permission.
        requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
    }

    private fun showToast(txt: String){
        Toast.makeText(activity!!, txt, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, check location settings
                    codeScanner.startPreview()
                }
                return
            }
        }
    }
}
