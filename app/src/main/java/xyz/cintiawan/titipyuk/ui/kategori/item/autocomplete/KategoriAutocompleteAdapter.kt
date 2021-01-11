package xyz.cintiawan.titipyuk.ui.kategori.item.autocomplete

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import kotlinx.android.synthetic.main.item_kategori_autocomplete.view.*
import xyz.cintiawan.titipyuk.databinding.ItemKategoriAutocompleteBinding
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.ui.kategori.item.ItemKategoriAutocompleteViewModel

class KategoriAutocompleteAdapter(context: Context, private val layout: Int) : ArrayAdapter<KategoriItem>(context, layout) {
    private val data = mutableListOf<KategoriItem>()
    private val tempData = mutableListOf<KategoriItem>()
    private val suggestions = mutableListOf<KategoriItem>()

//    private val viewModel = ItemKategoriAutocompleteViewModel()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val binding: ItemKategoriAutocompleteBinding
//        val r: View
//        if(convertView == null) {
//            binding = ItemKategoriAutocompleteBinding.inflate(LayoutInflater.from(context), parent, false)
//            r = binding.root
//        } else {
//            binding = convertView.tag as ItemKategoriAutocompleteBinding
//            r = convertView
//        }
//
//        viewModel.bind(getItem(position))
//        binding.viewModel = viewModel
//        r.tag = binding

        val r: View = convertView ?: LayoutInflater.from(context).inflate(layout, parent, false)

        r.txt_kategori_name.text = getItem(position)?.namaKategori

        return r
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.toLong()
    }

    override fun getItem(position: Int): KategoriItem? {
        return data[position]
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    fun setData(data: List<KategoriItem>) {
        this.tempData.clear()
        this.tempData.addAll(data)
        notifyDataSetChanged()
    }

    private var customFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as KategoriItem).namaKategori
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            if(!constraint.isNullOrEmpty()) {
                tempData.forEach { if(it.namaKategori.toLowerCase().contains(constraint.toString().toLowerCase())) suggestions.add(it) }

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
                    (results.values as List<*>).forEach { data.add(it as KategoriItem) }
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