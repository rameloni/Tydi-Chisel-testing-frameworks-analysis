package tydiexamples.helloworld

import chisel3._
import chisel3.util.Counter

import nl.tudelft.tydi_chisel._

import emit.Emit

object MyTypes {
  /** Bit(8) type, defined in main */
  def generated_0_16_OqHuFqKi_21 = UInt(8.W)

  assert(this.generated_0_16_OqHuFqKi_21.getWidth == 8)
}


/** Group element, defined in main. */
class Rgb extends Group {
  val b = MyTypes.generated_0_16_OqHuFqKi_21
  val g = MyTypes.generated_0_16_OqHuFqKi_21
  val r = MyTypes.generated_0_16_OqHuFqKi_21
}

/** Stream, defined in main. */
class Generated_0_101_bHWhCFjR_22 extends PhysicalStreamDetailed(e = new Rgb, n = 2, d = 1, c = 1, r = false, u = Null())

object Generated_0_101_bHWhCFjR_22 {
  def apply(): Generated_0_101_bHWhCFjR_22 = Wire(new Generated_0_101_bHWhCFjR_22())
}

/** Bit(8), defined in main. */
class Generated_0_16_OqHuFqKi_21 extends BitsEl(8.W)

/** Stream, defined in main. */
class Generated_0_86_q1AG1GZ7_18 extends PhysicalStreamDetailed(e = new Rgb, n = 1, d = 2, c = 1, r = false, u = new Rgb)

object Generated_0_86_q1AG1GZ7_18 {
  def apply(): Generated_0_86_q1AG1GZ7_18 = Wire(new Generated_0_86_q1AG1GZ7_18())
}

/**
 * Streamlet, defined in main.
 * RGB bypass streamlet documentation.
 */
class Rgb_bypass extends TydiModule {
  /** Stream of [[io.input]] with input direction. */
  val inputStream = Generated_0_86_q1AG1GZ7_18().flip

  /** Stream of [[io.input2]] with input direction. */
  val input2Stream = Generated_0_101_bHWhCFjR_22().flip

  /** Stream of [[io.output]] with output direction. */
  val outputStream = Generated_0_86_q1AG1GZ7_18()

  /** Stream of [[io.output2]] with output direction. */
  val output2Stream = Generated_0_101_bHWhCFjR_22()

  // Group
  val io = new Bundle {
    /** IO of [[inputStream]] with input direction. */
    val input = inputStream.toPhysical
    /** IO of [[input2Stream]] with input direction. */
    val input2 = input2Stream.toPhysical

    /** IO of [[outputStream]] with output direction. */
    val output = outputStream.toPhysical
    /** IO of [[output2Stream]] with output direction. */
    val output2 = output2Stream.toPhysical
  }

  // IO connections
  // Stream 1.
  inputStream := io.input
  outputStream := inputStream
  io.output := outputStream

  // Stream 2.
  input2Stream := io.input2
  output2Stream := input2Stream
  io.output2 := output2Stream
}

/**
 * Implementation, defined in main.
 * RGB bypass implement documentation.
 */
class Helloworld_rgb extends Rgb_bypass {
  // Connections
  inputStream.ready := true.B

  val accumulate = Counter(Int.MaxValue)

  when(inputStream.valid) {
    accumulate.value := accumulate.value + (inputStream.el.r + inputStream.el.g + inputStream.el.b) / 3.U
  }

  when(accumulate.value > 100.U && accumulate.value < 400.U) {
    input2Stream.ready := true.B
  }.otherwise(
    input2Stream.ready := false.B
  )
}


object HelloWorldRgbVerilog extends App {
  Emit(
    "output/Helloworld_rgb",
    () => new Helloworld_rgb(),
    "Helloworld_rgb"
  ).verilog()
}

object HelloWorldRgbFIRRTL extends App {
  Emit(
    "output/Helloworld_rgb",
    () => new Helloworld_rgb(),
    "Helloworld_rgb"
  ).firrtl()
}

object HelloWorldRgbGenerateHGDB extends App {
  Emit(
    "output/Helloworld_rgb",
    () => new Helloworld_rgb(),
    "Helloworld_rgb"
  ).hgdbOutputs()
}