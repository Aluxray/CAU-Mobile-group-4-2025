package com.example.yarni.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yarni.R
import com.example.yarni.domain.model.Pattern
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// --- Data Models ---
data class ChartSegment(val weight: Float, val color: Color)
data class Tip(val title: String, val description: String)

// --- Color Palette ---
val YarniPink = Color(0xFFE94057)
val YarniBackground = Color(0xFFF9F9F9)
val TipCardBackground = Color(0xFFF0E6FF)
val ChartColors = listOf(
    Color(0xFFC9BFFF),
    Color(0xFFFDC6E0),
    Color(0xFFFDE4A1),
    Color(0xFFB5E4CF)
)

// --- Main Screen ---
@Composable
fun HomeScreen(
    chartSegments: List<ChartSegment>,
    tips: List<Tip>,
    totalSize: String, // ViewModel로부터 받을 총 저장 공간
    recentPatterns: List<Pattern>, // ViewModel로부터 받을 최근 패턴 목록
    onNavigate: (Int) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                onFabClick = { /*TODO: FAB 클릭 이벤트 처리*/ },
                onNavigate = onNavigate
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(YarniBackground)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            TopAppBarSection()
            Spacer(Modifier.height(32.dp))
            StorageChartSection(segments = chartSegments, totalSize = totalSize, onNavigate = onNavigate)
            Spacer(Modifier.height(32.dp))
            RecentPatternsSection(patterns = recentPatterns)
            Spacer(Modifier.height(32.dp))
            TipsForCrochetSection(tips = tips)
        }
    }

}

// --- UI Sections ---
@Composable
fun TopAppBarSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Yarni",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        IconButton(onClick = { /* TODO: menu */ }) {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = "Menu"
            )
        }
    }
}

@Composable
fun StorageChartSection(segments: List<ChartSegment>, totalSize: String, onNavigate: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(R.id.action_nav_home_to_nav_patterns) }, // 전체 클릭 가능 영역
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                if (segments.isEmpty()) return@Canvas
                val strokeWidth = 35f
                val gapAngle = 4f
                val totalAngle = 360f - (segments.size * gapAngle)
                var startAngle = -90f - gapAngle / 2

                segments.forEach { segment ->
                    val sweep = totalAngle * segment.weight
                    drawArc(
                        color = segment.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    startAngle += sweep + gapAngle
                }
            }
            Text(text = totalSize, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Storage details >",
            style = MaterialTheme.typography.bodySmall.copy(
                textDecoration = TextDecoration.Underline,
                color = Color.Gray
            )
        )
    }
}

@Composable
fun RecentPatternsSection(patterns: List<Pattern>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Recently Added",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(16.dp))
        Divider(color = Color.LightGray)

        if (patterns.isEmpty()) {
            Text(
                text = "최근 추가된 파일이 없습니다.",
                color = Color.Gray,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Column {
                patterns.forEach { pattern ->
                    PatternRow(pattern)
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun PatternRow(pattern: Pattern) {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val formattedDate = dateFormat.format(pattern.date.toDate())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pattern.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1
            )
            Text(
                text = "Last updated: $formattedDate",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Icon(
            imageVector = Icons.Default.Description, // 파일 모양 아이콘으로 변경
            contentDescription = "File icon",
            tint = YarniPink,
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TipsForCrochetSection(tips: List<Tip>) {
    if (tips.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { tips.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tips for crochet",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(16.dp))
        HorizontalPager(state = pagerState) { page ->
            val tip = tips[page]
            TipCard(
                title = tip.title,
                koreanText = tip.description,
                pageNumber = "${page + 1}/${tips.size}",
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        PagerIndicator(pagerState = pagerState)
    }
}

// --- Reusable Components ---
@Composable
fun TipCard(title: String, koreanText: String, pageNumber: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TipCardBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Lightbulb, // 전구 아이콘으로 변경
                    contentDescription = "Tip icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = title, fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = koreanText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = pageNumber,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                text = "move to tip video >",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(8.dp)
            )
        }
    }
}

// --- Custom Bottom Navigation + Center FAB ---
@Composable
fun BottomNavBar(
    onFabClick: () -> Unit,
    onNavigate: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter),
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBarItem(icon = Icons.Default.Home, description = "Home", isSelected = true, onClick = {})
                NavBarItem(icon = Icons.Default.Folder, description = "Patterns", onClick = { onNavigate(R.id.action_nav_home_to_nav_patterns) })
                Spacer(Modifier.width(56.dp))
                NavBarItem(icon = Icons.Default.Notifications, description = "Alarm", onClick = {})
                NavBarItem(icon = Icons.Default.Settings, description = "Settings", onClick = {})
            }
        }
        FloatingActionButton(
            onClick = onFabClick,
            containerColor = YarniPink,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(64.dp)
                .offset(y = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
private fun RowScope.NavBarItem(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = if (isSelected) YarniPink else Color.Gray
        )
    }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    val chartData = listOf(
        ChartSegment(0.35f, ChartColors[0]),
        ChartSegment(0.25f, ChartColors[1]),
        ChartSegment(0.20f, ChartColors[2]),
        ChartSegment(0.20f, ChartColors[3])
    )
    val crochetTips = listOf(
        Tip("Tip1", "코잡기"),
        Tip("Tip2", "짧은뜨기"),
        Tip("Tip3", "한길긴뜨기")
    )
    val recentPatterns = listOf(
        Pattern(id = "1", name = "My First Scarf", date = Timestamp.now(), size = 12345),
        Pattern(id = "2", name = "Amigurumi Bear", date = Timestamp.now(), size = 6789)
    )

    MaterialTheme {
        HomeScreen(
            chartSegments = chartData,
            tips = crochetTips,
            totalSize = "1.2 GB",
            recentPatterns = recentPatterns,
            onNavigate = { }
        )
    }
}
