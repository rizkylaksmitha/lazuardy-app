package com.example.lazuardyapp.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lazuardyapp.R


@Composable
fun BimbelCard(){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
    {
        Column (modifier = Modifier.padding(16.dp))
        {
            Row (
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(68.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.text_logo),
                        contentDescription = "text_logo",
                        modifier = Modifier.size(122.dp, 22.dp)
                    )
                    Text(
                        text = "Belajar tanpa batas",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp, top = 5.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.lazuardy_promotion),
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 5.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}