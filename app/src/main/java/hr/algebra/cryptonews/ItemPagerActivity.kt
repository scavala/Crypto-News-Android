package hr.algebra.cryptonews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.cryptonews.adapter.ItemPagerAdapter
import hr.algebra.cryptonews.databinding.ActivityItemPagerBinding
import hr.algebra.cryptonews.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ITEM_POSITION = "hr.algebra.cryptonews.item_position"
const val ITEMS_FAVORITE = "hr.algebra.cryptonews.items_favorite"

class ItemPagerActivity : AppCompatActivity() {
    private var itemPosition = 0
    private var onlyFavorite = false
    private lateinit var items: MutableList<Item>
    private lateinit var binding: ActivityItemPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPager()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initPager() {
        itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        onlyFavorite = intent.getBooleanExtra(ITEMS_FAVORITE, false)

        GlobalScope.launch(Dispatchers.Main) {
            items = withContext(Dispatchers.IO) {
                (applicationContext as App).getNewsDao().getAll()
            }

            binding.viewPager.adapter = ItemPagerAdapter(this@ItemPagerActivity, items)
            binding.viewPager.currentItem = itemPosition
        }
    }
}