package ru.leushinilya.loftmoney.screens.main.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.cells.Item

@Preview
@Composable
fun ListScreen(viewModel: ListViewModel = ListViewModel()) {
    val isRefreshing = viewModel.isRefreshing
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {

        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.items) { transaction ->
                ItemView(item = transaction)
                Divider(
                    color = colorResource(id = R.color.medium_grey),
                    modifier = Modifier.alpha(0.2F)
                )
            }
        }
    }

}

@Composable
fun ItemView(item: Item = Item("111", "Name", "500", 1)) {
    val textStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )
    val priceColor = when (item.type) {
        0 -> colorResource(id = R.color.lightish_blue)
        1 -> colorResource(id = R.color.apple_green)
        else -> colorResource(id = R.color.medium_grey)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = false,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = {}
            )
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onLongPress = { item.isSelected = true }
//                    )
//                }
            .background(color = colorResource(id = R.color.white))
            .padding(dimensionResource(id = R.dimen.spacing_24)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            color = colorResource(id = R.color.medium_grey),
            style = textStyle
        )
        Text(
            text = item.price,
            style = textStyle,
            color = priceColor
        )
    }
}