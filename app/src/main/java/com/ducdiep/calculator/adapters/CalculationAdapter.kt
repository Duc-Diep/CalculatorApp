package com.ducdiep.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.calculator.R
import com.ducdiep.calculator.model.Calculation

class CalculationAdapter(var context:Context, var listCal:List<Calculation>):RecyclerView.Adapter<CalculationAdapter.CalViewHolder>() {
    var onClick:((Calculation)->Unit)?=null

    fun setOnClickItem(callBack:(Calculation)->Unit){
        onClick = callBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.calculation_item,parent,false)
        return CalViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalViewHolder, position: Int) {
        var cal = listCal[position]
        holder.tvInput.text = "Input: ${cal.input}"
        holder.tvOutput.text = "Output: ${cal.output}"
        holder.itemView.setOnClickListener {
            onClick?.invoke(cal)
        }
    }

    override fun getItemCount(): Int {
        return listCal.size
    }
    class CalViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var tvInput = itemView.findViewById<TextView>(R.id.input)
        var tvOutput = itemView.findViewById<TextView>(R.id.output)
    }
}