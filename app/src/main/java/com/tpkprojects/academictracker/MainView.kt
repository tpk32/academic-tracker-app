package com.tpkprojects.academictracker

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
//import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tpkprojects.academictracker.apiService.ApiViewModel
import com.tpkprojects.academictracker.ui.appviews.ShowAlertDialog
import com.tpkprojects.academictracker.ui.appviews.ShowLoadingDialog
import com.tpkprojects.academictracker.ui.appviews.triggerAddSubjectDialog
import com.tpkprojects.academictracker.ui.appviews.triggerAddTestDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(viewModel: MainViewModel) {

    val title = viewModel.currentTitle
    val currentScreen = viewModel.currentScreen
    val scope: CoroutineScope = rememberCoroutineScope()
    val drawerState:DrawerState = rememberDrawerState(initialValue=DrawerValue.Closed)
    val navController: NavHostController = rememberNavController()
    val apiViewModel : ApiViewModel = ApiViewModel()
    val context = LocalContext.current

    LaunchedEffect(viewModel.snackbarEvent){
        viewModel.snackbarEvent.collect { event ->
            if (event != null) {
                viewModel.snackbarHostState.showSnackbar(event.message, event.actionLabel)
                viewModel.setSnackbarEvent(null) // Clear event after handling
            }
        }
    }

    val topBar: @Composable () -> Unit = {
        TopAppBar(
            title = {
                Text(
                    text = title.value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            ),
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed) drawerState.open()
                        else drawerState.close()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    viewModel.accountExpanded.value = !(viewModel.accountExpanded.value)
                }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                AccountMenu(viewModel = viewModel, navController = navController)
            }
        )
    }

    val bottomBar: @Composable () -> Unit = {

        NavigationBar(
            Modifier.wrapContentSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            for (item in screensInBottom){
                NavigationBarItem(
                    selected = (item.route == viewModel.currentScreen.value),
                    onClick = {
                        navController.navigate(item.route){
                            popUpToTop(navController)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    icon = { Icon(
                        contentDescription = item.bTitle,
                        painter = painterResource(id = item.icon),
                    ) },
                    label = { Text(text = item.bTitle) }
                )
            }

        }
    }

    val floatingActionButton : @Composable () -> Unit = {
        val allSubjectsList = viewModel.getSubjectsByStudentId.collectAsState(initial = listOf())
        val allTestList = viewModel.allTestsWithSubject.collectAsState(initial = listOf())

        Column {
            if (viewModel.student.value!!.name != "Guest") {
                FloatingActionButton(
                    modifier = Modifier.size(40.dp, 40.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        apiViewModel.syncToBackend(
                            viewModel.student.value!!,
                            allSubjectsList.value,
                            allTestList.value
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cloudsync),
                        contentDescription = "sync"
                    )
                }
            }

            FloatingActionButton(
                modifier = Modifier.padding(top = 8.dp),
                contentColor = MaterialTheme.colorScheme.onTertiary,
                containerColor = MaterialTheme.colorScheme.tertiary,
                onClick = {
                    viewModel.addTestDialogOpen.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Test"
                )
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Drawer(viewModel = viewModel, drawerState, scope)
            SnackbarHost(hostState = viewModel.snackbarHostState){
                if(drawerState.isOpen) {
                    Snackbar(
                    ){
                        Text(it.visuals.message)
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = (if(currentScreen.value==Screen.UserLoginScreen.route) ({}) else topBar),
            bottomBar = (if(currentScreen.value==Screen.UserLoginScreen.route) ({}) else bottomBar),
            floatingActionButton = (if(currentScreen.value==Screen.UserLoginScreen.route) ({}) else floatingActionButton),
            snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState)
            {snackbarData ->
                if(drawerState.isClosed && !(viewModel.addTestDialogOpen.value)){
                    Snackbar(
                    ){
                        Text(snackbarData.visuals.message)
                    }
                }
            }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                LaunchedEffect(apiViewModel.toastEvent){
                    apiViewModel.toastEvent.collect{
                        if(it!=null){
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            apiViewModel.setToastEvent(null)
                        }
                    }
                }

                when{
                    apiViewModel.progressBarVisible.value->
                        ShowLoadingDialog("Syncing to Cloud")
                }
                Navigation(navController = navController, viewModel = viewModel)
                triggerAddTestDialog(viewModel = viewModel)

            }
        }
    }
}

@Composable
fun Drawer(viewModel: MainViewModel, drawerState: DrawerState, scope:CoroutineScope){
    val allSubjectsList = viewModel.getSubjectsByStudentId.collectAsState(initial = listOf())

    ModalDrawerSheet {
        Column(){
            NavigationDrawerItem(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp),
                label = { Text(text="Home", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface) },
                selected = false,
                onClick = {
                    viewModel.setCurrentSubject("Home")
                    viewModel.setCurrentTitle("Home")
                    scope.launch {
                        drawerState.close()
                    }
                }
            )

            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            NavigationDrawerItem(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp)
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(32.dp)
                    ),
                selected = false,
                onClick = {
                    viewModel.addSubjectDialogOpen.value = !(viewModel.addSubjectDialogOpen.value)
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.surface
                ),
                label = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Subject",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add new Subject",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp)
            ){
                items(allSubjectsList.value){
                    subject->
                    NavigationDrawerItem(
                        selected = false,
                        onClick = {
                            viewModel.setCurrentSubject(subject.subjectName)
                            viewModel.setCurrentTitle(subject.subjectName)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        label = {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding()
                                    .clip(RoundedCornerShape(8.dp)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween

                            ){
                                Text(text = subject.subjectName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable(
                                            onClick = {
                                                viewModel.subjectAlertDialog.value = true
                                                viewModel.setSelectedSubjectForDelete(subject)
                                            }
                                        )
                                ){
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(24.dp)
                                            .aspectRatio(1f),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
        triggerAddSubjectDialog(viewModel = viewModel)
    }

    when{
        viewModel.subjectAlertDialog.value ->{
            ShowAlertDialog(
                onDismissRequest = {
                    viewModel.setSelectedSubjectForDelete(null)
                    viewModel.subjectAlertDialog.value = false
                },
                confirmButton = {
                    viewModel.subjectAlertDialog.value = false
                    viewModel.deleteSubject()
                    viewModel.setCurrentSubject("Home")
                    viewModel.setSelectedSubjectForDelete(null)
                },
                dialogTitle = "Confirm Deletion of Subject",
                dialogText = "This will delete the Subject with all of its tests. Are you sure you want to continue?    "
            )
        }
    }
}

@Composable
fun AccountMenu(viewModel: MainViewModel, navController:NavHostController){

    val token = stringResource(R.string.web_client_id)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(token)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(LocalContext.current, gso)

    DropdownMenu(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        expanded = viewModel.accountExpanded.value,
        onDismissRequest = { viewModel.accountExpanded.value = false },
        content = {
            DropdownMenuItem(onClick = {},
                enabled = false
            ) {
                Text(viewModel.student.value!!.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            DropdownMenuItem(onClick = {},
                enabled = false
            ) {
                Text(viewModel.student.value!!.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            DropdownMenuItem(onClick = {
                viewModel.userAlertDialog.value = true
                viewModel.accountExpanded.value = false
            },
                enabled = true
            ) {
                Text("LOGOUT", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
    when{
        viewModel.userAlertDialog.value ->{
            ShowAlertDialog(
                onDismissRequest = {
                    viewModel.userAlertDialog.value = false
                },
                confirmButton = {
                    viewModel.userAlertDialog.value = false
                    viewModel.deleteUser()
                    viewModel.setStudent(null)
                    Firebase.auth.signOut()
                    googleSignInClient.signOut()
                    viewModel.setFirstView(1)
                    Log.d("user", "${viewModel.student.value?.name}")
                    navController.navigate(Screen.BottomScreen.Summary.route) {
                        popUpToTop(navController)
                    }
                    viewModel.setCurrentScreen(Screen.UserLoginScreen.route)
                },
                dialogTitle = "Confirm Logout",
                dialogText = "This will delete all your locally stored data but data on server will remain. Are you sure you want to continue?"
            )
        }
    }
}


//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewMainView(){
//    MainView()
//}
