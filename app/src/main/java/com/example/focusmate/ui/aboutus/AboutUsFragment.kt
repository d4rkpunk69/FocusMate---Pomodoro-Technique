package com.example.focusmate.ui.aboutus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.focusmate.R
import com.example.focusmate.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var emailButton: Button
    private lateinit var dev1Image: ImageView
    private lateinit var dev2Image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[AboutUsViewModel::class.java]

        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        emailButton = binding.emailButton
        dev1Image = binding.dev1Image
        dev2Image = binding.dev2Image

        // Set up click listeners with animation for dev1Image
        dev1Image.setOnClickListener {
            // Perform animation
            it.animate()
                .translationY(10f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .translationY(0f)
                        .setDuration(100)
                        .start()
                }
                .start()

            // Open Facebook profile after animation
            it.postDelayed({
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100080313262620"))
                startActivity(intent)
            }, 200) // Delay to let the animation finish
        }

        // Set up click listeners with animation for dev2Image
        dev2Image.setOnClickListener {
            // Perform animation
            it.animate()
                .translationY(10f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .translationY(0f)
                        .setDuration(100)
                        .start()
                }
                .start()

            // Open Facebook profile after animation
            it.postDelayed({
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/GeraldJay6/"))
                startActivity(intent)
            }, 200) // Delay to let the animation finish
        }

        // Set up long click listeners to show dialog with details for dev1Image
        dev1Image.setOnLongClickListener {
            val dialog = DeveloperDetailsDialogFragment.newInstance(
                R.raw.developer_justin,
                getString(R.string.dev1),
                getString(R.string.dev1_address)
            )
            dialog.show(parentFragmentManager, "DeveloperDetailsDialog")
            true
        }

        // Set up long click listeners to show dialog with details for dev2Image
        dev2Image.setOnLongClickListener {
            val dialog = DeveloperDetailsDialogFragment.newInstance(
                R.raw.dev2_2,
                getString(R.string.dev2),
                getString(R.string.dev2_address)
            )
            dialog.show(parentFragmentManager, "DeveloperDetailsDialog")
            true
        }

        // Set up click listener with animation for emailButton
        emailButton.setOnClickListener {
            // Perform animation
            it.animate()
                .translationX(1000f) // Fly off to the right
                .setDuration(300)
                .withEndAction {
                    // Open email intent after animation
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("jose.tuling@bisu.edu.ph"))
                        putExtra(Intent.EXTRA_SUBJECT, "Rate Us")
                        putExtra(Intent.EXTRA_TEXT, "I love your app!")
                    }
                    startActivity(emailIntent)
                    // Reset button position
                    it.translationX = 0f
                }
                .start()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
