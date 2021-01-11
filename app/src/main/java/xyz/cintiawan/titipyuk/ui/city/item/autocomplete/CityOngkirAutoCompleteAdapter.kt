package xyz.cintiawan.titipyuk.ui.city.item.autocomplete

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import kotlinx.android.synthetic.main.item_city_ongkir_autocomplete.view.*
import xyz.cintiawan.titipyuk.databinding.ItemCityOngkirAutocompleteBinding
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.ui.city.item.ItemCityOngkirAutocompleteViewModel

class CityOngkirAutoCompleteAdapter(context: Context, private val layout: Int) : ArrayAdapter<CityOngkirItem>(context, layout) {
    private val data = mutableListOf<CityOngkirItem>()
    private val tempData = mutableListOf<CityOngkirItem>()
    private val suggestions = mutableListOf<CityOngkirItem>()

//    private val viewModel = ItemCityOngkirAutocompleteViewModel()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val binding: ItemCityOngkirAutocompleteBinding
//        val r: View
//        if(convertView == null) {
//            binding = ItemCityOngkirAutocompleteBinding.inflate(LayoutInflater.from(context), parent, false)
//            r = binding.root
//        } else {
//            binding = convertView.tag as ItemCityOngkirAutocompleteBinding
//            r = convertView
//        }
//
//        viewModel.bind(getItem(position))
//        binding.viewModel = viewModel
//        r.tag = binding

        val r: View = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)

        r.txt_city_name.text = getItem(position)?.cityName
        r.txt_city_province.text = getItem(position)?.province

        return r
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    override fun getItem(position: Int): CityOngkirItem? {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    fun setData(data: List<CityOngkirItem>) {
        this.tempData.clear()
        this.tempData.addAll(data)
        notifyDataSetChanged()
    }

    private var customFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as CityOngkirItem).cityName
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            if(!constraint.isNullOrEmpty()) {
                tempData.forEach { if(it.cityName.toLowerCase().contains(constraint.toString().toLowerCase())) suggestions.add(it) }

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
                    (results.values as List<*>).forEach { data.add(it as CityOngkirItem) }
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