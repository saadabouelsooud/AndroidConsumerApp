package com.istnetworks.hivesdk.presentation.nps

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.istnetworks.hivesdk.R

class NpsFragmentDirections private constructor() {
    companion object {
        fun actionNpsToEmojie(): NavDirections = ActionOnlyNavDirections(R.id.action_nps_to_emojie)
    }
}
