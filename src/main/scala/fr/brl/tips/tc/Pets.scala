package fr.brl.tips.tc

object Pets extends App {

  trait Pet[A <: Pet[A]] {
    def name: String

    def renamed(newName: String): A
  }

  case class Fish(name: String, age: Int) extends Pet[Fish] {
    def renamed(newName: String): Fish = copy(name = newName)
  }

  case class Kitty(name: String, age: Int) extends Pet[Kitty] {
    def renamed(newName: String): Kitty = copy(name = newName)
  }

  def esquire[A <: Pet[A]](a: A): A = a.renamed(a.name + ", Esq.")

  def phd[A <: Pet[A]](a: A): A = a.renamed(a.name + ", Phd.")

  def rename[A <: Pet[A]](a: A, title: String): A = title match {
    case "esq" => esquire(a)
    case "phd" => phd(a)
    case _ => a
  }

  println(rename(Fish("Bob", 2), "esq"))

}