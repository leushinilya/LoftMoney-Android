package ru.leushinilya.loftmoney.screens.old.main.budget

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.cells.Item

class BudgetFragment : Fragment() {

    private var currentPosition = 0
    private var budgetViewModel: BudgetViewModel? = null
    private var toolbar: MaterialToolbar? = null
    private var tabLayout: TabLayout? = null
    private var iconBack: ImageView? = null
    private var iconTrash: ImageView? = null
    private var toolBarTextView: TextView? = null
    private var addFAB: FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            TransactionList()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.add_fab)?.visibility = View.VISIBLE
        currentPosition = (activity?.findViewById<View>(R.id.pages) as ViewPager2).currentItem
        budgetViewModel!!.updateListFromInternet(
            (activity?.application as LoftApp).itemsAPI,
            currentPosition,
            activity?.getSharedPreferences(getString(R.string.app_name), 0)!!
        )
        configureTabIcons()
    }

    override fun onPause() {
        super.onPause()
        budgetViewModel?.isEditMode?.postValue(false)
        switchColorsForEditMode(false)
    }

    private fun initViews() {
        toolbar = activity?.findViewById(R.id.toolBar)
        tabLayout = activity?.findViewById(R.id.tabs)
        iconBack = activity?.findViewById(R.id.iconBack)
        iconTrash = activity?.findViewById(R.id.iconTrash)
        toolBarTextView = activity?.findViewById(R.id.toolBarTextView)
        addFAB = activity?.findViewById(R.id.add_fab)
    }

    private fun configureTabIcons() {
        iconBack!!.setOnClickListener { click: View? ->
            budgetViewModel!!.isEditMode.postValue(false)
            switchColorsForEditMode(false)
        }
        iconTrash!!.setOnClickListener { click: View? ->
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.delete_dialog_title)
                .setPositiveButton(R.string.delete_dialog_yes) { dialog, which ->
                    for (item in budgetViewModel!!.items) {
                        if (item.isSelected) {
                            budgetViewModel!!.removeItem(
                                item,
                                (activity?.application as LoftApp).itemsAPI,
                                requireActivity().getSharedPreferences(
                                    getString(R.string.app_name),
                                    0
                                )
                            )
                        }
                    }
                    budgetViewModel!!.isEditMode.postValue(false)
                    switchColorsForEditMode(false)
                    budgetViewModel!!.updateListFromInternet(
                        (activity?.application as LoftApp).itemsAPI,
                        currentPosition,
                        requireActivity().getSharedPreferences(getString(R.string.app_name), 0)
                    )
                }
                .setNegativeButton(R.string.delete_dialog_no) { dialog: DialogInterface?, which: Int -> }
                .show()
        }
    }

    private fun switchColorsForEditMode(isEditMode: Boolean) {
        val window = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (isEditMode) {
            toolbar!!.setBackgroundColor(resources.getColor(R.color.selection_tab_color))
            tabLayout!!.setBackgroundColor(resources.getColor(R.color.selection_tab_color))
            window?.statusBarColor = resources.getColor(R.color.selection_tab_color)
            tabLayout!!.setTabTextColors(
                resources.getColor(R.color.white),
                resources.getColor(R.color.selection_text_color)
            )
            iconBack!!.visibility = View.VISIBLE
            iconTrash!!.visibility = View.VISIBLE
            addFAB!!.visibility = View.GONE
        } else {
            toolbar!!.setBackgroundColor(resources.getColor(R.color.lightish_blue))
            tabLayout!!.setBackgroundColor(resources.getColor(R.color.lightish_blue))
            window?.statusBarColor = resources.getColor(R.color.lightish_blue)
            tabLayout!!.setTabTextColors(
                resources.getColor(R.color.tabs_text),
                resources.getColor(R.color.white)
            )
            toolBarTextView!!.text = resources.getString(R.string.budget_accounting)
            iconBack!!.visibility = View.GONE
            iconTrash!!.visibility = View.GONE
            addFAB!!.visibility = View.VISIBLE
            budgetViewModel?.items?.forEach {
                if (it.isSelected) {
                    it.isSelected = false
                }
            }
        }
    }

    @Preview
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

    @Composable
    fun TransactionList(viewModel: BudgetViewModel = viewModel()) {
        val isRefreshing = viewModel.isRefreshing
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                viewModel.updateListFromInternet(
                    (activity?.application as LoftApp).itemsAPI,
                    currentPosition,
                    activity?.getSharedPreferences(getString(R.string.app_name), 0)!!
                )
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


    companion object {
        fun newInstance(position: Int): BudgetFragment {
            val budgetFragment = BudgetFragment()
            budgetFragment.currentPosition = position
            return budgetFragment
        }
    }
}