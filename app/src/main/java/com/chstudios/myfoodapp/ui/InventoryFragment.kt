package com.chstudios.myfoodapp.ui

import android.R.attr.startYear
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.chstudios.myfoodapp.R
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodPersistence
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.util.Status
import com.chstudios.myfoodapp.util.adapters.FoodAdapter
import com.chstudios.myfoodapp.util.adapters.ImageAdapter
import com.chstudios.myfoodapp.viewmodel.InventoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class InventoryFragment : DialogFragment(R.layout.fragment_inventory), DatePickerDialog.OnDateSetListener{

    lateinit var viewModel: InventoryViewModel
    lateinit var scanCode: String
    lateinit var listName: String
    val foodList = mutableListOf<Food>()
    val foodAdapter = FoodAdapter()
    lateinit var nutritionIxResponse: NutritionIxResponse
    lateinit var builder: AlertDialog.Builder
    lateinit var goodTill: Date
    lateinit var datePickerDialog: DatePickerDialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null)
            listName = requireArguments().getString("list_name", "Hello World")

        builder = AlertDialog.Builder(requireContext())

        val cal = Calendar.getInstance()
        goodTill = cal.time

        datePickerDialog = DatePickerDialog(
            requireContext(), this, cal.time.year, cal.time.month, cal.time.day
        )

        list_title.text = listName

        viewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

        rv_list_foods.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_list_foods.adapter = foodAdapter

        nutritionIxResponse = NutritionIxResponse(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            0.0,
            0,
            0.0,
            "",
            "",
            "",
            0.0,
            "",
            "",
            0,
            "",
            "",
            0.0,
            "",
            "",
            0.0,
            0,
            0,
            0,
            0.0,
            "",
            0,
            0,
            "",
            "",
            "",
            ""
        )


        subscribeToObservers()
        addClickListeners()
        addSwipeToDelete()
        addNavigateBack()

    }

    fun addNavigateBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    fun addClickListeners() {
        inventory_fab.setOnClickListener {
            scanBarCode()
            (requireActivity() as MainActivity).scanCode.observe(this, {
                if (it != "") {
                    scanCode = it
                    val args = Bundle()
                    this.arguments = args
                    args.putString("upc", scanCode)
                    viewModel.addFood(scanCode, listName, goodTill)
                }
            })
        }
    }

    fun addSwipeToDelete() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, LEFT or RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.layoutPosition
                val item = foodAdapter.foods[pos]
                viewModel?.deleteFood(item, listName)
                if (foodList.contains(item))
                    foodList.remove(item)
                foodAdapter.setFoods(foodList)
                Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.insertToDb1(item)
                            }
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_list_foods)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun subscribeToObservers() {
        if(!viewModel.foodInventory.hasObservers()){
            viewModel.foodInventory.observe(viewLifecycleOwner) {
                for (food in it) {
                    if (!foodList.contains(food))
                        foodList.add(food)
                }
                foodAdapter.setFoods(foodList)
            }
        }

        if(!viewModel.foodResponse.hasObservers()){
            viewModel.foodResponse.observe(viewLifecycleOwner) {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            val apiRepsonse = result

                            datePickerDialog.show()
                            Toast.makeText(requireContext(), "good until $goodTill", Toast.LENGTH_SHORT).show()

                            val food = Food(
                                apiRepsonse!!.data!!.barCode,
                                apiRepsonse.data!!.title,
                                apiRepsonse.data.imgpath,
                                listName,
                                apiRepsonse.data.cmnName,
                                apiRepsonse.data.description,
                                apiRepsonse.data.tags,
                                apiRepsonse.data.rating,
                                apiRepsonse.data.numOfServings,
                                apiRepsonse.data.cals,
                                apiRepsonse.data.carbos,
                                apiRepsonse.data.fats,
                                apiRepsonse.data.calc,
                                apiRepsonse.data.fE,
                                apiRepsonse.data.proteen,
                                apiRepsonse.data.servSize,
                                goodTill.toString()
                            )
                            viewModel.insertToDb1(food)

                            val foodPers = FoodPersistence(
                                apiRepsonse!!.data!!.barCode,
                                apiRepsonse.data!!.title,
                                apiRepsonse.data.imgpath,
                                listName,
                                apiRepsonse.data.cmnName,
                                apiRepsonse.data.description,
                                apiRepsonse.data.tags,
                                apiRepsonse.data.rating,
                                apiRepsonse.data.numOfServings,
                                apiRepsonse.data.cals,
                                apiRepsonse.data.carbos,
                                apiRepsonse.data.fats,
                                apiRepsonse.data.calc,
                                apiRepsonse.data.fE,
                                apiRepsonse.data.proteen,
                                apiRepsonse.data.servSize,
                                goodTill.toString()
                            )
                            viewModel.insertToDb2(foodPers)
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            viewModel.getFoodFromNutritionIx(scanCode)
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        if(!viewModel.nutritionResponse.hasObservers()){
            viewModel.nutritionResponse.observe(viewLifecycleOwner) {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            nutritionIxResponse = result.data!!
                            viewModel.getImagesFromApi(
                                result.data!!.brandName + " " + result.data.itemName,
                                30
                            )
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            createScanErrorDialog()
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }


        if(!viewModel.imageResponse.hasObservers()){
            viewModel.imageResponse.observe(viewLifecycleOwner) {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            val imgList = mutableListOf<String>()
                            val flagList = mutableListOf<Boolean>()
                            for (product in result.data!!.products) {
                                imgList.add(product.image)
                                flagList.add(false)
                            }
                            createNutritionIxImageDialog(
                                builder,
                                imgList,
                                flagList,
                                nutritionIxResponse
                            )
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            createScanErrorDialog()
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNutritionIxImageDialog(
        builder: AlertDialog.Builder,
        imgList: List<String>,
        flagList: MutableList<Boolean>,
        nutritionIxResponse: NutritionIxResponse
    ) {
        val rv = RecyclerView(requireContext())
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        val imageAdapter = ImageAdapter()
        rv.adapter = imageAdapter

        Log.d("imageList", imgList.toString())
        imageAdapter.setListNames(imgList, flagList)


        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        layout.addView(rv)
        builder.apply {
            setTitle("We're having trouble finding your product. Are any of these it?")
            setView(layout)
            setPositiveButton("Yes, This One") { dialog, which ->
                var selectedImage = ""
                for (i in imageAdapter.images.indices) {
                    if (imageAdapter.flags[i])
                        selectedImage = imageAdapter.images[i]
                }
                Log.d("NutritionIx Response", nutritionIxResponse.toString())
                viewModel.insertToDb1(
                    Food(
                        scanCode,
                        nutritionIxResponse.brandName + " - " + nutritionIxResponse.itemName,
                        selectedImage,
                        listName,
                        "",
                        nutritionIxResponse.itemDescription ?: "",
                        "",
                        0,
                        nutritionIxResponse.nfServingsPerContainer ?: 0.0,
                        nutritionIxResponse.nfCalories ?: 0,
                        nutritionIxResponse.nfTotalCarbohydrate.toString(),
                        nutritionIxResponse.nfTotalFat.toString(),
                        (nutritionIxResponse.nfCalciumDv ?: 0.0),
                        (nutritionIxResponse.nfIronDv ?: 0.0),
                        (nutritionIxResponse.nfProtein ?: 0).toString(),
                        (nutritionIxResponse.nfServingSizeQty
                            ?: 0.0).toString() + nutritionIxResponse.nfServingSizeUnit,
                        goodTill.toString()
                    )
                )
                viewModel.insertToDb2(
                    FoodPersistence(
                        scanCode,
                        nutritionIxResponse.brandName + " - " + nutritionIxResponse.itemName,
                        selectedImage,
                        listName,
                        "",
                        nutritionIxResponse.itemDescription ?: "",
                        "",
                        0,
                        nutritionIxResponse.nfServingsPerContainer ?: 0.0,
                        nutritionIxResponse.nfCalories ?: 0,
                        nutritionIxResponse.nfTotalCarbohydrate.toString(),
                        nutritionIxResponse.nfTotalFat.toString(),
                        nutritionIxResponse.nfCalciumDv ?: 0.0,
                        nutritionIxResponse.nfIronDv ?: 0.0,
                        (nutritionIxResponse.nfProtein ?: 0).toString(),
                        (nutritionIxResponse.nfServingSizeQty
                            ?: 0.0).toString() + nutritionIxResponse.nfServingSizeUnit,
                        goodTill.toString()
                    )
                )
                dialog.dismiss()

            }
            setNegativeButton("No, Manual Entry"){ dialog, which ->
                dialog.dismiss()
                createScanErrorDialog()
            }
        }
        val dialog = builder.create()
        if(!dialog.isShowing)
            dialog.show()

    }

    fun createImageDialog(
        foodName: String,
        foodServings: Double,
        foodCalories: Int,
        foodFat: Int
    ) {

        val searchImagesET = EditText(requireContext())
        searchImagesET.hint = "Enter search word then press search"

        val searchButton = Button(requireContext())
        searchButton.text = "Search"

        val rv = RecyclerView(requireContext())
        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        val imageAdapter = ImageAdapter()
        rv.adapter = imageAdapter

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        layout.addView(searchImagesET)
        layout.addView(searchButton)
        layout.addView(rv)
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Search an image for your food entry")
            setView(layout)
            setPositiveButton("Add Image") { dialog, which ->
                var selectedImage = ""
                for (i in imageAdapter.images.indices) {
                    if (imageAdapter.flags[i])
                        selectedImage = imageAdapter.images[i]
                }
                viewModel.insertToDb1(
                    Food(
                        scanCode,
                        foodName,
                        selectedImage,
                        listName,
                        "",
                        "",
                        "",
                        0,
                        foodServings,
                        foodCalories,
                        "",
                        foodFat.toString(),
                        0.toDouble(),
                        0.toDouble(),
                        "",
                        "",
                        goodTill.toString()
                    )
                )
                viewModel.insertToDb2(
                    FoodPersistence(
                        scanCode,
                        foodName,
                        selectedImage,
                        listName,
                        "",
                        "",
                        "",
                        0,
                        foodServings,
                        foodCalories,
                        "",
                        foodFat.toString(),
                        0.toDouble(),
                        0.toDouble(),
                        "",
                        "",
                        goodTill.toString()
                    )
                )
            }
        }
        val dialog = builder.create()
        dialog.show()

        searchButton.setOnClickListener {
            viewModel.getImagesFromApi(searchImagesET.text.toString(), 12)
            viewModel.imageResponse.observe(viewLifecycleOwner, {
                it?.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            val imageList = mutableListOf<String>()
                            val flagList = mutableListOf<Boolean>()
                            for (product in result.data!!.products) {
                                imageList.add(product.image)
                                flagList.add(false)
                            }
                            imageAdapter.setListNames(imageList, flagList)
                        }
                    }
                }
            })
        }
    }


    fun createScanErrorDialog() {

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val foodName = EditText(context)
        foodName.hint = "Title of Food"
        layout.addView(foodName)

        val foodServings = EditText(context)
        foodServings.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        foodServings.hint = "Number of servings"
        layout.addView(foodServings)

        val foodCalories = EditText(context)
        foodCalories.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        foodCalories.hint = "Number of calories"
        layout.addView(foodCalories)

        val foodFat = EditText(context)
        foodFat.setRawInputType(InputType.TYPE_CLASS_NUMBER)
        foodFat.hint = "Number of fats"
        layout.addView(foodFat)


        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Error scanning barcode... Manual entry required.")
            setView(layout)
            setPositiveButton("Add Food") { dialog, which ->
                createImageDialog(
                    foodName.text.toString(),
                    foodServings.text.toString().toDouble(),
                    foodCalories.text.toString().toInt(),
                    foodFat.text.toString().toInt()
                )
                dialog.dismiss()
            }
            setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        if (!dialog.isShowing)
            dialog.show()
    }

    fun scanBarCode() {
        val integrator = IntentIntegrator(requireActivity())
        integrator.apply {
            setOrientationLocked(true)
            setBeepEnabled(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scanning Code")
            captureActivity = ScannerActivity::class.java
            initiateScan()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        goodTill = Date(year, month, dayOfMonth)
    }

}