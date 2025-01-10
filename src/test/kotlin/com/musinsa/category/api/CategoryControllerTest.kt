package com.musinsa.category.api

import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.entity.Product
import com.musinsa.product.domain.repository.BrandRepository
import com.musinsa.product.domain.repository.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryControllerTest
    @Autowired
    constructor(
        private val mockMvc: MockMvc,
        private val categoryRepository: CategoryRepository,
        private val categoryProductRepository: CategoryProductRepository,
        private val brandRepository: BrandRepository,
        private val productRepository: ProductRepository,
    ) {
        @BeforeEach
        fun setup() {
            // Parent categories
            val categoryTop = categoryRepository.save(Category.create(displayName = "상의"))
            val categoryOuter = categoryRepository.save(Category.create(displayName = "아우터"))

            // Brands
            val brandA = brandRepository.save(Brand(name = "Brand A", status = Brand.Status.ON))
            val brandB = brandRepository.save(Brand(name = "Brand B", status = Brand.Status.ON))

            // Products
            val productA1 = productRepository.save(Product(name = "Product A1", price = BigDecimal(10000), brand = brandA))
            val productB1 = productRepository.save(Product(name = "Product B1", price = BigDecimal(11000), brand = brandB))

            // Category-Product relationships
            categoryProductRepository.save(CategoryProduct(category = categoryTop, product = productA1))
            categoryProductRepository.save(CategoryProduct(category = categoryOuter, product = productB1))
        }

        @Test
        fun `GET lowest-price should return lowest price for all categories`() {
            mockMvc
                .get("/v1/musinsa/categories/lowest-price")
                .andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.categories[0].categoryName").value("상의")
                        jsonPath("$.categories[0].brandName").value("Brand A")
                        jsonPath("$.categories[0].price").value(10000)
                        jsonPath("$.totalPrice").value(42000) // 예시 값
                    }
                }
        }

        @Test
        fun `GET prices should return lowest and highest price for a category`() {
            val categoryName = "상의"
            mockMvc
                .get("/v1/musinsa/categories/$categoryName/prices") {
                }.andExpect {
                    status { isOk() }
                    content {
                        jsonPath("$.categoryName").value(categoryName)
                        jsonPath("$.lowestPrice[0].brandName").value("Brand A")
                        jsonPath("$.lowestPrice[0].price").value(10000)
                        jsonPath("$.highestPrice[0].brandName").value("Brand A")
                        jsonPath("$.highestPrice[0].price").value(12000)
                    }
                }
        }
    }
