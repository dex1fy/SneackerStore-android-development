/**
 * Этот файл содержит реализацию репозитория для слоя data.
 *
 * Репозиторий получает данные из Supabase, при необходимости преобразует их и отдает в domain в удобном виде.
 */
package com.example.sneakerstoreapp.data.repository

import com.example.sneakerstoreapp.data.model.FavouriteDto
import com.example.sneakerstoreapp.data.model.FavouriteInsertDto
import com.example.sneakerstoreapp.data.model.ProductDto
import com.example.sneakerstoreapp.data.remote.SupabaseClientProvider
import com.example.sneakerstoreapp.domain.model.FavoriteData
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.domain.repository.FavoriteRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import com.example.sneakerstoreapp.presentation.favorite.FavoriteSyncManager

class FavoriteRepositoryImpl : FavoriteRepository {

    private val client = SupabaseClientProvider.client

    override suspend fun getFavoriteData(): FavoriteData {
        // Избранное строится только для текущего пользователя.
        val userId = client.auth.currentUserOrNull()?.id ?: return FavoriteData(emptyList())

        val favoriteProductIds = loadFavoriteIds(userId)
        if (favoriteProductIds.isEmpty()) {
            return FavoriteData(emptyList())
        }

        // Картинки подгружаем отдельно, как и на главном экране.
        val imageMap = loadImageMap()

        val products = client
            .from("products")
            .select()
            .decodeList<ProductDto>()
            .filter { favoriteProductIds.contains(it.id) }

        return FavoriteData(
            products = products.map { product ->
                HomeProduct(
                    id = product.id,
                    title = product.title,
                    price = product.cost,
                    isBestSeller = product.isBestSeller,
                    categoryId = product.categoryId,
                    imageUrl = imageMap[product.id],
                    isFavorite = true
                )
            }
        )
    }

    override suspend fun toggleFavorite(productId: String): Boolean {
        val userId = client.auth.currentUserOrNull()?.id ?: return false
        // Сначала ищем, есть ли уже такая запись в favourite.
        val existingFavorite = client
            .from("favourite")
            .select()
            .decodeList<FavouriteDto>()
            .firstOrNull { it.userId == userId && it.productId == productId }

        return if (existingFavorite != null) {
            // Если запись уже есть, удаляем ее.
            client.from("favourite").delete {
                filter {
                    eq("id", existingFavorite.id)
                }
            }
            FavoriteSyncManager.notifyFavoriteChanged(productId, false)
            false
        } else {
            // Если записи нет, создаем ее и отмечаем товар избранным.
            client.from("favourite").insert(
                FavouriteInsertDto(
                    productId = productId,
                    userId = userId
                )
            )
            FavoriteSyncManager.notifyFavoriteChanged(productId, true)
            true
        }
    }

    private suspend fun loadFavoriteIds(userId: String): Set<String> {
        return client
            .from("favourite")
            .select()
            .decodeList<FavouriteDto>()
            .filter { it.userId == userId }
            .mapNotNull { it.productId }
            .toSet()
    }

    private suspend fun loadImageMap(): Map<String, String> {
        val files = runCatching {
            client.storage
                .from("Products")
                .list("")
        }.getOrDefault(emptyList())

        return files.mapNotNull { file ->
            val productId = file.name.substringBeforeLast("-")

            if (productId.isBlank() || productId == file.name) {
                null
            } else {
                productId to client.storage.from("Products").publicUrl(file.name)
            }
        }.toMap()
    }
}
