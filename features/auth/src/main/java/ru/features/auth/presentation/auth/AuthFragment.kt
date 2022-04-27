package ru.features.auth.presentation.auth

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import ru.features.auth.Configuration
import ru.features.auth.databinding.FragmentAuthBinding
import ru.features.auth.di.injector
import ru.mvi.core.presentation.BaseFragment
import ru.mvi.core.presentation.LayoutInflateMethod
import ru.mvi.core.utils.collectFlow
import javax.inject.Inject

class AuthFragment : BaseFragment<FragmentAuthBinding>() {
    companion object {
        private const val TAG = "AuthFragment"
        fun newInstance(): AuthFragment = AuthFragment()
    }

    override val layoutInflater: LayoutInflateMethod = FragmentAuthBinding::inflate

    @Inject
    lateinit var viewModelFactory: AuthViewModel.Factory

    val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        injector.inject(this)
        super.onAttach(context)
    }

    override fun configure(binding: FragmentAuthBinding) = with(binding) {
        webview.configure()

        collectFlow(viewModel.sideEffect) { effect ->
            when (effect) {
                is SideEffect.ShowMessage -> showMessage(effect.message)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.configure() {
        clearCache(true)
        clearHistory()
        clearFormData()
        CookieManager.getInstance().apply {
            removeAllCookies(null)
            flush()
        }

        settings.javaScriptEnabled = true

        webViewClient = object : WebViewClient() {
            @Suppress("DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val uri = url.toUri()
                val host = uri.host
                if (host == Configuration.CALLBACK_HOST) {
                    val code = uri.getQueryParameter("code") ?: return false
                    viewModel.authorize(code)
                    return true
                }

                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.showProgress(false)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding?.showProgress(true)
            }
        }

        loadUrl(viewModel.createAuthUrl())
    }

    private fun FragmentAuthBinding.showProgress(isShow: Boolean) {
        progress.isVisible = isShow
        webview.isVisible = !isShow
    }

    private fun showMessage(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}
