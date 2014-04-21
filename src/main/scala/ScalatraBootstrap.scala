import org.bitfunding.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.bitfunding.app.init.migrations
import org.bitfunding.app.init.DatabaseInit

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  override def init(context: ServletContext) {

    migrations.migrate()
    this.configureDb()
    context.mount(new BitFundingServlet, "/*")
  }
}
