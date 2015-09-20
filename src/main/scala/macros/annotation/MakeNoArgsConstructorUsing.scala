package macros.annotation

/**
 * Created by YuJieShui on 2015/9/11.
 */
object MakeNoArgsConstructorUsing extends App {

  @MakeGetSet
  @MakeNoArgsConstructorMacros
  case class Module(i: Int, s: String)

  val m = new Module()
  println(m.getI) //0
  println(m.getS) //null
}