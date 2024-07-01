package com.tpkprojects.academictracker.ui.appviews

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.dataModel.Test
import com.tpkprojects.academictracker.dataModel.Converters
import com.tpkprojects.academictracker.dataModel.TestWithSubject
import com.tpkprojects.academictracker.dataModel.myConverter

@Composable
fun TestView(viewModel: MainViewModel){
    val allTestList: State<List<TestWithSubject>>
    val allSubjectTestList: State<List<Test>>

    allTestList= viewModel.allTestsWithSubject.collectAsState(initial = listOf())
    if(viewModel.currentSubject.value != "Home") viewModel.getTestsBySubjectName(viewModel.currentSubject.value)
    allSubjectTestList = viewModel.testsById.collectAsState(initial = listOf())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 128.dp)
    ){
        items(if(viewModel.currentSubject.value == "Home") allTestList.value
            else allSubjectTestList.value
        ){
            item->
            if(viewModel.currentSubject.value == "Home"){
                item as TestWithSubject
                TestItem(item.test, item.subjectName, viewModel)
            }else{
                item as Test
                TestItem(item, "",viewModel)
            }
        }
    }
    when {
        viewModel.testAlertDialog.value -> {
            ShowAlertDialog(
                onDismissRequest = {
                    viewModel.testAlertDialog.value = false
                },
                confirmButton = {
                    viewModel.testAlertDialog.value = false
                    viewModel.deleteTest()
                    viewModel.selectedTestforDelete.value = null
                },
                dialogTitle = "Confirm Deletion of Test",
                dialogText = "Are you sure you want to delete ${viewModel.selectedTestforDelete.value!!.testName} test?"
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestItem(test: Test, subjectName: String, viewModel: MainViewModel){

    val dtconverter = myConverter()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    viewModel.setSelectedTestForDelete(test)
                    viewModel.testAlertDialog.value = true
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )

        //border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()){
            if(viewModel.currentSubject.value == "Home"){
                Text(
                    text = subjectName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Row(modifier = Modifier.fillMaxWidth()){
                Text(
                    text = test.testName,
                    modifier=Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = "${test.obtainedMarks}/${test.maxMarks}",
                    modifier=Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()) {
                val getTestDate = dtconverter.dateToString(test.testDate)?:"Error getting Date"
                Text(
                    text = getTestDate,
                    fontWeight = FontWeight.Light,
                    modifier=Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Left
                )
                val formattedTestcore = "%.2f".format(test.obtainedMarks.toFloat()/test.maxMarks*100)
                Text(
                    text = "${formattedTestcore}%",
                    modifier=Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreviewTest(){
//    TestView(viewModel=viewModel())
//}