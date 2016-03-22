# scala-macro-example

```scala 
  lazy val `scala-macro-example` =
    ProjectRef( uri("git:https://github.com/1178615156/scala-macro-example"),"scala-macro-example")
```

#### play url annotation
```scala
import yjs.annotation.Routes._
import macross.play.MakeRoutes
```
use like this
![use like this](http://www.popo8.com/host/data/201512/13/11/8e2a82d.gif)

#### auto type tran
```scala

  object a {
    val value = Option(Option(1))
    val need_result: Option[Int] = value.flatten
    type To = Option[Int]
  }

  object b {
    val value: Option[Future[Option[Future[List[Int]]]]] = Option(Future(Option(Future(List(2)))))
    val need_result: Future[Option[List[Int]]] =
      value.traverse.map(_.flatten).map(_.traverse).flatMap(e ⇒ e)
    type To = Future[Option[List[Int]]]
  }

    import Data._

// use like this

    assert(
      a.value.tranTo[a.To] == a.need_result
    )
    assert(Await.result(
      b.value.tranTo[b.To] zip b.need_result map (e ⇒ e._1 == e._2)
      , Inf)
    )
```
#### slick sort by name
when we want sort by field name we need write follow code
```scala
val sortFieldName = "id"
table.sortBy((e)=>
    sortFieldName match {
        case "id" => if (true)
          e.id.asc
        else
          e.id.desc
        case "name" => if (true)
          e.name.asc
        else
          e.name.desc
        case "mobile" => if (true)
          e.mobile.asc
        else
          e.mobile.desc
      }
)
```
this is boring, so use sortByName improve it
```scala
table.sortBy((e) => SortByName.apply(e, "name", true))
```
#### get public val
collect public val to list and map 
like follow
```scala
  object Module_1 {
    val a = 1
    val b = 2
    val c = 3
    //need write return type
    val list: List[Int] = GetPublicValMacros.listValue[Module_1.type, Int]//List(1,2,3)
    val map: Map[String, Int] = GetPublicValMacros.mapValue[Module_1.type, Int]//Map(c -> 3, b -> 2, a -> 1)
  }
```
