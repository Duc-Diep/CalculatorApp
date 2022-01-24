package com.ducdiep.calculator.model

import java.util.*

class ConvertToPostFix {
    var baseOperators: MutableMap<String, Operator>

    init {
        val ADDITION = Operator("+", Associativity.LEFT, 0)
        val SUBTRACTION = Operator("-", Associativity.LEFT, 0)
        val DIVISION = Operator("÷", Associativity.LEFT, 1)
        val MULTIPLICATION = Operator("×", Associativity.LEFT, 1)

        val listOperator =
            mutableListOf(ADDITION, SUBTRACTION, DIVISION, MULTIPLICATION)

        baseOperators = mutableMapOf()
        listOperator.forEach {
            baseOperators[it.value] = it
        }
    }

    fun convert(tokens: List<String>): List<String> {

        val output: MutableList<String> = LinkedList()
        val stack: Stack<String> = Stack()

        //
        tokens.forEach { token ->
            if (baseOperators.containsKey(token)) { // is operator
                while (!stack.isEmpty() && baseOperators.containsKey(stack.peek())) {

                    var currentOP = baseOperators[token] // Current operator
                    var topOperator = baseOperators[stack.peek()] // Top operator from the stack
                    //check precedence
                    if ((currentOP!!.associativity == Associativity.LEFT && currentOP.comparePrecedence(
                            topOperator!!
                        ) <= 0) ||
                        (currentOP.associativity == Associativity.RIGHT && currentOP.comparePrecedence(
                            topOperator!!
                        ) < 0)
                    ) {
                        output.add(stack.pop()) // add operator to output
                        continue
                    }
                    break
                }
                stack.push(token);
            } else { //is number
                output.add(token)
            }
        }

        while (!stack.isEmpty()) {//put all operator to output
            output.add(stack.pop())
        }
        return output
    }
}
//            else if ("(" == token) {
//                stack.push(token)
//            } else if (")" == token) {
//                while (!stack.isEmpty() && !stack.peek().equals("(")) {
//                    output.add(stack.pop())
//                }
//                stack.pop()
//            }