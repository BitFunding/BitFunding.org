import org.bitfunding.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.bitfunding.app.init.migrations
import org.bitfunding.app.init.{
  DatabaseInit,
  OAuthInit,
  ConfigSupport}

class ScalatraBootstrap extends LifeCycle 
    with DatabaseInit
    with ConfigSupport
    with OAuthInit{

  var (ctx : ServletContext) = null

  def context = ctx

  override def init(context: ServletContext) {

    this.ctx = context
    migrations.migrate()
    this.configureDb()
    this.initOAuth
    context.mount(new BitFundingServlet, "/*")
  }
}
