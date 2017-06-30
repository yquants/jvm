package com.winson.jvm.classreader.service

import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

/**
 * Created by Wei on 2017/6/29.
 *
 * Ref: https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html
 * https://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
 */
fun main(args: Array<String>) {
    val input = getFileInputStream(args[0]!!)

    readMaigicNumber(input)
    readVersionNumber(input)
    readConstantPool(input)
    readClassAccessFlags(input)
    readClassHierarchy(input)
    readFields(input)
    readMethods(input)
    println("}")
    readAttributes(input)

    assert(input.available() == 0)

    input.close()
}

private fun getFileInputStream(fileName: String): DataInputStream =
        if (File(fileName).exists()) DataInputStream(FileInputStream(File(fileName))) else throw Exception("$fileName does not exist")
