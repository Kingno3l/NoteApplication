package android.example.noteapplication

import android.example.noteapplication.database.NotesDatabase
import android.example.noteapplication.databinding.FragmentCreateNoteBinding
import android.example.noteapplication.databinding.FragmentHomeBinding
import android.example.noteapplication.entities.Notes
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : BaseFragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding
    private lateinit var tvDateTime: TextView
    private lateinit var etNoteTitle: TextView
    private lateinit var etNoteSubTitle: TextView
    private lateinit var etNoteDesc: TextView
    private lateinit var notes: Note
    var currentDate:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding?.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding?.tvDateTime?.text = currentDate // Use binding to access the TextView

        etNoteTitle = binding?.etNoteTitle ?: return
        etNoteSubTitle = binding?.etNoteSubTitle ?: return
        etNoteDesc = binding?.etNoteDesc ?: return

        binding?.imgDone?.setOnClickListener{
            saveNote()
        }

        binding?.imgBack?.setOnClickListener {
            replaceFragment(HomeFragment.newInstance(), false)
        }
    }

    private fun saveNote() {

        if (etNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context, "Title Required", Toast.LENGTH_SHORT).show()
        }

        if (etNoteSubTitle.text.isNullOrEmpty()){
            Toast.makeText(context, "Sub Title Required", Toast.LENGTH_SHORT).show()
        }

        if (etNoteDesc.text.isNullOrEmpty()){
            Toast.makeText(context, "Note Desc Required", Toast.LENGTH_SHORT).show()
        }

        lifecycleScope.launch {
            val notes = Notes()
            notes.title = etNoteTitle.text.toString()
            notes.subTitle = etNoteSubTitle.text.toString()
            notes.noteText = etNoteDesc.text.toString()
            notes.dateTime = currentDate
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                etNoteTitle.setText("")
                etNoteSubTitle.setText("")
                etNoteDesc.setText("")
            }
        }
    }


    private fun replaceFragment(fragment: Fragment, isTransition: Boolean) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (isTransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransition.add(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()
    }
}