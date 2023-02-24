package com.example.calculator

import java.util.*

class ReversePolishNotation {
    private val operatorPriority = mapOf(
        '(' to 0, ')' to 0, '+' to 1, '-' to 1, '*' to 2, '/' to 2
    )

    private val operatorList = listOf(
        "(", ")", "+", "-", "*", "/"
    )


    private fun transform(expression: String): String {
        var rpn = ""
        val operatorStack = Stack<Char>()

        for (symbol in expression) {
            if (symbol.isDigit() || symbol == '.') {
                rpn += symbol
                continue
            }

            if (!operatorPriority.containsKey(symbol)) return "ERROR: NO SUCH SYMBOL $symbol"

            if (symbol == '(') {
                operatorStack.push(symbol)
                continue
            }

            if (symbol == ')') {
                var success = false

                while (operatorStack.isNotEmpty()) {
                    val last = operatorStack.pop()

                    if (last == '(') {
                        success = true
                        break
                    } else rpn = "$rpn $last "
                }

                if (!success) return "ERROR: CANT FIND ("

                continue
            }

            rpn += " "

            val currentPriority = operatorPriority.getValue(symbol)

            try {
                while (operatorStack.isNotEmpty())
                    if (operatorPriority.getValue(operatorStack.peek()) > currentPriority)
                        rpn += " " + operatorStack.pop() + " "
                    else break
            } catch (e: Exception) {
                return "ERROR: UNEXPECTED"
            }

            operatorStack.push(symbol)
        }

        while (operatorStack.isNotEmpty()) rpn += " " + operatorStack.pop() + " "

        return rpn.substring(0, rpn.length - 1).replace("  ", " ")
    }

    fun calculate(expression: String): String {
        val rpn = transform(expression)
        val numberStack = Stack<Double>()

        if (rpn.contains("ERROR")) return rpn

        val components = rpn.split(" ").toTypedArray()

        for (component in components) {
            if (operatorList.contains(component)) {
                val second = if (numberStack.isNotEmpty()) numberStack.pop() else 0.toDouble()
                val first = if (numberStack.isNotEmpty()) numberStack.pop() else 0.toDouble()

                val result = execute(component, first, second)

                if (result.contains("ERROR")) return result
                numberStack.push(result.toDouble())

            } else {
                try {
                    numberStack.push(component.toDouble())
                } catch (e: Exception) {
                    return "ERROR: UNEXPECTED $component"
                }
            }
        }

        return numberStack.pop().toString()
    }

    private fun execute(operation: String, first: Double, second: Double): String {
        when (operation) {
            "+" -> return (first + second).toString()
            "-" -> return (first - second).toString()
            "*" -> return (first * second).toString()
            "/" -> return if (second == 0.toDouble()) "ERROR: DIVIDE BY ZERO"
            else (first / second).toString()
        }
        return "ERROR"
    }

}