package com.example.bitwise_assignment

import java.text.NumberFormat
import java.util.Locale

data class MaxMinResult(
    val max: Double,
    val min: Double,
    val message: String,
    val isEqual: Boolean
)

data class DiscountResult(
    val originalAmount: Double,
    val discountPercent: Int,
    val discountAmount: Double,
    val finalAmount: Double
)

object BranchLabLogic {

    fun calculateMaxMin(input1: String, input2: String): Result<MaxMinResult> {
        if (input1.isBlank() || input2.isBlank()) {
            return Result.failure(IllegalArgumentException("Kedua bilangan tidak boleh kosong."))
        }
        val num1 = input1.toDoubleOrNull()
        val num2 = input2.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            return Result.failure(IllegalArgumentException("Format angka tidak valid. Masukkan angka yang benar."))
        }

        val max: Double
        val min: Double
        val message: String
        val isEqual: Boolean

        // Explicit if-else branching for Max & Min
        if (num1 > num2) {
            max = num1
            min = num2
            message = "Bilangan pertama (${formatNumber(num1)}) lebih besar dari bilangan kedua (${formatNumber(num2)})."
            isEqual = false
        } else if (num2 > num1) {
            max = num2
            min = num1
            message = "Bilangan kedua (${formatNumber(num2)}) lebih besar dari bilangan pertama (${formatNumber(num1)})."
            isEqual = false
        } else {
            max = num1
            min = num2
            message = "Kedua bilangan memiliki nilai yang sama yaitu ${formatNumber(num1)}."
            isEqual = true
        }

        return Result.success(MaxMinResult(max, min, message, isEqual))
    }

    // Nested IF Discount Calculation
    fun calculateNestedIfDiscount(inputPembelian: String): Result<DiscountResult> {
        if (inputPembelian.isBlank()) {
            return Result.failure(IllegalArgumentException("Jumlah pembelian tidak boleh kosong."))
        }
        val amount = inputPembelian.toDoubleOrNull()
        if (amount == null) {
            return Result.failure(IllegalArgumentException("Format jumlah pembelian tidak valid."))
        }
        if (amount < 0) {
            return Result.failure(IllegalArgumentException("Jumlah pembelian tidak boleh bernilai negatif."))
        }

        // Nested IF explicitly implemented
        val discountPercent = if (amount >= 1500000.0) {
            30
        } else {
            if (amount >= 1000000.0) {
                20
            } else {
                if (amount >= 500000.0) {
                    10
                } else {
                    0
                }
            }
        }

        val discountAmount = amount * discountPercent / 100.0
        val finalAmount = amount - discountAmount

        return Result.success(DiscountResult(amount, discountPercent, discountAmount, finalAmount))
    }

    // Switch-Case (when) Discount Calculation
    fun calculateSwitchCaseDiscount(inputPembelian: String): Result<DiscountResult> {
        if (inputPembelian.isBlank()) {
            return Result.failure(IllegalArgumentException("Jumlah pembelian tidak boleh kosong."))
        }
        val amount = inputPembelian.toDoubleOrNull()
        if (amount == null) {
            return Result.failure(IllegalArgumentException("Format jumlah pembelian tidak valid."))
        }
        if (amount < 0) {
            return Result.failure(IllegalArgumentException("Jumlah pembelian tidak boleh bernilai negatif."))
        }

        // Switch-Case equivalent using Kotlin 'when'
        val discountPercent = when {
            amount >= 1500000.0 -> 30
            amount >= 1000000.0 -> 20
            amount >= 500000.0 -> 10
            else -> 0
        }

        val discountAmount = amount * discountPercent / 100.0
        val finalAmount = amount - discountAmount

        return Result.success(DiscountResult(amount, discountPercent, discountAmount, finalAmount))
    }

    fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            String.format(Locale.US, "%.2f", number)
        }
    }

    fun formatRupiah(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
        return format.format(amount).replace(",00", "")
    }
}
