package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

//  def index() = Action { implicit request: Request[AnyContent] =>
//    Ok(views.html.index())
//  }

  def preflight(all: String): Action[AnyContent] = Action {req =>
    println("validate origin ...." + req)
    Ok("").withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Allow" -> "*",
      "Access-Control-Allow-Methods" -> "*",
      "Access-Control-Allow-Headers" ->
        "*")
  }

}
