package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }
    var firstOperand by remember { mutableStateOf(0.0) }
    var con by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.DarkGray)
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 72.sp, color = Color.White, textAlign = TextAlign.Right),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        CalculatorButtons(
            onNumberClick = { number ->
                if (con) {
                    input = TextFieldValue("")
                    con = false
                }
                input = TextFieldValue(input.text + number)},
            onOperatorClick = { op ->
                firstOperand = input.text.toDoubleOrNull() ?: 0.0
                operator = op
                input = TextFieldValue("")
            },
            onEqualsClick = {
                val secondOperand = input.text.toDoubleOrNull() ?: 0.0
                result = when (operator) {
                    "+" -> (firstOperand + secondOperand).toString()
                    "-" -> (firstOperand - secondOperand).toString()
                    "*" -> (firstOperand * secondOperand).toString()
                    "/" -> if (secondOperand != 0.0) (firstOperand / secondOperand).toString() else "Cannot divide by zero"
                    "sqrt" -> if (secondOperand >= 0) kotlin.math.sqrt(secondOperand).toString() else "Invalid input"
                    else -> ""
                }
                input = TextFieldValue(result)
                con = true
            },
            onClearClick = {
                input = TextFieldValue("")
                result = ""
                operator = ""
                firstOperand = 0.0
            }
        )
    }
}

@Composable
fun CalculatorButtons(
    onNumberClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onEqualsClick: () -> Unit,
    onClearClick: () -> Unit
) {
    val buttons = listOf(
        listOf("1", "2", "3", "+", "*"),
        listOf("4", "5", "6", "-", "/"),
        listOf("7", "8", "9", "sqrt"),
        listOf("0", ".", "=")
    )

    Column {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { button ->
                    Button(
                        shape = RectangleShape,
                        onClick = {
                            when (button) {
                                in "0".."9", "." -> onNumberClick(button)
                                in listOf("+", "-", "*", "/", "sqrt") -> onOperatorClick(button)
                                "=" -> onEqualsClick()
                                else -> onClearClick()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (button == "1" || button == "0") Color.Cyan else Color.LightGray
                        ),
                        modifier = Modifier
                            .weight(if (button == "sqrt" || button == "=" || button == "0") 2f else 1f)
                            .border(
                                width = 1.dp,
                                color = if (button == "1") Color.Blue else Color.Black
                            )
                            .padding(0.dp)  // Removes gaps between buttons
                            .size(height = 100.dp, width = 100.dp)
                    ) {
                        Text(text = button, fontSize = 48.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
