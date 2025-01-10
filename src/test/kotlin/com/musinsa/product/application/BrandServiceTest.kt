package com.musinsa.product.application

import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.common.exception.BrandNotFoundException
import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.repository.BrandRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class BrandServiceTest {
    private val brandRepository: BrandRepository = mock()
    private val categoryRepository: CategoryRepository = mock()
    private val categoryProductRepository: CategoryProductRepository = mock()
    private val brandService = BrandService(brandRepository, categoryRepository, categoryProductRepository)

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
    fun `update modify and return updated brand`() {
        // Given
        val brandId = 1L
        val originalBrand = Brand(id = brandId, name = "나이스", status = Brand.Status.ON)
        val updateDTO =
            BrandResources.UpdateDTO(
                id = brandId,
                name = "나이키",
                status = Brand.Status.OFF.desc,
            )

        whenever(brandRepository.findById(brandId)).thenReturn(Optional.of(originalBrand))
        whenever(brandRepository.save(any())).thenAnswer { it.arguments[0] as Brand }

        // When
        val updatedBrand = brandService.update(updateDTO)

        // Then
        verify(brandRepository).findById(brandId)
        verify(brandRepository).save(updatedBrand)

        assertEquals("나이키", updatedBrand.name)
        assertEquals(Brand.Status.OFF, updatedBrand.status)
    }

    @Test
    fun `update throw exception if brand not found`() {
        // Given
        val brandId = 1L
        val updateDTO =
            BrandResources.UpdateDTO(
                id = brandId,
                name = "나이키",
                status = Brand.Status.ON.desc,
            )

        whenever(brandRepository.findById(brandId)).thenReturn(Optional.empty())

        // When & Then
        val exception =
            assertThrows<BrandNotFoundException> {
                brandService.update(updateDTO)
            }

        assertEquals("Brand ID $brandId not found", exception.message)
        verify(brandRepository).findById(brandId)
    }

    @Test
    fun `update throw exception for invalid status`() {
        // Given
        val brandId = 1L
        val originalBrand = Brand(id = brandId, name = "나이스", status = Brand.Status.ON)
        val updateDTO =
            BrandResources.UpdateDTO(
                id = brandId,
                name = "나이키",
                status = "Invalid Status",
            )

        whenever(brandRepository.findById(brandId)).thenReturn(Optional.of(originalBrand))

        // When & Then
        val exception =
            assertThrows<IllegalArgumentException> {
                brandService.update(updateDTO)
            }

        assertEquals("Invalid status: Invalid Status", exception.message)
        verify(brandRepository).findById(brandId)
        verify(brandRepository, never()).save(any())
    }

    @Test
    fun `delete terminate then save`() {
        // Given
        val brandId = 1L
        val brand = Brand(id = brandId, name = "나이스")
        val deleteDTO = BrandResources.DeleteDTO(id = brandId)

        whenever(brandRepository.findById(brandId)).thenReturn(Optional.of(brand))

        // When
        brandService.delete(deleteDTO)

        // Then
        verify(brandRepository).findById(brandId)
        verify(brandRepository).delete(brand)
        assertTrue(brand.isTerminated()) // 상태가 Terminated로 변경되었는지 확인
    }

    @Test
    fun `delete throw exception if brand not found`() {
        // Given
        val brandId = 1L
        val deleteDTO = BrandResources.DeleteDTO(id = brandId)

        whenever(brandRepository.findById(any())).thenReturn(Optional.empty())

        // When & Then
        val exception =
            assertThrows<BrandNotFoundException> {
                brandService.delete(deleteDTO)
            }

        assertEquals("Brand ID $brandId not found", exception.message)
        verify(brandRepository).findById(brandId)
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
