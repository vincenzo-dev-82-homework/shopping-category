package com.musinsa.product.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.musinsa.product.application.BrandService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

    @Test
    fun `POST createBrand should return created brand`() {
        // Given
        val request = BrandResources.RequestDTO(name = "나이스")
        val requestJson = objectMapper.writeValueAsString(request)

        // When
        val result =
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

        println("Response Body: ${result.response.contentAsString}")
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
