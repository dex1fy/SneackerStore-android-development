/**
 * Этот файл содержит реализацию репозитория для слоя data.
 *
 * Репозиторий получает данные из Supabase, при необходимости преобразует их и отдает в domain в удобном виде.
 */
package com.example.sneakerstoreapp.data.repository

import com.example.sneakerstoreapp.data.model.CategoryDto
import com.example.sneakerstoreapp.data.model.FavouriteDto
import com.example.sneakerstoreapp.data.model.ProductDto
import com.example.sneakerstoreapp.data.remote.SupabaseClientProvider
import com.example.sneakerstoreapp.domain.model.HomeCategory
import com.example.sneakerstoreapp.domain.model.HomeData
import com.example.sneakerstoreapp.domain.model.HomeProduct
import com.example.sneakerstoreapp.domain.repository.HomeRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class HomeRepositoryImpl : HomeRepository {

    private val client = SupabaseClientProvider.client

    override suspend fun getHomeData(): HomeData {
        // Если пользователь вошел, позже подтянем его ids избранного.
        val userId = client.auth.currentUserOrNull()?.id
        // Загружаем категории для верхнего списка на главном экране.
        val categories = client
            .from("categories")
            .select()
            .decodeList<CategoryDto>()

        // Картинки и ids избранного загружаем отдельно, чтобы потом быстро собрать карточки.
        val imageMap = loadImageMap()
        val favoriteIds = loadFavoriteIds(userId)

        // После этого получаем сами товары.
        val products = client
            .from("products")
            .select()
            .decodeList<ProductDto>()

        return HomeData(
            categories = categories.map { category ->
                HomeCategory(
                    id = category.id,
                    title = category.title
                )
            },
            products = products.map { product ->
                HomeProduct(
                    id = product.id,
                    title = product.title,
                    price = product.cost,
                    isBestSeller = product.isBestSeller,
                    categoryId = product.categoryId,
                    imageUrl = imageMap[product.id],
                    isFavorite = favoriteIds.contains(product.id)
                )
            }
        )
    }

    private suspend fun loadFavoriteIds(userId: String?): Set<String> {
        // Если пользователь не авторизован, его избранное пока пустое.
        if (userId.isNullOrBlank()) return emptySet()

        return client
            .from("favourite")
            .select()
            .decodeList<FavouriteDto>()
            .filter { it.userId == userId }
            .mapNotNull { it.productId }
            .toSet()
    }

    private suspend fun loadImageMap(): Map<String, String> {
        // В storage лежат файлы вида productId-что-то.png.
        val files = runCatching {
            client.storage
                .from("Products")
                .list("")
        }.getOrDefault(emptyList())

        return files.mapNotNull { file ->
            // Отбрасываем хвост после последнего дефиса, чтобы получить id товара.
            val productId = file.name.substringBeforeLast("-")

            if (productId.isBlank() || productId == file.name) {
                null
            } else {
                // Для найденного файла сразу строим публичный URL.
                productId to client.storage.from("Products").publicUrl(file.name)
            }
        }.toMap()
    }
}
