package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
/*
method_info {
    u2             access_flags;
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
*/
fun readMethods(input: DataInputStream){
    val methodsCount = input.readUnsignedShort()
    println("\t//methods count: ${methodsCount}")

    for (i in 1..methodsCount){
        readMethodAccessFlags(input)
        val nameIndex = input.readUnsignedShort()
        println(" ${pool(nameIndex)}() {")
        val descIndex = input.readUnsignedShort()
        readAttributes(input)
        println("}")
    }
}