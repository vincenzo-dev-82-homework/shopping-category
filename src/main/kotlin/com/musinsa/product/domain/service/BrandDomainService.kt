package com.musinsa.product.domain.service

import com.musinsa.category.domain.service.CategoryDomainService
import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.entity.Brand
import org.springframework.stereotype.Service

@Service
class BrandDomainService(
    private val categoryDomainService: CategoryDomainService,
) {
    fun calculateLowestPriceBrand(brands: List<Brand>): BrandResources.LowestPriceBrandResponse {
        val brandTotals =
            brands.mapNotNull { brand ->
                val categoryPrices = categoryDomainService.getCategoryPricesForBrand(brand.id!!)
                val totalCategoryCount = categoryDomainService.countCategories()

                if (categoryPrices.size.toLong() == totalCategoryCount) {
                    val totalPrice = categoryPrices.sumOf { it.price }
                    BrandResources.BrandTotalPrice(
                        brandName = brand.name,
                        categoryPrices = BrandResources.CategoryPrice.convert(categoryPrices),
                        totalPrice = totalPrice,
                    )
                } else {
                    null
                }
            }

        val lowestPriceBrand =
            brandTotals.minByOrNull { it.totalPrice }
                ?: throw IllegalArgumentException("No brand meets the criteria for lowest price")

        return BrandResources.LowestPriceBrandResponse(
            brandName = lowestPriceBrand.brandName,
            categoryPrices = lowestPriceBrand.categoryPrices,
            totalPrice = lowestPriceBrand.totalPrice,
        )
    }
}
