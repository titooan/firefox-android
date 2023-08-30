/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.feature.prompts.identitycredential

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Represents the colors used by the IdentityCredential dialogs.
 */
data class IdentityCredentialColors(
    val title: Color,
    val description: Color,
    val ripple: Color,
)

/**
 * An [IdentityCredentialColorsProvider] implementation can provide an [IdentityCredentialColors]
 */
fun interface IdentityCredentialColorsProvider {
    @Composable
    fun provideColors(): IdentityCredentialColors
}
