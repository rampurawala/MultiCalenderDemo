package com.app.multicalenderdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.multicalenderdemo.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonHorizontal.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_HorizontalCalendarFragment)
        }
        binding.buttonSingle.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SingleCalendarFragment)
        }
        binding.buttonMultiple.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_MultipleCalendarFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}