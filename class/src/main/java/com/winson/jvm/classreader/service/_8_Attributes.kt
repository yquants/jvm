package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
enum class AttrType(val value: String) {
    Code_attribute("Code"),
    LineNumberTable_attribute("LineNumberTable"),
    LocalVariableTable_attribute("LocalVariableTable"),
    Exceptions_attribute("Exceptions"),
    SourceFile_attribute("SourceFile"),
    InnerClasses_attribute("InnerClasses")
}

/*
attribute_info {
    u2 attribute_name_index;
    u4 attribute_length;
    u1 info[attribute_length];
}
 */
fun readAttributes(input: DataInputStream) {
    val attributesCount = input.readUnsignedShort()

    for (i in 1..attributesCount)
        readAttribute(input)
}

private fun readAttribute(input: DataInputStream) {
    val nameIndex = input.readUnsignedShort()
    when (pool(nameIndex)) {
        AttrType.Code_attribute.value -> readCodeAttribte(input)
        AttrType.LineNumberTable_attribute.value -> readLineNumberTableAttr(input)
        AttrType.LocalVariableTable_attribute.value -> readLocalVariableTable(input)
        AttrType.Exceptions_attribute.value -> readExceptionAttr(input)
        AttrType.SourceFile_attribute.value -> readSourceFileAttr(input)
        AttrType.InnerClasses_attribute.value -> readInnerClassAttr(input)
        else -> {
            print("---------------------${pool(nameIndex)}----")
            val len = input.readInt()
            val bytes = ByteArray(len)
            input.read(bytes)
            println("len: $len")
            println("${String(bytes)}")
        }
    }
}


/*
LineNumberTable_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 line_number_table_length;
    {   u2 start_pc;
        u2 line_number;
    } line_number_table[line_number_table_length];
}
 */
private fun readLineNumberTableAttr(input: DataInputStream) {
    input.readInt()
    val lineNumberTableLen = input.readUnsignedShort()
    for (i in 1..lineNumberTableLen) {
        val startPc = input.readUnsignedShort()
        val lineNumber = input.readUnsignedShort()
        println("<Additional code Info>opcode: #$startPc <=> line #$lineNumber in source file")
    }
}

/*
LocalVariableTable_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 local_variable_table_length;
    {   u2 start_pc;
        u2 length;
        u2 name_index;
        u2 descriptor_index;
        u2 index;
    } local_variable_table[local_variable_table_length];
}
 */
private fun readLocalVariableTable(input: DataInputStream) {
    input.readInt()
    val localTableLen = input.readUnsignedShort()
    for (i in 1..localTableLen) {
        val startPc = input.readUnsignedShort()
        val length = input.readUnsignedShort()
        val name = pool(input.readUnsignedShort())
        val desc = pool(input.readUnsignedShort())
        val index = input.readUnsignedShort()

        println("<Additional code info> start pc: $startPc, length: $length, mnemonic: $name, desc: $desc, index: $index")
    }
}

/*
Exceptions_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 number_of_exceptions;
    u2 exception_index_table[number_of_exceptions];
}
 */
private fun readExceptionAttr(input: DataInputStream) {
    input.readInt()
    val exceptionCount = input.readUnsignedShort()
    print("throws")
    for (i in 1..exceptionCount) {
        print(" ${pool(input.readUnsignedShort())}")
    }
    println()
}

/*
SourceFile_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 sourcefile_index;
}
 */
private fun readSourceFileAttr(input: DataInputStream) {
    val len = input.readInt()
    println("Source File: ${pool(input.readUnsignedShort())}")
}

/*
InnerClasses_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 number_of_classes;
    {   u2 inner_class_info_index;
        u2 outer_class_info_index;
        u2 inner_name_index;
        u2 inner_class_access_flags;
    } classes[number_of_classes];
}
 */
private fun readInnerClassAttr(input: DataInputStream) {
    val len = input.readInt()
    val classCount = input.readUnsignedShort()
    for (i in 1..classCount) {
        val innerClass = pool(input.readUnsignedShort())
        val outerClass = pool(input.readUnsignedShort())
        val innerName = pool(input.readUnsignedShort())
        val innerClassAccess = input.readUnsignedShort()
        println("inner class: $innerClass, outer class: $outerClass, inner mnemonic: $innerName, inner access: $innerClassAccess")
    }
}