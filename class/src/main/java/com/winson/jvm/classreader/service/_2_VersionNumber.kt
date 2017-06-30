package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
fun readVersionNumber(input: DataInputStream) {
    printVersion(input, "Minor Version")
    printVersion(input, "Major Version")
}

private fun printVersion(input: DataInputStream, text: String) = println("$text: ${input.readUnsignedShort()}")