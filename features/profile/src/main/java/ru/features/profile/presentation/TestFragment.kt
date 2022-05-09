package ru.features.profile.presentation

import ru.features.profile.databinding.FragmentTestBinding
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod

class TestFragment : BaseFragment<FragmentTestBinding>() {
    override val layoutInflater: LayoutInflateMethod = FragmentTestBinding::inflate

}