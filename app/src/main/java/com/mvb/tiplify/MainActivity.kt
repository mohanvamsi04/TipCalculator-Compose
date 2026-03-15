package com.mvb.tiplify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvb.tiplify.ui.theme.TiplifyTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TiplifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    TipCalculator(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun TipCalculator(modifier: Modifier = Modifier) {

    var billInput by remember { mutableStateOf("") }
    var peopleInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(15f) }

    val billAmount = billInput.toIntOrNull() ?: 0
    val peopleCount = peopleInput.toIntOrNull()?.coerceIn(1, 100) ?: 1

    val tipAmount = billAmount * tipPercent.toInt() / 100
    val totalAmount = billAmount + tipAmount

    val perPerson =
        if (peopleCount > 0) totalAmount.toDouble() / peopleCount
        else totalAmount.toDouble()

    val animatedTip by animateIntAsState(
        targetValue = tipAmount,
        animationSpec = tween(400),
        label = "TipAnimation"
    )

    val animatedTotal by animateIntAsState(
        targetValue = totalAmount,
        animationSpec = tween(400),
        label = "TotalAnimation"
    )

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4CAF50),
                        Color(0xFF81C784)
                    )
                )
            )
    ) {

        BackgroundIcons()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {

                Text(
                    text = "Tiplify – Tip Calculator",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF1B5E20),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                )

                BillInputRow(billInput) {
                    if (it.all { c -> c.isDigit() }) billInput = it
                }

                TipSliderRow(tipPercent) { tipPercent = it }

                PeopleRow(peopleInput) {
                    if (it.all { c -> c.isDigit() }) {
                        val value = it.toIntOrNull() ?: 1
                        if (value <= 100) peopleInput = it
                    }
                }

                InfoRow("Tip", currencyFormatter.format(animatedTip))
                InfoRow("Total", currencyFormatter.format(animatedTotal))
                InfoRow("Per Person", currencyFormatter.format(perPerson))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "MADE WITH",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Heart",
                    tint = Color.Red,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                Text(
                    "BY MOHAN",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun BackgroundIcons() {

    Box(Modifier.fillMaxSize()) {

        Icon(
            Icons.Default.AttachMoney,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopStart)
                .padding(40.dp)
        )

        Icon(
            Icons.Default.Percent,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.TopEnd)
                .padding(40.dp)
        )

        Icon(
            Icons.Default.Groups,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.BottomStart)
                .padding(40.dp)
        )

        Icon(
            Icons.Default.Receipt,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.BottomEnd)
                .padding(40.dp)
        )
    }
}

@Composable
fun BillInputRow(value: String, onChange: (String) -> Unit) {

    Row(
        Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("Base", Modifier.weight(1f), textAlign = TextAlign.Center, color = Color(0xFF1B5E20))
        Text(":", Modifier.weight(0.2f), color = Color(0xFF1B5E20))

        TextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text("Bill Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Black),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F8E9),
                unfocusedContainerColor = Color(0xFFF1F8E9),
                cursorColor = Color(0xFF2E7D32)
            ),
            modifier = Modifier.weight(2f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipSliderRow(percent: Float, onChange: (Float) -> Unit) {

    val sliderColors = SliderDefaults.colors(
        thumbColor = Color(0xFF2E7D32),
        activeTrackColor = Color(0xFF4CAF50),
        inactiveTrackColor = Color(0xFFB2DFDB)
    )

    Row(
        Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("${percent.toInt()}%", Modifier.weight(1f),
            textAlign = TextAlign.Center, color = Color(0xFF1B5E20))

        Text(":", Modifier.weight(0.2f), color = Color(0xFF1B5E20))

        Slider(
            value = percent,
            onValueChange = onChange,
            valueRange = 0f..30f,
            modifier = Modifier.weight(2f),
            colors = sliderColors
        )
    }
}

@Composable
fun PeopleRow(value: String, onChange: (String) -> Unit) {

    Row(
        Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text("People", Modifier.weight(1f),
            textAlign = TextAlign.Center, color = Color(0xFF1B5E20))

        Text(":", Modifier.weight(0.2f), color = Color(0xFF1B5E20))

        TextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text("No. of people") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Black),

            supportingText = {
                Text("Max 100 people allowed", color = Color.Gray)
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F8E9),
                unfocusedContainerColor = Color(0xFFF1F8E9),
                cursorColor = Color(0xFF2E7D32)
            ),

            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {

    Row(
        Modifier.fillMaxWidth().padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(label, Modifier.weight(1f),
            textAlign = TextAlign.Center, color = Color(0xFF1B5E20))

        Text(":", Modifier.weight(0.2f), color = Color(0xFF1B5E20))

        Text(
            value,
            Modifier.weight(2f),
            color = Color(0xFF1B5E20),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTipCalculator() {
    TiplifyTheme {
        TipCalculator()
    }
}