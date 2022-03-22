package com.example.okl_app_mvvm.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.okl_app_mvvm.other.Account
import com.example.okl_app_mvvm.R

class MyAdapter(private val userList: ArrayList<Account>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val accountuser : Account = userList[position]
        holder.mail.text = accountuser.mail
        holder.name.text = accountuser.nazwa
        holder.points.text = accountuser.punkty.toString()
        holder.counting.text = position.plus(1).toString()

    }

    override fun getItemCount(): Int {

        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mail : TextView = itemView.findViewById(R.id.mail)
        val name : TextView = itemView.findViewById(R.id.name)
        val points : TextView = itemView.findViewById(R.id.points)
        val counting : TextView = itemView.findViewById(R.id.countingCardView)
    }

}