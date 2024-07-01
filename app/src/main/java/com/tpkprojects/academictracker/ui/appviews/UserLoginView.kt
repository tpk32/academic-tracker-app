package com.tpkprojects.academictracker.ui.appviews

import android.content.Intent
import android.graphics.PointF
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.R
import com.tpkprojects.academictracker.Screen
import com.tpkprojects.academictracker.apiService.ApiViewModel
import com.tpkprojects.academictracker.apiService.apiService
import com.tpkprojects.academictracker.dataModel.Student
import com.tpkprojects.academictracker.presentation.signIn.AuthViewModel
import com.tpkprojects.academictracker.presentation.signIn.handleFirebaseToken
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.toRadians() = this * PI.toFloat() / 180f

internal val PointZero = PointF(0f, 0f)
internal fun radialToCartesian(
    radius: Float,
    angleRadians: Float,
    center: PointF = PointZero
) = directionVectorPointF(angleRadians) * radius + center

internal fun directionVectorPointF(angleRadians: Float) =
    PointF(cos(angleRadians), sin(angleRadians))

@Composable
fun UserLoginView(viewModel:MainViewModel){


    val authViewModel = viewModel<AuthViewModel>()
    val apiViewModel = viewModel<ApiViewModel>()
    var loggedIn = authViewModel.loggedIn.asStateFlow()
    var response = apiViewModel.response.asStateFlow()
    val context = LocalContext.current

    when{
        authViewModel.progressBarVisible.value->
            ShowLoadingDialog()
    }
    when{
        apiViewModel.progressBarVisible.value->
            ShowLoadingDialog()
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try{
            authViewModel.setProgressBarVisible(true)
            val account = task.getResult(ApiException::class.java)
            //val idToken = account.idToken //no need right now
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            authViewModel.signWithCredential(credential)
            Log.d("logintag", "firebaseAuthWithGoogle:" + account.id)

            //handleFirebaseToken(apiViewModel)
        } catch (e: ApiException) {
            Log.w("logintag", "Google sign in failed", e)
            authViewModel.setProgressBarVisible(false)
            authViewModel.triggerToast("Login Failed")
        }
    }

    // get firebase token after succesful googleCLient login
    LaunchedEffect (loggedIn){
        loggedIn.collect {
            if(loggedIn.value == true ){

                Log.d("logintag", "lf start")
                handleFirebaseToken(apiViewModel)
                authViewModel.setLoggedInState(false)
                Log.d("logintag", "lf end")

                //closes whole login time progress bar
                authViewModel.setProgressBarVisible(false)
            }
        }
    }

    // get api response after result from firebase token sent to backend
    LaunchedEffect(response) {
        response.collect {
            if (response.value != null) {
                apiViewModel.setProgressBarVisible(true)
                viewModel.addStudent(response.value!!.Data)
                if (response.value!!.Message == "Student exists") {
                    Log.d("usermsg", "api response")
                    apiViewModel.fetchData(response.value!!.Data.studentId)
                    apiViewModel.resetResponse()
                    viewModel.setCurrentScreen(Screen.BottomScreen.Summary.route)
                }
                apiViewModel.resetResponse()
                apiViewModel.setProgressBarVisible(false)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column() {
            TopBox(modifier=Modifier.weight(2f))
            BottomBox(modifier=Modifier.weight(3f), viewModel, launcher)
        }
    }

    //Launched Effect for catching toast
    LaunchedEffect(authViewModel.toastEvent){
        authViewModel.toastEvent.collect{
            if(it!=null){
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                authViewModel.setToastEvent(null)
            }
        }
    }
    LaunchedEffect(apiViewModel.toastEvent){
        apiViewModel.toastEvent.collect{
            if(it!=null){
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                apiViewModel.setToastEvent(null)
            }
        }
    }
}

@Composable
fun BottomBox(modifier : Modifier, viewModel: MainViewModel, launcher : ManagedActivityResultLauncher<Intent, ActivityResult>){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
//            EmailTextField()
//            PasswordTextField()
            LoginButton(viewModel, launcher)
            OrDivider()
            GuestLoginButton(viewModel)

            Spacer(modifier = Modifier.height(128.dp))
        }
    }
}

@Composable
fun TopBox(modifier : Modifier){
    val vertices = remember{
        val radius = 1f
        val cornerradius = radius/cos(45f.toRadians())

        floatArrayOf(
            radialToCartesian(cornerradius, 45f.toRadians()).x,
            radialToCartesian(cornerradius, 45f.toRadians()).y,

            radialToCartesian(cornerradius, 135f.toRadians()).x,
            radialToCartesian(cornerradius, 135f.toRadians()).y,

            radialToCartesian(cornerradius, 225f.toRadians()).x,
            radialToCartesian(cornerradius, 225f.toRadians()).y,

            radialToCartesian(cornerradius, 315f.toRadians()).x,
            radialToCartesian(cornerradius, 315f.toRadians()).y
        )
    }

    val rounding = remember{
        val roundingnormal = 0.2f
        listOf(
            CornerRounding(roundingnormal),
            CornerRounding(roundingnormal),
            CornerRounding(0f),
            CornerRounding(0f)
        )
    }

    val polygon = remember(vertices, rounding) {
        RoundedPolygon(
            vertices = vertices,
            perVertexRounding = rounding
        )
    }
    val color = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .drawWithCache {

                val roundedPolygonPath = polygon
                    .toPath()
                    .asComposePath()
                onDrawBehind {
                    scale(size.width * 0.5f, size.height * 0.5f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(roundedPolygonPath, color = color)
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ){
        val image = painterResource(id = R.drawable.book)
        Image(
            modifier = Modifier
                .padding(40.dp)
                .aspectRatio(1f)
                //.border(8.dp, Color.Black, RoundedCornerShape(32.dp))
                .padding(30.dp),
            painter = image,
            contentDescription = "logo",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
        )
    }
}


@Composable
fun EmailTextField(){
    val email = remember{ mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        value = email.value,
        onValueChange = {email.value=it},
        label = {Text("Email", style = MaterialTheme.typography.bodyMedium)},
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun PasswordTextField(){
    val showPassword = remember{ mutableStateOf(false)  }
    val password = remember{ mutableStateOf("") }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 8.dp),
        value = password.value,
        //textStyle = TextStyle(baselineShift = BaselineShift(0f)),
        onValueChange = {password.value = it},
        label = {Text("Password", style = MaterialTheme.typography.bodyMedium)},
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = { // Add the trailing icon here
            IconButton(onClick = { showPassword.value = !(showPassword.value) },
                modifier = Modifier
            ) {
                Icon( // Replace with your desired eye icon
                    painter = painterResource(id = if (showPassword.value) R.drawable.ic_visibility else R.drawable.ic_visibilityoff),
                    contentDescription = if (showPassword.value) "Hide password" else "Show password",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun LoginButton(viewModel: MainViewModel, launcher : ManagedActivityResultLauncher<Intent, ActivityResult>){

    val context = LocalContext.current
    val token = stringResource(R.string.web_client_id)

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ){
        Text("Login With Google", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OrDivider(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Divider(modifier = Modifier
            .weight(1f)
            .padding(end = 10.dp), color = MaterialTheme.colorScheme.onBackground)
        Text("or", modifier = Modifier.wrapContentSize(), color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall)
        Divider(modifier = Modifier
            .weight(1f)
            .padding(start = 10.dp), color = MaterialTheme.colorScheme.onBackground)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestLoginButton(viewModel: MainViewModel){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp)
            .clip(RoundedCornerShape(10.dp)),
        onClick = {
            val guestStudent = Student(studentId = "##", name = "Guest", email = "noEmail")
            viewModel.addStudent(guestStudent)
            viewModel.setCurrentScreen(Screen.BottomScreen.Summary.route)
        },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text("Guest Login", modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp),
            textAlign = TextAlign.Center,
            //color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

//@Preview
//@Composable
//fun DefaultUserLoginView(){
//    UserLoginView(viewModel = viewModel())
//}