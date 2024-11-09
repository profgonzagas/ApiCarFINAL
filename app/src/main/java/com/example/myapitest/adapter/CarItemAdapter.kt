package com.example.myapitest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myapitest.R
import com.example.myapitest.model.Car
import com.example.myapitest.ui.CircleTransform
import com.squareup.picasso.Picasso
import com.example.myapitest.ui.loadUrl

//import com.example.myapitest.ui.loadUrl

class CarItemAdapter(
    private val cars: List<Car>,

    private val carClickListener: (Car) -> Unit
) : Adapter<CarItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageUrl)
        val model: TextView = view.findViewById(R.id.model)
        val year: TextView = view.findViewById(R.id.year)
        val license: TextView = view.findViewById(R.id.license)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val car = cars[position]

        holder.itemView.setOnClickListener{
            carClickListener.invoke(car)
        }
        holder.imageView.loadUrl(car.imageUrl)
        holder.model.text = car.name
        holder.year.text = car.year
        holder.license.text = car.license

        Picasso.get().setIndicatorsEnabled(true)
         Picasso.get()
             .load(car.imageUrl)
            .placeholder(R.drawable.ic_download)
             .error(R.drawable.ic_error)
             .transform(CircleTransform())
             .fit()
             .centerCrop()
             .into(holder.imageView, object : com.squareup.picasso.Callback {
                 override fun onSuccess() {
                     Toast.makeText(holder.itemView.context, "Imagem carregada com sucesso", Toast.LENGTH_LONG).show()
                 }

                 override fun onError(e: Exception?) {
                     //Log.e("Picasso", "Failed to load image", e)
                     Toast.makeText(holder.itemView.context, "Falha ao carregar a imagem", Toast.LENGTH_LONG).show()
                 }
             })
        holder.imageView.loadUrl(car.imageUrl)
    }


}