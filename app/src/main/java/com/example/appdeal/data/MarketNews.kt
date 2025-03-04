package com.example.appdeal.data

data class MarketNews(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val market: String
)

object SampleMarketNews {
    val news = listOf(
        MarketNews(
            id = "1",
            title = "Whole Foods Weekly Specials",
            description = "20% off on all organic produce! Plus, Prime members get an additional 10% off. Valid through this weekend.",
            date = "March 15, 2024",
            market = "Whole Foods"
        ),
        MarketNews(
            id = "2",
            title = "ACME Super Savings",
            description = "Buy one, get one free on select dairy products. Don't miss our meat department specials!",
            date = "March 14, 2024",
            market = "ACME"
        ),
        MarketNews(
            id = "3",
            title = "Giant Food Deals",
            description = "Save big on fresh produce! Up to 25% off on fruits and vegetables this week.",
            date = "March 14, 2024",
            market = "Giant"
        ),
        MarketNews(
            id = "4",
            title = "ShopRite Weekend Sale",
            description = "Special discounts on household essentials. Get $10 off when you spend $100 or more!",
            date = "March 13, 2024",
            market = "ShopRite"
        ),
        MarketNews(
            id = "5",
            title = "Grand Opening: Armanch Market",
            description = "Visit our new store in Philadelphia! Opening specials on fresh meat, produce, and dairy products.",
            date = "March 15, 2024",
            market = "Armanch"
        )
    )
} 