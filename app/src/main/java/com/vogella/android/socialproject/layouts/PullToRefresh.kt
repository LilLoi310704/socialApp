package com.vogella.android.socialproject.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.play.core.integrity.p

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> PullToRefresh(
    items: List<T>,
    content:@Composable (T) ->Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier:Modifier=Modifier,
    lazyListState: LazyListState= rememberLazyListState(),
    navController: NavController
){
    val pullToRefresh= rememberPullToRefreshState()
    Box(
        modifier=Modifier.nestedScroll(pullToRefresh.nestedScrollConnection)
    ){
        ProfileScreen(navController )
        if(pullToRefresh.isRefreshing){
            LaunchedEffect (true){
                onRefresh()
            }
        }
        LaunchedEffect (isRefreshing){
            if(isRefreshing){
                pullToRefresh.startRefresh()
            }
            else{
                pullToRefresh.endRefresh()
            }
        }
        PullToRefreshContainer(
            state = pullToRefresh,
            modifier=Modifier.align(Alignment.TopCenter),

        )
    }
}