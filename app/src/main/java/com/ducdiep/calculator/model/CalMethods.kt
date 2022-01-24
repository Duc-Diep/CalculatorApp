package com.ducdiep.calculator.model

import android.util.Log

class CalMethods {
    companion object{
        //    number1.legth>number2.legth
        fun addTwoNumber(number1: String, number2: String): String {
            var s1 = number1
            var s2 = number2
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
            return String(result.reverse())
        }

        fun subtractTwoNumber(number1: String, number2: String): String {
            if (number1 == number2) return "0"

            var s1 = number1
            var s2 = number2
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
            var result = ArrayList<Char>()
            for (i in 0..length1 + 1) {
                result.add('0')
            }

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
                result[length1 - i] = (temp + 48).toChar()
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
                result[length1 - length2 - i] = (temp + 48).toChar()
            }

//      case 1000-999 = 0001
            if (result[1] != '0' && sign) {
                result[0] = '-';
                result.removeLast()
            } else if (result[1] != '0' && !sign) {
                result.removeAt(0)
                result.removeLast()
            } else {
                result[0] = '0'

                while (result[0] == '0') {
                    if (result[1] != '0' && sign) {
                        result[0] = '-';
                        break;
                    }
                    result.removeAt(0)
                }
                result.removeLast()

            }
            return String(result.toCharArray())
        }

        fun multiTwoNumber(number1: String, number2: String): String {
            var s1 = number1
            var s2 = number2

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
            return result.toString()
        }

        fun divideTwoNumber(number1: String, number2: String): String {
            var s1 = number1
            var s2 = number2

            if (s1 == s2) return "1"

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

            return result
        }
    }
}