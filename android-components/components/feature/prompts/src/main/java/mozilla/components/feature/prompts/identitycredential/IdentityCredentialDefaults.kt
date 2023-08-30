/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.feature.prompts.identitycredential

import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Contains the default values used by the IdentityCredential dialogs.
 */
object IdentityCredentialDefaults {

    /**
     * Creates an [IdentityCredentialColors] that represents the default colors used in an
     * IdentityCredential dialog.
     *
     * @param background The background of the AwesomeBar.
     * @param title The text color for the title of a suggestion.
     * @param description The text color for the description of a suggestion.
     */
    @Composable
    fun colors(
        title: Color = MaterialTheme.colors.onBackground,
        description: Color = MaterialTheme.colors.onBackground.copy(
            alpha = ContentAlpha.medium,
        ),
        ripple: Color = Color.Cyan,
    ) = IdentityCredentialColors(
        title,
        description,
        ripple,
    )

    fun provider() = IdentityCredentialColorsProvider { colors() }
}
