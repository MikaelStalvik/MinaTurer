package com.imploded.minaturer.fragments

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.LandingPageAdapter
import com.imploded.minaturer.interfaces.LandingViewModelInterface
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.interfaces.SettingsInterface
import com.imploded.minaturer.utils.*
import org.jetbrains.anko.support.v4.alert
import kotlinx.android.synthetic.main.fragment_landing_page.*
import javax.inject.Inject


class LandingPageFragment : Fragment() {

    @Inject lateinit var viewModel: LandingViewModelInterface
    @Inject lateinit var appSettings: SettingsInterface

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LandingPageAdapter

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.title(getString(R.string.my_stops))
        this.hideBackNavigation()
        val view = inflater.inflate(R.layout.fragment_landing_page, container, false)
        this.inject()
        showHint()
        setHasOptionsMenu(false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            if (mListener != null) {
                mListener!!.onFindStopsSelected(ArgChangeToFindStopsView)
            }
        }

        adapter = createAdapter()
        recyclerView = recyclerViewStops
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        initSwipe()
        updateAdapter()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun initSwipe() {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        mListener?.sendFirebaseEvent(FirebaseConstants.StopRemoved)
                        viewModel.removeStop(position)
                        adapter.removeItem(position)
                    }
                }
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                         viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                         actionState: Int, isCurrentlyActive: Boolean) {
                val foregroundView = (viewHolder as LandingPageAdapter.UiStopHolder).viewForeground()
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                val foregroundView = (viewHolder as LandingPageAdapter.UiStopHolder).viewForeground()
                getDefaultUIUtil().clearView(foregroundView)
            }
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                     actionState: Int, isCurrentlyActive: Boolean) {
                val foregroundView = (viewHolder as LandingPageAdapter.UiStopHolder).viewForeground()

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun updateAdapter() {
        adapter.updateItems { viewModel.selectedStops }
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)
    }

    private fun createAdapter(): LandingPageAdapter {
        viewModel.getStops()
        return LandingPageAdapter({ mListener?.onStopSelected(it)})
    }

    private fun showHint() {
        val settings =  appSettings.loadSettings()
        if (settings.LandingHintPageShown) return
        alert(getString(R.string.landing_page_hint), getString(R.string.tip)) {
            positiveButton(getString(R.string.got_it)) {
                settings.LandingHintPageShown = true
                appSettings.saveSettings(settings)
            }
        }.show()
    }

    companion object {
        const val ArgChangeToFindStopsView = 1
        fun newInstance(): LandingPageFragment {
            return LandingPageFragment()
        }
    }
}
