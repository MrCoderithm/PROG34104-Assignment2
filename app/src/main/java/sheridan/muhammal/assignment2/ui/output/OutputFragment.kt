package sheridan.muhammal.assignment2.ui.output

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sheridan.muhammal.assignment2.R
import sheridan.muhammal.assignment2.databinding.FragmentOutputBinding
import sheridan.muhammal.assignment2.ui.dialogs.ConfirmationDialog.ConfirmationResult
import sheridan.muhammal.assignment2.ui.dialogs.ConfirmationDialog.Companion.CONFIRMATION_RESULT
import sheridan.muhammal.assignment2.ui.settings.DiceSettings

class OutputFragment : Fragment() {

    companion object{
        const val CONFIRM_DELETE: Int = 1
    }

    private val safeArgs: OutputFragmentArgs by navArgs()

    private val viewModel: OutputViewModel by viewModels {
        OutputViewModelFactory(safeArgs.envelopeId, requireActivity().application)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =
            FragmentOutputBinding.inflate(inflater, container, false)

        // data-bind the viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.backButton.setOnClickListener { showInput() }

        navController = findNavController()

        // make the delete confirmation dialog work
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<ConfirmationResult>(CONFIRMATION_RESULT)
            ?.observe(viewLifecycleOwner) {
                if(it.requestCode == CONFIRM_DELETE && it.resultCode == Activity.RESULT_OK){
                    delete()
                }
            }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_output, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_delete -> {
                val settings = DiceSettings(requireContext())
                if(settings.confirmDelete){
                    val action = OutputFragmentDirections.actionOutputToConfirmation(
                        getString(R.string.confirm_delete_message), CONFIRM_DELETE)
                    navController.navigate(action)
                } else {
                    delete()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun delete(){
        viewModel.delete()
        showInput()
    }

    private fun showInput(){
        val action = OutputFragmentDirections.actionOutputToHistory()
        navController.navigate(action)
    }

}