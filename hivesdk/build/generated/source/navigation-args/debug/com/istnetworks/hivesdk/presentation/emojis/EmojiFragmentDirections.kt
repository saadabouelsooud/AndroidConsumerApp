package com.istnetworks.hivesdk.presentation.emojis

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.istnetworks.hivesdk.R

class EmojiFragmentDirections private constructor() {
    companion object {
        fun actionEmojiToNps(): NavDirections = ActionOnlyNavDirections(R.id.action_emoji_to_nps)
    }
}
