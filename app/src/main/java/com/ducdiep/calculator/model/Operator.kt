package com.ducdiep.calculator.model

enum class Associativity {
    LEFT, RIGHT
}

class Operator(val value: String, val associativity: Associativity, val precedence: Int) {
    fun comparePrecedence(operator: Operator): Int {
        return (precedence - operator.precedence)
    }
}