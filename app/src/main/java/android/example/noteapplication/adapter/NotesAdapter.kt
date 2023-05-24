package android.example.noteapplication.adapter

import android.example.noteapplication.R
import android.example.noteapplication.entities.Notes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import javax.xml.transform.ErrorListener

class NotesAdapter(
    var listener: OnItemClickListener? = null,
    var arrList: ArrayList<Notes> = ArrayList<Notes>()
) :
RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(){
  inner class NotesViewHolder(ItemView: View) :RecyclerView.ViewHolder(ItemView){
      val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
      val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
      val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
  }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notes, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.apply {
            val note = arrList[position]
            holder.tvTitle.text = note.title
            holder.tvDesc.text = note.noteText
            holder.tvDateTime.text = note.dateTime
        }
    }


}