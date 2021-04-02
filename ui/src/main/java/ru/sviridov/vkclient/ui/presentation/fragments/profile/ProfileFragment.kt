package ru.sviridov.vkclient.ui.presentation.fragments.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.profile_fragment.*
import ru.sviridov.core.extension.viewModels
import ru.sviridov.vkclient.component.profile.model.ProfileModel
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import ru.sviridov.vkclient.ui.getTimeFromTimestamp
import ru.sviridov.vkclient.ui.presentation.mvi.profile.ProfileViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.profile.ProfileViewState
import ru.sviridov.vkclient.ui.presentation.viewmodel.profile.ProfileViewModel
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : Fragment() {

    @Inject
    internal lateinit var vmProvider: Provider<ProfileViewModel>
    private val profileViewModel: ProfileViewModel by viewModels { vmProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UiComponentInjector.getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            Log.e(TAG, "observing viewstate: $viewState ")
            render(viewState)
        })

        profileViewModel.handleAction(ProfileViewActions.FetchProfileInfo)
    }

    private fun render(state: ProfileViewState) {
        when (state) {
            is ProfileViewState.RenderProfileInfo -> renderProfileModel(state.profileModel)
            is ProfileViewState.RenderError -> renderError()
        }
    }

    private fun renderError() {
        Log.e(TAG, "error state catched!")
        progressBar.visibility = View.INVISIBLE

        profileNameErrorStub.visibility = View.VISIBLE
        profileLoadingErrorStub.visibility = View.VISIBLE
    }

    private fun renderProfileModel(profileModel: ProfileModel) {
        Log.d(TAG, "renderProfileModel: got model")
        progressBar.visibility = View.INVISIBLE

        Glide.with(requireActivity())
            .load(profileModel.photoUrl)
            .into(profileImageView)

        profileName.text = profileModel.userName
        lastSeen.text = profileModel.lastSeen.getTimeFromTimestamp()
        birthdayTextView.text = profileModel.birthdayDate
        followersCount.text = profileModel.followersCount.toString()
        domainTextView.text = profileModel.domain

        userCityAndCountry.text = if (profileModel.cityAndCountry.isNullOrBlank()) {
            getString(R.string.no_data)
        } else {
            profileModel.cityAndCountry
        }

        profile_item_widget_wall.setOnClickListener {
            (requireActivity() as WallFragmentHost).openWallFragment(profileModel.userId)
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"

        fun newInstance() = ProfileFragment()
    }

}