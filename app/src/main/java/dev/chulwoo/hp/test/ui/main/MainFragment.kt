package dev.chulwoo.hp.test.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.chulwoo.hp.test.HardcodedViewModelFactory
import dev.chulwoo.hp.test.R
import dev.chulwoo.hp.test.databinding.MainFragmentBinding
import dev.chulwoo.hp.test.feature.user.presentation.model.UserListState
import dev.chulwoo.hp.test.feature.user.presentation.viewmodel.UserListViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<UserListViewModel>(factoryProducer = {
        HardcodedViewModelFactory(requireContext())
    })
    private val adapter = MainItemAdapter { keyword ->
        keyword.let { viewModel.filter(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState != RecyclerView.SCROLL_STATE_IDLE && isKeyboardShown()) {
                        hideKeyboard()
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        listenState()
        viewModel.load()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun listenState() {
        viewModel.states.asLiveData().observe(viewLifecycleOwner) { state ->
            with(binding) {
                when (state) {
                    is UserListState.Initial -> {
                        recyclerView.isVisible = false
                    }
                    is UserListState.Loading -> {
                        recyclerView.isVisible = false
                        loadingIndicator.show()
                    }
                    is UserListState.Success -> {
                        loadingIndicator.hide()
                        recyclerView.isVisible = true
                        adapter.submitList(state.users)
                    }
                    is UserListState.Failure -> {
                        loadingIndicator.hide()
                        recyclerView.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            R.string.loading_failure,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun isKeyboardShown(): Boolean {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isAcceptingText
    }

    private fun hideKeyboard() {
        try {
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        } catch (e: Exception) {
        }
    }
}
