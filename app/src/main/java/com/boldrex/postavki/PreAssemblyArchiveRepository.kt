package com.boldrex.postavki

class PreAssemblyArchiveRepository(private val dao: AppDao) {
    suspend fun listArchive(): List<PreAssemblyArchiveEntry> {
        return dao.listPreAssemblyArchiveSummaries().map { summary ->
            val items = dao.listPreAssemblyArchiveItems(summary.id).map { row ->
                PreAssemblyItem(
                    id = row.offerId,
                    orderId = row.orderId,
                    offerId = row.offerId,
                    sku = row.sku,
                    name = row.name,
                    requiredQuantity = row.requiredQuantity,
                    status = runCatching { PreAssemblyStatus.valueOf(row.status) }
                        .getOrDefault(PreAssemblyStatus.NOT_CHECKED),
                    transferQuantity = row.transferQuantity,
                    comment = row.comment
                )
            }
            PreAssemblyArchiveEntry(
                id = summary.id,
                title = summary.title,
                completedAt = summary.completedAt,
                completedAtText = summary.completedAtText,
                resultTitle = summary.resultTitle,
                total = summary.total,
                checked = summary.checked,
                available = summary.available,
                toTransfer = summary.toTransfer,
                notChecked = summary.notChecked,
                comments = summary.comments,
                items = items
            )
        }
    }

    suspend fun save(entry: PreAssemblyArchiveEntry): Long {
        val archiveId = dao.insertPreAssemblyArchive(
            PreAssemblyArchiveEntity(
                title = entry.title,
                completedAt = entry.completedAt,
                completedAtText = entry.completedAtText,
                resultTitle = entry.resultTitle,
                total = entry.total,
                checked = entry.checked,
                available = entry.available,
                toTransfer = entry.toTransfer,
                notChecked = entry.notChecked,
                comments = entry.comments
            )
        )
        val itemEntities = entry.items.mapIndexed { index, item ->
            PreAssemblyArchiveItemEntity(
                archiveId = archiveId,
                sortIndex = index,
                orderId = item.orderId,
                offerId = item.offerId,
                sku = item.sku,
                name = item.name,
                requiredQuantity = item.requiredQuantity,
                status = item.status.name,
                transferQuantity = item.transferQuantity,
                comment = item.comment
            )
        }
        if (itemEntities.isNotEmpty()) {
            dao.insertPreAssemblyArchiveItems(itemEntities)
        }
        return archiveId
    }

    suspend fun update(entry: PreAssemblyArchiveEntry) {
        dao.updatePreAssemblyArchive(
            PreAssemblyArchiveEntity(
                id = entry.id,
                title = entry.title,
                completedAt = entry.completedAt,
                completedAtText = entry.completedAtText,
                resultTitle = entry.resultTitle,
                total = entry.total,
                checked = entry.checked,
                available = entry.available,
                toTransfer = entry.toTransfer,
                notChecked = entry.notChecked,
                comments = entry.comments
            )
        )
        dao.deletePreAssemblyArchiveItems(entry.id)
        val itemEntities = entry.items.mapIndexed { index, item ->
            PreAssemblyArchiveItemEntity(
                archiveId = entry.id,
                sortIndex = index,
                orderId = item.orderId,
                offerId = item.offerId,
                sku = item.sku,
                name = item.name,
                requiredQuantity = item.requiredQuantity,
                status = item.status.name,
                transferQuantity = item.transferQuantity,
                comment = item.comment
            )
        }
        if (itemEntities.isNotEmpty()) {
            dao.insertPreAssemblyArchiveItems(itemEntities)
        }
    }
}
