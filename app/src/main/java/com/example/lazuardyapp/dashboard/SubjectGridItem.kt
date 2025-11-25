package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun SubjectGridItem(
    painter: Painter, // Untuk ikon (Drawable/Vector)
    title: String,    // Judul mata pelajaran
    onClick: () -> Unit, // Aksi ketika item diklik
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .clickable(onClick = onClick) // Membuat seluruh item bisa diklik
            .padding(8.dp), // Padding di dalam item
        horizontalAlignment = Alignment.CenterHorizontally // Rata tengah horizontal
    ) {
        Icon(
            painter = painter,
            contentDescription = title,
            modifier = Modifier.size(48.dp), // Ukuran ikon
            tint = MaterialTheme.colorScheme.primary // Warna ikon (opsional)
        )
        Spacer(modifier = Modifier.height(4.dp)) // Jarak antara ikon dan teks
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall, // Gaya teks
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}