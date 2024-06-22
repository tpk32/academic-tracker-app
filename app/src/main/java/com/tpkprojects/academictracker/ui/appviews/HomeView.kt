package com.tpkprojects.academictracker.ui.appviews


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.dataModel.Subject
import com.tpkprojects.academictracker.dataModel.myConverter


@Composable
fun HomeView(viewModel: MainViewModel){
    val scrollState = rememberScrollState(/*offset.value*/)
    Column (
        modifier = Modifier.verticalScroll(scrollState)
    ){
        GreetingsSection(viewModel = viewModel)
        AverageScoreSection(viewModel = viewModel)
        StatsSection(viewModel = viewModel)
        Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp), color = MaterialTheme.colorScheme.onBackground, thickness = 1.dp)
        Spacer(modifier = Modifier.height(128.dp))
    }
}

@Composable
fun GreetingsSection(viewModel: MainViewModel){
    val userWelcome = if(viewModel.user.value != null) "Welcome ${(viewModel.user.value)!!.name}" else "No User Created"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            userWelcome,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Let's have a look at your performance",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun AverageScoreSection(viewModel: MainViewModel){
    Log.d("usermsg", "homeview")
    val allSubjectsList = viewModel.getSubjectsByUserId.collectAsState(initial = listOf())
    val averagePercentageState = viewModel.averagePercentage.collectAsState(initial = null)
    val averagePercentage = "%.2f".format(if(averagePercentageState.value != null) averagePercentageState.value else 0f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                "Average Score : ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = if(allSubjectsList.value.isEmpty()) {"Oops. No Subjects Found. Open Left Drawer."} else "${averagePercentage}%",
                style = if(allSubjectsList.value.isEmpty()) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

@Composable
fun StatsSection(viewModel: MainViewModel){
    val converter = myConverter()
    val totalTestCountState = viewModel.totalTestCount.collectAsState(initial = null)
    val mostPracticedSubjectState = viewModel.mostPracticedSubject.collectAsState(initial = null)
    val latestTestDateState = viewModel.latestTestDate.collectAsState(initial = null)

    val totalTestCount = if(totalTestCountState.value != null) totalTestCountState.value.toString() else "0"
    val mostPracticedSubject = if(mostPracticedSubjectState.value != null) mostPracticedSubjectState.value.toString() else "None"
    val latestTestDate = if(latestTestDateState.value != null) converter.dateToString(latestTestDateState.value) else "N/A"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Text(
            text = "Stats :",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        StatsItem("Total tests :", totalTestCount)
        StatsItem("Most Practiced Subject :", mostPracticedSubject)
        StatsItem("Last test date :", latestTestDate!!)
    }
}

@Composable
fun StatsItem(labelName:String, ansVal:String){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = labelName,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Left
            )
            Text(
                text = ansVal,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Right
            )
        }
    }
}

//@Preview
//@Composable
//fun DefaultPreviewHome(){
//    HomeView(viewModel = viewModel())
//}