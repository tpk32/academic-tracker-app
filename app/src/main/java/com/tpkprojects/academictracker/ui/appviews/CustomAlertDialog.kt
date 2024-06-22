package com.tpkprojects.academictracker.ui.appviews

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tpkprojects.academictracker.R

@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    dialogTitle: String,
    dialogText: String,
){

    AlertDialog(onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { confirmButton() }) {
                Text("Confirm",style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Dismiss",style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }
        },
        title = { Text(dialogTitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
        text = { Text(dialogText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface) },
        icon = {Icon(painter = painterResource(id = R.drawable.ic_error), contentDescription = "", modifier = Modifier.size(40.dp, 40.dp), tint = androidx.compose.ui.graphics.Color.Red ) },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = Color.Red,
        textContentColor = MaterialTheme.colorScheme.onSurface
    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultAlertView(){
//    ShowAlertDialog({}, {}, "Confirm Deletion", "Are you sure you want to delete this")
//}