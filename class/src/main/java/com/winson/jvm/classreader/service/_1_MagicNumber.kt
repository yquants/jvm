package com.winson.jvm.classreader.service

import java.io.DataInputStream

/**
 * Created by Wei on 2017/6/29.
 */

fun readMaigicNumber(input: DataInputStream) =
        if (input.readInt() != 0xCAFEBABE.toInt()) throw Exception("Magic Number not right") else 0
