package com.musinsa.product.application

import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import com.musinsa.product.domain.aDummy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class BrandServiceTest {
    private val brandRepository: BrandRepository = mock()
    private val brandService = BrandService(brandRepository)

    @Test
    fun `create save then return brand`() {
        // Given
        val expected = "나이스"
        val request = BrandResources.RequestDTO(name = expected)
        val brand = Brand(name = expected, status = Brand.Status.ON).apply { id = 1L }
        whenever(brandRepository.save(any())).thenReturn(brand)
        whenever(brandRepository.findById(1L)).thenReturn(Optional.of(brand))

        // When
        val result = brandService.create(request)

        // Then
        val actual = brandRepository.findById(result.id!!)
        verify(brandRepository).save(any())
        assertTrue(actual.isPresent)
        assertEquals(expected, actual.get().name)
    }

    @Test
    fun `update brand's name and status`() {
        // Given
        val existingBrand = Brand(id = 1L, name = "나이스", status = Brand.Status.ON)
        val updatedName = "나이키"
        val updatedStatus = "중지"
        val request = BrandResources.RequestDTO(id = 1L, name = updatedName, status = updatedStatus)

        whenever(brandRepository.findById(1L)).thenReturn(Optional.of(existingBrand))
        whenever(brandRepository.save(any())).thenReturn(
            existingBrand.apply {
                name = updatedName
                status = Brand.Status.OFF
            },
        )

        // When
        val updatedBrand = brandService.update(request)

        // Then
        verify(brandRepository).findById(1L)
        verify(brandRepository).save(existingBrand)

        assertEquals(updatedName, updatedBrand.name)
        assertEquals(Brand.Status.OFF, updatedBrand.status)
    }

    @Test
    fun `delete off then save the brand`() {
        // Given
        val expected = Brand.aDummy()
        doNothing().whenever(brandRepository).delete(expected)

        // When
        brandService.delete(expected)

        // Then
        verify(brandRepository).delete(expected)
        assertTrue(expected.isTerminated())
    }

    @Test
    fun `findAll return list of brands`() {
        // Given
        val brands =
            listOf(
                Brand(name = "나이스1", status = Brand.Status.ON).apply {
                    id = 1L
                    createdAt = LocalDateTime.now()
                    modifiedAt = LocalDateTime.now()
                },
                Brand(name = "나이스2", status = Brand.Status.ON).apply {
                    id = 2L
                    createdAt = LocalDateTime.now()
                    modifiedAt = LocalDateTime.now()
                },
            )
        whenever(brandRepository.findAll()).thenReturn(brands)

        // When
        val actual = brandService.findAll()

        // Then
        verify(brandRepository).findAll()
        assertEquals(2, actual.size)
        assertEquals("나이스1", actual[0].name)
        assertEquals("나이스2", actual[1].name)
    }

    @Test
    fun `findById return brand`() {
        // Given
        val expected = "나이스"
        val brand =
            Brand(name = expected, status = Brand.Status.ON).apply {
                id = 1L
                createdAt = LocalDateTime.now()
                modifiedAt = LocalDateTime.now()
            }
        whenever(brandRepository.findById(1L)).thenReturn(Optional.of(brand))

        // When
        val actual = brandService.findById(1L)

        // Then
        verify(brandRepository).findById(1L)
        assertEquals(expected, actual.name)
    }
}
