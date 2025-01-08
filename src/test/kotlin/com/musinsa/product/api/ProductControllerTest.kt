package com.musinsa.product.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.entity.Product
import com.musinsa.product.domain.repository.BrandRepository
import com.musinsa.product.domain.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var brandRepository: BrandRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    lateinit var brand: Brand

    @BeforeEach
    fun setup() {
        // 브랜드 데이터 초기화
        brand = brandRepository.save(Brand(name = "브랜드 A"))
    }

    @Test
    fun `POST createProduct should return created product`() {
        // Given
        val createDTO =
            ProductResources.CreateDTO(
                name = "상품 A",
                price = BigDecimal(1000),
                brandId = brand.id!!,
            )

        // When & Then
        mockMvc
            .perform(
                post("/v1/musinsa/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createDTO)),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNumber)
    }

    @Test
    fun `PUT updateProduct should return updated product with links`() {
        // Given
        val product =
            productRepository.save(
                Product(
                    name = "상품 A",
                    price = BigDecimal(1000),
                    brand = brand,
                ),
            )
        val updateDTO =
            ProductResources.UpdateDTO(
                id = product.id!!,
                name = "상품 수정 A",
                price = BigDecimal(2000),
            )

        // When & Then
        mockMvc
            .perform(
                put("/v1/musinsa/products/${product.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(product.id!!)) // 상품 ID 확인

        val updatedProduct = productRepository.findById(product.id!!).get()
        assertEquals("상품 수정 A", updatedProduct.name) // 변경된 이름 확인
        assertEquals(BigDecimal(2000), updatedProduct.price) // 변경된 가격 확인
    }

    @Test
    fun `DELETE deleteProduct should return no content`() {
        // Given
        val product =
            productRepository.save(
                Product(
                    name = "상품 A",
                    price = BigDecimal(1000),
                    brand = brand,
                ),
            )
        val deleteDTO = ProductResources.DeleteDTO(id = product.id!!)

        // When & Then
        mockMvc
            .perform(
                delete("/v1/musinsa/products/${product.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(deleteDTO)),
            ).andExpect(status().isNoContent) // HTTP 204 상태 확인
    }

    @Test
    fun `GET findAll should return list of products`() {
        // Given
        val product1 =
            productRepository.save(
                Product(
                    name = "상품 A",
                    price = BigDecimal(1000),
                    brand = brand,
                ),
            )
        val product2 =
            productRepository.save(
                Product(
                    name = "상품 B",
                    price = BigDecimal(2000),
                    brand = brand,
                ),
            )

        // When & Then
        mockMvc
            .perform(
                get("/v1/musinsa/products/"),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(product1.id!!))
            .andExpect(jsonPath("$[1].id").value(product2.id!!))
    }

    @Test
    fun `GET findById should return a product`() {
        // Given
        val product =
            productRepository.save(
                Product(
                    name = "상품 A",
                    price = BigDecimal(1000),
                    brand = brand,
                ),
            )

        // When & Then
        mockMvc
            .perform(
                get("/v1/musinsa/products/${product.id}"),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(product.id!!))
            .andExpect(jsonPath("$.name").value("상품 A"))
    }
}
