package com.ducdiep.calculator.model

import android.util.Log
import java.lang.Exception

class CalMethods {
    companion object {

        fun calculateBit(n1: String, n2: String): HashMap<String, Int> {
            val posDot1 = if (!n1.contains(".")) 0 else n1.lastIndexOf(".")
            val posDot2 = if (!n2.contains(".")) 0 else n2.lastIndexOf(".")

            val dotToEnd1 = if (posDot1 == 0) 0 else n1.length - 1 - posDot1
            val dotToEnd2 = if (posDot2 == 0) 0 else n2.length - 1 - posDot2
            //calcul teleBit was parse
            val teleBitRight = Math.max(dotToEnd1, dotToEnd2)
            //number 0 need add to equal
            val delta1 = teleBitRight - (dotToEnd1)
            val delta2 = teleBitRight - (dotToEnd2)
            return hashMapOf(
                "posDot1" to posDot1,
                "posDot2" to posDot2,
                "teleRight" to teleBitRight,
                "delta1" to delta1,
                "delta2" to delta2,
                "dotToEnd1" to dotToEnd1,
                "dotToEnd2" to dotToEnd2
            )
        }

        //    number1.legth>number2.legth
        fun addTwoNumber(number1: String, number2: String): String {

            val cal = calculateBit(number1, number2)
            var posDot1 = cal["posDot1"]!!
            var posDot2 = cal["posDot2"]!!
            var delta1 = cal["delta1"]!!
            var delta2 = cal["delta2"]!!
            val teleBitRight = cal["teleRight"]!!

            var s1 = if (posDot1 > 0) number1.removeRange(posDot1..posDot1) else number1
            var s2 = if (posDot2 > 0) number2.removeRange(posDot2..posDot2) else number2
            if (delta1 > 0) {
                repeat(delta1) {
                    s1 += "0"
                }
            }
            if (delta2 > 0) {
                repeat(delta2) {
                    s2 += "0"
                }
            }

            Log.d("test", "addTwoNumber: s1= $s1, s2= $s2")


            //add integer
            if (s1.length < s2.length) {
                s1 = s2.also { s2 = s1 }
            }

            var length1 = s1.length
            var length2 = s2.length

            var result = StringBuilder()
            var remember = false

            //for number2
            for (i in s2.indices) {
                var temp = s2[length2 - i - 1].digitToInt() + s1[length1 - i - 1].digitToInt()
                if (remember)
                    temp++
                remember = temp > 9
                temp %= 10
                result.append(temp)
            }

            //
            for (i in 0 until length1 - length2) {
                var temp = s1[length1 - length2 - i - 1].digitToInt()
                if (remember)
                    temp++
                remember = temp > 9
                temp %= 10
                result.append(temp)
            }

            //case 999+1 du 1
            if (remember) {
                result.append(1)
            }
            Log.d("test", "addTwoNumber: teleRight $teleBitRight")
            result.reverse()
            if (teleBitRight > 0) result.insert(result.length - teleBitRight, ".")
            return String(result)
        }

        fun subtractTwoNumber(number1: String, number2: String): String {
            if (number1 == number2) return "0"

            val cal = calculateBit(number1, number2)
            var posDot1 = cal["posDot1"]!!
            var posDot2 = cal["posDot2"]!!
            var delta1 = cal["delta1"]!!
            var delta2 = cal["delta2"]!!
            val teleBitRight = cal["teleRight"]!!



            var s1 = if (posDot1 > 0) number1.removeRange(posDot1..posDot1) else number1
            var s2 = if (posDot2 > 0) number2.removeRange(posDot2..posDot2) else number2
            if (delta1 > 0) {
                repeat(delta1) {
                    s1 += "0"
                }
            }
            if (delta2 > 0) {
                repeat(delta2) {
                    s2 += "0"
                }
            }


            //subtract integer
            var sign = false
            var remember = false

            if (s1.length < s2.length) {
                sign = true
                s1 = s2.also { s2 = s1 }
            } else if (s1.length == s2.length) {
                var i = 0
                while (i < s1.length) {
                    if (s1[i] > s2[i]) {
                        Log.d("result", "subtractTwoNumber: ${s1[i]},${s2[i]},$i")
                        sign = false
                        break
                    } else if (s1[i] == s2[i]) {
                        i++
                    } else {
                        sign = true
                        s1 = s2.also { s2 = s1 }
                        break
                    }
                }
            }

            Log.d("result", "subtractTwoNumber: s1 = $s1, s2 = $s2, sign = $sign")
            var length1 = s1.length
            var length2 = s2.length
            var result = StringBuilder()

            //for s2
            for (i in 0 until length2) {
                Log.d("temp", "subtractTwoNumber: ${s1[length1 - i - 1]}, ${s2[length2 - i - 1]}")
                var temp = s1[length1 - i - 1].digitToInt() - s2[length2 - i - 1].digitToInt()
                if (remember)
                    temp--
                if (temp < 0) {
                    temp += 10
                    remember = true
                } else {
                    remember = false
                }
                result.append(temp)
            }

            //
            for (i in 0 until length1 - length2) {
                var temp = s1[length1 - length2 - i - 1].digitToInt()
                if (remember)
                    temp--
                if (temp < 0) {
                    temp += 10
                    remember = true
                } else {
                    remember = false
                }
                result.append(temp)
            }

            result = result.reverse()
            if (teleBitRight > 0) result.insert(result.length - teleBitRight, ".")
            var index = 0
            while (index < result.length && result[index] == '0') {
                index++
            }
            if (index == result.length) return "0"

            var lastResult = result.substring(index)
            if (lastResult.startsWith(".")) {
                lastResult = "0$lastResult"
            }
            if (sign) {
                lastResult = "-$lastResult"
            }
            return lastResult
        }

        fun multiTwoNumber(number1: String, number2: String): String {
            if (number1 == "0" || number2 == "0") return "0"
            //
            val cal = calculateBit(number1, number2)
            val dot1 = cal["posDot1"]!!
            val dot2 = cal["posDot2"]!!
            val teleBitRight = cal["dotToEnd1"]!! + cal["dotToEnd2"]!!
            Log.d("test", "multiTwoNumber: dottoEnd1 ${cal["dotToEnd1"]}, dottoEnd2 ${cal["dotToEnd2"]} dot1 $dot1, dot2 $dot2 telebit $teleBitRight")
            //// remove
            var s1 = if (dot1 > 0) number1.removeRange(dot1..dot1) else number1
            var s2 = if (dot2 > 0) number2.removeRange(dot2..dot2) else number2


            //multi integer
            if (s1.length < s2.length) {
                s1 = s2.also { s2 = s1 }
            }
            Log.d("result", "multiTwoNumber: $s1 $s2")
            var length1 = s1.length
            var length2 = s2.length

            var listS1 = ArrayList<Int>()
            var listS2 = ArrayList<Int>()
            for (i in s1.indices) {
                listS1.add(s1[i].digitToInt())
            }
            for (i in s2.indices) {
                listS2.add(s2[i].digitToInt())
            }
            var temp = IntArray(length1 + length2)

            for (i in listS1.size - 1 downTo 0) {
                for (j in listS2.size - 1 downTo 0) {
                    val index = length1 - 1 - i + length2 - 1 - j
                    temp[index] += listS1[i] * listS2[j]
                }
            }
            for (i in 0 until temp.size - 1) {
                temp[i + 1] += temp[i] / 10
                temp[i] = temp[i] % 10
            }

            var index = temp.size - 1

            while (temp[index] == 0) {
                index--
            }
            val result = StringBuilder()
            while (index >= 0) {
                result.append(temp[index--])
            }
            if (teleBitRight > 0) result.insert(result.length - teleBitRight , ".")
            return result.toString()
        }

        fun divideTwoNumber(number1: String, number2: String): String {
            var s1 = number1
            var s2 = number2

            if (s1 == s2) return "1"

            var index = 0
            while (index < s2.length && (s2[index] == '0' || s2[index] == '.')) {
                index++
            }

            if (index == s2.length) {
                throw Exception("Divide by zero")
            }

            val cal = calculateBit(number1, number2)
            val dot1 = cal["posDot1"]!!
            val dot2 = cal["posDot2"]!!
            val delta1 = cal["delta1"]!!
            val delta2 = cal["delta2"]!!
            //
            s1 = if (dot1 > 0) number1.removeRange(dot1..dot1) else number1
            s2 = if (dot2 > 0) number2.removeRange(dot2..dot2) else number2

            if (delta1 > 0) {
                repeat(delta1) {
                    s1 += "0"
                }
            }
            if (delta2 > 0) {
                repeat(delta2) {
                    s2 += "0"
                }
            }
            repeat(3) {
                s1 += "0"
            }


            //divide integer
            var difference = s1.length - s2.length - 1

            if (difference > 0) {
                repeat(difference) {
                    s2 = s2.plus("0")
                }
            } else if (difference < 0) {
                repeat(-difference) {
                    s1 = s1.plus("0")
                }
            }

            Log.d("result", "divideTwoNumber: $s1, $s2")

            var sub = s1
            var result = "0"
            for (i in 0..difference) {
                if (i > 0) {
                    s2 = s2.dropLast(1)
                }
                var canMul = true
                while (canMul) {
                    Log.d("result", "divideTwoNumber: $sub, $s2")
                    sub = subtractTwoNumber(sub, s2)
                    if (sub.startsWith('-')) {
                        var absSub = sub.substring(1)
                        sub = subtractTwoNumber(s2, absSub)
                        if (i < difference) {
                            result = multiTwoNumber(result, "10")
                        }
                        canMul = false
                    } else {
                        result = addTwoNumber(result, "1")
                        Log.d("result", "result: $result")
                    }
                }
            }
            // Format result
            if (result.length > 3) result =
                result.substring(0, result.length - 3) + "." + result.substring(result.length - 3)
            else {
                val size = result.length
                repeat(3 - size) {
                    result = "0$result"
                }
                result = "0.$result"
            }

            return result
        }
    }
}