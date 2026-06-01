package com.boldrex.postavki

import org.junit.Assert.assertEquals
import org.junit.Test

class PreAssemblyQuantityTest {
    @Test
    fun usesPiecesFromProductNameAsMultiplier() {
        val item = OzonOrderItem(
            orderId = "1001",
            offerId = "A-100",
            sku = null,
            name = "Насадки сменные 3 шт.",
            quantity = 2
        )

        assertEquals(6, preAssemblyRequiredQuantity(item))
    }

    @Test
    fun usesUpToKgFromProductNameWhenPiecesAreAbsent() {
        val item = OzonOrderItem(
            orderId = "1002",
            offerId = "B-200",
            sku = null,
            name = "Товар до 2кг",
            quantity = 3
        )

        assertEquals(6, preAssemblyRequiredQuantity(item))
    }

    @Test
    fun explicitPiecesHavePriorityOverUpToKgText() {
        assertEquals(4, preAssemblyNameQuantityMultiplier("Набор 4 шт. до 2кг"))
    }

    @Test
    fun defaultsToSingleUnitWhenNameHasNoQuantityMarker() {
        val item = OzonOrderItem(
            orderId = "1003",
            offerId = "C-300",
            sku = null,
            name = "Блок питания",
            quantity = 5
        )

        assertEquals(5, preAssemblyRequiredQuantity(item))
    }
}
