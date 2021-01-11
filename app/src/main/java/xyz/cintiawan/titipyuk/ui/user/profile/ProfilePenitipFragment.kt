package xyz.cintiawan.titipyuk.ui.user.profile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_profile_penitip.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.model.Menu
import xyz.cintiawan.titipyuk.ui.list.WishlistActivity
import xyz.cintiawan.titipyuk.ui.request.list.RequestVerticalListActivity
import xyz.cintiawan.titipyuk.ui.titipan.list.TitipanUserListActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfilePenitipFragment : BaseFragment() {
    // Adapter
    private lateinit var adapter: ProfileMenuAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile_penitip, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val menus = mutableListOf<Menu>()
        menus.add(Menu(ContextCompat.getDrawable(context!!, R.drawable.ic_saya_fill), "Titipan Saya", TitipanUserListActivity::class.java))
        menus.add(Menu(ContextCompat.getDrawable(context!!, R.drawable.ic_handshake), "Request Saya", RequestVerticalListActivity::class.java))
        menus.add(Menu(ContextCompat.getDrawable(context!!, R.drawable.ic_love_fill), "Wishlist Saya", WishlistActivity::class.java))
        menus.add(Menu(ContextCompat.getDrawable(context!!, R.drawable.ic_info), "Bantuan", TitipanUserListActivity::class.java))

        adapter = ProfileMenuAdapter(context as Context, menus)

        list_menu.adapter = adapter
        list_menu.onItemClickListener = AdapterView.OnItemClickListener { adapter, _, position, _ ->
            startActivity(Intent(context, (adapter.getItemAtPosition(position) as Menu).startActivity))
        }
    }

    override fun toString(): String = "Penitip"

}
