package ru.leushinilya.loftmoney.ui.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
                onClick = {},
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
                    ColorItem(bluePalette)
                    ColorItem(redPalette)
                    ColorItem(purplePalette)
                }
                TypographySpinner(viewModel = viewModel)
            }
        }
    )
}

@Composable
fun ColorItem(palette: LoftColors) {
    Button(
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = palette.primaryBackground
        ),
        modifier = Modifier
            .size(36.dp)
            .border(2.dp, palette.secondaryBackground, shape = CircleShape),
        onClick = {},
        content = {}
    )
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
                DropdownMenuItem(onClick = { }) {
                    Text(
                        text = stringResource(id = R.string.small),
                        style = smallTypography.contentNormal,
                        color = LoftTheme.colors.primaryText
                    )
                }
                DropdownMenuItem(onClick = { }) {
                    Text(
                        text = stringResource(id = R.string.normal),
                        style = normalTypography.contentNormal,
                        color = LoftTheme.colors.primaryText
                    )
                }
                DropdownMenuItem(onClick = { }) {
                    Text(
                        text = stringResource(id = R.string.large),
                        style = largeTypography.contentNormal,
                        color = LoftTheme.colors.primaryText
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsDialogPreview() = MainTheme(
    colorStyle = LoftColorStyle.BLUE,
    fontStyle = LoftFontStyle.NORMAL
) {
    SettingsDialog {}
}