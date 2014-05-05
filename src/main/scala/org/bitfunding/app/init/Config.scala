package org.bitfunding.app.init

import java.lang.RuntimeException
import java.io.{File, FileInputStream, FileNotFoundException}
import org.scalatra._
import com.lambdaworks.jacks._
import javax.servlet.ServletContext

object ConfigSupport{

  def configLoader[T](src : String)(implicit m: Manifest[T]) : T = {

    try{
      val fis = new FileInputStream(src)
      val res = JacksMapper.readValue[T](fis)
      fis.close
      res

    }catch{

      case e : FileNotFoundException =>
        throw new RuntimeException(
          "Could not load the settings file located at: " + src)
    }
  }
}

trait ConfigSupport{

  def context : ServletContext
  def ctxFile(file : String) = {this.context.getRealPath("/WEB-INF/") + File.separator + file}
}
