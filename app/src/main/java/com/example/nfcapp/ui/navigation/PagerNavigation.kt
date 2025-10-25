package com.example.nfcapp.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcapp.R
import com.example.nfcapp.ui.ReadCopyScreen
import com.example.nfcapp.ui.WriteProtectScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerNavigation(
    inputText: String,
    remainingBlocks: Int,
    writtenStrLength: Int,
    onInputTextChanged: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F2))
                .padding(22.dp, 8.dp, 16.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "LoopO NFC",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            // Logo
            Image(
                painter = painterResource(id = R.drawable.loopo_logo),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .size(120.dp, 60.dp)
                    .padding(end = 12.dp)
            )
        }
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(R.color.light_grey),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = colorResource(R.color.medium_grey)
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                text = {
                    Text(
                        text = "Reader",
                        fontSize = 18.sp,
                        color = colorResource(R.color.medium_grey)
                    )
                }
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                text = {
                    Text(
                        text = "Writer",
                        fontSize = 18.sp,
                        color = colorResource(R.color.medium_grey)
                    )
                }
            )
        }
        HorizontalPager(state = pagerState) {
            when (it) {
                0 -> ReadCopyScreen(
                    onNavigateToWrite = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }
                )

                1 -> WriteProtectScreen(
                    inputText = inputText,
                    remainingBlocks = remainingBlocks,
                    writtenStrLength = writtenStrLength,
                    onInputTextChanged = onInputTextChanged,
                    onNavigateBack = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }
                )
            }
        }
    }
}

@Preview
@Composable
fun PagerNavigationPreview() {
    PagerNavigation(
        inputText = "",
        remainingBlocks = 0,
        writtenStrLength = 0,
        onInputTextChanged = {}
    )
}


