package bitlap.rolls.csv.internal

import scala.compiletime.*
import scala.quoted.*

/** @author
 *    梦境迷离
 *  @version 1.0,2023/4/5
 */
private[csv] object FieldSelector:

  object SelectorLambda:

    def unapply(using
        quotes: Quotes
    )(
        arg: quotes.reflect.Term
    ): Option[(List[quotes.reflect.ValDef], quotes.reflect.Term)] =
      import quotes.reflect.*
      arg match {
        case Inlined(_, _, Lambda(vals, term)) => Some((vals, term))
        case Inlined(_, _, nested)             => SelectorLambda.unapply(nested)
        case t                                 => None
      }
  end SelectorLambda

  def unapply(using quotes: Quotes)(arg: quotes.reflect.Term): Option[String] =
    import quotes.reflect.*
    arg match {
      case SelectorLambda(_, Select(Ident(_), fieldName)) => Some(fieldName)
      case _                                              => None
    }

end FieldSelector
