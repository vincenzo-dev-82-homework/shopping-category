package com.musinsa.product.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.application.BrandService
import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BrandControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var brandService: BrandService

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @Test
    fun `POST createBrand should return created brand`() {
        // Given
        val request = BrandResources.RequestDTO(name = "나이스")
        val requestJson = objectMapper.writeValueAsString(request)

        // When & Then
        mockMvc
            .perform(
                post("/v1/musinsa/brands")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNumber)
            .andExpect(jsonPath("$._links.self.href").exists())
            .andExpect(jsonPath("$._links['[GET]브랜드 목록 조회 API'].href").exists())
            .andExpect(jsonPath("$._links['[GET]브랜드 단일 조회 API'].href").exists())
            .andReturn()
    }

    @Test
    fun `PUT updateBrand should return updated brand`() {
        // Given: Brand 생성
        val brand =
            brandRepository.save(
                Brand(
                    name = "나이스",
                    status = Brand.Status.ON,
                ),
            )
        val updateDTO =
            BrandResources.UpdateDTO(
                id = brand.id!!,
                name = "나이키",
                status = "중지",
            )
        val updateJson = objectMapper.writeValueAsString(updateDTO)

        // When
        mockMvc
            .perform(
                put("/v1/musinsa/brands/${brand.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(brand.id))

        // Then verify
        val updatedBrand = brandRepository.findById(brand.id!!).get()
        assertEquals("나이키", updatedBrand.name) // 변경된 이름 확인
        assertEquals(Brand.Status.OFF, updatedBrand.status) // 변경된 상태 확인
    }

    @Test
    fun `DELETE deleteBrand should return no content`() {
        // Given: Brand 생성
        val brand =
            brandRepository.save(
                Brand(
                    name = "나이스",
                    status = Brand.Status.ON,
                ),
            )

        // When
        mockMvc
            .perform(
                delete("/v1/musinsa/brands/${brand.id}")
                    .contentType(MediaType.APPLICATION_JSON),
            ).andExpect(status().isNoContent)

        // Then verify
        val deletedBrand = brandRepository.findById(brand.id!!)
        assertTrue(deletedBrand.isPresent) // 브랜드가 여전히 존재하는지 확인
        assertEquals(Brand.Status.TERMINATED, deletedBrand.get().status) // 상태가 TERMINATED인지 확인
    }

    @Test
    fun `GET findBrandById should return a brand`() {
        // Given
        val createdBrand = brandService.create(BrandResources.RequestDTO(name = "나이스"))

        // When & Then
        mockMvc
            .perform(
                get("/v1/musinsa/brands/{brandId}", createdBrand.id)
                    .accept(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(createdBrand.id))
            .andExpect(jsonPath("$.name").value("나이스"))
            .andExpect(jsonPath("$.status").value("ON"))
    }

    @Test
    fun `GET findAll should return a list of brands`() {
        // Given
        brandService.create(BrandResources.RequestDTO(name = "나이스1"))
        brandService.create(BrandResources.RequestDTO(name = "나이스2"))

        // When & Then
        mockMvc
            .perform(
                get("/v1/musinsa/brands/")
                    .accept(MediaType.APPLICATION_JSON),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("나이스1"))
            .andExpect(jsonPath("$[1].name").value("나이스2"))
    }
}
