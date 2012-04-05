package com.kpox.model

import net.liftweb.util.FieldError
import net.liftweb.record.{MetaMegaProtoUser, MegaProtoUser}
import net.liftweb.mongodb.record.{MongoRecord, MongoMetaRecord}
import net.liftweb.common.{Empty, Full, Box}
import net.liftweb.sitemap.Loc.{Template, LocParam}
import net.liftweb.sitemap.{Loc, Menu}
import Loc._

/**
 * Created by IntelliJ IDEA.
 * User: chukwuma
 * Date: 3/14/12
 * Time: 11:26 PM
 * To change this template use File | Settings | File Templates.
 */

class User extends MegaProtoUser[User]
with MongoRecord[User] {
  def meta = User
  override def valUnique(errorMsg: => String)(emailValue: String) = {
    meta.findAll("email", emailValue) match {
      case Nil => Nil
      case usr :: Nil if (usr.id == id) => Nil
      case _ => List(FieldError(email, "The email should be unique"))
    }
  }
}

object User extends User
with MetaMegaProtoUser[User]
with MongoMetaRecord[User] {
  override def screenWrap = Full(<lift:surround with="default" at="content">
      <lift:bind /></lift:surround>)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
    locale, timezone, password)

  /**
   * Given an username (probably email address), find the user
   */
  protected def findUserByUserName(email: String): Box[User] = findUserByEmail(email)

  protected def findUserByEmail(email: String): Box[User] = {
    findAll("email",email).headOption match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }

  protected def findUserByUniqueId(id: String): Box[User] =  {
    findAll("_id", id).headOption match {
      case Some(x) => Full(x)
      case None => return Empty
    }
  }

  protected def userFromStringId(id: String): Box[User] = find(id)

  override protected def editUserMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.editUserMenuLocParams

  override protected def createUserMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.createUserMenuLocParams

  override protected def loginMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.loginMenuLocParams

  override protected def logoutMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.logoutMenuLocParams

  override protected def lostPasswordMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.lostPasswordMenuLocParams

  override protected def changePasswordMenuLocParams: List[LocParam[Unit]] =
    Hidden :: //hides it but still reachable
      super.changePasswordMenuLocParams
  
  override def thePath(end: String): List[String] =  List("users", end)

  override def skipEmailValidation = true

}
