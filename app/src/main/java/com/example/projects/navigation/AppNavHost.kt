package com.example.projects.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projects.ui.theme.screens.SplashScreen
import com.example.projects.ui.theme.screens.client.AddClientScreen
import com.example.projects.ui.theme.screens.client.UpdateClientScreen
import com.example.projects.ui.theme.screens.client.ViewClients
import com.example.projects.ui.theme.screens.dashboard.DashBoard
import com.example.projects.ui.theme.screens.login.SignupScreen
import com.example.projects.ui.theme.screens.signup.LoginScreen

//map url or fun in specific screen
@Composable
fun AppNavHost(navController: NavHostController= rememberNavController(),
               startDestination:String= ROUTE_SPLASH){
    NavHost(navController = navController,
        startDestination = startDestination){
        composable(ROUTE_SPLASH){ SplashScreen {
            navController.navigate(ROUTE_REGISTER){
                popUpTo(ROUTE_SPLASH) {inclusive=true }
            }
        }}
        composable(ROUTE_REGISTER){ SignupScreen(navController)}
        composable(ROUTE_LOGIN){ LoginScreen(navController )}
        composable(ROUTE_HOME){ DashBoard(navController ) }
        composable(ROUTE_ADD_CLIENT){ AddClientScreen(navController)}
        composable(ROUTE_VIEW_CLIENT){ ViewClients(navController)}
        composable("$ROUTE_UPDATE_CLIENT/{id}"){                                         //want to update specific record
            passedData -> UpdateClientScreen(
            navController,passedData.arguments?.getString( "id")!! )
            }                                                                                   //want to update specific record

    }


}