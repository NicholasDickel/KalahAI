package info.kwarc.teaching.AI.Kalah

import info.kwarc.teaching.AI.Kalah.Agents.{HumanPlayer, RandomPlayer}
import info.kwarc.teaching.AI.Kalah.Interfaces.{Fancy, Terminal}
import info.kwarc.teaching.AI.Kalah.util._
import info.kwarc.teaching.AI.Kalah.WS1819.Agents._;

object Test {
  def main(args: Array[String]): Unit = {

    val int = new Fancy.FancyInterface(12)

    new Game(new AgentPAJANI("Hans"),new RandomPlayer("Hurtz"),Terminal)(12,12).play
  }
}
