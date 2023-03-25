package com.example.acase.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acase.R
import com.example.acase.adapter.ShoppingAdapter
import com.example.acase.databinding.FragmentShoppingBinding
import com.example.acase.model.ShoppingItems
import com.example.acase.viewmodel.ShoppingViewModel

class ShoppingFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding : FragmentShoppingBinding
    private lateinit var viewModel : ShoppingViewModel
    private lateinit var itemAdapter : ShoppingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        binding = FragmentShoppingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ShoppingViewModel::class.java)
        viewModel.datas()

        itemAdapter = ShoppingAdapter(ArrayList(), viewModel)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = itemAdapter

        observeData()


        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)






        binding.delete.setOnClickListener {
            viewModel.clearItems()
            itemAdapter.clearItems()
        }

        binding.history.setOnClickListener{
            findNavController().navigate(R.id.action_shoppingFragment_to_historyFragment)
        }

        binding.add.setOnClickListener {
            val itemName = binding.edit.text.toString()

            if (itemName.isNotBlank()) {
                viewModel.addItem(ShoppingItems(itemName, 1))
                binding.edit.text.clear()
            }
        }
        return binding.root
    }





    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner, Observer { things ->
            things?.let {
                binding.recyclerView.visibility = View.VISIBLE
                itemAdapter.updateList(things)

                if (things.isNotEmpty()) {
                    binding.delete.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                } else {
                    binding.delete.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                }
            }

        })
        viewModel.firstError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) {
                    binding.errorText.visibility = View.VISIBLE
                } else {
                    binding.errorText.visibility = View.GONE
                }
            }
        })
    }
}