package com.ducdiep.calculator.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.ducdiep.calculator.R
import com.ducdiep.calculator.model.CalMethods
import com.ducdiep.calculator.model.Calculation
import com.ducdiep.calculator.model.ConvertToPostFix
import com.ducdiep.calculator.sqlite.SQLHelper
import com.ducdiep.calculator.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
//    var expression = StringBuilder()
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        var s1 = "723547568"
        var s2 = "3456777"
        var result = CalMethods.divideTwoNumber(s1,s2)
        Log.d("abc", "onCreate: $result")
        init()
        setClick()
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(MainViewModel::class.java)
        viewModel.input.observe(this,{ t->
            edt_input.setText(t)
        })
        viewModel.output.observe(this,{ t->
            tv_output.text = t
        })
    }

    private fun setClick() {
        tv_history.setOnClickListener(this)
        btn_clear.setOnClickListener(this)
        btn_delete1.setOnClickListener(this)
        btn_exponential.setOnClickListener(this)
        btn_square2.setOnClickListener(this)
        btn_add.setOnClickListener(this)
        btn_multi.setOnClickListener(this)
        btn_sub.setOnClickListener(this)
        btn_divide.setOnClickListener(this)
        btn_equal.setOnClickListener(this)
        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
            btn_clear -> viewModel.clearExpression()
            btn_delete1 -> viewModel.deleteLast()
            btn_exponential -> viewModel.addOperator(" ^ ")
            btn_square2 -> viewModel.addOperator(" √ ")
            btn_add -> viewModel.addOperator(" + ")
            btn_multi -> viewModel.addOperator(" × ")
            btn_sub -> viewModel.addOperator(" - ")
            btn_divide -> viewModel.addOperator(" ÷ ")
            btn_equal -> viewModel.calculate()

            btn0 -> viewModel.addNumber("0")
            btn1 -> viewModel.addNumber("1")
            btn2 -> viewModel.addNumber("2")
            btn3 -> viewModel.addNumber("3")
            btn4 -> viewModel.addNumber("4")
            btn5 -> viewModel.addNumber("5")
            btn6 -> viewModel.addNumber("6")
            btn7 -> viewModel.addNumber("7")
            btn8 -> viewModel.addNumber("8")
            btn9 -> viewModel.addNumber("9")
        }
        edt_input.setSelection(edt_input.text.length)
    }

//    private fun clearExpresion() {
//        edt_input.setText("")
//        tv_output.text = ""
//        expression.clear()
//    }




}