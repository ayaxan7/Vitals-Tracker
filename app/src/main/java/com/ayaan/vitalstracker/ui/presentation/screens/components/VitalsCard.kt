package com.ayaan.vitalstracker.ui.presentation.screens.components
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ayaan.vitalstracker.R
import com.ayaan.vitalstracker.data.entity.Vitals
import com.ayaan.vitalstracker.ui.presentation.viewmodel.VitalsViewModel
import com.ayaan.vitalstracker.ui.theme.DarkPink
import com.ayaan.vitalstracker.ui.theme.LightPink
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VitalsCard(vital: Vitals, viewModel: VitalsViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LightPink),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VitalIcon(
                    imageResId = R.drawable.heart_rate,
                    value = "${vital.heartRate} bpm"
                )
                VitalIcon(
                    imageResId = R.drawable.scale,
                    value = "${vital.weight.toInt()} kg"
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VitalIcon(
                    imageResId = R.drawable.blood_pressure,
                    value = "${vital.systolicPressure}/${vital.diastolicPressure} mmHg"
                )
                VitalIcon(
                    imageResId = R.drawable.newborn,
                    value = "${vital.babyKicksCount} kicks"
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(36.dp)
                .background(DarkPink),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "${formatDayAndDate(vital.timestamp)} ${formatTime(vital.timestamp)}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
    }
}

@Composable
fun VitalIcon(@DrawableRes imageResId: Int, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatDayAndDate(date: Date): String {
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    return formatter.format(date)
}

private fun formatTime(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm a", Locale.getDefault())
    return formatter.format(date)
}
