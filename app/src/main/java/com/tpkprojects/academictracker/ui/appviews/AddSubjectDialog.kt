package com.tpkprojects.academictracker.ui.appviews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tpkprojects.academictracker.MainViewModel

@Composable
fun triggerAddSubjectDialog(viewModel: MainViewModel){
    if(viewModel.addSubjectDialogOpen.value){
        AddSubjectDialog(viewModel)
    }
}
@Composable
fun AddSubjectDialog(viewModel: MainViewModel){
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { viewModel.addSubjectDialogOpen.value = false },

        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                .background(color = MaterialTheme.colorScheme.surface),
        ){
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Subject",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)
                )

                SubjectDialogData(viewModel = viewModel)
                SubjectDialogButtons(viewModel = viewModel)

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
fun SubjectDialogData(viewModel: MainViewModel){
    when{
        viewModel.progressBarVisible.value->
            ShowLoadingDialog()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text="New Subject", Modifier.weight(5f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(text=":", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

        SubjectTextField(
            Modifier
                .weight(4f)
                .fillMaxWidth(),
            value = viewModel.addSubjectDialogTFState.value,
            keyboardType = KeyboardType.Text,
            onValueChanged = {viewModel.onAddSubjectDialogTFChange(it)},
            viewModel = viewModel
        )
    }
}

@Composable
fun SubjectTextField(modifier: Modifier, value: String, keyboardType: KeyboardType, onValueChanged: (String)->Unit, viewModel: MainViewModel){
    OutlinedTextField(value = value, onValueChange = onValueChanged,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

@Composable
fun SubjectDialogButtons(viewModel: MainViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        SubjectConfirmButton(viewModel = viewModel)
        SubjectDismissButton(viewModel = viewModel)
    }
}

@Composable
fun SubjectConfirmButton(viewModel: MainViewModel){
    TextButton(onClick = {
        if(viewModel.addSubjectDialogTFState.value==""){
            viewModel.triggerToast("Enter a name for subject")
        }else if(viewModel.progressBarVisible.value){
            viewModel.triggerToast("Wait for current task to finish")
        }else{
            viewModel.addSubject {
                    response->
                if(response == -1L){
                    viewModel.triggerToast("Subject already exists")
                }else{
                    viewModel.triggerToast("Subject added Successfully")
                    viewModel.addSubjectDialogOpen.value = false
                    viewModel.addSubjectDialogTFState.value = ""
                }
            }
        }
    }){
        Text("Confirm", style = MaterialTheme.typography.bodyMedium, fontWeight =  FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun SubjectDismissButton(viewModel: MainViewModel){
    TextButton(onClick = {
        viewModel.addSubjectDialogOpen.value = false
        viewModel.triggerSnackBar("Dismiss clicked")
    }){
        Text("Dismiss", style = MaterialTheme.typography.bodyMedium, fontWeight =  FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
    }
}


//@Preview
//@Composable
//fun DefaultSubjectDialog(){
//    AddSubjectDialog(MainViewModel())
//}
