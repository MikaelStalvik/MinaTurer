package com.imploded.minaturer.fragments

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imploded.minaturer.R
import com.imploded.minaturer.adapters.LandingPageAdapter
import com.imploded.minaturer.interfaces.OnFragmentInteractionListener
import com.imploded.minaturer.viewmodel.LandingViewModel


class LandingPageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LandingPageAdapter
    private val viewModel: LandingViewModel = LandingViewModel()
    private val p = Paint()

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.my_stops)
        var view = inflater!!.inflate(R.layout.fragment_landing_page, container, false)
        /*
        var button = view.findViewById<Button>(R.id.addStopsButton)
        button.setOnClickListener {
            if (mListener != null) {
                mListener!!.onFindStopsSelected(ArgChangeToFindStopsView)
            }
        }*/
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            if (mListener != null) {
                mListener!!.onFindStopsSelected(ArgChangeToFindStopsView)
            }
        }

        adapter = createAdapter()
        recyclerView = view.findViewById(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
        initSwipe()
        updateAdapter()

        return view
    }

    private fun initSwipe() {
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.removeItem(position)
                    }
                }
                Log.d("SWIPED", "SWIPED: " + direction)
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
        /*
        val simpleTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.removeItem(position)
                    }
                }
                Log.d("SWIPED", "SWIPED: " + direction)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3

                    if (dX > 0) {
                        p.color = Color.parseColor("#388E3C")
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_edit_white)
                        val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)
                    } else {
                        p.color = Color.parseColor("#D32F2F")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)

                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete_white)
                        val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, icon_dest, p)

                        p.color = Color.parseColor("#ffffff")
                        p.textSize = 50f
                        c.drawText("DELETE", itemView.right.toFloat() - 2 * width - 100, itemView.top.toFloat() + 40f, p)
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        */
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

    companion object {

        val ArgChangeToFindStopsView = 1

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): LandingPageFragment {
            val fragment = LandingPageFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
