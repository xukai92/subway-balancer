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

// java -cp balancer.jar  SubwayCarRouting --input "C:\\Users\\57557\\Documents\\Project\\subway-balancer\\balancer\\balancer\\src\\main\\scala\\input" --output "C:\\Users\\57557\\Documents\\Project\\subway-balancer\\balancer\\balancer\\src\\main\\scala\\output"
object SubwayCarRouting {

  import scala.io.Source

  import scala.io.Source
  import java.io._

  /**
   * Reads the subway car passenger data from a file.
   *
   * @param filePath Path to the input file.
   * @return List of lists of passenger counts for each train.
   */
  def readFile(filePath: String): List[List[Int]] = {
    val bufferedSource = Source.fromFile(filePath)
    val data = bufferedSource.getLines.map { line =>
      line.split(",").map(_.trim.toInt).toList
    }.toList
    bufferedSource.close()
    data
  }

  /**
   * Writes the routing recommendations to an output file.
   *
   * @param data     List of strings representing the recommendations for each train.
   * @param filePath Path to the output file.
   */
  def writeFile(data: List[String], filePath: String): Unit = {
    val pw = new PrintWriter(new File(filePath))
    data.foreach(pw.println)
    pw.close()
  }

  /**
   * The main function that reads the passenger data, generates routing recommendations,
   * and writes the recommendations to an output file.
   *
   * @param args Command line arguments.
   */
  def main(args: Array[String]): Unit = {

    val argsMap = args.sliding(2, 2).toList.collect {
      case Array(flag, value) => flag -> value
    }.toMap

    val inputFilePath = argsMap.getOrElse("--input", {
      println("Usage: --input <input-file-path>")
      sys.exit(1)
    })

    val outputFilePath = argsMap.getOrElse("--output", {
      println("Usage: --output <output-file-path>")
      sys.exit(1)
    })

    val trains = readFile(inputFilePath)
    val recommendations = trains.map { passengersList =>
      val routingRecommendation = generateRoutingRecommendations(passengersList).toSeq.sortBy(_._1)
      val resultStr = routingRecommendation.map {
        case (carriage, (congestion, numberOfPeople, direction)) =>
          s"Carriage_$carriage: congestion level $congestion, number of people $numberOfPeople, direction suggestion ${direction.getOrElse("None")}"
      }.mkString("\n ")
      println(resultStr)
      routingRecommendation.mkString(",")
    }

    writeFile(recommendations, outputFilePath)
    println("Routing recommendations have been successfully written to the output file.")
  }


  def generateRoutingRecommendations(passengersList: List[Int]): Map[Int, (String, Int, Option[String])] = {
    passengersList.zipWithIndex.map { case (passengers, index) => val congestionLevel = getCongestionLevel(passengers)
      val direction = getBalancedDirection(passengersList, index)
      (index + 1) -> (congestionLevel, passengers, direction)
    }.toMap
  }

  def getCongestionLevel(passengers: Int): String = passengers match {
    case p if p < 5 => "Empty"
    case p if p < 10 => "Mild"
    case p if p < 30 => "Moderate"
    case _ => "Crowded"
  }

  /**
   * Determines the direction recommendation for passengers in a subway car.
   *
   * @param passengersList List of passenger counts in each subway car.
   * @param index          Index of the current subway car (0-based).
   * @param passengers     Count of passengers in the current subway car.
   * @return Optional direction recommendation: Some("Left"), Some("Right"), or None.
   *         Returns None if the current car is not crowded or if all cars are crowded.
   */


  def getBalancedDirection(passengersList: List[Int], index: Int): Option[String] = {
    // If the number of passengers in the current car is less than or equal to 5, no adjustment is needed
    if (passengersList(index) <= 5) return None

    // If at the ends of the train, check only the adjacent car
    if (index == 0) {
      if (passengersList(1) > passengersList(0)) return None
    } else if (index == passengersList.length - 1) {
      if (passengersList(index - 1) > passengersList(index)) return None
    } else {
      // If both adjacent cars have more passengers, return None
      if (passengersList(index - 1) > passengersList(index) && passengersList(index + 1) > passengersList(index)) {
        return None
      }
    }

    // Find a car with fewer passengers on the left
    val leftIndex = (index - 1 to 0 by -1).find(passengersList(_) < passengersList(index))
    // Find a car with fewer passengers on the right
    val rightIndex = (index + 1 until passengersList.length).find(passengersList(_) < passengersList(index))

    (leftIndex, rightIndex) match {
      case (None, None) => None // If there are no cars with fewer passengers on both sides, return None
      case (Some(l), None) => Some("Left") // If there is a car with fewer passengers only on the left, recommend going left
      case (None, Some(r)) => Some("Right") // If there is a car with fewer passengers only on the right, recommend going right
      case (Some(l), Some(r)) =>
        // If there are cars with fewer passengers on both sides, recommend the direction to the nearest one
        if ((index - l) < (r - index)) Some("Left")
        else Some("Right")
    }
  }


}

