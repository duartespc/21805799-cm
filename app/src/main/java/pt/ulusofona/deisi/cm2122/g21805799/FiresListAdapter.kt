package pt.ulusofona.deisi.cm2122.g21805799

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2122.g21805799.databinding.ItemFireBinding

class FiresListAdapter(
    private var items: List<FireUi> = listOf(),
    private val onClick: (FireUi) -> Unit,
) : RecyclerView.Adapter<FiresListAdapter.FiresListViewHolder>() {

    class FiresListViewHolder(val binding: ItemFireBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiresListViewHolder {
        return FiresListViewHolder(ItemFireBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FiresListViewHolder, position: Int) {
        holder.binding.district.text = items[position].district
        holder.binding.concelho.text = items[position].concelho
        holder.binding.freguesia.text = items[position].freguesia
        holder.binding.state.text = items[position].status
        holder.binding.date.text = items[position].date
        holder.binding.hourItem.text = items[position].hour
        holder.binding.location?.text = items[position].location
        holder.binding.created?.text = items[position].created
        holder.binding.updated?.text = items[position].updated

        holder.itemView.setOnClickListener { onClick(items[position]) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<FireUi>) {
        this.items = items
        notifyDataSetChanged()
    }

}
