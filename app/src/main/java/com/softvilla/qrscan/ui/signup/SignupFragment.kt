package com.softvilla.qrscan.ui.signup


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.softvilla.qrscan.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initCallbacks() //initialzie callback

    }

    private fun initCallbacks() {
        //action back button callback
        btn_back.setOnClickListener {
            navController.popBackStack() //pop backstack
        }

        //name clear button callback
        btn_name_clear.setOnClickListener{
            et_name.setText("")
        }

        //phone number clear button callback
        btn_number_clear.setOnClickListener{
            et_phone_number.setText("")
        }

    }
}
