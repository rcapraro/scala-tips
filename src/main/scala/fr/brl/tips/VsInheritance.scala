package fr.brl.tips

object VsInheritance extends App {

  trait Instrumentalist {
    def play(): String
  }

  trait Singer { self: Instrumentalist =>
    def sing(): String
  }

  trait InheritanceSinger extends Instrumentalist {
    def sing(): String
  }

  // valid
  trait Lead extends Singer with Instrumentalist

  // invalid
  /*
  trait Lead extends Singer {

  }
  */
  
  // valid 
  trait InheritanceLead extends InheritanceSinger
}
