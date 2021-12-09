package com.feylabs.sumbangsih.presentation.ui.home.news

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.FragmentDetailNewsBinding
import com.feylabs.sumbangsih.presentation.CommonControllerActivity
import com.feylabs.sumbangsih.util.BaseFragment
import com.feylabs.sumbangsih.util.ImageView.loadImage


class DetailNewsFragment : BaseFragment() {


    private var _binding: FragmentDetailNewsBinding? = null
    private val binding get() = _binding as FragmentDetailNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideActionBar()
        (getActivity() as CommonControllerActivity).hideCustomTopbar()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val data = arguments?.getParcelable<NewsResponse.NewsResponseItem>("data")
        if (data != null) {

            binding.tvAuthor.text =  "Oleh : " + data.author
            binding.apply {
                tvTitle.text = data.title
                tvDate.text = data.dateIndo
                binding.imgNews.loadImage(data.photoPath, false)

                var text = data?.content.replace("\n", "<br>");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    text =
                        (Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)).toString()
                } else {
                    text = (Html.fromHtml(text)).toString()
                }

                binding.tvContent.text = text
            }
        }
    }


}