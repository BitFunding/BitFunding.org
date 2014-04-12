package org.bitfunding.app.init

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.squeryl.adapters.{H2Adapter, MySQLAdapter}
import org.squeryl.Session
import org.squeryl.SessionFactory
import org.slf4j.LoggerFactory

object dbConfig{

  val databaseName = "bitfunding"
  val databaseUsername = "bitfunding"
  val databasePassword = "demopass"
}

trait DatabaseInit {

  //val logger = LoggerFactory.getLogger(getClass)

  var cpds = new ComboPooledDataSource

  def configureDb() {

    
    cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver
    cpds.setJdbcUrl( "jdbc:postgresql://localhost/" + dbConfig.databaseName );
    cpds.setUser(dbConfig.databaseUsername)
    cpds.setPassword(dbConfig.databasePassword)

    cpds.setMinPoolSize(1)
    cpds.setAcquireIncrement(1)
    cpds.setMaxPoolSize(50)

    SessionFactory.concreteFactory = Some(() => connection)

    def connection = {
      //logger.info("Creating connection with c3po connection pool")
      Session.create(cpds.getConnection, new H2Adapter)
    }
  }

  def closeDbConnection() {
    //logger.info("Closing c3po connection pool")
    cpds.close()
  }
}


