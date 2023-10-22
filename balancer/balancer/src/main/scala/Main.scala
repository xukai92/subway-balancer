object Main {

  // Algorithm Description:
  //
  // Input: List of Integers, simulating train carriages and passenger counts inside.

  // For example, if the size of the List is 20, it means simulating a subway train with 20 carriages,
  // and the list represents the number of passengers in each carriage.
  // For instance, List(1) = 10 means carriage 1 has 10 passengers.

  // ---------------------------------------------

  // The goal is to generate recommendations for passengers based on the crowding level of the carriages
  // to help them quickly find empty carriages.

  // Algorithm: Routing Algorithm

  // ---------------------------------------------

  // Output:
  //
  // (1) Current crowding level of each carriage, where
  //  Empty (fewer than 5 passengers) represented as "000",
  //  Free (5 - 10 passengers) represented as "100",
  //  Moderate (10 - 30 passengers) represented as "110",
  //  Crowded (30+ passengers) as "111"

  // (2) Direction indication: If crowded, display the direction passengers should go (Some(Left / Right)),
  // otherwise, display None.

  // For example: If the leftmost carriage is crowded, it should always display Some(Right).
  // For example:
  // If the 10th carriage is crowded, the 11th carriage is free, the 9th carriage is crowded,
  // and the 8th carriage is free, then the 10th carriage should display Some(Right),
  // and the 8th carriage should display None.

  // A complete output is a map, with instruction examples like:
  // 10th carriage -> "111", Some(Right).

  object SubwayCarRouting {

    def main(args: Array[String]): Unit = {
      val passengersList = List(20, 6, 35, 4, 8, 70, 15, 3, 60, 7, 1, 12, 50, 25, 4, 9, 30, 60, 5, 15)
      val routingRecommendation = generateRoutingRecommendations(passengersList)
      println(routingRecommendation)
    }

    def generateRoutingRecommendations(passengersList: List[Int]): Map[Int, (String, Option[String])] = {
      passengersList.zipWithIndex.map {
        case (passengers, index) =>
          val congestionLevel = getCongestionLevel(passengers)
          val direction = getDirection(passengersList, index, passengers)
          (index + 1) -> (congestionLevel, direction)
      }.toMap
    }

    def getCongestionLevel(passengers: Int): String = passengers match {
      case p if p < 5  => "000"
      case p if p < 10 => "100"
      case p if p < 30 => "110"
      case _ => "111"

    }

    def getDirection(passengersList: List[Int], index: Int, passengers: Int): Option[String] = {
      if (passengers >= 60) {
        if (index == 0) Some("Right")
        else if (index == passengersList.length - 1) Some("Left")
        else {
          val leftCar = passengersList(index - 1)
          val rightCar = passengersList(index + 1)
          if (leftCar >= 60 && rightCar >= 60) None
          else if (leftCar < rightCar) Some("Left")
          else Some("Right")
        }
      } else None
    }
  }

}