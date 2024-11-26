package com.example.myapplication

import android.app.AlertDialog
import android.content.Context

class HelpManager(private val context: Context) {

    fun showHelpDialog(onHelpTopicSelected: (Int) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Help")

        // Create a list of help topics
        val helpTopics = arrayOf("FAQs", "User  Guide", "Contact Support", "Version Info", "Feedback")

        builder.setItems(helpTopics) { dialog, which ->
            onHelpTopicSelected(which) // Call the callback with the selected index
        }

        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    fun showFAQs() {
        val faqMessage = "1. How do I use the calculator?\n" +
                "2. What operations can I perform?\n" +
                "3. How do I clear the calculator?\n" +
                "4. What happens if I divide by zero?"
        showAlertDialog("Frequently Asked Questions", faqMessage)
    }


    fun contactSupport() {
        val supportMessage = "For support, please contact us at:\n" +
                "support@example.com"
        showAlertDialog("Contact Support", supportMessage)
    }

    fun showVersionInfo() {
        val versionInfoMessage = "Calculator App Version: 1.0.0\n" +
                "Release Date: October 2023"
        showAlertDialog("Version Info", versionInfoMessage)
    }

    fun giveFeedback() {
        val feedbackMessage = "We value your feedback! Please send your comments to:\n" +
                "feedback@example.com"
        showAlertDialog("Feedback", feedbackMessage)
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}