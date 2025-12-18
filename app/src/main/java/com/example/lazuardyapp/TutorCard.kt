package com.example.lazuardyapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.tutorselection.Tutor


val PrimaryColor = Color(0xFF1E88E5)
val ButtonColor = Color(0xFF3892A4)
val TextColor = Color(0xFF333333)
val SecondaryTextColor = Color(0xFF6B7280)

@Composable
fun TutorCard(tutor: Tutor, onSelectTutor: (Tutor) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tutor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = tutor.subjectIcon,
                            contentDescription = tutor.subject,
                            tint = tutor.subjectIconColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = tutor.subject, fontSize = 14.sp, color = TextColor)

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "•", fontSize = 14.sp, color = Color(0xFFBDBDBD))
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Level",
                            tint = TextColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = tutor.level, fontSize = 14.sp, color = TextColor)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", tutor.rating) + " (${tutor.reviewCount} ulasan)",
                            fontSize = 14.sp,
                            color = TextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tutor.description,
                fontSize = 13.sp,
                color = TextColor.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Pelajar",
                    tint = SecondaryTextColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${tutor.studentCount} pelajar", fontSize = 12.sp, color = SecondaryTextColor)

                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "•", fontSize = 12.sp, color = Color(0xFFBDBDBD))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "${tutor.sessionCount} sesi", fontSize = 12.sp, color = SecondaryTextColor)

                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "•", fontSize = 12.sp, color = Color(0xFFBDBDBD))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = tutor.availability, fontSize = 12.sp, color = SecondaryTextColor)

                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "•", fontSize = 12.sp, color = Color(0xFFBDBDBD))
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp",
                    tint = Color(0xFF25D366),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {  }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFFF0F0F0))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onSelectTutor(tutor) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    modifier = Modifier
                        .height(36.dp)
                        .widthIn(min = 120.dp)
                ) {
                    Text(text = "Pilih Tutor", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}