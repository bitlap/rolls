package bitlap.rolls.zio

import scala.compiletime.*
import scala.quoted.*

import izumi.reflect.Tag
import zio.*
import zio.ZLayer.FunctionConstructor

/** @author
 *    梦境迷离
 *  @version 1.0,2023/6/30
 */

object AutoLiveMacros:

  inline def derived[A, R](implicit constructor: FunctionConstructor[R => A]): ZLayer[Any, Nothing, A] = ${
    AutoLiveMacros.autoLiveImpl[A, R]('constructor)
  }

  def autoLiveImpl[A: Type, R: Type](constructor: Expr[FunctionConstructor[R => A]])(using
    quotes: Quotes
  ): Expr[ZLayer[Any, Nothing, A]] =
    import quotes.reflect.*
    val repr                    = TypeRepr.of[A]
    val symbol: Symbol          = repr.typeSymbol
    val constr                  = symbol.primaryConstructor.tree.asInstanceOf[DefDef]
    val pcss: List[ParamClause] = constr.paramss
    val pss: List[?]            = pcss.flatMap(_.asInstanceOf[List[?]])
    val paramss: List[ValDef]   = pss.asInstanceOf[List[ValDef]]
    val tpe: List[TypeRepr]     = paramss.map(_.tpt.tpe)
    val argsName                = tpe.indices.map(i => s"arg$i").toList
    val mtpe                    = MethodType(argsName)(_ => tpe, _ => TypeRepr.of[A])
    val lambda = Lambda(
      Symbol.spliceOwner,
      mtpe,
      (methSym, arg1: List[Tree]) =>
        New(Inferred(repr)).select(symbol.primaryConstructor).appliedToArgs(arg1.map(_.asExpr.asTerm))
    )
    '{
      implicit val trace: Trace = Trace.empty
      // TODO create Tag
      implicit val ctor: ZLayer.FunctionConstructor[Any] = $constructor.asInstanceOf[ZLayer.FunctionConstructor[Any]]
      ZLayer.fromFunction(${ lambda.asExpr }).asInstanceOf[ZLayer[Any, Nothing, A]]
    }
