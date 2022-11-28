package ru.leushinilya.loftmoney.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.Screens

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val authorized = viewModel.authorized.observeAsState()
    if (authorized.value == true && navController.currentDestination?.route != Screens.MAIN.name) {
        navController.navigate(Screens.MAIN.name)
        return
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            it.data?.let { intent ->
                GoogleSignIn.getSignedInAccountFromIntent(intent).addOnCompleteListener {
                    it.result.id?.let { id -> viewModel.onIdReceived(id) }
                }
            }
        }
    )
    val intent = viewModel.googleSignIntent.observeAsState()
    if (intent.value != null) {
        SideEffect {
            launcher.launch(intent.value)
        }
    }
    val activity = LocalContext.current as Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightish_blue)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
            contentDescription = "",
            modifier = Modifier.padding(100.dp)
        )
        Button(
            onClick = {
                viewModel.onLoginClicked(activity)
            },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(colorResource(id = R.color.white)),
            modifier = Modifier.width(170.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                contentDescription = "",
                Modifier.padding(end = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.action_login),
                color = colorResource(id = R.color.lightish_blue),
                fontSize = 14.sp
            )
        }
    }
}