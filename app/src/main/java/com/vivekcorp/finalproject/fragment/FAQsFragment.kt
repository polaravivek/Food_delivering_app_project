package com.vivekcorp.finalproject.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.adapter.FAQRecyclerAdapter
import com.vivekcorp.finalproject.model.FAQ

class FAQsFragment : Fragment() {

    lateinit var recyclerFaq: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    val faqList = arrayListOf<FAQ>(
        FAQ(
            "Q.1 I don't remember my password?",
            "A.1 You have already created an account but you can't remember your password? " +
                    "Click on 'Login/Sign Up' at the top of the page. Then click on 'Forgot Password?'." +
                    " Fill out your phone number and a password recovery will be sent to you by phone."
        ),
        FAQ(
            "Q.2 How can I create an account at www.abc.com?",
            "A.2 Click on 'Login & Sign up' at the top of the page. Then fill out your" +
                    " information in the 'Create an account' section and click the 'Sign Up' button. " +
                    "You can also create an account directly after placing an order. " +
                    "Your delivery address and the order details will then be saved in your account."
        ),
        FAQ(
            "Q.3 What are your delivery hours?",
            "A.3 Our delivery hour is from 10:00 AM to 08:00 PM."
        ),
        FAQ(
            "Q.4 How much time it takes to deliver the order?",
            "A.4 Generally it takes between 45 minutes to 1 hour time to deliver the order." +
                    " Due to long distance or heavy traffic, delivery might take few extra minutes."
        ),
        FAQ(
            "Q.5 How do I know status of my order?",
            "A.5 After you place your order, it is sent immediately to the bliss " +
                    "kitchen, which starts preparing it without any delay. The kitchen does everything" +
                    " to process your order as quickly as possible. However," +
                    " sometimes Blissmeal receives large amount of orders, or drivers get stuck in " +
                    "heavy traffic this might cause little delays. If the amount of time you've waited " +
                    "has exceeded the estimated delivery time, you may please contact us. You will " +
                    "a receive an SMS as soon as your order is dispatched."
        ),
        FAQ(
            "Q.6 How to order online at www.abc.com?",
            "A.6 It is really easy, as easy as 1, 2, 3...\n" +
                    "\n" +
                    "Tell us where you are: enter your location so that we know your address to " +
                    "deliver your order.\n" +
                    "Choose what you would like: select items you'd like to order. You can search by" +
                    " cuisine type, dish name.\n" +
                    "Checkout: Enter your exact delivery address, payment method and your phone " +
                    "number. Always make sure that you enter the correct phone number" +
                    " to help us contact you regarding your order if needed. Now sit back, relax, " +
                    "and we'll get your food delivered to your doorstep."
        ),
        FAQ(
            "Q.7 I need to cancel or change my order! How can I do this?",
            "A.7 Please contact helpline Number as soon as possible, we can let the kitchen " +
                    "know before it starts preparing" +
                    " your order. Once the order goes in the process, it can not be changed. With " +
                    "regard to any refund of a payment you have made online, please contact Bliss Meal delivery."
        ),
        FAQ(
            "Q.8 Do I have to create an account to place an order?",
            "A.8 Creating an account is mandatory, but you can see order without having to " +
                    "sign up. We make sure that ordering " +
                    "food online at Bliss Meal delivery is quick and fuss-free. After placing your" +
                    " order, you will have the option of creating an account."
        )
    )

    lateinit var recyclerAdapter: FAQRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_a_qs, container, false)

        recyclerFaq = view.findViewById(R.id.faqRecyclerView)

        layoutManager = LinearLayoutManager(activity)

        recyclerAdapter = FAQRecyclerAdapter(activity as Context, faqList)

        recyclerFaq.adapter = recyclerAdapter

        recyclerFaq.layoutManager = layoutManager

        recyclerFaq.addItemDecoration(
            DividerItemDecoration(
                recyclerFaq.context,
                DividerItemDecoration.VERTICAL
            )
        )

        return view
    }

}