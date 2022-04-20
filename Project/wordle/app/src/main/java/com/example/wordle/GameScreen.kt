package com.example.wordle

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordle.model.Letter
import com.example.wordle.model.LetterStatus
import com.example.wordle.ui.theme.WordleTheme
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random



@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun letterBox(letter: Letter, w: Dp = 48.dp, h: Dp = 48.dp) {

    when (letter.status) {
        LetterStatus.ExactPosition-> {
            Surface (modifier = Modifier
                .size(width = w, height = h)/*.background(color = Color.LightGray)*/ ){
                Box{
                    Rectangle(color = Color.Green)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = letter.name
                    )
                }
            }
        }
        LetterStatus.NotThere-> {
            Surface (modifier = Modifier
                .size(width = w, height = h)/*.background(color = Color.LightGray)*/ ){
                Box{
                    Rectangle(color = Color.DarkGray)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = letter.name
                    )
                }
            }
        }
        LetterStatus.WrongPosition-> {
            Surface (modifier = Modifier
                .size(width = w, height = h)/*.background(color = Color.LightGray)*/ ){
                Box{
                    Rectangle(color = Color.Yellow)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = letter.name
                    )
                }
            }
        }
        else -> {
            Surface (modifier = Modifier
                .size(width = w, height = h)/*.background(color = Color.LightGray)*/ ){
                Box{
                    Rectangle(color = Color.LightGray)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = letter.name
                    )
                }
            }
        }
    }
}


@Composable
fun WordleMainScreen(viewModel: MainViewModel = hiltViewModel(),
                     onNavigate: (String) -> Unit) {

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    WordleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(/*verticalArrangement = Arrangement.Top,*/
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.End) {
                    //Spacer(modifier = Modifier.size(64.dp))
                    IconButton(
                        enabled = false,
                        onClick = { viewModel.resetGame(5) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Start A New Game"
                        )
                    }
                    IconButton(
                        onClick = { onNavigate("settings_screen") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }

                    IconButton(
                        onClick = { onNavigate("words_screen") }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Contribute Words to the next game"
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,//    .SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Spacer(modifier = Modifier.size(24.dp))
                    for (index in 0 until viewModel.MAX_ALLOWED_GUESS) {
                        line(viewModel, index)
                    }
                    Spacer(modifier = Modifier.size(24.dp))
                    keyboard(viewModel)
                }
            }
        }
    }
}
@Composable
private fun line(viewModel: MainViewModel, rowIndex:Int) {
    Row(modifier = Modifier.padding(2.dp)) {
        for (index in 0 until viewModel.WORD_LENGTH) {
            letterBox(letter = viewModel.allLetters[viewModel.WORD_LENGTH*rowIndex+index])
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}
@Composable
private fun keyboard(viewModel: MainViewModel) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row() {
            val firstRow = "QWERTYUIOP"
            for (char in firstRow) {
                keyButton(viewModel = viewModel, char = char)
            }
        }
        Row() {
            val secondRow = "ASDFGHJKL"
            for (char in secondRow) {
                keyButton(viewModel = viewModel, char = char)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            val thirdRow = "ZXCVBNM"
            IconButton(onClick = {viewModel.onEnter()}) {
                Icon(
                    imageVector = Icons.Default.Search, //Refresh, ThumbUp,
                    contentDescription = "Enter"
                )
            }
            for (char in thirdRow) {
                keyButton(viewModel = viewModel, char = char)
            }
            IconButton(onClick = {viewModel.onDelete()}) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun keyButton(viewModel: MainViewModel, char: Char,w: Dp = 40.dp, h: Dp = 60.dp) {
    when (viewModel.a2z[viewModel.a2z.indexOfFirst { it.name == char.toString() }].status) {
        LetterStatus.ExactPosition-> {
            Surface (modifier = Modifier
                .size(width = w, height = h),
                onClick = {
                    viewModel.onClickKey(char.toString())
                }){
                Box{
                    Circle(color = Color.Green)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = char.toString()
                    )
                }
            }
/*            OutlinedButton(
                modifier = Modifier
                    .size(40.dp,60.dp),
                onClick = {
                    viewModel.onClickKey(char.toString())
                }
            ) { Text(text=char.toString(),color = Color.Green) }*/
        }
        LetterStatus.WrongPosition-> {
            Surface (modifier = Modifier
                .size(width = w, height = h),
                onClick = {
                    viewModel.onClickKey(char.toString())
                }){
                Box{
                    Circle(color = Color.Yellow)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = char.toString()
                    )
                }
            }
        }
        LetterStatus.NotThere-> {
            Surface (modifier = Modifier
                .size(width = w, height = h),
                onClick = {
                    viewModel.onClickKey(char.toString())
                }){
                Box{
                    Circle(color = Color.DarkGray)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.White,
                        text = char.toString()
                    )
                }
            }
        }
        else -> {
            Surface (modifier = Modifier
                .size(width = w, height = h),
                onClick = {
                    viewModel.onClickKey(char.toString())
                }){
                Box{
                    Circle(color = Color.LightGray)
                    Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        color = Color.Black,
                        text = char.toString()
                    )
                }
            }
        }
    }
}