package com.example.wordle.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.MainViewModel
import com.example.wordle.ui.theme.WordleTheme


@Composable
fun SettingsScreen(viewModel: MainViewModel = hiltViewModel(),
                   onNavigate: (String) -> Unit) {
    WordleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(horizontalArrangement = Arrangement.End) {
                    //Spacer(modifier = Modifier.size(64.dp))
                    IconButton(
                        onClick = { onNavigate("game_screen") }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Back to Game"
                        )
                    }
/*                    IconButton(
                        onClick = { viewModel.saveChanges()}) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Save Changes"
                        )
                    }*/
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("Player Nickname:")
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = viewModel.usernameText.value,
                        onValueChange = viewModel::onUsernameChange,
                        placeholder = {
                            Text(text = viewModel.usernameText.value)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("Server Host Name:")
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = viewModel.servernameText.value,
                        onValueChange = viewModel::onServernameChange,
                        placeholder = {
                            Text(text = viewModel.servernameText.value)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text("Practice Mode:")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = true, onClick = { /*TODO*/ })
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}