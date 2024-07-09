package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapItemViewModel = MapItemViewModel(this)

        val mapList = findViewById<RecyclerView>(R.id.mapList)
        val selectList = findViewById<RecyclerView>(R.id.selectList)
        val inputSpace = findViewById<EditText>(R.id.inputSpace)
        val mainText = findViewById<TextView>(R.id.main_text)
        val cancelBtn = findViewById<ImageView>(R.id.cancelBtn)

        val mapListAdapter =
            MapListAdapter(listOf<MapItem>(), LayoutInflater.from(this))
        val selectListAdapter =
            SelectListAdapter(listOf<MapItem>(), LayoutInflater.from(this))

        mapList.adapter = mapListAdapter
        mapList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        selectList.adapter = selectListAdapter
        selectList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mapListAdapter.setItemClickListener(object : MapListAdapter.ItemClickListener {
            override fun onClick(v: View, position: Int) {
                val mapItem = mapListAdapter.mapItemList.get(position)
                mapItemViewModel.insertSelectItem(
                    mapItem.name,
                    mapItem.address,
                    mapItem.category,
                    mapItem.id
                )
            }
        })

        selectListAdapter.setCancelBtnClickListener(object :
            SelectListAdapter.CancelBtnClickListener {
            override fun onClick(v: View, position: Int) {
                val selectItem = selectListAdapter.selectItemList.get(position)
                mapItemViewModel.deleteSelectItem(selectItem.id)
            }
        })

        mapItemViewModel.mapItemList.observe(this) {
            mapListAdapter.updateMapItemList(it)
        }

        mapItemViewModel.selectItemList.observe(this) {
            selectListAdapter.updateMapItemList(it)
        }

        inputSpace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mapItemViewModel.searchMapItem(s.toString())
                if (mapItemViewModel.mapItemList.value == null) {
                    mainText.visibility = View.VISIBLE
                } else {
                    mainText.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        cancelBtn.setOnClickListener {
            inputSpace.setText("")
        }
    }
}


