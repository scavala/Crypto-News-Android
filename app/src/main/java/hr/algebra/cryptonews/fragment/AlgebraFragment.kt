package hr.algebra.cryptonews.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.cryptonews.R
import hr.algebra.cryptonews.databinding.FragmentAlgebraBinding
import hr.algebra.cryptonews.framework.startIntent

class AlgebraFragment : Fragment() {

    private lateinit var binding: FragmentAlgebraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlgebraBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.cvCall.setOnClickListener {

            requireContext().startIntent(
                Intent.ACTION_DIAL,
                "tel:" + getString(R.string.algebraPhone)
            )
        }

        binding.cvLocation.setOnClickListener {

            val gmmIntentUri =
                Uri.parse(
                    "geo:"
                            + getString(R.string.algebraCoordinates) + "?q=" + Uri.encode(
                        getString(
                            R.string.algebraFullName
                        )
                    )
                )
            Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
                startActivity(this)
            }
        }

        binding.cvMail.setOnClickListener {
            requireContext().startIntent(
                Intent.ACTION_SENDTO,
                "mailto:" + getString(R.string.algebraMail)
            )
        }

        binding.cvWeb.setOnClickListener {
            requireContext().startIntent(
                Intent.ACTION_VIEW,
                getString(R.string.algebraURL)
            )
        }
    }

}