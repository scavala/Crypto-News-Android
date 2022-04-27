package hr.algebra.cryptonews.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.cryptonews.App
import hr.algebra.cryptonews.adapter.ItemAdapter
import hr.algebra.cryptonews.databinding.FragmentItemsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemsFragment : Fragment() {

    private lateinit var binding: FragmentItemsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        GlobalScope.launch(Dispatchers.Main) {
            val items = withContext(Dispatchers.IO) {
                (requireContext().applicationContext as App).getNewsDao().getAll()
            }
            binding.rvItems.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ItemAdapter(context, items)

            }
        }
        super.onResume()
    }

}






