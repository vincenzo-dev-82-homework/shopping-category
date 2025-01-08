package com.musinsa.category.application

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.product.domain.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
    private val brandRepository: BrandRepository,
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
                val lowestProducts = categoryProductRepository.findLowestPriceByCategories(category.id!!)

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

    @Transactional(readOnly = true)
    fun getLowestPriceBrand(): CategoryResources.LowestPriceBrandResponse {
        // 모든 브랜드를 조회
        val brands = brandRepository.findAll()

        // 각 브랜드에서 모든 카테고리의 최저 가격 상품을 계산
        val brandTotals =
            brands.mapNotNull { brand ->
                val categoryPrices =
                    categoryRepository.findAll().mapNotNull { category ->
                        val lowestProduct = categoryProductRepository.findLowestPriceByBrandAndCategory(brand.id!!, category.id!!)
                        lowestProduct?.let {
                            CategoryResources.CategoryPriceDTO(
                                categoryName = category.name,
                                price = it.product.price,
                            )
                        }
                    }

                if (categoryPrices.size.toLong() == categoryRepository.count()) {
                    // 모든 카테고리의 상품이 존재하는 브랜드만 포함
                    val totalPrice = categoryPrices.sumOf { it.price }
                    BrandTotalPrice(brandName = brand.name, categoryPrices = categoryPrices, totalPrice = totalPrice)
                } else {
                    null
                }
            }

        // 최저가 브랜드를 선택
        val lowestPriceBrand =
            brandTotals.minByOrNull { it.totalPrice }
                ?: throw IllegalArgumentException("No brand meets the criteria for lowest price")

        // 최종 응답 생성
        return CategoryResources.LowestPriceBrandResponse(
            brandName = lowestPriceBrand.brandName,
            categoryPrices = lowestPriceBrand.categoryPrices,
            totalPrice = lowestPriceBrand.totalPrice,
        )
    }

    data class BrandTotalPrice(
        val brandName: String,
        val categoryPrices: List<CategoryResources.CategoryPriceDTO>,
        val totalPrice: BigDecimal,
    )

//    @Transactional(readOnly = true)
//    fun getCategoryPriceDetails(categoryName: String): CategoryResources.PriceResponse {
//        // 1. 카테고리 조회
//        val category =
//            categoryRepository.findByName(categoryName)
//                ?: throw IllegalArgumentException("Category '$categoryName' not found")
//
//        // 2. 최저 가격 상품
//        val lowestPriceProduct =
//            categoryProductRepository
//                .findLowestPriceByCategoryId(category.id!!)
//                .orElseThrow { throw IllegalArgumentException("No products found for category '${category.name}'") }
//
//        // 3. 최고 가격 상품
//        val highestPriceProduct =
//            categoryProductRepository
//                .findHighestPriceByCategoryId(category.id!!)
//                .orElseThrow { throw IllegalArgumentException("No products found for category '${category.name}'") }
//
//        // 4. 응답 생성
//        return CategoryResources.PriceResponse(
//            categoryName = category.name,
//            lowestPrice =
//                listOf(
//                    CategoryResources.PriceDetailDTO(
//                        brandName = lowestPriceProduct.product.brand.name,
//                        price = lowestPriceProduct.product.price,
//                    ),
//                ),
//            highestPrice =
//                listOf(
//                    CategoryResources.PriceDetailDTO(
//                        brandName = highestPriceProduct.product.brand.name,
//                        price = highestPriceProduct.product.price,
//                    ),
//                ),
//        )
//    }

//    @Transactional(readOnly = true)
//    fun getCategoryPriceDetails(categoryName: String): CategoryResources.PriceResponse {
//        val category =
//            categoryRepository.findByName(categoryName)
//                ?: throw IllegalArgumentException("Category '$categoryName' not found")
//
//        val lowestPriceProduct =
//            categoryProductRepository
//                .findLowestPriceByCategoryId(category.id!!)
//                .orElseThrow { IllegalArgumentException("No products found for category '${category.name}'") }
//
//        val highestPriceProduct =
//            categoryProductRepository
//                .findHighestPriceByCategoryId(category.id!!)
//                .orElseThrow { IllegalArgumentException("No products found for category '${category.name}'") }
//
//        return CategoryResources.PriceResponse(
//            categoryName = category.name,
//            lowestPrice =
//                listOf(
//                    CategoryResources.PriceDetailDTO(
//                        brandName = lowestPriceProduct.product.brand.name,
//                        price = lowestPriceProduct.product.price,
//                    ),
//                ),
//            highestPrice =
//                listOf(
//                    CategoryResources.PriceDetailDTO(
//                        brandName = highestPriceProduct.product.brand.name,
//                        price = highestPriceProduct.product.price,
//                    ),
//                ),
//        )
//    }

    @Transactional(readOnly = true)
    fun getCategoryPriceDetails(categoryName: String): CategoryResources.PriceResponse {
        // 카테고리를 조회
        val category =
            categoryRepository.findByName(categoryName)
                ?: throw IllegalArgumentException("Category '$categoryName' not found")

        // 최저 가격 상품 조회
        val lowestPriceResult =
            categoryProductRepository.findLowestPriceByCategoryId(category.id!!)
                ?: throw IllegalArgumentException("No products found for category '${category.name}'")

        // 최고 가격 상품 조회
        val highestPriceResult =
            categoryProductRepository.findHighestPriceByCategoryId(category.id!!)
                ?: throw IllegalArgumentException("No products found for category '${category.name}'")

        // 결과 매핑
        return CategoryResources.PriceResponse(
            categoryName = category.name,
            lowestPrice =
                listOf(
                    CategoryResources.PriceDetailDTO(
                        brandName = lowestPriceResult["brandName"] as String,
                        price = lowestPriceResult["productPrice"] as BigDecimal,
                    ),
                ),
            highestPrice =
                listOf(
                    CategoryResources.PriceDetailDTO(
                        brandName = highestPriceResult["brandName"] as String,
                        price = highestPriceResult["productPrice"] as BigDecimal,
                    ),
                ),
        )
    }
}
