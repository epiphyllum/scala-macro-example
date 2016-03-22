package macross.slick

import scala.language.experimental.macros

/**
  * Created by yuJieShui on 2016/1/25.
  */


/**
  * only support primary constructor
  */
object GetResultMacro {
  def apply[T]: slick.jdbc.GetResult[T] = macro GetResultMacroImpl.apply[T]
}

class GetResultMacroImpl(val c: scala.reflect.macros.blackbox.Context) {

  import c.universe._

  def apply[Entity: c.WeakTypeTag] = {

    val entity = c.weakTypeOf[Entity]

    val entityConstructorSize = entity.typeSymbol.typeSignature.members
      .filter(_.isConstructor)
      .filter(_.asMethod.paramLists.head.nonEmpty)
      .size

    if (entityConstructorSize > 1)
      c.warning(
        c.enclosingPosition,
        s"GetResultMacro only support primary constructor ,but ${show(entity)} has ${entityConstructorSize} constructor")

    val entityConstructor = entity.typeSymbol.typeSignature.members.filter(_.isConstructor).filter(_.asMethod.isPrimaryConstructor).head

    val entityParams = entityConstructor.asMethod.paramLists

    val positionedResultFunctionName = TermName("positionedResult")

    val positionedResultEntityConstructor = entityParams map (params ⇒ params.map(param ⇒ {
      def asOption = param.info <:< typeOf[Option[_]]

      if (asOption) {
        val tpe = param.info.typeArgs.tail.foldLeft(tq"${param.info.typeArgs.head}") { (l, r) ⇒
          tq"${l}[${r}]"
        }
        q"${positionedResultFunctionName}.<<?[$tpe]"
      }
      else {
        val tpe = tq"${param.info}"
        q"${positionedResultFunctionName}.<<[$tpe]"
      }
    }))
    val out =
      q"""
      slick.jdbc.GetResult((${positionedResultFunctionName} : slick.jdbc.PositionedResult)=> new $entity (...${positionedResultEntityConstructor}))
      """

    println(out)
    out
  }
}