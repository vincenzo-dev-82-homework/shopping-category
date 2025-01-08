package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CategoryRepositoryImpl(
    private val categoryJpaRepository: CategoryJpaRepository,
) : CategoryRepository {
    override fun findAll(): List<Category> = categoryJpaRepository.findAll()

    override fun findById(id: Long): Category? = categoryJpaRepository.findByIdOrNull(id)

    override fun findByName(name: String): Category? = categoryJpaRepository.findByName(name).orElse(null)

    override fun save(category: Category): Category = categoryJpaRepository.save(category)

    override fun delete(category: Category) = categoryJpaRepository.delete(category)

    override fun count(): Long = categoryJpaRepository.count()
}
