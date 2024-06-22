package com.tpkprojects.academictracker.ui.appviews

import android.graphics.PointF
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
import androidx.compose.material.icons.materialIcon
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.plus
import androidx.core.graphics.times
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.R
import com.tpkprojects.academictracker.Screen
import com.tpkprojects.academictracker.dataModel.User
import com.tpkprojects.academictracker.ui.theme.ThemeColors.Dark.background
import com.tpkprojects.academictracker.ui.theme.ThemeColors.Dark.primary
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column() {
            TopBox(modifier=Modifier.weight(2f))
            BottomBox(modifier=Modifier.weight(3f), viewModel)
        }
    }
}

@Composable
fun BottomBox(modifier : Modifier, viewModel: MainViewModel){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
//            EmailTextField()
//            PasswordTextField()
//            LoginButton(viewModel)
//            OrDivider()
            GuestLoginButton(viewModel)

           // Spacer(modifier = Modifier.height(32.dp))
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
fun LoginButton(viewModel: MainViewModel){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        onClick = {
            //viewModel.currentScreen.value = Screen.BottomScreen.Summary.route
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ){
        Text("Login", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
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
            val guestUser = User(userId = "##", name = "Guest", email = "none")
            viewModel.addUser(guestUser)
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