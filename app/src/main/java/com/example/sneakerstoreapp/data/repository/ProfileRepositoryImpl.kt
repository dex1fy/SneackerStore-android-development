/**
 * Этот файл содержит реализацию репозитория для слоя data.
 *
 * Репозиторий получает данные из Supabase, при необходимости преобразует их и отдает в domain в удобном виде.
 */
package com.example.sneakerstoreapp.data.repository

import com.example.sneakerstoreapp.data.model.ProfileDto
import com.example.sneakerstoreapp.data.model.ProfileUpsertDto
import com.example.sneakerstoreapp.data.remote.SupabaseClientProvider
import com.example.sneakerstoreapp.domain.model.ProfileData
import com.example.sneakerstoreapp.domain.repository.ProfileRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class ProfileRepositoryImpl : ProfileRepository {

    private val client = SupabaseClientProvider.client

    override suspend fun getProfile(): ProfileData? {
        // Профиль всегда привязан к авторизованному пользователю.
        val currentUser = client.auth.currentUserOrNull() ?: return null
        val profile = loadProfileDto(currentUser.id)

        return ProfileData(
            userId = currentUser.id,
            photoUrl = profile?.photo,
            firstName = profile?.firstname.orEmpty(),
            lastName = profile?.lastname.orEmpty(),
            address = profile?.address.orEmpty(),
            phone = profile?.phone.orEmpty()
        )
    }

    override suspend fun saveProfile(profile: ProfileData): ProfileData? {
        val currentUser = client.auth.currentUserOrNull() ?: return null
        val existingProfile = loadProfileDto(currentUser.id)
        // Собираем тело запроса в формате таблицы profiles.
        val requestBody = ProfileUpsertDto(
            userId = currentUser.id,
            photo = profile.photoUrl,
            firstname = profile.firstName.trim(),
            lastname = profile.lastName.trim(),
            address = profile.address.trim(),
            phone = profile.phone.trim()
        )

        if (existingProfile == null) {
            // Если профиля еще нет, создаем новую запись.
            client.from("profiles").insert(requestBody)
        } else {
            // Если запись уже существует, обновляем именно ее.
            client.from("profiles").update(requestBody) {
                filter {
                    eq("id", existingProfile.id)
                }
            }
        }

        return getProfile()
    }

    private suspend fun loadProfileDto(userId: String): ProfileDto? {
        return client
            .from("profiles")
            .select()
            // Для одного пользователя ожидаем одну запись профиля.
            .decodeList<ProfileDto>()
            .firstOrNull { it.userId == userId }
    }
}
