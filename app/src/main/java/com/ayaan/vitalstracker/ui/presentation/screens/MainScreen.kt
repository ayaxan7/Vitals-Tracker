package com.ayaan.vitalstracker.ui.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import com.ayaan.vitalstracker.data.entity.Vitals
import com.ayaan.vitalstracker.ui.presentation.screens.components.AddVitalsDialog
import com.ayaan.vitalstracker.ui.presentation.screens.components.VitalsCard
import com.ayaan.vitalstracker.ui.presentation.viewmodel.VitalsViewModel
import com.ayaan.vitalstracker.ui.theme.DarkPink
import com.ayaan.vitalstracker.ui.theme.topBarTextColor
import com.ayaan.vitalstracker.ui.theme.topbarColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vitals: List<Vitals>,
    isLoading: Boolean,
    onAddVitals: (Int, Int, Int, Float, Int) -> Unit,
    viewModel: VitalsViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track My Pregnancy"
                , fontWeight = FontWeight.Bold) },
                colors=TopAppBarDefaults.topAppBarColors(
                    containerColor = topbarColor,
                    titleContentColor = topBarTextColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = DarkPink,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Vitals")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (vitals.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No vitals recorded yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add your first entry",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vitals) { vital ->
                        VitalsCard(vital = vital, viewModel=viewModel)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddVitalsDialog(
            onDismiss = { showDialog = false },
            onSave = { systolic, diastolic, heartRate, weight, babyKicks ->
                onAddVitals(systolic, diastolic, heartRate, weight, babyKicks)
            }
        )
    }
}




