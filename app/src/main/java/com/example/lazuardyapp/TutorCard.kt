package com.example.lazuardyapp.tutorselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person // Mengatasi 'Unresolved reference ic_person_outline'
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R
import com.example.lazuardyapp.ui.screens.PrimaryColor
import com.example.lazuardyapp.ui.theme.TextColor


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
                // Placeholder Image/Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0)) // Light gray background
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Tutor Name & Subject
                    Text(
                        text = tutor.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Icon Subjek
                        Icon(
                            imageVector = tutor.subjectIcon,
                            contentDescription = tutor.subject,
                            tint = tutor.subjectIconColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = tutor.subject,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextColor
                        )
                    }
                    Text(
                        text = tutor.level,
                        fontSize = 12.sp,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFC107), // Yellow star color
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${tutor.rating} (${tutor.reviewCount} ulasan)",
                            fontSize = 12.sp,
                            color = TextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = tutor.description,
                fontSize = 13.sp,
                color = TextColor.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats and Button (Dibagi menjadi dua baris untuk memastikan tombol terlihat jelas)
            Column {
                // Baris 1: Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Pelajar
                    Icon(
                        imageVector = Icons.Default.Person, // Menggantikan R.drawable.ic_person_outline
                        contentDescription = "Pelajar",
                        tint = TextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${tutor.studentCount} pelajar", fontSize = 12.sp, color = TextColor)

                    Spacer(modifier = Modifier.width(12.dp))

                    // Divider
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(TextColor)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${tutor.sessionCount} sesi", fontSize = 12.sp, color = TextColor)

                    Spacer(modifier = Modifier.width(12.dp))

                    // Divider
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(TextColor)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = tutor.availability, fontSize = 12.sp, color = TextColor)

                    Spacer(modifier = Modifier.width(12.dp))

                    // WhatsApp Icon (MEMERLUKAN FILE R.drawable.ic_whatsapp)
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF25D366), // WhatsApp Green
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp)) // Jarak sebelum tombol

                // Baris 2: Tombol Pilih Tutor
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // Pastikan tombol rata kanan
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onSelectTutor(tutor) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "Pilih Tutor",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}