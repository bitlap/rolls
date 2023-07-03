package bitlap.rolls.zio

import bitlap.rolls.zio.autoLive.*

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/** @author
 *    梦境迷离
 *  @version 1.0,2023/7/3
 */
class AutoLiveSpec extends AnyFlatSpec with Matchers {

  "autoLive" should "ok" in {
    """object PositionRepository extends deriveLive0[PositionRepository]
     |    final class PositionRepository():
     |      def getPositionInfo: String = ???
     |    end PositionRepository
     |
     |    val live = PositionRepository.live""".stripMargin should compile
  }

}
