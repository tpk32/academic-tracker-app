package com.tpkprojects.academictracker.ui.appviews


import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.dataModel.myConverter

@Composable
fun SubjectView(viewModel: MainViewModel){
    viewModel.getAvgPercentageInSubject(viewModel.currentSubject.value)
    val allSubjectsList = viewModel.getSubjectsByStudentId.collectAsState(initial = listOf())
    val converter = myConverter()

    viewModel.getTestCountForSubject(viewModel.currentSubject.value)
    viewModel.getSubjectFirstTestData(viewModel.currentSubject.value)
    viewModel.getSubjectLastTestData(viewModel.currentSubject.value)

    val testCountForSubjectState = viewModel.testCountForSubject.collectAsState(initial = null)
    val subjectLastTestDataState = viewModel.subjectLastTestData.collectAsState(initial = null)
    val subjectFirstTestDataState = viewModel.subjectFirstTestData.collectAsState(initial = null)
    if(!allSubjectsList.value.isEmpty()){

    }

    if(testCountForSubjectState.value==0){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "No Test Data for this subject ! :(",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ){
            Text(
                text = "Here is the summary for the subject",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            SubjectAverageSection(viewModel = viewModel)
            Spacer(modifier= Modifier.height(8.dp))
            SubSummaryItem(labelName = "Tests taken", ansVal = testCountForSubjectState.value.toString())
            SubSummaryItem(labelName = "Last Test Date", ansVal = if(subjectLastTestDataState.value != null) converter.dateToString(subjectLastTestDataState.value!!.thisTestDate)!! else "N/A")
            SubSummaryItem(labelName = "Last Test Marks", ansVal = if(subjectLastTestDataState.value != null) "${subjectLastTestDataState.value!!.obtainedMarks} / ${subjectLastTestDataState.value!!.maxMarks}" else "N/A")
            SubSummaryItem(labelName = "First Test Date", ansVal =  if(subjectFirstTestDataState.value != null) converter.dateToString(subjectFirstTestDataState.value!!.thisTestDate)!! else "N/A")
            SubSummaryItem(labelName = "First Test Marks", ansVal = if(subjectFirstTestDataState.value != null) "${subjectFirstTestDataState.value!!.obtainedMarks} / ${subjectFirstTestDataState.value!!.maxMarks}" else "N/A")

            Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp), color = MaterialTheme.colorScheme.onBackground, thickness = 1.dp)
            Spacer(modifier = Modifier.height(128.dp))

        }
    }

}

@Composable
fun SubjectAverageSection(viewModel: MainViewModel){
    val subjectAvgPercentageState = viewModel.subjectPercentage.collectAsState(initial = null)
    val subjectAveragePercentage = "%.2f".format(if(subjectAvgPercentageState.value != null) subjectAvgPercentageState.value else 0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                "Average Score in ${viewModel.currentSubject.value}: ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text =  "${subjectAveragePercentage}%",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SubSummaryItem(labelName:String, ansVal:String){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        shape = RoundedCornerShape(6.dp),
        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ){
        Row(modifier= Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(labelName, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Left, modifier=Modifier.weight(16f))
            Text(":", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center, modifier=Modifier.weight(1f))
            Text(ansVal, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Right, modifier=Modifier.weight(10f))
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewSubject(){
//    SubjectView(viewModel = viewModel())
//}