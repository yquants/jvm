package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
/*
field_info {
    u2             access_flags;
    u2             name_index;
    u2             descriptor_index;
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
 */
fun readFields(input:DataInputStream){
    val fieldsCount = input.readUnsignedShort()
    println("\t//fields count: ${fieldsCount}")
    for (i in 1..fieldsCount){
        val accessFlags = input.readUnsignedShort()
        val nameIndex = input.readUnsignedShort()
        val descriptorIndex = input.readUnsignedShort()
        readAttributes(input)
    }
}