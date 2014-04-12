import org.bitfunding.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.bitfunding.app.init.migrations

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {

    migrations.migrate()
    context.mount(new BitFundingServlet, "/*")
  }
}
