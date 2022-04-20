package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wordle.model.Letter
import com.example.wordle.model.LetterStatus
import com.example.wordle.ui.theme.WordleTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.exchangeword.ExchangeWordScreen
import com.example.wordle.settings.SettingsScreen
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                // A surface container using the 'background' color from the theme
                //Surface(
                //    modifier = Modifier.fillMaxSize(),
                //    color = MaterialTheme.colors.background
                //) {
                    val navController = rememberNavController()
                    //val viewModel : MainViewModel = MainViewModel()
                    NavHost(
                        navController = navController,
                        startDestination = "game_screen"
                    ) {
                        composable("game_screen") {
                            WordleMainScreen(/*viewModel,*/ onNavigate = navController::navigate)
                        }
                        composable(
                            route = "settings_screen"
                        ) {
                            SettingsScreen(/*viewModel*/onNavigate = navController::navigate)
                        }
                        composable(
                            route = "words_screen"
                        ) {
                            ExchangeWordScreen(/*viewModel*/onNavigate = navController::navigate)
                        }
                    }
                //}
            }
        }
    }
}
