This is a flexible BottomSheet that is consistent with the API of `BottomSheetScaffold`, addressing a few cases where multiple anchors are required, such as `Google Map` and its BottomSheet. I should also implement `AnchoredModalBottomSheet`, but currently I do not have this requirement.

Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.CurrentIndex:AnchoredBottomSheet:0.0.2'
}
```


https://github.com/CurrentIndex/AnchoredBottomSheet/assets/59065234/d6c8630b-4e1f-4cbc-9528-aa44b59aa58a


1. Define some object classes that inherit from AnchoredBottomSheetStateValue.
```kotlin
object SheetValues {
    data object Maximum : AnchoredBottomSheetStateValue(1f)
    data object Medium : AnchoredBottomSheetStateValue(.5f)
    data object Minimum : AnchoredBottomSheetStateValue(.1f)
    data object Hide : AnchoredBottomSheetStateValue(.0f) // If you need to hide
}
```
2. Defining a state to control the bottomsheet and defining anchors to tell the bottomsheet how to snap.
```kotlin
val scope = rememberCoroutineScope()
val anchors = listOf(SheetValues.Maximum, SheetValues.Medium, SheetValues.Minimum, SheetValues.Hide)
val scaffoldState = rememberAnchoredBottomSheetScaffoldState(
    anchors = anchors,
    initialValue = anchors.first(),
    animationSpec = tween(easing = EaseOutBack)
)
```
3.
```kotlin
AnchoredBottomSheetScaffold(
    scaffoldState = scaffoldState,
    sheetContent = {
        // content
    },
) { // body }
```
...
Hide 
```kotlin
scope.launch {
    scaffoldState.anchoredSheetState.animateTo(SheetValues.Medium)
}
```
Show
you should save the previous state in any way to restore it. like this:
```kotlin
var previousAnchor by rememberSaveable { mutableStateOf<Float?>(null) }
Button(onClick = {
    scope.launch {
        previousAnchor = scaffoldState.anchoredSheetState.currentValue.value
        scaffoldState.anchoredSheetState.animateTo(SheetValues.Hide)
    }
}) {
    Text(text = "Hide")
}
Button(onClick = {
    scope.launch { scaffoldState.anchoredSheetState.animateTo(anchors.first { anchor -> previousAnchor == anchor.value }) }
}) {
    Text(text = "Show")
}
```
