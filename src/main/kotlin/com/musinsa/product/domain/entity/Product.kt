package com.musinsa.product.domain.entity

import com.musinsa.common.Audit
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
import java.math.BigDecimal

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var price: BigDecimal,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,
) : Audit() {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.ON

    fun isOn(): Boolean = this.status == Status.ON

    fun isOff(): Boolean = this.status == Status.OFF

    fun isTerminated(): Boolean = this.status == Status.TERMINATED

    fun terminate() {
        this.status = Status.TERMINATED
    }

    enum class Status(
        val desc: String,
    ) {
        ON("사용"),
        OFF("중지"),
        TERMINATED("종료"),
        ;

        companion object {
            fun fromDesc(desc: String): Status? = Status.values().find { it.desc == desc }
        }
    }

    companion object
}
