package fr.brl.tips.variance

object AnimalVariance extends App {

  trait Animal

  class Dog extends Animal

  class Cat extends Animal

  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics
  // if X <: Y is T[X] <: T[Y] ?

  // no - invariance
  class Cage[T]

  // does not compile because Cage[Cat] is not a subtype fof Cage[Animal]
  // val iCage: Cage[Animal] = new Cage[Cat]
  //  same as saying val x: Int = "hello"

  // yes - covariance
  class CCage[+T]

  val cCage: CCage[Animal] = new CCage[Cat]

  // hell no - opposite = contravariance
  class XCage[-T]

  val xCage: XCage[Cat] = new XCage[Animal]

  //<editor-fold desc="Variance positions demystified">

  class InvariantCage[T](val animal: T) // invariant POSITION

  // covariant positions
  class CovariantCage[+T](val animal: T) // COVARIANT POSITION

  // class ContravariantCage[-T](val animal: T) // error: CONTRAVARIANT type T occurs in COVARIANT position
  // because types of vars are in CONTRAVARIANT POSITION

  // without this compilation error, it would be possible to write:
  // val catCage: ContravariantCage[Cat] = new ContravariantCage[Animal](new Crocodile)
  // i don't want a cage for cat ot contain a crocodile !

  // contravariant positions
  // class CovariantVariableCage[+T](var animal: T)  // error: COVARIANT type T occurs in CONTRAVARIANT position
  // because types of vars are in CONTRAVARIANT POSITION

  // without this compilation error, it would be possible to write:
  // val animalCage: CovariantCage[Animal] = new CovariantCage[Cat](new Cat)
  // animalCage.animal = new Crocodile

  // class ContravariantVariableCage[-T](var animal: T) // error: CONTRAVARIANT type T occurs in COVARIANT position
  // also in COVARIANT POSITION
  // without this compilation error, it would be possible to write:
  // val catCage: ContravariantVariableCage[Cat] = new ContravariantVariableCage[Animal](new Crocodile)

  class InvariantVariableCage[T](var animal: T) // ok

  // express the fact that a cage of cat or dog is a subtype of a cage of animal
  class AnotherCovariantCage[+T] {
      // def addAnimal(animal: T) = true // error: COVARIANT type T occurs in CONTRAVARIANT position
  }
  // also in CONTRAVARIANT POSITION

  // without this compilation error, it would be possible to write:
  // val anotherCCage: AnotherCovariantCage[Animal] = new AnotherCovariantCage[Dog]
  // anotherCCage.addAnimal(new Crocodile)  // wait ! i can add a crocodile in a cage made for a dog ???

  // express the fact that a cage of animal is a subtype of a cage of cat
  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  // so we can use a cage of animal to capture a cat or a subtype of cat
  val anotherXCage: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  anotherXCage.addAnimal(new Cat)
  // anotherXCage.addAnimal(new Dog) // required Cat
  class Kitty extends Cat
  anotherXCage.addAnimal(new Kitty)

  //</editor-fold>

  //<editor-fold desc="Solution: Widening the types">

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals: MyList[Kitty] = emptyList.add(new Kitty)
  val moreAnimals: MyList[Cat] = animals.add(new Cat)
  val evenMoreAnimals: MyList[Animal] = moreAnimals.add(new Dog)

  //</editor-fold>



}
