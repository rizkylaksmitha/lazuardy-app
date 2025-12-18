package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.data.model.ActivePackage
import com.example.lazuardyapp.ui.screens.PrimaryColor

@Composable
fun ProgressSummaryContainer(
    activePackages: List<ActivePackage>,
    onViewDetail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onViewDetail
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Progres Belajar",
                color = PrimaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            activePackages.forEachIndexed { index, packageItem ->
                ProgressCardContent(
                    packageName = packageItem.packageName,
                    subject = packageItem.subject,
                    tutor = packageItem.tutorName,
                    progress = packageItem.currentProgress,
                    totalSessions = packageItem.totalSessions,
                    scheduleDays = packageItem.scheduleDays,
                    hideHeader = true
                )

                if (index < activePackages.size - 1) {
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}