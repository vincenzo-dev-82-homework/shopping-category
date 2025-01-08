package com.musinsa.category.domain.entity

import com.musinsa.category.domain.CategoryType
import com.musinsa.common.Audit
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@DynamicUpdate
@Entity
@Table
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent: Category? = null,
    @Column(nullable = false, unique = true)
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: CategoryType,
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val children: MutableList<Category> = mutableListOf(),
) : Audit() {
    companion object {
        fun create(
            displayName: String,
            parent: Category? = null,
        ): Category {
            val type =
                CategoryType.fromDisplayName(displayName)
                    ?: throw IllegalArgumentException("Invalid category type: $displayName")

            return Category(
                parent = parent,
                name = displayName,
                type = type,
            )
        }
    }
}
