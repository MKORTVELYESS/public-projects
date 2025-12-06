package example

import scala.util.{Failure, Success, Try}

class HelloSpec extends munit.FunSuite {
  test("say hello") {
    assertEquals(Hello.greeting, "hello")
  }

  test("scan left") {
    val functions: Seq[String => Try[String]] = Seq((a: String) => Success(a), (a: String) => Success(a), (a: String) => Success(a))
    val nums = Seq(1, 2, 3, 4)
    val result = nums.scanLeft(0)(_ + _)
    println(result)
    val r = "hi"

    val validationResultsLastFirst = functions.foldLeft(List.empty[Try[String]]) {
      case (Nil, fn) => List(fn(r))
      case (res@prev :: _, fn) =>
        prev match {
          case Failure(_) => fn(r) :: res
          case Success(v) => fn(v) :: res
        }
    }

    val validationResultsLastLast = functions.foldRight(List.empty[Try[String]]) {
      case (fn, Nil) => List(fn(r))
      case (fn, rest :+ last) =>
        last match {
          case Failure(_) => rest :+ last :+ fn(r)
          case Success(v) => rest :+ last :+ fn(v)
        }
    }

    assertEquals(Hello.greeting, "hello")
  }
}
