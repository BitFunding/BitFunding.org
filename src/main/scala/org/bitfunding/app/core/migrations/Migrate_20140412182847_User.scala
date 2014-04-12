package org.bitfunding.app.core.migrations

import com.imageworks.migration.{
  Limit,
  Migration,
  Name,
  NotNull,
  OnDelete,
  Restrict,
  Unique,
  PrimaryKey
}

/**
 * Create the 'facility_set_membership' table, which is a many-to-many
 * join table between the 'facility' and 'facility_set' tables.  It
 * represents the sets that a facility is a member of and the
 * facilities that are in a set.  Rows do not have a their own primary
 * key.
 */
class Migrate_20140412182847_User
  extends Migration
{
  val tableName = "User"

  def up() {
    createTable(tableName) { t =>
      t.bigint("id", NotNull, PrimaryKey)
      t.varchar("username", NotNull, Limit(24))
      t.varchar("email", NotNull, Limit(64))
      t.varchar("password", Limit(255))
    }
  }

  def down() {
    dropTable(tableName)
  }
}
