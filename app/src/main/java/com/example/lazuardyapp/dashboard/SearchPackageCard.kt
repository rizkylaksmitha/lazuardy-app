package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SubjectItem(
    val id: Int,
    val iconResId: Int,
    val title: String
)

@Composable
fun SearchPackageCard(onSubjectClick:(SubjectItem)-> Unit){

//    val subjects = listOf(
////        SubjectItem(1, R.drawable.ic_math, "Matematika"),
//    )

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Cari paket belajar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 4 kolom
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround, // Spasi di antara kolom
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spasi di antara baris
            ) {
//                items(subjects) { subject ->
//                    SubjectGridItem(
//                        painter = painterResource(id = subject.iconResId),
//                        title = subject.title,
//                        onClick = { onSubjectClick(subject) } // Memberikan aksi klik
//                    )
//                }
            }

        }
    }
}