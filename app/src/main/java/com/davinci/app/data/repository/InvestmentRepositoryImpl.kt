package com.davinci.app.data.repository

import com.davinci.app.data.local.dao.PriceDao
import com.davinci.app.data.local.entity.PriceEntity
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.InvestmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestmentRepositoryImpl @Inject constructor(
    private val priceDao: PriceDao,
    // TODO: Add Ktor client for metals API
) : InvestmentRepository {

    override fun getPrices(): Flow<List<InvestmentPrice>> {
        return priceDao.getAllPrices().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHistory(metal: Metal, range: String): Result<List<PriceHistoryPoint>> {
        // TODO: Fetch from metals API
        // For now, return sample data
        return Result.success(
            generateSampleHistory(range)
        )
    }

    override suspend fun getNews(): Result<List<NewsItem>> {
        // TODO: Fetch from NewsAPI / RSS feeds
        // For now, return sample data
        return Result.success(
            listOf(
                NewsItem(
                    id = "1",
                    source = "Financial Times",
                    headline = "Central banks signal continued bullion purchases through Q4",
                    summary = "Institutional demand remains strong as reserve diversification strategies continue to favor physical...",
                    url = "",
                    publishedAt = "2H AGO",
                ),
                NewsItem(
                    id = "2",
                    source = "Reuters",
                    headline = "Silver industrial demand forecasts revised upward for EV sector",
                    summary = "New manufacturing data suggests solar and electric vehicle production will consume record levels of silver...",
                    url = "",
                    publishedAt = "5H AGO",
                ),
                NewsItem(
                    id = "3",
                    source = "Bloomberg",
                    headline = "Yields stabilize, providing breathing room for precious metals",
                    summary = "Treasury yields paused their upward march on Tuesday, allowing non-yielding assets to regain some...",
                    url = "",
                    publishedAt = "1D AGO",
                ),
            )
        )
    }

    override suspend fun refreshPrices(): Result<Unit> {
        return try {
            // TODO: Fetch from metals API and cache
            // Sample data insertion for development
            priceDao.insertPrices(
                listOf(
                    PriceEntity(
                        metal = "gold",
                        priceUsd = 1984.50,
                        change24h = 1.2,
                        direction = "up",
                    ),
                    PriceEntity(
                        metal = "silver",
                        priceUsd = 22.84,
                        change24h = -0.4,
                        direction = "down",
                    ),
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateSampleHistory(range: String): List<PriceHistoryPoint> {
        val days = when (range) {
            "1W" -> 7
            "1M" -> 30
            "1Y" -> 365
            "ALL" -> 730
            else -> 30
        }
        return (0 until days).map { i ->
            PriceHistoryPoint(
                date = "Day $i",
                price = 1950.0 + (Math.random() * 80 - 20),
            )
        }
    }
}
