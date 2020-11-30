package ru.sviridov.vkclient.ui.di

import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.feature_newsfeed.domain.FavouriteNewsRepository
import ru.sviridov.vkclient.feature_newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragment
import ru.sviridov.vkclient.ui.presentation.fragments.NewsFeedGroupFragment

@Component
interface UiComponent {

    fun inject(target: FeedFragment)
    fun inject(target: NewsFeedGroupFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance newsFeedRepository: NewsFeedRepository,
            @BindsInstance favouriteNewsRepository: FavouriteNewsRepository
        ): UiComponent
    }

}