package com.boldrex.postavki

private val PreAssemblyPiecesInNameRegex =
    Regex("""(?:^|[^\p{L}\p{N}])(\d{1,4})\s*(?:шт\.?|штук(?:и|а)?|штуки|штука)(?=$|[^\p{L}])""")

private val PreAssemblyUpToKgInNameRegex =
    Regex("""(?:^|[^\p{L}\p{N}])до\s*(\d{1,4})\s*кг\.?(?=$|[^\p{L}])""")

internal fun preAssemblyRequiredQuantity(item: OzonOrderItem): Int {
    return item.quantity.coerceAtLeast(1) * preAssemblyNameQuantityMultiplier(item.name)
}

internal fun preAssemblyNameQuantityMultiplier(name: String): Int {
    val normalized = name.lowercase().replace('\u00A0', ' ')
    val piecesCount = PreAssemblyPiecesInNameRegex.findAll(normalized)
        .mapNotNull { match -> match.groupValues.getOrNull(1)?.toIntOrNull() }
        .filter { it > 0 }
        .maxOrNull()

    if (piecesCount != null) return piecesCount

    return PreAssemblyUpToKgInNameRegex.findAll(normalized)
        .mapNotNull { match -> match.groupValues.getOrNull(1)?.toIntOrNull() }
        .filter { it > 0 }
        .maxOrNull()
        ?: 1
}
