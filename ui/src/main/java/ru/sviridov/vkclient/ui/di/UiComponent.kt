package ru.sviridov.vkclient.ui.di

import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.feature_newsfeed.domain.FavouriteNewsRepository
import ru.sviridov.vkclient.feature_newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.ui.presentation.fragments.BottomNavContainerFragment
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragment

@Component
interface UiComponent {

    fun inject(target: FeedFragment)
    fun inject(target: BottomNavContainerFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance newsFeedRepository: NewsFeedRepository,
            @BindsInstance favouriteNewsRepository: FavouriteNewsRepository
        ): UiComponent
    }

}