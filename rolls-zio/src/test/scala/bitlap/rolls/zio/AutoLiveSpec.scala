package bitlap.rolls.zio

import scala.util.Try

import bitlap.rolls.zio.autoLive.*

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import zio.*

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

  "autoLive" should "error" in {

    object PositionRepository  extends deriveLive0[PositionRepository]
    object Position2Repository extends deriveLive0[Position2Repository]
    object UserRepository      extends deriveLive1[UserRepository, Position2Repository]

    final class UserRepository(po: PositionRepository):
      def getUserInfo: String = "a"

    final class Position2Repository()
    final class PositionRepository():
      def getPositionInfo: String = "b"
    end PositionRepository

    val zio = ZIO.serviceWith[UserRepository](_.getUserInfo).provide(UserRepository.live, Position2Repository.live)
    Try {
      Unsafe.unsafe { _ ?=>
        Runtime.default.unsafe.run(zio).getOrThrowFiberFailure()
      }
      false
    }.getOrElse(
      true
    ) shouldEqual true
  }

}
