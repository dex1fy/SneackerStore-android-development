/**
 * Этот файл отвечает за создание и хранение клиента Supabase.
 *
 * Через этот клиент остальные части слоя data работают с авторизацией, таблицами и storage.
 */
package com.example.sneakerstoreapp.data.remote

import com.example.sneakerstoreapp.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {

    val client by lazy {
        createSupabaseClient(
            // Берем адрес проекта из BuildConfig, чтобы не хранить его прямо в UI.
            supabaseUrl = BuildConfig.SUPABASE_URL,
            // Используем публичный anon key, который допустим для клиентского приложения.
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            // Auth нужен для регистрации, входа и смены пароля.
            install(Auth)
            // Postgrest нужен для чтения и записи таблиц.
            install(Postgrest)
            // Storage нужен для картинок товаров и других файлов.
            install(Storage)
        }
    }
}
