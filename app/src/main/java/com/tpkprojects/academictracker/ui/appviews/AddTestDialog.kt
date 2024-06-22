package com.tpkprojects.academictracker.ui.appviews

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tpkprojects.academictracker.MainViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun triggerAddTestDialog(viewModel: MainViewModel){
    viewModel.resetAddTestDialog()

    if(viewModel.addTestDialogOpen.value){
        AddTestDialog(viewModel)
    }
}

@Composable
fun AddTestDialog(viewModel: MainViewModel) {
    when{
        viewModel.progressBarVisible.value->
            ShowLoadingDialog()
    }
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { viewModel.addTestDialogOpen.value = false },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Add Test",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)
                )

                SubjectSection(viewModel)
                TestTitleSection(viewModel)
                MaxMarksSection(viewModel)
                MarksObtainedSection(viewModel)
                DateSection(viewModel)
                TestDialogButtons(viewModel)

                LaunchedEffect(viewModel.toastEvent){
                    viewModel.toastEvent.collect{
                        if(it!=null){
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            viewModel.setToastEvent(null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectSection(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text="Subject", Modifier.weight(5f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(text=":", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Button(
            onClick = {
                viewModel.subjectExpanded.value = !(viewModel.subjectExpanded.value)
            },
            modifier = Modifier
                .weight(4f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 0.dp, top = 0.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            ),
        ) {
            var SelectedSubject = viewModel.subjectSelectedOption.value
            if(SelectedSubject == ""){
                SelectedSubject = "Select Subject"
            }
            Text(SelectedSubject, modifier=Modifier.weight(5f),style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Subject", modifier = Modifier.weight(1f))
            SubjectDropDownMenu(viewModel)
        }
    }
}

@Composable
fun SubjectDropDownMenu(viewModel: MainViewModel){
    val allSubjectsList = viewModel.getSubjectsByUserId.collectAsState(initial = listOf())
    if(allSubjectsList.value.isEmpty() && viewModel.subjectExpanded.value==true){
        viewModel.triggerToast("No Subjects added yet")
    }
    DropdownMenu(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        expanded = viewModel.subjectExpanded.value,
        onDismissRequest = { viewModel.subjectExpanded.value = false },
        content = {
            allSubjectsList.value.forEach{
                DropdownMenuItem(onClick = {
                    viewModel.subjectSelectedOption.value = it.subjectName
                    viewModel.subjectExpanded.value = false
                }) {
                    Text(text = it.subjectName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    )
}

@Composable
fun TestTitleSection(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text="Test title", Modifier.weight(5f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(text=":", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        TestItemTextField(
            Modifier
                .weight(4f)
                .fillMaxWidth(),
            value = viewModel.addTestDialogTTState.value,
            keyboardType = KeyboardType.Text,
            onValueChanged = {viewModel.onAddTestDialogTTChange(it)},
            viewModel = viewModel
        )
    }
}

@Composable
fun MaxMarksSection(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = "Maximum Marks",
            Modifier.weight(5f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = ":",
            Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        TestItemTextField(
            Modifier
                .weight(4f)
                .fillMaxWidth(),
            value = viewModel.addTestDialogMMState.value,
            keyboardType = KeyboardType.Number,
            onValueChanged = { viewModel.onAddTestDialogMMChange(it) },
            viewModel = viewModel
        )
    }
}

@Composable
fun MarksObtainedSection(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text="Marks Obtained", Modifier.weight(5f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(text=":", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        TestItemTextField(
            Modifier
                .weight(4f)
                .fillMaxWidth(),
            value = viewModel.addTestDialogMOState.value,
            keyboardType = KeyboardType.Number,
            onValueChanged = {viewModel.onAddTestDialogMOChange(it)},
            viewModel = viewModel
        )
    }
}

@Composable
fun DateSection(viewModel: MainViewModel){
    val dateDialogState = rememberMaterialDialogState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text="Date", Modifier.weight(5f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(text=":", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

        Button(
            onClick = {
                dateDialogState.show()
                if(viewModel.pickedDate.value == LocalDate.now().plusDays(10)) {
                    viewModel.pickedDate.value = LocalDate.now()
                }
            },
            modifier = Modifier
                .weight(4f)
                .fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            contentPadding = PaddingValues(start = 2.dp, end = 2.dp, bottom = 0.dp, top = 0.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            ),

            ) {
            DateDialog(dateDialogState, viewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(viewModel.addTestDialogTDState.value?: "", modifier= Modifier
                    .weight(10f)
                    .offset(y = 1.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date", modifier = Modifier.weight(2f))
            }
        }
    }
}

@Composable
fun TestItemTextField(modifier:Modifier, value: String, keyboardType: KeyboardType, onValueChanged: (String)->Unit, viewModel: MainViewModel){
    OutlinedTextField(value = value, onValueChange = onValueChanged,
        modifier = modifier.height(50.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        maxLines = 1
    )
}

@Composable
fun DateDialog(datedialogState: MaterialDialogState, viewModel: MainViewModel){
    val bgcolor = MaterialTheme.colorScheme.surface
    val bgclr = remember {
        mutableStateOf(bgcolor)
    }
    val fgcolor = MaterialTheme.colorScheme.onSurface
    val fgclr = remember {
        mutableStateOf(fgcolor)
    }

    val colors = object: com.vanpra.composematerialdialogs.datetime.date.DatePickerColors{
        override val calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface
        override val headerBackgroundColor = MaterialTheme.colorScheme.primary
        override val headerTextColor = MaterialTheme.colorScheme.onPrimary
        @Composable
        override fun dateBackgroundColor(active: Boolean): State<Color> {

            if(active) bgclr.value = MaterialTheme.colorScheme.primary
            else bgclr.value = MaterialTheme.colorScheme.surface
            return bgclr
        }

        @Composable
        override fun dateTextColor(active: Boolean): State<Color> {

            if(active) fgclr.value = MaterialTheme.colorScheme.onPrimary
            else fgclr.value = MaterialTheme.colorScheme.onSurface
            return fgclr
        }
    }
    MaterialDialog(
        dialogState = datedialogState,
        backgroundColor = MaterialTheme.colorScheme.surface,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        buttons = {
            positiveButton(text = "OK"){

            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = viewModel.pickedDate.value,
            title = "Pick a date",
            allowedDateValidator = {
                it.isBefore(LocalDate.now().plusDays(1))
            },
            colors = colors
        ){
            viewModel.onAddTestDialogTDChange(it)
        }
    }
}

@Composable
fun TestDialogButtons(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        TestConfirmButton(viewModel = viewModel)
        TestDismissButton(viewModel = viewModel)
    }
}

@Composable
fun TestConfirmButton(viewModel: MainViewModel){
    TextButton(onClick = {
        if(viewModel.subjectSelectedOption.value == ""
            || viewModel.addTestDialogTTState.value == ""
            || viewModel.addTestDialogMMState.value == ""
            || viewModel.addTestDialogMOState.value == ""
            || viewModel.addTestDialogTDState.value == ""
        ){
            viewModel.triggerToast("Please fill all the fields .")
        }else if(viewModel.addTestDialogMMState.value.toInt()<viewModel.addTestDialogMOState.value.toInt()){
            viewModel.triggerToast("Marks Obtained cannot be greater than Maximum Marks")
        } else if(viewModel.progressBarVisible.value){
            viewModel.triggerToast("Wait for current task to finish")
        }else{
            viewModel.addTest{
                    response->
                if(response == -1L){
                    viewModel.triggerToast("Test with same subject, name and date already exists.")
                }else {
                    viewModel.addTestDialogOpen.value = false
                    viewModel.triggerSnackBar("Test Added Successfully.")
                }
            }
        }
    }){
        Text("Confirm", style = MaterialTheme.typography.bodyMedium, fontWeight =  FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun TestDismissButton(viewModel: MainViewModel){
    TextButton(onClick = {
        viewModel.addTestDialogOpen.value = false
    }){
        Text("Dismiss", style = MaterialTheme.typography.bodyMedium, fontWeight =  FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultTestAlertView() {
//    val b = remember { mutableStateOf(true) }
//    val viewModel = MainViewModel()
//    AddTestDialog(viewModel = MainViewModel())
//}