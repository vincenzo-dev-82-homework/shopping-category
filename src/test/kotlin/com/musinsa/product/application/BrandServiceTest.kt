package com.musinsa.product.application

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import com.musinsa.product.domain.aDummy
import com.musinsa.product.domain.create
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BrandServiceTest {
    private val brandRepository: BrandRepository = mock()
    private val brandService = BrandService(brandRepository)

    @Test
    fun `create save then return brand`() {
        // Given
        val brand = Brand.aDummy()
        whenever(brandRepository.save(brand)).thenReturn(brand)

        // When
        val result = brandService.create(brand)

        // Then
        verify(brandRepository).save(brand)
        assertEquals(brand, result)
    }

    @Test
    fun `update save then return brand`() {
        // Given
        val brand = Brand.aDummy()
        whenever(brandRepository.save(brand)).thenReturn(brand)

        // When
        val result = brandService.update(brand)

        // Then
        verify(brandRepository).save(brand)
        assertEquals(brand, result)
    }

    @Test
    fun `delete off then save the brand`() {
        // Given
        val brand = Brand.aDummy()
        doNothing().whenever(brandRepository).delete(brand)

        // When
        brandService.delete(brand)

        // Then
        verify(brandRepository).delete(brand)
        assertTrue(brand.isOff())
    }

    @Test
    fun `findAll return list of brands`() {
        // Given
        val brands = listOf(Brand.aDummy(), Brand.create(2L, "B"))
        whenever(brandRepository.findAll()).thenReturn(brands)

        // When
        val result = brandService.findAll()

        // Then
        verify(brandRepository).findAll()
        assertEquals(brands, result)
    }
}
