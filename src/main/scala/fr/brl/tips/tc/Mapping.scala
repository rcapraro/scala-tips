package fr.brl.tips.tc

object Mapping extends App {

  sealed trait Input
  case class A(id: Int) extends Input
  case class B(id: String) extends Input

  sealed trait Output[+T <: Input]
  case class OutA(some: String) extends Output[A]
  case class OutB(thing: Int) extends Output[B]

  // bad !
  def map[I <: Input, O <: Output[I]](input: I): Output[I] =
    input match {
      case A(i) => OutA(i.toString).asInstanceOf[O] // bad !
      case B(s) => OutB(s.toInt).asInstanceOf[O] // bad !
    }

  // Exercise: recode it with a type class

  // better !
  case class Mapper[I<: Input](mapTo: I => Output[I])
  object Mapper {
    implicit val ADoer: Mapper[A] = Mapper(x => OutA(x.id.toString))
    implicit val BDoer: Mapper[B] = Mapper(x => OutB(x.id.toInt))
    implicit val InputDoer: Mapper[Input] = Mapper {
      case a: A => ADoer.mapTo(a)
      case b: B => BDoer.mapTo(b)
    }
  }

  def betterMap[I <: Input](input: I)(implicit doer: Mapper[I]): Output[I] = doer.mapTo(input)

  println(map(A(1)))

  println(betterMap(B("2")))
}
