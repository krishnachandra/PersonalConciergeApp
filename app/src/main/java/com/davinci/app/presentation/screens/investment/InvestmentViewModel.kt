package com.davinci.app.presentation.screens.investment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinci.app.domain.model.*
import com.davinci.app.domain.repository.InvestmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InvestmentUiState(
    val goldPrice: String = "---",
    val goldChange: String = "+0.0% (24h)",
    val goldPositive: Boolean = true,
    val silverPrice: String = "---",
    val silverChange: String = "-0.0% (24h)",
    val silverPositive: Boolean = false,
    val selectedRange: String = "1M",
    val priceHistory: List<PriceHistoryPoint> = emptyList(),
    val news: List<NewsItem> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class InvestmentViewModel @Inject constructor(
    private val investmentRepository: InvestmentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvestmentUiState())
    val uiState: StateFlow<InvestmentUiState> = _uiState.asStateFlow()

    init {
        loadPrices()
        loadHistory("1M")
        loadNews()
    }

    private fun loadPrices() {
        viewModelScope.launch {
            investmentRepository.getPrices().collect { prices ->
                val gold = prices.find { it.metal == Metal.GOLD }
                val silver = prices.find { it.metal == Metal.SILVER }

                _uiState.update {
                    it.copy(
                        goldPrice = gold?.let { p -> "$${String.format("%,.2f", p.priceUsd)}" } ?: "---",
                        goldChange = gold?.let { p ->
                            val sign = if (p.change24h >= 0) "+" else ""
                            "$sign${String.format("%.1f", p.change24h)}% (24h)"
                        } ?: "+0.0% (24h)",
                        goldPositive = (gold?.change24h ?: 0.0) >= 0,
                        silverPrice = silver?.let { p -> "$${String.format("%.2f", p.priceUsd)}" } ?: "---",
                        silverChange = silver?.let { p ->
                            val sign = if (p.change24h >= 0) "+" else ""
                            "$sign${String.format("%.1f", p.change24h)}% (24h)"
                        } ?: "-0.0% (24h)",
                        silverPositive = (silver?.change24h ?: 0.0) >= 0,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun loadHistory(range: String) {
        viewModelScope.launch {
            investmentRepository.getHistory(Metal.GOLD, range)
                .onSuccess { history ->
                    _uiState.update { it.copy(priceHistory = history) }
                }
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            investmentRepository.getNews()
                .onSuccess { news ->
                    _uiState.update { it.copy(news = news) }
                }
        }
    }

    fun selectRange(range: String) {
        _uiState.update { it.copy(selectedRange = range) }
        loadHistory(range)
    }
}
