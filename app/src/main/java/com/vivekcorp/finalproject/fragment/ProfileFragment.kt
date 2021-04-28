package com.vivekcorp.finalproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vivekcorp.finalproject.R

class ProfileFragment(val bundle: Bundle) : Fragment() {

    lateinit var nameProfile: TextView
    lateinit var numberRegister: TextView
    lateinit var emailAddress: TextView
    lateinit var deliveryAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        nameProfile = view.findViewById(R.id.nameProfile)
        numberRegister = view.findViewById(R.id.numberRegister)
        emailAddress = view.findViewById(R.id.emailAddress)
        deliveryAddress = view.findViewById(R.id.deliveryAddress)

        val data: String = bundle.getString("data").toString()

        if(data == "login"){
            nameProfile.text = bundle.getString("name")
            numberRegister.text = bundle.getString("mobile number")
            emailAddress.text = bundle.getString("email")
            deliveryAddress.text = bundle.getString("address")
        }else if(data == "register"){
            nameProfile.text = bundle.getString("name")
            numberRegister.text = bundle.getString("mobile number")
            emailAddress.text = bundle.getString("email")
            deliveryAddress.text = bundle.getString("address")
        }

        return view
    }
}