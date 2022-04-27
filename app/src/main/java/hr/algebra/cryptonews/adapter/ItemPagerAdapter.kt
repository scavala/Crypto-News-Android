package hr.algebra.cryptonews.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.cryptonews.App
import hr.algebra.cryptonews.R
import hr.algebra.cryptonews.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ItemPagerAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivItem: ImageView = itemView.findViewById(R.id.ivItem)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvProvider = itemView.findViewById<TextView>(R.id.tvProvider)


        fun bind(item: Item) {
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvDate.text = item.date
            tvProvider.text = item.provider
            ivShare.setImageResource(R.drawable.ic_share)
            ivFavorite.setImageResource(if (item.favorite) R.drawable.ic_favoriteyellow else R.drawable.ic_notfavorite)

            Picasso.get().load(File(item.picturePath)).error(R.drawable.bitcoin_black)
                .transform(RoundedCornersTransformation(20, 20)).into(ivItem)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_pager,
            parent,
            false
        )

    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.ivFavorite.setOnClickListener {
            item.favorite = !item.favorite

            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    (context.applicationContext as App).getNewsDao().update(item)

                }
                notifyItemChanged(position)
            }
/*
            context.contentResolver.update(
                ContentUris.withAppendedId(CRYPTONEWS_PROVIDER_URI, item._id!!),
                ContentValues().apply { put(Item::favorite.name, item.favorite) },
                null,
                null
            )

//sqllite way
 */

        }

        holder.ivItem.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(item.articleUrl)
                context.startActivity(this)
            }

        }

        holder.ivShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, item.articleUrl)

                context.startActivity(Intent.createChooser(this, "Share article"))
            }
        }

        holder.bind(item)

    }

    override fun getItemCount() = items.size

}