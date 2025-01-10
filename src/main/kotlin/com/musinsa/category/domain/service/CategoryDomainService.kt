package com.musinsa.category.domain.service

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CategoryDomainService(
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
) {
    fun getLowestPriceForCategory(categoryId: Long): CategoryResources.CategoryProductDTO? {
        val lowestProduct = categoryProductRepository.findLowestPriceByCategories(categoryId).firstOrNull()
        return lowestProduct?.let {
            CategoryResources.CategoryProductDTO(
                categoryName = it.category.name,
                brandName = it.product.brand.name,
                price = it.product.price,
            )
        }
    }

    fun getLowestAndHighestPricesForCategory(categoryId: Long): Pair<CategoryResources.PriceDetailDTO?, CategoryResources.PriceDetailDTO?> {
        val lowestProduct = categoryProductRepository.findLowestPriceByCategoryId(categoryId)
        val highestProduct = categoryProductRepository.findHighestPriceByCategoryId(categoryId)

        val lowestPrice =
            lowestProduct?.let {
                CategoryResources.PriceDetailDTO(
                    brandName = it["brandName"] as String,
                    price = it["productPrice"] as BigDecimal,
                )
            }
        val highestPrice =
            highestProduct?.let {
                CategoryResources.PriceDetailDTO(
                    brandName = it["brandName"] as String,
                    price = it["productPrice"] as BigDecimal,
                )
            }

        return Pair(lowestPrice, highestPrice)
    }

    fun findCategoryByName(categoryName: String): Category =
        categoryRepository.findByName(categoryName)
            ?: throw IllegalArgumentException("Category $categoryName not found")

    fun getCategoryPricesForBrand(brandId: Long): List<CategoryResources.CategoryPriceDTO> {
        val categories = categoryRepository.findAll()
        return categories.mapNotNull { category ->
            val lowestProduct = categoryProductRepository.findLowestPriceByBrandAndCategory(brandId, category.id!!)
            lowestProduct?.let {
                CategoryResources.CategoryPriceDTO(
                    categoryName = category.name,
                    price = it.product.price,
                )
            }
        }
    }

    fun countCategories(): Long = categoryRepository.count()
}
