package xyz.cintiawan.titipyuk.ui.user.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_profile_menu.view.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.model.Menu

class ProfileMenuAdapter(context: Context, menus: List<Menu>) : ArrayAdapter<Menu>(context, R.layout.item_profile_menu, R.id.txt_menu, menus) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var v = convertView
        if(v == null) v = LayoutInflater.from(context).inflate(R.layout.item_profile_menu, null)

        v?.img_menu?.setImageDrawable(getItem(position)?.image)
        v?.txt_menu?.text = getItem(position)?.title

        return v
    }

}