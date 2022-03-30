package fr.brl.tips

object SelfTypes extends App {

  // self types are a way of declaring a type to be mixed in

  // design an API for a music where all singers must also play an instrument
  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // marker at the language level: whoever implements Singer to implement Instrumentalist
    def sing(): Unit
  }

  // 'self' can be named what you want

  // valid !
  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???

    override def sing(): Unit = ???
  }

  // not valid: Vocalist must also implement play()

  /*  class Vocalist extends Singer {
      override def sing(): Unit = ???
    }
    */

  val neilYoung = new Singer with Instrumentalist {
    override def sing(): Unit = ???

    override def play(): Unit = ???
  }

  // fine
  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // self types vs inheritance

  class A

  class B extends A // B is also an A

  trait T

  trait S {
    self: T =>} // S REQUIRES a T: composition

  // cake pattern => "dependency injection"

  class Component {
    // API
  }

  class ComponentA extends Component

  class ComponentB extends Component

  class DependentComponent(val component: Component) // we want the right component to be injected at runtime

  // cake pattern
  type UserInfo= String

  trait UserRegistry {
    def get(userId: String): UserInfo
  }

  trait UserRegistryComponent {
    val userRegistry: UserRegistry

    class MongoUserRegistry extends UserRegistry {
      override def get(userId: String): UserInfo = "userid-logged"
    }
  }

  trait AuthenticationServiceComponent {
    self: UserRegistryComponent =>

    val authenticationService: AuthenticationService

    class AuthenticationService {
      def authenticate(userId: String): UserInfo = userRegistry.get(userId)
    }
  }

  object Application extends AuthenticationServiceComponent with UserRegistryComponent {
    override val userRegistry: UserRegistry = new MongoUserRegistry
    override val authenticationService: AuthenticationService = new AuthenticationService
  }

  println(Application.authenticationService.authenticate("richard"))


  // difference with classic DI: not inject at runtime but checked at compile time !

  // cyclical dependencies
  /*  class X extend Y
    class Y extends X*/
  // does not compile

  trait X {
    self: Y => }

  trait Y {
    self: X => }

  // X and Y are "sister concepts"
}
