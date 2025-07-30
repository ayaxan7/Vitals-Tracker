package com.ayaan.vitalstracker.ui.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ayaan.vitalstracker.ui.theme.DarkPink
import com.ayaan.vitalstracker.ui.theme.TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVitalsDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Int, Int, Float, Int) -> Unit
) {
    var systolicPressure by remember { mutableStateOf("") }
    var diastolicPressure by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var babyKicks by remember { mutableStateOf("") }

    var systolicError by remember { mutableStateOf(false) }
    var diastolicError by remember { mutableStateOf(false) }
    var heartRateError by remember { mutableStateOf(false) }
    var weightError by remember { mutableStateOf(false) }
    var babyKicksError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Add Vitals",
                    color= TextColor,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = systolicPressure,
                        onValueChange = {
                            systolicPressure = it
                            systolicError = false
                        },
                        label = { Text("Sys BP") },
                        placeholder = { Text("120") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = systolicError,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = diastolicPressure,
                        onValueChange = {
                            diastolicPressure = it
                            diastolicError = false
                        },
                        label = { Text("Dia BP") },
                        placeholder = { Text("80") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = diastolicError,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = heartRate,
                    onValueChange = {
                        heartRate = it
                        heartRateError = false
                    },
                    label = { Text("Heart Rate (bpm)") },
                    placeholder = { Text("72") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = heartRateError,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                        weightError = false
                    },
                    label = { Text("Weight (in kg)") },
                    placeholder = { Text("65.5") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = weightError,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = babyKicks,
                    onValueChange = {
                        babyKicks = it
                        babyKicksError = false
                    },
                    label = { Text("Baby Kicks") },
                    placeholder = { Text("10") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = babyKicksError,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
//                    TextButton(onClick = onDismiss) {
//                        Text("Cancel")
//                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            // Validate inputs
                            val systolic = systolicPressure.toIntOrNull()
                            val diastolic = diastolicPressure.toIntOrNull()
                            val hr = heartRate.toIntOrNull()
                            val wt = weight.toFloatOrNull()
                            val kicks = babyKicks.toIntOrNull()

                            systolicError = systolic == null || systolic <= 0
                            diastolicError = diastolic == null || diastolic <= 0
                            heartRateError = hr == null || hr <= 0
                            weightError = wt == null || wt <= 0
                            babyKicksError = kicks == null || kicks < 0

                            if (!systolicError && !diastolicError && !heartRateError && !weightError && !babyKicksError) {
                                onSave(systolic!!, diastolic!!, hr!!, wt!!, kicks!!)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkPink,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
