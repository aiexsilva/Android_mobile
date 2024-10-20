package com.example.calculator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CalculatorScreen(modifier: Modifier = Modifier){

    var display by remember { mutableStateOf("0") }
    var operator by remember { mutableStateOf<String?>(null) }
    var firstNumber by remember { mutableStateOf<Double?>(null) }

    val buttons = listOf(
        listOf("AC","±","%","/"),
        listOf("7","8","9","X"),
        listOf("4","5","6","-"),
        listOf("1","2","3","+"),
        listOf("","0",".","=",)
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = display, modifier = Modifier.padding(64.dp).fillMaxWidth(), maxLines = 1, fontSize = 55.sp, overflow = Ellipsis)

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { label ->
                    val buttonColor = if (label == "=" || label == "AC" || label == "±"|| label == "%"
                        || label == "/" || label == "X"|| label == "-" ||
                        label == "+" ) Color(0xFF007FFF) else Color(0xFFFEF250)

                    fun isItFractional(value : Double) : String {
                        return if (value % 1.0 == 0.0){
                            value.toInt().toString()
                        }else{
                            value.toString()
                        }
                    }

                    fun performCalculationIfNeeded() {
                        val secondNumber = display.toDoubleOrNull()
                        if (firstNumber != null && secondNumber != null && operator != null) {
                            when (operator) {
                                "+" -> {
                                    firstNumber = (firstNumber!! + secondNumber)
                                }
                                "-" -> {
                                    firstNumber = (firstNumber!! - secondNumber)
                                }
                                "X" -> {
                                    firstNumber = (firstNumber!! * secondNumber)
                                }
                                "/" -> {
                                    firstNumber = (firstNumber!! / secondNumber)
                                }
                            }
                            display = isItFractional(firstNumber!!)
                        } else {
                            firstNumber = display.toDoubleOrNull()
                        }
                    }

                    Button(
                        onClick = {
                            val labelbutton = label.toDoubleOrNull()
                            if(labelbutton != null){
                                val number = label.toInt()
                                display = if (display == "0") {
                                    number.toString()
                                } else {
                                    display + number.toString()
                                }
                            }else{
                                when (label) {
                                    "." -> {
                                        if(!display.contains(".")){
                                            display += "."
                                        }
                                    }
                                    "±" -> {
                                        val currentNumber = display.toDoubleOrNull()
                                        display = if (currentNumber != null) {
                                            isItFractional(-currentNumber)
                                        } else {
                                            display
                                        }
                                    }
                                    "%" ->{
                                        val currentNumber = display.toDoubleOrNull()
                                        display = if( currentNumber != null) {
                                            isItFractional(currentNumber * 0.01)
                                        }else{
                                            display
                                        }
                                    }
                                    "+" -> {
                                        performCalculationIfNeeded()
                                        operator = "+"
                                        display = "0"
                                    }

                                    "-" -> {
                                        performCalculationIfNeeded()
                                        operator = "-"
                                        display = "0"
                                    }

                                    "/" -> {
                                        performCalculationIfNeeded()
                                        operator = "/"
                                        display = "0"
                                    }

                                    "X" -> {
                                        performCalculationIfNeeded()
                                        operator = "X"
                                        display = "0"
                                    }
                                    "AC" -> {
                                        display = "0"
                                        firstNumber = null
                                        operator = null
                                    }
                                    "=" -> {
                                        performCalculationIfNeeded()
                                        operator = null
                                    }
                                    else -> {
                                        display = "Unknown input"
                                    }
                                }
                            }
                        },
                            modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .height(64.dp),

                            colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor
                        )
                    ) {
                        Text(text = label, fontSize = 24.sp)
                    }

                }
            }
        }
    }
}

