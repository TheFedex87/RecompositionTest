package it.thefedex87.recompositiontest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import it.thefedex87.recompositiontest.R
import it.thefedex87.recompositiontest.ui.theme.RecompositionTestTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecompositionTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val state = viewModel.state

                    /*val painter = rememberAsyncImagePainter(
                        model = R.drawable.test,
                        contentScale = ContentScale.FillBounds
                    )*/

                    val pagerState = rememberPagerState()
                    LaunchedEffect(key1 = true) {
                        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect {
                            viewModel.selectedValueChanged(state.values[it])
                        }
                    }

                    /*Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        alpha = 0.6f
                    )*/

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Counter(
                            onDecrement = { viewModel.decrement()},
                            onIncrement = { viewModel.increment() },
                            value = state.value.toString()
                        )

                        state.selectedValue?.let {
                            Text(
                                text = it.text,
                                fontSize = 50.sp
                            )
                        }

                        SegmentedButton(
                            options = listOf(
                                "Option 1",
                                "Option 2",
                                "Option 3"
                            ),
                            selectedOption = viewModel.state.selectedOption,
                            onOptionClicked = { i ->
                                viewModel.changeOption(i)
                            },
                            modifier = Modifier
                                .height(30.dp)
                        )
                        


                        HorizontalPager(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 24.dp),
                            contentPadding = PaddingValues(
                                horizontal = 32.dp
                            ),
                            count = viewModel.state.values.count(),
                            state = pagerState
                        ) { page ->
                            MyItem(
                                myObject = state.values[page]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Counter(
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clickable { }
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 150.sp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onDecrement) {
                Text(text = "Decrement")
            }
            Button(onClick = onIncrement) {
                Text(text = "Increment")
            }
        }
    }
}

@Composable
fun MyItem(
    myObject: MyObject,
    modifier: Modifier = Modifier
) {
    Text(text = myObject.text)
}

data class MyObject(
    val text: String,
    val value: Int
)

@Composable
fun MyItem2(
    color: Color,
    onColorChanged: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                onColorChanged(Color.Red)
            }) {
                Text(text = "Red")
            }
            Button(onClick = {
                onColorChanged(Color.Green)
            }) {
                Text(text = "Green")
            }
            Button(onClick = {
                onColorChanged(Color.Blue)
            }) {
                Text(text = "Blue")
            }
        }
    }
}


@Composable
fun SegmentedButton(
    options: List<String>,
    selectedOption: Int = 0,
    onOptionClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        options.forEachIndexed { index, option ->
            OutlinedButton(
                onClick = {
                    onOptionClicked(index)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = if (selectedOption == index) Color.Green else Color.Red
                ),
                shape = when (index) {
                    0 -> {
                        RoundedCornerShape(
                            topStartPercent = 50,
                            bottomStartPercent = 50,
                            topEndPercent = 0,
                            bottomEndPercent = 0
                        )
                    }
                    options.lastIndex -> {
                        RoundedCornerShape(
                            topEndPercent = 50,
                            bottomEndPercent = 50,
                            topStartPercent = 0,
                            bottomStartPercent = 0
                        )
                    }
                    else -> {
                        RoundedCornerShape(
                            topEndPercent = 0,
                            bottomEndPercent = 0,
                            topStartPercent = 0,
                            bottomStartPercent = 0
                        )
                    }
                }
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedOption == index) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Selected option: $option",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = option,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}