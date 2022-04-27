package hr.algebra.cryptonews.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import hr.algebra.cryptonews.R
import hr.algebra.cryptonews.databinding.FragmentAboutBinding
import hr.algebra.cryptonews.framework.copyToClipboard
import hr.algebra.cryptonews.framework.startIntent

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAboutBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {

        binding.cvDiscord.setOnClickListener {
            requireContext().copyToClipboard("discord_handle", getString(R.string.discordHandle))
            Toast.makeText(context, "Copied to clipboard.", Toast.LENGTH_SHORT).show()
        }

        binding.cvMail.setOnClickListener {
            requireContext().startIntent(
                Intent.ACTION_SENDTO,
                "mailto:" + getString(R.string.authorMail)
            )
        }

        binding.cvGitHub.setOnClickListener {
            requireContext().startIntent(
                Intent.ACTION_VIEW,
                getString(R.string.githubURL)
            )
        }

        binding.cvLinkedin.setOnClickListener {
            requireContext().startIntent(
                Intent.ACTION_VIEW,
                getString(R.string.linkedinURL)
            )
        }
    }

}