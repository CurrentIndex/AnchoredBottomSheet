Step 2. Add it in your root build.gradle at the end of repositories:
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

