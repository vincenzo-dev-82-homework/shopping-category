package com.musinsa.product.domain

import com.musinsa.common.Audit
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Brand(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.ON,
) : Audit() {
    fun isOn(): Boolean = this.status == Status.ON

    fun on() {
        this.status = Status.ON
    }

    fun isOff(): Boolean = this.status == Status.OFF

    fun off() {
        this.status = Status.OFF
    }

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
