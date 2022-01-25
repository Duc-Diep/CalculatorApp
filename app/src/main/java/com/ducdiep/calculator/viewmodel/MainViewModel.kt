package com.ducdiep.calculator.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ducdiep.calculator.model.CalMethods.Companion.addTwoNumber
import com.ducdiep.calculator.model.CalMethods.Companion.divideTwoNumber
import com.ducdiep.calculator.model.CalMethods.Companion.multiTwoNumber
import com.ducdiep.calculator.model.CalMethods.Companion.subtractTwoNumber
import com.ducdiep.calculator.model.Calculation
import com.ducdiep.calculator.model.ConvertToPostFix
import com.ducdiep.calculator.sqlite.SQLHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val context: Context = getApplication<Application>().applicationContext
    val input = MutableLiveData<StringBuilder>()
    val output = MutableLiveData<String>()
    val isError = MutableLiveData<Boolean>()
    var sqlHelper: SQLHelper

    init {
        input.value = StringBuilder()
        sqlHelper = SQLHelper(context)
    }

    fun clearExpression() {
        input.value = input.value?.clear()
        output.value = ""
    }

    fun deleteLast() {
        if (input.value?.length!! < 2) {
            input.value = input.value?.clear()
        } else {
            if (input.value?.last()?.isDigit()!!) {
                input.value = input.value?.deleteCharAt(input.value?.length!! - 1)
            } else {
                input.value = input.value?.delete(input.value?.length!! - 3, input.value?.length!!)
            }
        }
    }

    fun addNumber(s: String) {
        input.value = input.value?.append(s)
        Log.d("value", "addNumber: ${input.value}")
    }

    fun addOperator(s: String) {
        if (input.value?.isEmpty()!! || input.value?.get(input.value?.length!! - 1)?.isDigit()!!) {
            input.value = input.value?.append(s)
        }
    }

    private fun isError():Boolean{
        if (input.value?.length == 0 || !input.value?.get(input.value?.length!! - 1)?.isDigit()!!){
            return true
        }
        return false
    }

    fun calculate() {
        if (isError()){
            isError.value = true
        }else{
            var list: MutableList<String> = (input.value?.split(" ")) as MutableList<String>
            var list2 = mutableListOf<String>()
            list.forEach {
                if (it.first() == ' ') list2.add(it.removeRange(0..0))
                else list2.add(it)
            }

            val postFix = ConvertToPostFix().convert(list2)

            val lastResult = calculateResult(postFix)
            val cal = Calculation(input.value.toString(), lastResult)
            sqlHelper.addCalculation(cal)
            output.value = lastResult
        }

    }


    private fun calculateResult(list: List<String>): String {
        val stack: Stack<String> = Stack()
        val ops = ConvertToPostFix().baseOperators
        Log.d("tag", "calculateResult: $list")
        list.forEach { item ->
            if (!ops.containsKey(item)) { // !operator -> push stack
                stack.push(item)
            } else { // operator
                val p2 = stack.pop()
                val p1 = stack.pop()
                val absP1 = if (p1.length > 1) p1.substring(1) else p1
                val absP2 = if (p2.length > 1) p2.substring(1) else p2
                when (item) {
                    "+" -> {
                        when {
                            p1.startsWith('-') -> {
                                stack.push(subtractTwoNumber(p2, absP1))
                            }
                            p2.startsWith('-') -> {
                                stack.push(subtractTwoNumber(p1, absP2))
                            }
                            else -> stack.push(addTwoNumber(p1, p2))
                        }
                    }
                    "-" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            val value = subtractTwoNumber(absP2, absP1)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            stack.push("-" + addTwoNumber(absP1, p2))
                        } else if (p2.startsWith('-')) {
                            stack.push(addTwoNumber(p1, absP2))
                        } else stack.push(subtractTwoNumber(p1, p2))
                    }
                    "×" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            var value = multiTwoNumber(absP1, absP2)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            var value = multiTwoNumber(p2, absP1)
                            value = "-$value"
                            stack.push(value)
                        } else if (p2.startsWith('-')) {
                            var value = multiTwoNumber(p1, absP2)
                            value = "-$value"
                            stack.push(value)
                        } else stack.push(multiTwoNumber(p1, p2))
                    }
                    "÷" -> {
                        if (p1.startsWith('-') && p2.startsWith('-')) {
                            var value = divideTwoNumber(absP1, absP2)
                            stack.push(value)
                        } else if (p1.startsWith('-')) {
                            var value = divideTwoNumber(p2, absP1)
                            value = "-$value"
                            stack.push(value)
                        } else if (p2.startsWith('-')) {
                            var value = divideTwoNumber(p1, absP2)
                            value = "-$value"
                            stack.push(value)
                        } else stack.push(divideTwoNumber(p1, p2))
                    }
                }
            }
            Log.d("tag", "calculateResult: ${stack}")
        }
        return stack.peek().toString()
    }

}