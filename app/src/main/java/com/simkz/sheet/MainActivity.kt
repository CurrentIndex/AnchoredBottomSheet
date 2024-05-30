package com.simkz.sheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simkz.bottomsheet.AnchoredBottomSheetScaffold
import com.simkz.bottomsheet.AnchoredBottomSheetStateValue
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


object SheetValues {
    data object Maximum : AnchoredBottomSheetStateValue(1f)
    data object Medium : AnchoredBottomSheetStateValue(.5f)
    data object Minimum : AnchoredBottomSheetStateValue(0f)
//    data object Hide : AnchoredBottomSheetStateValue(.0f)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScaffoldExample() {
    val scope = rememberCoroutineScope()
    val anchors = listOf(SheetValues.Maximum, SheetValues.Medium, SheetValues.Minimum)
    val scaffoldState = rememberAnchoredBottomSheetScaffoldState(
        anchors = anchors,
        initialValue = anchors.first(),
        animationSpec = tween(easing = EaseOutBack)
    )
    AnchoredBottomSheetScaffold(
        topBar = {
            TopAppBar(title = { Text(text = "TITLE") })
        },
        sheetPeekHeight = 56.dp,
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
                            scaffoldState.anchoredSheetState.animateTo(SheetValues.Maximum)
                        }
                    }
                ) {
                    Text(text = "Maximum")
                }
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(SheetValues.Medium)
                        }
                    }
                ) {
                    Text(text = "Medium")
                }
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.anchoredSheetState.animateTo(SheetValues.Minimum)
                        }
                    }
                ) {
                    Text(text = "Minimum")
                }
            }
        },
        sheetDragHandle = null
    ) {

    }
}