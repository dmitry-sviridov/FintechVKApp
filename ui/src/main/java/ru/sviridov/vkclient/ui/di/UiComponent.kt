package ru.sviridov.vkclient.ui.di

import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.feature.newsfeed.domain.FavouriteNewsRepository
import ru.sviridov.vkclient.feature.newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.feature.newsfeed.domain.PostCommentsRepository
import ru.sviridov.vkclient.feature.profile.domain.ProfileScreenRepository
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallRepository
import ru.sviridov.vkclient.ui.presentation.fragments.BottomNavContainerFragment
import ru.sviridov.vkclient.ui.presentation.fragments.comments.PostCommentsFragment
import ru.sviridov.vkclient.ui.presentation.fragments.newsfeed.FeedFragment
import ru.sviridov.vkclient.ui.presentation.fragments.profile.ProfileFragment
import ru.sviridov.vkclient.ui.presentation.fragments.wall.WallFragment

@Component
interface UiComponent {

    fun inject(target: FeedFragment)
    fun inject(target: BottomNavContainerFragment)
    fun inject(target: PostCommentsFragment)
    fun inject(target: ProfileFragment)
    fun inject(target: WallFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance newsFeedRepository: NewsFeedRepository,
            @BindsInstance favouriteNewsRepository: FavouriteNewsRepository,
            @BindsInstance postCommentsRepository: PostCommentsRepository,
            @BindsInstance profileScreenRepository: ProfileScreenRepository,
            @BindsInstance profileWallRepository: ProfileWallRepository
        ): UiComponent
    }

}