/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.nimbus.messaging

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

/**
 * Bookkeeping for message actions in terms of Glean messages and the messaging store.
 */
class NimbusMessagingController(
    private val messagingStorage: NimbusMessagingStorage,
    private val deepLinkScheme: String = "",
    private val now: () -> Long = { System.currentTimeMillis() },
) {
    /**
     * Called when a message is just about to be shown to the user.
     *
     * Update the display count, time shown and boot identifier metadata for the given [message].
     */
    fun updateMessageAsDisplayed(message: Message, bootIdentifier: String? = null): Message {
        val updatedMetadata = message.metadata.copy(
            displayCount = message.metadata.displayCount + 1,
            lastTimeShown = now(),
            latestBootIdentifier = bootIdentifier,
        )
        return message.copy(
            metadata = updatedMetadata,
        )
    }

    /**
     * Records telemetry and metadata for a newly processed displayed message.
     */
    suspend fun onMessageDisplayed(message: Message) {
        sendShownMessageTelemetry(message.id)
        if (message.isExpired) {
            sendExpiredMessageTelemetry(message.id)
        }
        messagingStorage.updateMetadata(message.metadata)
    }

    /**
     * Called when a message has been dismissed by the user.
     *
     * Records a messageDismissed event, and records that the message
     * has been dismissed.
     */
    suspend fun onMessageDismissed(messageMetadata: Message.Metadata) {
        sendDismissedMessageTelemetry(messageMetadata.id)
        val updatedMetadata = messageMetadata.copy(dismissed = true)
        messagingStorage.updateMetadata(updatedMetadata)
    }

    /**
     * Called once the user has clicked on a message.
     *
     * This records that the message has been clicked on, but does not record a
     * glean event. That should be done via [processMessageActionToUri].
     */
    suspend fun onMessageClicked(messageMetadata: Message.Metadata) {
        val updatedMetadata = messageMetadata.copy(pressed = true)
        messagingStorage.updateMetadata(updatedMetadata)
    }

    /**
     * Create and return the relevant [Intent] for the given [action].
     *
     * @param action the [Message.action] to create the [Intent] for.
     * @return an [Intent] using the processed [Message.action].
     */
    fun getIntentForMessageAction(action: String): Intent {
        return Intent(Intent.ACTION_VIEW, action.toDeepLinkSchemeUri(deepLinkScheme))
    }

    /**
     * Will attempt to get the [Message] for the given [id].
     *
     * @param id the [Message.id] of the [Message] to try to match.
     * @return the [Message] with a matching [id], or null if no [Message] has a matching [id].
     */
    suspend fun getMessage(id: String): Message? {
        return messagingStorage.getMessages().find { it.id == id }
    }

    /**
     * The [message] action needs to be examined for string substitutions
     * and any `uuid` needs to be recorded in the Glean event.
     *
     * We call this `process` as it has a side effect of logging a Glean event while it
     * creates a URI string for the message action.
     */
    fun processMessageActionToUri(message: Message): Uri {
        val (uuid, action) = messagingStorage.generateUuidAndFormatAction(message.action)
        sendClickedMessageTelemetry(message.id, uuid)

        return action.toDeepLinkSchemeUri(deepLinkScheme)
    }

    private fun sendDismissedMessageTelemetry(messageId: String) {
        println(messageId)
    //TODO find a way to workaround this dependency to Fenix
    //Messaging.messageDismissed.record(Messaging.MessageDismissedExtra(messageId))
    }

    private fun sendShownMessageTelemetry(messageId: String) {
        println(messageId)
        //TODO find a way to workaround this dependency to Fenix
//        Messaging.messageShown.record(Messaging.MessageShownExtra(messageId))
    }

    private fun sendExpiredMessageTelemetry(messageId: String) {
        println(messageId)
        //TODO find a way to workaround this dependency to Fenix
//        Messaging.messageExpired.record(Messaging.MessageExpiredExtra(messageId))
    }

    private fun sendClickedMessageTelemetry(messageId: String, uuid: String?) {
        println(messageId)
        println(uuid)
        //TODO find a way to workaround this dependency to Fenix
/*        Messaging.messageClicked.record(
            Messaging.MessageClickedExtra(messageKey = messageId, actionUuid = uuid),
        )*/
    }
}

private fun String.toDeepLinkSchemeUri(deepLinkScheme: String): Uri {
    val actionWithDeepLinkScheme = if (startsWith("http", ignoreCase = true)) {
        "$deepLinkScheme://open?url=${Uri.encode(this)}"
    } else if (startsWith("://")) {
        "$deepLinkScheme$this"
    } else {
        this
    }

    return actionWithDeepLinkScheme.toUri()
}
