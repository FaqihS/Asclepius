package com.dicoding.asclepius.view.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.util.ViewModelFactory
import com.dicoding.asclepius.view.adapter.NewsListAdapter

class NewsFragment : Fragment(), NewsListAdapter.NewsListener {

   
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.articleList.observe(viewLifecycleOwner) { articlesItemList ->
            articlesItemList?.let {
                setNews(it)
            }
        }
    }

    private fun setNews(listArticle: List<ArticlesItem?>) {
        val filteredNews = listArticle.filterNot { it!!.title == "[Removed]" }
        val newsLayout = LinearLayoutManager(requireActivity())
        val newsAdapter = NewsListAdapter(filteredNews,this)
        binding.rvNews.apply {
            layoutManager = newsLayout
            adapter = newsAdapter
        }
        if (listArticle.isEmpty()){
            binding.tvNotFound.visibility = View.VISIBLE
        }

        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(uri: Uri) {
        startActivity(Intent(
            Intent.ACTION_VIEW,
            uri
        ))
    }
}