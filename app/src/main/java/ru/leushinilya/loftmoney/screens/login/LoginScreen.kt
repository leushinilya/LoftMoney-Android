package ru.leushinilya.loftmoney.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.leushinilya.loftmoney.R

@Preview
@Composable
fun LoginScreen(viewModel: LoginViewModel = LoginViewModel()) {
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
                viewModel.onLoginClicked()
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