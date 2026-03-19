/**
 * Этот файл хранит простые зависимости для экрана или группы экранов.
 *
 * Здесь вручную создаются репозитории и use case, чтобы не усложнять проект лишним DI на старте.
 */
package com.example.sneakerstoreapp.presentation.sidemenu

import com.example.sneakerstoreapp.presentation.profile.ProfileModule

object SideMenuModule {
    val getProfileUseCase = ProfileModule.getProfileUseCase
}
