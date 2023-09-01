package bitlap.rolls.core

import scala.compiletime.*
import scala.deriving.Mirror
import scala.quoted.*
import scala.reflect.{ classTag, ClassTag }
import scala.runtime.Tuples

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/9
 */
object extensions:

  extension (tpe: Type[?])

    def fullName(using Quotes): String = {
      import quotes.reflect.*
      TypeRepr.of(using tpe).show(using Printer.TypeReprCode)
    }
  end extension

  extension (tuple: Tuple) inline def toProduct[T](using mirror: Mirror.ProductOf[T]): T = mirrors.toProduct(tuple)
  end extension

  extension (product: Product) inline def toTuple: Tuple = Tuples.fromProduct(product)
  end extension

end extensions

object shows:

  inline def showTree_[A](inline a: A): String      = ${ showTree[A]('{ a }) }
  inline def showCode_[A](inline a: A): String      = ${ showCode[A]('{ a }) }
  inline def showShortCode_[A](inline a: A): String = ${ showShortCode[A]('{ a }) }

  def showTree[A: Type](a: Expr[A])(using quotes: Quotes): Expr[String] =
    import quotes.reflect.*
    Expr(Printer.TreeStructure.show(a.asTerm))

  def showShortCode[A: Type](a: Expr[A])(using quotes: Quotes): Expr[String] =
    import quotes.reflect.*
    Expr(Printer.TreeShortCode.show(a.asTerm))

  def showCode[A: Type](a: Expr[A])(using quotes: Quotes): Expr[String] =
    import quotes.reflect.*
    Expr(Printer.TreeCode.show(a.asTerm))

end shows

object mirrors:

  inline def toProduct[T](inline tuple: Tuple)(using m: Mirror.ProductOf[T]): T = m.fromProduct(tuple)

  inline def tupleTypeToString[A <: Tuple]: List[String] = inline erasedValue[A] match {
    case _: EmptyTuple => Nil
    case _: (head *: tail) =>
      constValue[head].toString :: tupleTypeToString[tail]
  }

  inline def labels[T](using m: Mirror.Of[T]): List[String] =
    tupleTypeToString[m.MirroredElemLabels]
end mirrors

inline def isExpectedReturnType[R: Type](using quotes: Quotes): quotes.reflect.Symbol => Boolean = { method =>
  import quotes.reflect.*
  val expectedReturnType = TypeRepr.of[R]
  method.tree match {
    case DefDef(_, _, typedTree, _) =>
      TypeRepr.of(using typedTree.tpe.asType) <:< expectedReturnType
    case _ => false
  }
}

transparent inline def isEnum[X]: Boolean =
  inline erasedValue[X] match
    case _: Null         => false
    case _: Nothing      => false
    case _: reflect.Enum => true
    case _               => false

end isEnum

def runtimeName[T: ClassTag] =
  classTag[T].runtimeClass.getTypeName

def productElement[T](x: Any, idx: Int) =
  x.asInstanceOf[Product].productElement(idx).asInstanceOf[T]

def to[T: Type, R: Type](f: Expr[T] => Expr[R])(using Quotes): Expr[T => R] =
  '{ (x: T) => ${ f('{ x }) } }

def from[T: Type, R: Type](f: Expr[T => R])(using Quotes): Expr[T] => Expr[R] =
  (x: Expr[T]) => '{ $f($x) }
