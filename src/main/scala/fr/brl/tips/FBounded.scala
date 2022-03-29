package fr.brl.tips

object FBounded extends App {

  // database entities with CRUD methods

  class Apple {
    def create(): Apple = ???
    def read(id: Long): Option[Apple] = ???
  }

  class Orange {
    def create(): Orange = ???
    def read(id: Long): Option[Orange] = ???
  }

  // a lot of copy / paste

  trait Entity {
    def create(): Entity = ???
    def read(id: Long): Option[Entity] = ???
  }

  // well this sucks

  class Lemon extends Entity {
    override def read(id: Long): Option[Entity] = ???
    override def create(): Entity = ???
  }

  class Banana extends Entity {
    override def read(id: Long): Option[Lemon] = ???   // LEMON ???
    override def create(): Entity = ???  // yes because the methods can return any entity
  }

  trait BetterEntity[E] {
    def create(): E = ???
    def read(id: Long): Option[E] = ???
  }

  class Apricot extends BetterEntity[Apricot] {
    override def read(id: Long): Option[Apricot] = ???
    override def create(): Apricot = ???
  }

  class Nectarine extends BetterEntity[Nectarine]


  // still not good - entity can be extended in unintedned ways

  class FlyingSaucer
  class PineApple extends BetterEntity[FlyingSaucer] // !!!

  // problem: BetterEntity[E] does not restrict the type of E


  class FBoundEntity[E <: FBoundEntity[E]] {
    def create(): E = ???
    def read(id: Long): Option[E] = ???
  }

  class Coconut extends FBoundEntity[Coconut] {
    override def read(id: Long): Option[Coconut] = ???

    override def create(): Coconut = ???
  }

  // class Cherry extends FBoundEntity[FlyingSaucer] //  does not compile

  class Lychee extends FBoundEntity[Coconut] // Damn ! it sucks !

  // we need a way to ensure that Orange extends Entity[Orange]

  // self types to the rescue

  class BestEntity[E <: BestEntity[E]] { self: E =>
    def create(): E = ???
    def read(id: Long): Option[E] = ???
  }

  // ensures any class that extends BestEntity[E] is of type E
  class Fig extends BestEntity[Fig]
  // class Papaya extends BestEntity[Fig] // does not compile !

}
