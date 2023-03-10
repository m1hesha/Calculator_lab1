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
                    if (operatorPriority.getValue(operatorStack.peek()) >= currentPriority)
                        rpn += " " + operatorStack.pop() + " "
                    else break
            } catch (e: Exception) {
                return "ERROR: UNEXPECTED"
            }

            operatorStack.push(symbol)
        }

        while (operatorStack.isNotEmpty()) rpn += " " + operatorStack.pop() + " "

        if (rpn.length > 1 && rpn.substring(rpn.length - 1, rpn.length) == " ")
            rpn = rpn.substring(0, rpn.length - 1)

        return rpn.replace("  ", " ")
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
                if (component != "") try {
                    if (component == ".") numberStack.push(0.toDouble())
                    else numberStack.push(component.toDouble())
                } catch (e: Exception) {
                    return "ERROR: UNEXPECTED"
                }
            }
        }

        if (numberStack.size == 0) numberStack.push(0.toDouble())

        var numberString = numberStack.pop().toBigDecimal().toPlainString()
        val numberStringLength = numberString.length

        if (numberString.substring(numberStringLength - 2, numberStringLength) == ".0")
            numberString = numberString.substring(0, numberStringLength - 2)

        return numberString
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