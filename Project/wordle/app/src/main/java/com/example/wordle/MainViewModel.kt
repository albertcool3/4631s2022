package com.example.wordle


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.model.Letter
import com.example.wordle.model.LetterStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * Used to keep game states
 */

private var wordList = listOf(
    "HOARD",
)

public var username: String = "Wordle Player"
public var servername: String = "192.168.1.7"
public var wordOfTheDay: String = wordList.random()


//fun Evaluation.isComplete(): Boolean = rightPosition == CODE_LENGTH
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _usernameText = mutableStateOf(username)
    val usernameText: State<String> = _usernameText

    private val _servernameText = mutableStateOf(servername)
    val servernameText: State<String> = _servernameText

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()


    fun onUsernameChange(input: String) {
        _usernameText.value = input
        username = input
    }

    fun onServernameChange(input: String) {
        _servernameText.value = input
        servername = input
    }

    var WORD_LENGTH = 5
    var MAX_ALLOWED_GUESS = 6
    val ALPHABET = List(26){'A'+ it}
    var secret: String = generateSecret(false)
    var win = false

    private var rowIndex = 0
    private var columnIndex = 0

    var a2z = mutableListOf<Letter>().apply {
        for (i in ALPHABET) {
            add(Letter(i.toString(), LetterStatus.Unknown))
        }
    }.toList()

    var allLetters = mutableListOf<Letter>().apply {
        for (i in 1..WORD_LENGTH * MAX_ALLOWED_GUESS) {
            add(Letter(" ", LetterStatus.Unknown))
        }
    }.toList()

    private val _isComplete = MutableStateFlow(false)
    val isComplete: StateFlow<Boolean> = _isComplete


    fun resetGame(codeLength: Int) {
        if (codeLength > 8 || codeLength <3) {
            WORD_LENGTH = 4
        } else {
            WORD_LENGTH = codeLength
        }
        secret = generateSecret(false)
        rowIndex = 0
        columnIndex = 0

        var allLetters = mutableListOf<Letter>().apply {
            for (i in 1..WORD_LENGTH * MAX_ALLOWED_GUESS) {
                add(Letter(" ", LetterStatus.Unknown))
            }
        }.toList()

        win = false
        _isComplete.value = false
    }
//    suspend fun isGamecompleted(guess: String) {
//        val eval = evaluateGuess(secret,guess)
//        if (eval.count { it == LetterStatus.ExactPosition } == WORD_LENGTH) {win = true}
//        if (rowIndex > MAX_ALLOWED_GUESS) {win = false}
//        if (eval.count { it == LetterStatus.ExactPosition } == WORD_LENGTH || rowIndex > MAX_ALLOWED_GUESS)  {
//            _isComplete.value = true
//            _toastEvent.emit("Congrats")
//        }
//    }



    fun generateSecret(differentLetters: Boolean): String {
        return wordOfTheDay
    }

    fun onClickKey(letterName:String) {
        if (columnIndex < WORD_LENGTH && a2z[a2z.indexOfFirst { it.name == letterName }].status != LetterStatus.NotThere) {
            allLetters[rowIndex * WORD_LENGTH + columnIndex].name = letterName
            if (rowIndex > 0 && allLetters[(rowIndex - 1)*WORD_LENGTH + columnIndex].status == LetterStatus.ExactPosition &&
                allLetters[(rowIndex - 1)*WORD_LENGTH + columnIndex].name == allLetters[rowIndex * WORD_LENGTH + columnIndex].name  ) {
                allLetters[rowIndex * WORD_LENGTH + columnIndex].status = LetterStatus.ExactPosition
            }
            else {
                allLetters[rowIndex * WORD_LENGTH + columnIndex].status = LetterStatus.Unknown
            }
            columnIndex++
        }
    }

    fun onEnter() {
        if (columnIndex == WORD_LENGTH) {
            columnIndex = columnIndex.mod(WORD_LENGTH)
            val guess = buildString {
                for (i in 0..WORD_LENGTH - 1) {
                    append(allLetters[rowIndex * WORD_LENGTH + i].name)
                }
            }
            val eval = evaluateGuess(secret, guess, rowIndex)
            for (i in 0..WORD_LENGTH - 1) {
                allLetters[rowIndex * WORD_LENGTH + i].status = eval[i]
            }
            for (i in 0..WORD_LENGTH - 1) {
                if (eval[i] == LetterStatus.NotThere ||
                    eval[i] == LetterStatus.ExactPosition
                )
                    a2z[a2z.indexOfFirst { it.name == allLetters[rowIndex * WORD_LENGTH + i].name }].status =
                        eval[i]

                if (eval[i] == LetterStatus.WrongPosition &&
                    a2z[a2z.indexOfFirst { it.name == allLetters[rowIndex * WORD_LENGTH + i].name }].status != LetterStatus.ExactPosition
                )
                    a2z[a2z.indexOfFirst { it.name == allLetters[rowIndex * WORD_LENGTH + i].name }].status =
                        eval[i]
            }
            rowIndex++
        }
    }

    fun onDelete() {
        if (columnIndex > 0) {
            columnIndex--
            allLetters[rowIndex * WORD_LENGTH + columnIndex].status = LetterStatus.Unknown
            allLetters[rowIndex * WORD_LENGTH + columnIndex].name = " "
        }
    }

    fun evaluateGuess(secret: String, guess: String, rowIndex: Int): Array<LetterStatus> {
        var secretArr = secret.toCharArray()
        var guessArr = guess.toCharArray()
        var resultArr = Array<LetterStatus>(secret.length) { LetterStatus.NotThere }

        for (i in 0..secret.length - 1) {
            if (secretArr[i] == guessArr[i]) {
                resultArr[i] = LetterStatus.ExactPosition
                secretArr[i] = 0.toChar()
                guessArr[i] = 0.toChar()
            }
        }
        for (i in 0..secret.length - 1) {
            if (guessArr[i] != 0.toChar()) {
                val index = secretArr.indexOf(guessArr[i])
                if (index != -1) {
                    secretArr[index] = 0.toChar()
                    resultArr[i] = LetterStatus.WrongPosition
                }
            }
        }
        if (resultArr.all { it == LetterStatus.ExactPosition }) {
            viewModelScope.launch {
                _toastEvent.emit("Congrats")
            }
        } else {
            if (rowIndex + 1 >= MAX_ALLOWED_GUESS) {
                viewModelScope.launch {
                    _toastEvent.emit(wordOfTheDay)
                }
            }
        }
        return resultArr
    }
}


