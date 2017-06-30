package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/30.
 */

private val map = hashMapOf(0 to OpCode(0, "nop", 0, "perform no operation"),
        0xb9 to OpCode(0xb9, "invokeinterface", 4, "4: indexbyte1, indexbyte2, count, 0"),
        0xbb to OpCode(0xbb, "new", 2, "create new object of type"),
        0x59 to OpCode(value = 0x59, name = "dup", operand = 0, desc = "duplicate the value on top of the stack"),
        0xb7 to OpCode(value = 0xB7, name = "invokespecial", operand = 2, desc = "invoke instance method on object"),
        0x4e to OpCode(value = 0x4e, name = "astore_3", operand = 0, desc = "store a reference into local variable 3"),
        0x4c to OpCode(value = 0x4c, name = "astore_1", operand = 0, desc = "store a reference into local variable 1"),
        0x4d to OpCode(value = 0x4d, name = "astore_2", operand = 0, desc = "store a reference into local variable 2"),
        0x2a to OpCode(value = 0x2a, name = "aload_0", operand = 0, desc = "load a reference onto the stack from local variable 0"),
        0xb1 to OpCode(value = 0xb1, name = "return", operand = 0, desc = "return void from method"),
        0x2c to OpCode(value = 0x2c, name = "aload_2", operand = 0, desc = "load a reference onto the stack from local variable 2"),
        0x12 to OpCode(value = 0x12, name = "ldc", operand = 1, desc = "push a constant #index from a constant pool"),
        0xb6 to OpCode(value = 0xb6, name = "invokevirtual", operand = 2, desc = "invoke virtual method on object objectref and puts the result on the stack"),
        0x2d to OpCode(value = 0x2d, name = "aload_3", operand = 0, desc = "load a reference onto the stack from local variable 3"),
        0x2b to OpCode(value = 0x2d, name = "aload_1", operand = 0, desc = "load a reference onto the stack from local variable 1")
)

data class OpCode(val value: Int, val name: String, val operand: Int, val desc: String) {}

/*
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;
    u2 max_locals;
    u4 code_length;
    u1 code[code_length];
    u2 exception_table_length;
    {   u2 start_pc;
        u2 end_pc;
        u2 handler_pc;
        u2 catch_type;
    } exception_table[exception_table_length];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
 */
fun readCodeAttribte(input: DataInputStream) {
    val attrLen = input.readInt()
    val maxStack = input.readUnsignedShort()
    val maxLocal = input.readUnsignedShort()
    val codeLen = input.readInt()
    println("\t<Method Code level> maxStack: $maxStack, maxLocal: $maxLocal")
    var offset = 0
    while (offset++ < codeLen) {
        val opCode = map[input.readUnsignedByte()]
        print("\t${offset-1} ${opCode!!.name}")
        when (opCode.operand) {
            2 -> println(" ${pool(input.readUnsignedShort())}")
            1 -> println(" ${pool(input.readUnsignedByte())}")
            0 -> println()
            else -> throw Exception("Not Implemented yet")
        }
        offset += opCode!!.operand
    }

    val exceptLen = input.readUnsignedShort()
    for (i in 1..exceptLen) {
        val startPc = input.readUnsignedShort()
        val endPc = input.readUnsignedShort()
        val handlerPc = input.readUnsignedShort()
        val catchType = input.readUnsignedShort()
        println("Exception #$i: from $startPc to $endPc hander $handlerPc under type ${if (catchType > 0) pool(catchType) else ""}")
    }
    readAttributes(input)
}