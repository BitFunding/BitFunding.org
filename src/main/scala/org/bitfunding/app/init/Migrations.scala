package org.bitfunding.app.init

// import org.postgresql.jdbc3.Jdbc3ConnectionPool
import com.imageworks.migration.{DatabaseAdapter,
                                 InstallAllMigrations,
                                 Vendor,
                                 Migrator}

import org.postgresql.ds.PGSimpleDataSource

object migrations{

  def migrate(): Unit = {
    val migrationAdapter = DatabaseAdapter.forVendor(Vendor.forDriver("org.postgresql.Driver"), None)
    val dataSource = new PGSimpleDataSource()
    dataSource.setServerName("localhost")
    dataSource.setDatabaseName(dbConfig.databaseName)
    dataSource.setUser(dbConfig.databaseUsername)
    dataSource.setPassword(dbConfig.databasePassword)
    val migrator = new Migrator(dataSource, migrationAdapter)

    migrator.migrate(InstallAllMigrations,
      "org.bitfunding.app.core.migrations",
      false)
  }


}
