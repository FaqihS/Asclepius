package com.dicoding.asclepius.view.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.util.ViewModelFactory
import com.dicoding.asclepius.view.adapter.HistoryListAdapter

class HistoryFragment : Fragment(), HistoryListAdapter.DeleteListener {


    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHistories().observe(viewLifecycleOwner) {
            if (it != null) {
                showHistoryList(it)
            }
        }
    }

    private fun showHistoryList(historyList: List<History>) {
        val rvLayoutManager = LinearLayoutManager(requireActivity())
        val rvAdapter = HistoryListAdapter(historyList, this)

        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = rvLayoutManager
            adapter = rvAdapter
        }
        if (historyList.isEmpty()){
            binding.tvNotFound.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteHistory(history: History) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete") { _, _ ->
                try {
                    viewModel.delete(history)
                } catch (_: Exception) {

                }
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}