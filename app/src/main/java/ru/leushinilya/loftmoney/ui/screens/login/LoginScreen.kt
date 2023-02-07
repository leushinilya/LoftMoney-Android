package ru.leushinilya.loftmoney.ui.screens.login

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.ui.themes.LoftTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val googleSignClient = getGoogleSignClient(LocalContext.current as Activity)
    val authorized = viewModel.authorized.observeAsState()
    if (authorized.value != true) {
        googleSignClient.signOut()
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoftTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
            contentDescription = "",
            modifier = Modifier.padding(100.dp)
        )
        Button(
            onClick = { launcher.launch(googleSignClient.signInIntent) },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(LoftTheme.colors.contentBackground),
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
                color = LoftTheme.colors.primaryBackground,
                style = LoftTheme.typography.contentNormal
            )
        }
    }
}

fun getGoogleSignClient(activity: Activity): GoogleSignInClient {
    val signOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(activity, signOptions)
}