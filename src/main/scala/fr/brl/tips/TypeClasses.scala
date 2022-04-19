package fr.brl.tips

object TypeClasses extends App {

  // Type classes are used to add add capabilities to existing classes

  // example serialize to JSON

  // pattern consist of 4 steps

  // part 1 - type class definition

  trait JSONSerializer[T] {
    def toJSON(value: T): String
  }

  // part 2 - create implicit type class INSTANCES
  case class Person(name: String, age: Int)

  implicit object StringSerializer extends JSONSerializer[String] {
    override def toJSON(value: String): String = "\"" + value + "\""
  }

  implicit object IntSerializer extends JSONSerializer[Int] {
    override def toJSON(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JSONSerializer[Person] {
    override def toJSON(value: Person): String =
      s"""
         |{ "name" "${value.name}, "age": ${value.age} }
         |""".stripMargin.trim
  }

  // part 3 - offer some additional API
  def convertListToJSON[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(v => serializer.toJSON(v)).mkString("[", ",", "]")

  println(convertListToJSON(List(Person("Alice", 23), Person("Richard", 47))))

  // part 4 - extending the existing types via extension methods
  object JSONSyntax {
    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer[T]) {
      def toJSON: String = serializer.toJSON(value)
    }
  }

  val bob = Person("Bob", 35)
  import JSONSyntax._  // use the two implicits JSONSerializable and JSONSerializer[Person]

  println(bob.toJSON)

  // With context bounds (:)
  def convertListToJSON2[T:JSONSerializer](list: List[T]): String = {
    list.map(v => v.toJSON).mkString("[", ",", "]")
  }

  println(convertListToJSON2(List(Person("Alice", 23), Person("Richard", 47))))

  // example cats TC
  // part 1 - type class import
  import cats.Eq

  // part 2 - import TC instances for the types you need
  import cats.instances.int._

  // part 3 - use the TC API
  val intEquality = Eq[Int]
  val typeSafeEquality = intEquality.eqv(2, 3) // replace 3 with "toto", it won't compile
  val unsafeEquality = 2 == "toto"

  // part 4 - use extension methods
  import cats.syntax.eq._
  val anotherTypeSafeEq = 2 === 3 //false
  println(anotherTypeSafeEq)
  val neqComparison = 2 =!= 3 //true
  println(neqComparison)

  // part 5 - extending the TC operations to composite types, e.g. List
  import cats.instances.list._
  val aListComparison = List(2) === List(3) // false
  println(aListComparison)

  // part 6 - create a TC instance for a custom type
  case class ToyCar(model: String, price: Double)
  implicit val carEq: Eq[ToyCar] = Eq.instance[ToyCar] { (car1, car2) =>
    car1.price == car2.price
  }

  val compareTwoToyCars = ToyCar("Ferrari", 29.99) === ToyCar("Lamborghini", 29.99) // true
  println(compareTwoToyCars)

}
