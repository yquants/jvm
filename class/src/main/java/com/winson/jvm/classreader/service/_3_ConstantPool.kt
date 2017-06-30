package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */
enum class ConstantType(val value: Int) {
    CONSTANT_Methodref(10),
    CONSTANT_Class(7),
    CONSTANT_String(8),
    CONSTANT_Utf8(1),
    CONSTANT_NameAndType(12)
}

private val map: MutableMap<Int, String> = HashMap<Int, String>()
private val internalMap: MutableMap<Int, IntArray> = HashMap<Int, IntArray>()

fun readConstantPool(input: DataInputStream) {
    val count = input.readUnsignedShort()
    println("Constant Pool Size: $count")
    for (i in 1..count - 1) {
        val tag = input.readUnsignedByte()
        when (tag) {
            ConstantType.CONSTANT_Methodref.value -> readMethodRef(input, i)
            ConstantType.CONSTANT_Class.value -> readClass(input, i)
            ConstantType.CONSTANT_String.value -> readString(input, i)
            ConstantType.CONSTANT_Utf8.value -> readUtf8(input, i)
            ConstantType.CONSTANT_NameAndType.value -> readNameType(input, i)
            else -> throw Exception("not implemented yet")
        }
    }
    cleanInternalMap()

    map.forEach { t, u -> println("Constant #$t => $u") }
}

fun pool(index: Int): String = map[index]!!

/*
CONSTANT_Methodref_info {
    u1 tag;
    u2 class_index;
    u2 name_and_type_index;
}
 */
private fun readMethodRef(input: DataInputStream, index: Int) {
    val classIndex = input.readUnsignedShort()
    val nameTypeIndex = input.readUnsignedShort()
    internalMap[index] = intArrayOf(classIndex, nameTypeIndex)
}

/*
CONSTANT_Class_info {
    u1 tag;
    u2 name_index;
}
 */
private fun readClass(input: DataInputStream, index: Int) {
    val nameIndex = input.readUnsignedShort()
    internalMap[index] = intArrayOf(nameIndex)
}

/*
CONSTANT_String_info {
    u1 tag;
    u2 string_index;
}
 */
private fun readString(input: DataInputStream, index: Int) {
    val nameIndex = input.readUnsignedShort()
    internalMap[index] = intArrayOf(nameIndex)
}

/*
CONSTANT_Utf8_info {
    u1 tag;
    u2 length;
    u1 bytes[length];
}
 */
private fun readUtf8(input: DataInputStream, index: Int) {
    val len = input.readUnsignedShort()
    if (len > 0) {
        val bytes = ByteArray(len)
        input.read(bytes)
        map[index] = String(bytes)
    }
}

/*
CONSTANT_NameAndType_info {
    u1 tag;
    u2 name_index;
    u2 descriptor_index;
}
 */
private fun readNameType(input: DataInputStream, index: Int) {
    val nameIndex = input.readUnsignedShort()
    val descriptorIndex = input.readUnsignedShort()
    internalMap[index] = intArrayOf(nameIndex, descriptorIndex)
}

private fun cleanInternalMap() {
    while (!internalMap.isEmpty()) {
        val it = internalMap.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val values = entry.value
            if (!map.containsKey(values[0]))
                continue
            var stringBuilder = StringBuilder()
            values.forEachIndexed { index, i -> stringBuilder.append(if (index > 0) ";" else "").append(map[i]) }

            map[entry.key] = stringBuilder.toString()
            it.remove()
        }
    }
}