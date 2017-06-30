package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
fun readClassHierarchy(input: DataInputStream) {
    val thisClassIndex = input.readUnsignedShort()
    val superClassIndex = input.readUnsignedShort()
    print(" class ${pool(thisClassIndex)} extends ${pool(superClassIndex)} ")
    readInterfaces(input)
    println("{")
}

private fun readInterfaces(input: DataInputStream) {
    val interfaceCount = input.readUnsignedShort()
    if (interfaceCount > 0) {
        println(" implements")
        for (i in 1..interfaceCount) {
            print(" ${pool(input.readUnsignedShort())}")
        }
    }
}