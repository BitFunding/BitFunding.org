import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object BitfundingBuild extends Build {
  val Organization = "org.bitfunding"
  val Name = "BitFunding"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.2.2"

  lazy val project = Project (
    "bitfunding",
    file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),
        "com.imageworks.scala-migrations" %% "scala-migrations" % "1.1.1",
        "org.squeryl" %% "squeryl" % "0.9.5-6", 
        "com.h2database" % "h2" % "1.3.166",
        "c3p0" % "c3p0" % "0.9.1.2",
        "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
        "com.google.http-client" % "google-http-client" % "1.18.0-rc",
        "com.google.http-client" % "google-http-client-jackson" % "1.18.0-rc",
        "com.google.oauth-client" % "google-oauth-client" % "1.8.0-beta",
        "com.google.api-client" % "google-api-client" % "1.18.0-rc",
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
        "com.github.nscala-time" %% "nscala-time" % "1.0.0",
        "com.lambdaworks" % "jacks_2.10" % "2.2.3"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
