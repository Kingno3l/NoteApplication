package android.example.noteapplication

import android.example.noteapplication.adapter.NotesAdapter
import android.example.noteapplication.database.NotesDatabase
import android.example.noteapplication.databinding.FragmentHomeBinding
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the NotesAdapter
        notesAdapter = NotesAdapter()

        // Set up the RecyclerView
        binding?.apply {
            // Set up the RecyclerView with StaggeredGridLayoutManager
            binding?.recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            recyclerView.adapter = notesAdapter
        }

        // Load the notes from the database and update the adapter
        loadNotesFromDatabase()

        binding?.fabBtnCreateNote?.setOnClickListener {
            replaceFragment(CreateNoteFragment.newInstance(), true)
        }
    }

    private fun loadNotesFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            val noteDao = NotesDatabase.getDatabase(requireContext()).noteDao()

            // Observe the database changes using Flow and update the adapter
            noteDao.allNotes.collect { notesList ->
                notesAdapter.arrList = ArrayList(notesList)
                notesAdapter.notifyDataSetChanged()
            }
        }
    }

    fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransition.add(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
