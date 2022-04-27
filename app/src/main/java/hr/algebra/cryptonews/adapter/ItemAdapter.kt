package hr.algebra.cryptonews.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.cryptonews.App
import hr.algebra.cryptonews.ITEM_POSITION
import hr.algebra.cryptonews.ItemPagerActivity
import hr.algebra.cryptonews.R
import hr.algebra.cryptonews.framework.startActivity
import hr.algebra.cryptonews.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ItemAdapter(private val context: Context, private val items: MutableList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        private val lCard = itemView.findViewById<LinearLayout>(R.id.cardItem)
        fun bind(item: Item) {

            lCard.setBackgroundResource(if (item.favorite) R.color.dark_yellow else R.color.black)

            tvItem.text = item.title
            Picasso.get().load(File(item.picturePath)).error(R.drawable.bitcoin)
                .transform(RoundedCornersTransformation(50, 50)).into(ivItem)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent,
            false
        )
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(ITEM_POSITION, position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(R.string.delete)
                setMessage(context.getString(R.string.sure) + " '${items[position].title}'?")
                setIcon(R.drawable.ic_delete)
                setCancelable(true)
                setNegativeButton(R.string.cancel, null)
                setPositiveButton(context.getString(R.string.ok)) { _, _ -> deleteItem(position) }
                show()

            }
            true
        }
        holder.bind(items[position])
    }

    private fun deleteItem(position: Int) {

        val item = items[position]
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                (context.applicationContext as App).getNewsDao().delete(item)
            }
            File(item.picturePath).delete()
            items.removeAt(position)
            notifyDataSetChanged()

        }
        /*
        context.contentResolver.delete(
            ContentUris.withAppendedId(
                CRYPTONEWS_PROVIDER_URI,
                item._id!!
            ), null, null
        )
               //sqllite way
         */


    }

    override fun getItemCount() = items.size
}