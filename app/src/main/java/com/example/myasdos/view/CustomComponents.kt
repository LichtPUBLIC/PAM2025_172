package com.example.myasdos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myasdos.ui.theme.BlueGradientEnd
import com.example.myasdos.ui.theme.BlueGradientStart
import com.example.myasdos.ui.theme.RoyalBlue

@Composable
fun TopSectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp) // Header agak tinggi
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueGradientStart, BlueGradientEnd)
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 24.dp) // Spacer untuk status bar
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ModernCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp), // Sudut sangat membulat
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Shadow tebal
        colors = CardDefaults.cardColors(containerColor = Color.White), // Putih bersih
        content = content
    )
}
