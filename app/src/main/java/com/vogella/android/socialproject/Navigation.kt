package com.vogella.android.socialproject

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.social.Routes
import com.vogella.android.socialproject.layouts.SignUpScreen
import com.vogella.android.socialproject.layouts.TacVu2
import com.vogella.android.socialproject.layouts.SignInScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SIGN_IN){
        composable(Routes.SIGN_IN) {
            SignInScreen(navController, Modifier)
        }
        composable(Routes.SIGN_UP) { SignUpScreen(navController) }
        composable(Routes.TAC_VU) { TacVu2(navController) }
    }
}