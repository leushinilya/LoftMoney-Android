package ru.leushinilya.loftmoney.screens.main.budget

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.cells.ItemAdapterClick
import ru.leushinilya.loftmoney.cells.ItemsAdapter

class BudgetFragment : Fragment() {

    private var itemsView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currentPosition = 0
    private var budgetViewModel: BudgetViewModel? = null
    private val itemsAdapter = ItemsAdapter()
    private var toolbar: MaterialToolbar? = null
    private var tabLayout: TabLayout? = null
    private var iconBack: ImageView? = null
    private var iconTrash: ImageView? = null
    private var toolBarTextView: TextView? = null
    private var addFAB: FloatingActionButton? = null

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_budget, container, false)
//    }

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
        configureRecyclerView()
        configureRefreshLayout()
        configureViewModel()
        configureViews()
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
        budgetViewModel!!.isEditMode.postValue(false)
        switchColorsForEditMode(false)
    }

    private fun configureRecyclerView() {
        itemsView = view?.findViewById(R.id.itemsView)
        itemsView?.setAdapter(itemsAdapter)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL, false
        )
        itemsView?.layoutManager = layoutManager
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        itemsView?.addItemDecoration(divider)
        itemsAdapter.setItemAdapterClick(object : ItemAdapterClick {
            override fun onItemClick(item: Item) {
                if (budgetViewModel!!.isEditMode.value!!) {
                    item.isSelected = !item.isSelected
                    itemsAdapter.updateItem(item)
                    if (checkSelectedCount() > 0) {
                        toolBarTextView!!.text = (
                                resources.getString(R.string.tool_bar_title_selection)
                                        + checkSelectedCount())
                    } else {
                        budgetViewModel!!.isEditMode.postValue(false)
                        switchColorsForEditMode(false)
                    }
                }
            }

            override fun onLongItemClick(item: Item) {
                if (!budgetViewModel!!.isEditMode.value!!) {
                    budgetViewModel!!.isEditMode.postValue(true)
                    switchColorsForEditMode(true)
                    item.isSelected = true
                    itemsAdapter.updateItem(item)
                    toolBarTextView!!.text = (
                            resources.getString(R.string.tool_bar_title_selection)
                                    + checkSelectedCount())
                }
            }
        })
    }

    private fun checkSelectedCount(): Int {
        var selectedCount = 0
//        for (item in budgetViewModel!!.items.value!!) {
//            if (item.isSelected) {
//                selectedCount++
//            }
//        }
        return selectedCount
    }

    private fun configureViewModel() {
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
//        budgetViewModel!!.items.observe(viewLifecycleOwner) {
//            itemsAdapter.setData(items)
//        }
    }

    private fun configureRefreshLayout() {
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.setOnRefreshListener(OnRefreshListener {
            budgetViewModel?.updateListFromInternet(
                (activity?.application as LoftApp).itemsAPI,
                currentPosition,
                activity?.getSharedPreferences(getString(R.string.app_name), 0)!!
            )
            swipeRefreshLayout?.setRefreshing(false)
        })
    }

    private fun configureViews() {
        toolbar = activity?.findViewById(R.id.toolBar)
        tabLayout = activity?.findViewById(R.id.tabs)
        iconBack = activity?.findViewById(R.id.iconBack)
        iconTrash = activity?.findViewById(R.id.iconTrash)
        toolBarTextView = activity?.findViewById(R.id.toolBarTextView)
        addFAB = activity?.findViewById(R.id.add_fab)
    }

    private fun configureTabIcons() {
//        iconBack!!.setOnClickListener { click: View? ->
//            budgetViewModel!!.isEditMode.postValue(false)
//            switchColorsForEditMode(false)
//        }
//        iconTrash!!.setOnClickListener { click: View? ->
//            AlertDialog.Builder(requireActivity())
//                .setTitle(R.string.delete_dialog_title)
//                .setPositiveButton(R.string.delete_dialog_yes) { dialog, which ->
//                    for (item in budgetViewModel!!.items.value!!) {
//                        if (item.isSelected) {
//                            budgetViewModel!!.removeItem(
//                                item,
//                                (activity?.application as LoftApp).itemsAPI,
//                                activity?.getSharedPreferences(getString(R.string.app_name), 0)
//                            )
//                        }
//                    }
//                    budgetViewModel!!.isEditMode.postValue(false)
//                    switchColorsForEditMode(false)
//                    budgetViewModel!!.updateListFromInternet(
//                        (activity?.application as LoftApp).itemsAPI,
//                        currentPosition,
//                        activity?.getSharedPreferences(getString(R.string.app_name), 0)
//                    )
//                }
//                .setNegativeButton(R.string.delete_dialog_no) { dialog: DialogInterface?, which: Int -> }
//                .show()
//        }
    }

    private fun switchColorsForEditMode(isEditMode: Boolean) {
//        val window = activity?.window
//        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (isEditMode) {
//            toolbar!!.setBackgroundColor(resources.getColor(R.color.selection_tab_color))
//            tabLayout!!.setBackgroundColor(resources.getColor(R.color.selection_tab_color))
//            window?.statusBarColor = resources.getColor(R.color.selection_tab_color)
//            tabLayout!!.setTabTextColors(
//                resources.getColor(R.color.white),
//                resources.getColor(R.color.selection_text_color)
//            )
//            iconBack!!.visibility = View.VISIBLE
//            iconTrash!!.visibility = View.VISIBLE
//            addFAB!!.visibility = View.GONE
//        } else {
//            toolbar!!.setBackgroundColor(resources.getColor(R.color.lightish_blue))
//            tabLayout!!.setBackgroundColor(resources.getColor(R.color.lightish_blue))
//            window?.statusBarColor = resources.getColor(R.color.lightish_blue)
//            tabLayout!!.setTabTextColors(
//                resources.getColor(R.color.tabs_text),
//                resources.getColor(R.color.white)
//            )
//            toolBarTextView!!.text = resources.getString(R.string.tool_bar_title)
//            iconBack!!.visibility = View.GONE
//            iconTrash!!.visibility = View.GONE
//            addFAB!!.visibility = View.VISIBLE
//            for (item in budgetViewModel!!.items.value!!) {
//                if (item.isSelected) item.isSelected = false
//                itemsAdapter.updateItem(item)
//            }
//        }
    }

    @Composable
    fun ItemView(item: Item) {
        Row {
            Text(text = item.name)
            Text(text = item.price)
        }
    }

    @Composable
    fun TransactionList(viewModel: BudgetViewModel = viewModel()) {
        val isRefreshing = viewModel.isRefreshing
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