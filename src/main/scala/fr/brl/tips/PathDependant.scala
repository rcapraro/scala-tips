package fr.brl.tips

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object PathDependant extends App {

  type Id[A] = A

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  trait DataSource[M[_]] {
    type CollectedValue
    val values: Seq[this.CollectedValue]

    def collect(): M[this.CollectedValue]
  }

  def DataSourceOptionFactory[T](inValues: Seq[T]) = new DataSource[Option] {
    override type CollectedValue = T
    override val values: Seq[T] = inValues

    override def collect(): Option[T] = Some(values.head)
  }

  def DataSourceFutureFactory[T](inValues: Seq[T]) = new DataSource[Future] {
    override type CollectedValue = T
    override val values: Seq[T] = inValues

    override def collect(): Future[T] = Future.successful(values.head)
  }

  def DataSourceIdFactory[T](inValues: Seq[T]) = new DataSource[Id] {
    override type CollectedValue = T
    override val values: Seq[T] = inValues
    override def collect(): Id[T] = values.head
  }

  val dataSource: DataSource[Option] = DataSourceOptionFactory[String](List("a", "b", "c"))
  println(dataSource.collect())

  val dataSource2: DataSource[Future] = DataSourceFutureFactory[String](List("a", "b", "c"))
  dataSource2.collect().onComplete {
    case Failure(exception) => ???
    case Success(value) => println(value)
  }

}
