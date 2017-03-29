package com.meetup.jirastats.printer

import java.io.{BufferedWriter, File}
import java.nio.file.Files

class FileWriter(fileName: String) extends IO {
  lazy val writer: BufferedWriter = {
    val f = new File(fileName)

    if (Files.notExists(f.toPath)) {
      Files.createFile(f.toPath)
    }

    Files.newBufferedWriter(f.toPath)
  }

  def outln(output: String): Unit = {
    writer.write(output)
    writer.write("\n")
  }

  def close(): Unit = {
    writer.close()
  }
}
