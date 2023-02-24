package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


interface ValueProvider {
    fun getValue(variableName: String): Double
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_0.setOnClickListener { setTextFields("0") }
        button_1.setOnClickListener { setTextFields("1") }
        button_2.setOnClickListener { setTextFields("2") }
        button_3.setOnClickListener { setTextFields("3") }
        button_4.setOnClickListener { setTextFields("4") }
        button_5.setOnClickListener { setTextFields("5") }
        button_6.setOnClickListener { setTextFields("6") }
        button_7.setOnClickListener { setTextFields("7") }
        button_8.setOnClickListener { setTextFields("8") }
        button_9.setOnClickListener { setTextFields("9") }
        minus_button.setOnClickListener { setTextFields("-") }
        plus_button.setOnClickListener { setTextFields("+") }
        mult_button.setOnClickListener { setTextFields("×") }
        div_button.setOnClickListener { setTextFields("÷") }
        point_button.setOnClickListener { setTextFields(".") }
        left_bracket.setOnClickListener { setTextFields("(") }
        right_bracket.setOnClickListener { setTextFields(")") }

        clr_button.setOnClickListener {
            math_operation.text = ""
            result_text.text = ""
        }

        back_button.setOnClickListener {
            val str = math_operation.text.toString()
            if (str.isNotEmpty())
                math_operation.text = str.substring(0, str.length - 1)
            result_text.text = ""
        }

        back_button.setOnLongClickListener {
            math_operation.text = ""
            result_text.text = ""
            true
        }


        equal_button.setOnClickListener {
            if (math_operation.text.toString() != "") {
            val expression = replacedSymbols()
            val input = expression.toString()
            val result = ReversePolishNotation().calculate(input)
            val num = result.toDoubleOrNull()
            divZero()

                if (num != null) {
                    val intPart = num.toInt()
                    val decimalPart = num - intPart

                    if (decimalPart == 0.0) {
                        result_text.text = intPart.toString()
                    } else {
                        result_text.text = num.toString()
                    }
                }
            }
            else{

            }
        }

    }

    private fun replacedSymbols(): String {
        var newSymbol = math_operation.text.replace(Regex("÷"), "/")
        newSymbol = newSymbol.replace(Regex("×"), "*")
        newSymbol = newSymbol.replace(Regex(","), ".")
        return newSymbol
    }

    fun setTextFields (str: String){
        if(result_text.text !=""){
            math_operation.text = result_text.text
            result_text.text= ""
        }
        math_operation.append(str)
    }

    fun divZero(){
        val input = math_operation.text.toString()
        val check = Regex("÷0")
        if (check.containsMatchIn(input)) {
            result_text.text = "Делить на 0 нельзя!"
        }
    }

}








