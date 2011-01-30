package bootstrap.liftweb

import net.liftweb._
import example.model.Person
import http.{S, LiftRules, NotFoundAsTemplate, ParsePath}
import mapper.{DB, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import util.{Props, NamedPF}
import sitemap.{SiteMap, Menu, Loc}
class Boot {
  def boot {


    // where to search snippet
    LiftRules.addToPackages("example")

    // build sitemap
    val entries = List(Menu("Home") / "index",
                       Menu("Create Person") / "manage_people") :::
      Nil

    LiftRules.uriNotFound.prepend(NamedPF("404handler") {
      case (req, failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions", "404"), "html", false, false))
    })

    LiftRules.setSiteMap(SiteMap(entries: _*))

    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)

    LiftRules.unloadHooks.append(() => DBVendor.closeAllConnections_!())

    S.addAround(DB.buildLoanWrapper)

    Schemifier.schemify(true, Schemifier.infoF _, Person)
  }

  object DBVendor extends StandardDBVendor(
    Props.get("db.class").openOr("org.h2.Driver"),
    Props.get("db.url").openOr("jdbc:h2:database/link_issue;DB_CLOSE_DELAY=-1"),
    Props.get("db.user"),
    Props.get("db.pass"))

}