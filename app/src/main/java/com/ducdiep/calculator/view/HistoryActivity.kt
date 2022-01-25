package com.ducdiep.calculator.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.calculator.R
import com.ducdiep.calculator.adapters.CalculationAdapter
import com.ducdiep.calculator.model.Calculation
import com.ducdiep.calculator.sqlite.SQLHelper
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    lateinit var sqlHelper: SQLHelper
    lateinit var listCal: List<Calculation>
    lateinit var calAdapter: CalculationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar?.hide()
        sqlHelper = SQLHelper(this)
        listCal = sqlHelper.getAllCalculation()
        calAdapter = CalculationAdapter(this, listCal.reversed())
        Log.d("list", "onCreate: $listCal, $calAdapter")
        var divider = DividerItemDecoration(this, RecyclerView.VERTICAL)
        rcv_cal.adapter = calAdapter
        rcv_cal.addItemDecoration(divider)

    }
}