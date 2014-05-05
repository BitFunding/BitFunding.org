package org.bitfunding.app.init

import java.io.{File, FileInputStream}
import com.lambdaworks.jacks._
import java.io.File.{separator => </>}
import java.io.FileNotFoundException
import org.scalatra._

case class OAuthConf(
  google_client : String,
  google_secret : String)

object OAuthSupport{

  var conf : OAuthConf = null
}

trait OAuthInit{this: ConfigSupport =>

  val secrets = "secrets.json"

  def initOAuth(implicit m: Manifest[OAuthConf]) = {

    val basePath = this.ctxFile(this.secrets)
    OAuthSupport.conf = ConfigSupport.configLoader(basePath)
  }
}

trait OAuthSupport{

  def oAuthConf = OAuthSupport.conf
}
