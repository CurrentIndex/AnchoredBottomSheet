package com.simkz.sheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simkz.bottomsheet.AnchoredBottomSheetScaffold
import com.simkz.bottomsheet.AnchoredBottomSheetStateSize
import com.simkz.bottomsheet.rememberAnchoredBottomSheetScaffoldState
import com.simkz.sheet.ui.theme.AnchoredBottomSheetTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnchoredBottomSheetTheme {
                BottomSheetScaffoldExample()
            }
        }
    }
}


//object SheetValues {
//    data object Maximum : AnchoredBottomSheetStateValue(1f)
//    data object Medium : AnchoredBottomSheetStateValue(.5f)
//    data object Minimum : AnchoredBottomSheetStateValue(0f)
////    data object Hide : AnchoredBottomSheetStateValue(.0f)
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffoldExample() {
    val scope = rememberCoroutineScope()
    val anchors = remember {
        listOf(
            AnchoredBottomSheetStateSize.Weight(1f),
            AnchoredBottomSheetStateSize.Weight(0.5f),
            AnchoredBottomSheetStateSize.Weight(0f),
            AnchoredBottomSheetStateSize.Fixed(54.dp),
        )
    }
    val (anchorMaximum, anchorMedium, anchorMinimum, anchor54dp) = anchors
    val scaffoldState = rememberAnchoredBottomSheetScaffoldState(
        anchors = anchors,
        initialValue = anchorMaximum,
        animationSpec = tween(easing = EaseOutBack)
    )
    AnchoredBottomSheetScaffold(
        topBar = {
            TopAppBar(title = { Text(text = "TITLE") })
        },
        sheetPeekHeight = 0.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(anchorMaximum)
                        }
                    }
                ) {
                    Text(text = "Maximum")
                }
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(anchorMedium)
                        }
                    }
                ) {
                    Text(text = "Medium")
                }
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(anchorMinimum)
                        }
                    }
                ) {
                    Text(text = "Minimum")
                }
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(anchor54dp)
                        }
                    }
                ) {
                    Text(text = "54dp")
                }
            }
        },
        sheetDragHandle = null
    ) {

    }
}