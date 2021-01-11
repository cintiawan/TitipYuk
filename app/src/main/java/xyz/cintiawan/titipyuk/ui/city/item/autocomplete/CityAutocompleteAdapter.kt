package xyz.cintiawan.titipyuk.ui.city.item.autocomplete

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_city_autocomplete.view.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemCityAutocompleteBinding
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.ui.city.item.ItemCityAutocompleteViewModel

class CityAutocompleteAdapter(context: Context, private val layout: Int) : ArrayAdapter<CityItem>(context, layout) {
    private val data = mutableListOf<CityItem>()
    private val tempData = mutableListOf<CityItem>()
    private val suggestions = mutableListOf<CityItem>()

//    private val viewModel = ItemCityAutocompleteViewModel()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val binding: ItemCityAutocompleteBinding
//        val r: View
//        if(convertView == null) {
//            binding = ItemCityAutocompleteBinding.inflate(LayoutInflater.from(context), parent, false)
//            r = binding.root
//        } else {
//            binding = convertView.tag as ItemCityAutocompleteBinding
//            r = convertView
//        }
//
//        viewModel.bind(getItem(position))
//        binding.viewModel = viewModel
//        r.tag = binding

        val r: View = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)

        r.txt_city_name.text = getItem(position)?.name
        r.txt_city_country.text = getItem(position)?.negara?.name
        if(!getItem(position)?.negara?.flag.isNullOrEmpty())
            Picasso.get().load(getItem(position)?.negara?.flag).placeholder(R.drawable.placeholder).into(r.img_flag)
        else
            r.img_flag.setImageResource(R.drawable.placeholder)

        return r
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    override fun getItem(position: Int): CityItem? {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    fun setData(data: List<CityItem>) {
        this.tempData.clear()
        this.tempData.addAll(data)
        notifyDataSetChanged()
    }

    private var customFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as CityItem).name
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            if(!constraint.isNullOrEmpty()) {
                tempData.forEach {
                    if(it.name.toLowerCase().contains(constraint.toString().toLowerCase())
                    || it.negara.name.toLowerCase().contains(constraint.toString().toLowerCase()))
                        suggestions.add(it)
                }

                val results = FilterResults()
                results.values = suggestions
                results.count = suggestions.size

                return results
            }

            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            try {
                if(results != null && results.count > 0) {
                    data.clear()
                    (results.values as List<*>).forEach { data.add(it as CityItem) }
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            } catch (e: TypeCastException) {
                Log.e("apalah_ini_error", e.message)
            }
        }
    }

}