package ru.leushinilya.loftmoney.ui.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.ui.themes.*

@Composable
fun SettingsDialog(viewModel: SettingsViewModel = hiltViewModel(), onDismissRequest: () -> Unit) {
    val uiSettings = viewModel.uiSettings.collectAsState()

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(id = R.string.settings),
                style = LoftTheme.typography.toolbar,
                color = LoftTheme.colors.primaryText
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.onSaveButtonClicked()
                    onDismissRequest()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = LoftTheme.typography.contentSmall,
                    color = LoftTheme.colors.primaryText
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = LoftTheme.typography.contentSmall,
                    color = LoftTheme.colors.primaryText
                )
            }
        },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.color_scheme),
                    style = LoftTheme.typography.contentNormal,
                    color = LoftTheme.colors.primaryText
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    listOf(LoftColors.BLUE, LoftColors.RED, LoftColors.PURPLE).forEach {
                        ColorItem(
                            palette = it.colorSet,
                            onClick = { viewModel.onColorSchemeSelected(it) },
                            isSelected = uiSettings.value.colors == it
                        )
                    }
                }
                TypographySpinner(viewModel = viewModel)
            }
        }
    )
}

@Composable
fun ColorItem(palette: ColorSet, onClick: () -> Unit, isSelected: Boolean) {
    Button(
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = palette.primaryBackground
        ),
        modifier = Modifier
            .size(36.dp)
            .border(
                if (isSelected) 2.dp else 0.dp,
                palette.secondaryBackground,
                shape = CircleShape
            ),
        onClick = { onClick() }
    ) {}
}

@Composable
fun TypographySpinner(viewModel: SettingsViewModel) {
    var expanded by remember { mutableStateOf(false) }
    TextButton(
        onClick = { expanded = !expanded },
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.font_size),
                style = LoftTheme.typography.contentNormal,
                color = LoftTheme.colors.primaryText
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val fonts = listOf(
                    LoftTypography.SMALL to stringResource(id = R.string.small),
                    LoftTypography.NORMAL to stringResource(id = R.string.normal),
                    LoftTypography.LARGE to stringResource(id = R.string.large)
                )
                fonts.forEach {
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onTypographySelected(it.first)
                            expanded = false
                        }
                    ) {
                        Text(
                            text = it.second,
                            style = it.first.fontSet.contentNormal,
                            color = LoftTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsDialogPreview() = MainTheme(
    UiSettings(
        colors = LoftColors.BLUE,
        typography = LoftTypography.NORMAL
    )
) {
    SettingsDialog {}
}