package com.boldrex.postavki

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity(tableName = "marketplaces", indices = [Index(value = ["name"], unique = true)])
data class MarketplaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(tableName = "cities", indices = [Index(value = ["name"], unique = true)])
data class CityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

@Entity(tableName = "products", indices = [Index(value = ["barcode"], unique = true)])
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val article: String,
    val name: String,
    val barcode: String?,
    val createdAt: Long,
    val isCreatedFromScan: Boolean
)

@Entity(tableName = "shipments", indices = [Index("marketplaceId"), Index("date")])
data class ShipmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: String,
    val marketplaceId: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val isArchived: Boolean = false
)

@Entity(
    tableName = "shipment_cities",
    indices = [Index(value = ["shipmentId", "cityName"], unique = true), Index("cityId")]
)
data class ShipmentCityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shipmentId: Long,
    val cityId: Long?,
    val cityName: String
)

@Entity(
    tableName = "boxes",
    indices = [Index(value = ["shipmentId", "shipmentCityId", "boxNumber"], unique = true)]
)
data class BoxEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shipmentId: Long,
    val shipmentCityId: Long,
    val cityName: String,
    val boxNumber: String,
    val comment: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "box_items", indices = [Index(value = ["boxId", "productId"], unique = true), Index("productId")])
data class BoxItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val boxId: Long,
    val productId: Long,
    val quantity: Int,
    val addedAt: Long
)

@Entity(tableName = "import_logs")
data class ImportLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val type: String,
    val date: Long,
    val rowsTotal: Int,
    val rowsImported: Int,
    val errorsCount: Int
)

@Entity(tableName = "report_logs")
data class ReportLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shipmentId: Long,
    val fileName: String,
    val filePath: String,
    val generatedAt: Long
)

@Entity(tableName = "app_settings", indices = [Index(value = ["key"], unique = true)])
data class AppSettingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,
    val value: String
)

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMarketplace(entity: MarketplaceEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(entity: CityEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(entity: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertShipment(entity: ShipmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShipmentCity(entity: ShipmentCityEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBox(entity: BoxEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBoxItem(entity: BoxItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(entity: AppSettingEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImportLog(entity: ImportLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertReportLog(entity: ReportLogEntity): Long

    @Update suspend fun updateShipment(entity: ShipmentEntity)
    @Update suspend fun updateBox(entity: BoxEntity)
    @Update suspend fun updateBoxItem(entity: BoxItemEntity)
    @Update suspend fun updateProduct(entity: ProductEntity)
    @Delete suspend fun deleteBox(entity: BoxEntity)
    @Delete suspend fun deleteBoxItem(entity: BoxItemEntity)

    @Query("DELETE FROM box_items WHERE boxId = :boxId")
    suspend fun deleteItemsForBox(boxId: Long)

    @Query("SELECT id FROM marketplaces WHERE name = :name LIMIT 1")
    suspend fun marketplaceIdByName(name: String): Long?

    @Query("SELECT id FROM cities WHERE name = :name LIMIT 1")
    suspend fun cityIdByName(name: String): Long?

    @Query("SELECT * FROM shipments WHERE id = :id LIMIT 1")
    suspend fun shipmentById(id: Long): ShipmentEntity?

    @Query("SELECT * FROM boxes WHERE id = :id LIMIT 1")
    suspend fun boxById(id: Long): BoxEntity?

    @Query("SELECT * FROM box_items WHERE id = :id LIMIT 1")
    suspend fun boxItemById(id: Long): BoxItemEntity?

    @Query("SELECT * FROM box_items WHERE boxId = :boxId AND productId = :productId LIMIT 1")
    suspend fun boxItemByBoxAndProduct(boxId: Long, productId: Long): BoxItemEntity?

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun productById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun productByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM shipment_cities WHERE id = :id LIMIT 1")
    suspend fun shipmentCityById(id: Long): ShipmentCityEntity?

    @Query("SELECT COUNT(*) FROM boxes WHERE shipmentId = :shipmentId AND shipmentCityId = :shipmentCityId AND boxNumber = :boxNumber")
    suspend fun countBoxesWithNumber(shipmentId: Long, shipmentCityId: Long, boxNumber: String): Int

    @Query("SELECT COUNT(*) FROM boxes WHERE shipmentId = :shipmentId AND shipmentCityId = :shipmentCityId")
    suspend fun countBoxesInCity(shipmentId: Long, shipmentCityId: Long): Int

    @Query("SELECT boxNumber FROM boxes WHERE shipmentId = :shipmentId AND shipmentCityId = :shipmentCityId ORDER BY id")
    suspend fun boxNumbersForCity(shipmentId: Long, shipmentCityId: Long): List<String>

    @Query("""
        SELECT s.id, s.title, s.date, m.name AS marketplace,
               (SELECT COUNT(*) FROM shipment_cities sc WHERE sc.shipmentId = s.id) AS cityCount,
               (SELECT COUNT(*) FROM boxes b WHERE b.shipmentId = s.id) AS boxCount,
               (SELECT COUNT(*) FROM box_items bi JOIN boxes b ON b.id = bi.boxId WHERE b.shipmentId = s.id AND bi.quantity > 0) AS positionCount,
               (SELECT COALESCE(SUM(bi.quantity), 0) FROM box_items bi JOIN boxes b ON b.id = bi.boxId WHERE b.shipmentId = s.id AND bi.quantity > 0) AS itemCount,
               s.isArchived AS isArchived
        FROM shipments s
        JOIN marketplaces m ON m.id = s.marketplaceId
        ORDER BY s.isArchived ASC, s.date DESC, s.id DESC
    """)
    suspend fun listShipmentCards(): List<ShipmentCardData>

    @Query("""
        SELECT sc.id, sc.cityName,
               (SELECT COUNT(*) FROM boxes b WHERE b.shipmentCityId = sc.id) AS boxCount,
               (SELECT COALESCE(SUM(bi.quantity), 0) FROM box_items bi JOIN boxes b ON b.id = bi.boxId WHERE b.shipmentCityId = sc.id AND bi.quantity > 0) AS itemCount
        FROM shipment_cities sc
        WHERE sc.shipmentId = :shipmentId
        ORDER BY sc.cityName
    """)
    suspend fun listShipmentCities(shipmentId: Long): List<ShipmentCityCard>

    @Query("""
        SELECT b.id, b.boxNumber, b.cityName, b.comment,
               (SELECT COUNT(*) FROM box_items bi WHERE bi.boxId = b.id AND bi.quantity > 0) AS positionCount,
               (SELECT COALESCE(SUM(bi.quantity), 0) FROM box_items bi WHERE bi.boxId = b.id AND bi.quantity > 0) AS itemCount
        FROM boxes b
        WHERE b.shipmentCityId = :shipmentCityId
        ORDER BY b.boxNumber
    """)
    suspend fun listBoxes(shipmentCityId: Long): List<BoxCardData>

    @Query("""
        SELECT bi.id, bi.productId, p.article, p.name, p.barcode, bi.quantity
        FROM box_items bi
        JOIN products p ON p.id = bi.productId
        WHERE bi.boxId = :boxId AND bi.quantity > 0
        ORDER BY p.article, p.name
    """)
    suspend fun listBoxItems(boxId: Long): List<BoxItemData>

    @Query("""
        SELECT id, article, name, barcode
        FROM products
        WHERE article LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%'
        ORDER BY article, name
        LIMIT 50
    """)
    suspend fun searchProducts(query: String): List<ProductSearchData>

    @Query("""
        SELECT s.id, s.title, s.date, m.name AS marketplace
        FROM shipments s JOIN marketplaces m ON m.id = s.marketplaceId
        WHERE s.id = :shipmentId
        LIMIT 1
    """)
    suspend fun shipmentInfo(shipmentId: Long): ShipmentInfo?

    @Query("""
        SELECT m.name AS marketplace, s.title AS shipment, s.date AS shipmentDate, sc.cityName AS city,
               b.boxNumber AS boxNumber, b.comment AS boxComment, p.article AS article, p.name AS productName,
               p.barcode AS barcode, bi.quantity AS quantity, p.createdAt AS productCreatedAt,
               p.isCreatedFromScan AS isCreatedFromScan
        FROM box_items bi
        JOIN boxes b ON b.id = bi.boxId
        JOIN shipment_cities sc ON sc.id = b.shipmentCityId
        JOIN shipments s ON s.id = b.shipmentId
        JOIN marketplaces m ON m.id = s.marketplaceId
        JOIN products p ON p.id = bi.productId
        WHERE s.id = :shipmentId AND bi.quantity > 0
        ORDER BY sc.cityName, b.boxNumber, p.article
    """)
    suspend fun reportRows(shipmentId: Long): List<ReportRow>

    @Query("""
        SELECT b.cityName AS city, b.boxNumber AS boxNumber, m.name AS marketplace, b.comment AS comment,
               (SELECT COUNT(*) FROM box_items bi WHERE bi.boxId = b.id AND bi.quantity > 0) AS positionCount,
               (SELECT COALESCE(SUM(bi.quantity), 0) FROM box_items bi WHERE bi.boxId = b.id AND bi.quantity > 0) AS itemCount
        FROM boxes b
        JOIN shipments s ON s.id = b.shipmentId
        JOIN marketplaces m ON m.id = s.marketplaceId
        WHERE b.shipmentId = :shipmentId
        ORDER BY b.cityName, b.boxNumber
    """)
    suspend fun reportBoxes(shipmentId: Long): List<ReportBox>

    @Query("SELECT COUNT(*) FROM marketplaces")
    suspend fun marketplaceCount(): Int
}

@Database(
    entities = [
        MarketplaceEntity::class, CityEntity::class, ProductEntity::class, ShipmentEntity::class,
        ShipmentCityEntity::class, BoxEntity::class, BoxItemEntity::class, ImportLogEntity::class,
        ReportLogEntity::class, AppSettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun dao(): AppDao

    companion object {
        @Volatile private var INSTANCE: LocalDatabase? = null

        fun get(context: Context): LocalDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "postavki.db")
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}
