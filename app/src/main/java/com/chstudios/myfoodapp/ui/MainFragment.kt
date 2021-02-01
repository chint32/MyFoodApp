package com.chstudios.myfoodapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chstudios.myfoodapp.R
import com.chstudios.myfoodapp.data.local.model.Recipe
import com.chstudios.myfoodapp.util.Status
import com.chstudios.myfoodapp.util.adapters.ListNameAdapter
import com.chstudios.myfoodapp.util.adapters.RecipeAdapter
import com.chstudios.myfoodapp.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    lateinit var viewModel: MainViewModel
    val listNames = mutableListOf<String>()
    val listAdapter = ListNameAdapter()
    var recipeList = mutableListOf<Recipe>()
    val recipeAdapter = RecipeAdapter()
    var ingredientsList = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        rv_list_names.layoutManager = LinearLayoutManager(requireContext())
        rv_list_names.adapter = listAdapter

        rv_list_recipes.layoutManager = LinearLayoutManager(requireContext())
        rv_list_recipes.adapter = recipeAdapter

        subscribeToObservers()
        addClickListeners()
        addSwipeToDelete()
        addNavigateBack()
    }

    fun addNavigateBack(){
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    fun addClickListeners(){
        requireView().findViewById<FloatingActionButton>(R.id.main_fab).setOnClickListener{
            createNewListDialog(listNames, listAdapter)
        }

        find_recipes_btn.setOnClickListener{
            viewModel.getRecipesFromApi(ingredientsList)
        }
    }

    fun subscribeToObservers(){
        viewModel.foodLists.observe(requireActivity(), {
            listAdapter.setListNames(it)
        })

        viewModel.foodInventory.observe(requireActivity(), {
            for(food in it){
                ingredientsList += (food.cmnName + ",")
            }
        })
        viewModel.response.observe(requireActivity(), {
            when (it.peekContent().status) {
                Status.SUCCESS -> {
                    for(recipe in it.peekContent().data!!){
                        recipeList.add(Recipe(recipe.title, recipe.image))
                    }
//                                progress.visibility = View.GONE
                    Log.d("observer", "success")
                    recipeAdapter.setListNames(recipeList)
                }
                Status.LOADING -> {
//                                progress.visibility = View.VISIBLE
                    Log.d("observer", "loading")

                }
                Status.ERROR -> {
                    Log.d("observer", "error")
//                                progress.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


    }

    fun addSwipeToDelete(){
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.layoutPosition
                val item = listAdapter.listNames[pos]
                viewModel.deleteList(item)
                if(listNames.contains(item))
                    listNames.remove(item)
                listAdapter.setListNames(listNames)
                Snackbar.make(requireView(), "Successfully deleted list", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.insertList(item)
                            }
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_list_names)
    }

    fun createNewListDialog(listNames: MutableList<String>, adapter: ListNameAdapter){
        val input = EditText(requireContext())
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Make A New List")
            setView(input)
            setPositiveButton("Add List") { dialog, which ->
                viewModel.insertList(input.text.toString())
            }
            setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }
        }
        builder.create().show()
    }

}