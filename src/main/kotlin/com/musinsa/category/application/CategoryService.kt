package com.musinsa.category.application

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
) {
    @Transactional
    fun create(category: Category): Category = categoryRepository.save(category)

    @Transactional(readOnly = true)
    fun getCategoryWithLowestPrice(): List<CategoryResources.LowestPriceResponse> {
        // 모든 카테고리를 조회
        val categories = categoryRepository.findAll()

        // 각 카테고리에서 최저 가격 상품과 해당 브랜드를 찾음
        val categoryProducts =
            categories.mapNotNull { category ->
                val lowestProducts = categoryProductRepository.findLowestPriceByCategoryId(category.id!!)

                // 상품이 존재하면 첫 번째 상품을 선택, 없으면 null 반환
                lowestProducts.firstOrNull()?.let { lowestProduct ->
                    CategoryResources.CategoryProductDTO(
                        categoryName = category.name,
                        brandName = lowestProduct.product.brand.name,
                        price = lowestProduct.product.price,
                    )
                }
            }

        // 총합 계산
        val totalPrice = categoryProducts.sumOf { it.price }

        // 최종 응답 생성
        return listOf(CategoryResources.LowestPriceResponse(categories = categoryProducts, totalPrice = totalPrice))
    }
}
