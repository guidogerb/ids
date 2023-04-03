package com.bpm.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BitUtils {

    private BitUtils() {}

    public static List<BigInteger> digestIDs(byte[] input) {
        final List<BigInteger> resultList = new ArrayList<>();
        String binaryString = "";
        boolean buildingInProgress = false;
        for (byte b : input) {
            if (resultList.size() == 2) {
                System.out.println("stop");
            }
            // Test if the most significant bit is on
            boolean mostSignificantBitOn = (b & 0b10000000) != 0;
            // Test if the least significant bit is on
            boolean leastSignificantBitOn = (b & 0b00000001) != 0;

            // If the most/least significant bits are both turned on and building is not in progress
            // add inside 6 bits to the binary string i.e. 0b10111111 means add 0b011111 to the result collection
            if (mostSignificantBitOn && leastSignificantBitOn && !buildingInProgress) {
                String byteString = String.format("%7s", Integer
                                .toBinaryString(b & 0xFF))
                        .replace(' ', '0');
                byteString = byteString.substring(1, 7);
                BigInteger bigInteger = new BigInteger(byteString, 2);
                resultList.add(bigInteger);
                // Clear the binary string for the next number
                binaryString = "";
            }
            // Check if the least significant bit is turned on, this is the end of the number
            else if (leastSignificantBitOn && buildingInProgress) {
                String byteString = String.format("%8s", Integer
                        .toBinaryString(b & 0xFF));
                byteString = byteString.substring(0, 7);
                BigInteger bigInteger = new BigInteger(byteString + binaryString, 2);
                resultList.add(bigInteger);
                // Clear the binary string for the next number
                binaryString = "";
                buildingInProgress = false;
            }
            // Check if the most significant bit is turned on and !buildingInProgress, this is the start of the number
            // and the next byte (or more) will also be added to the binary string
            else if (mostSignificantBitOn && !buildingInProgress) {
                buildingInProgress = true;
                // Re-init the binary string to start a new number
                String byteString = String.format("%7s", Integer
                                .toBinaryString(b & 0xFF))
                        .replace(' ', '0');
                binaryString = byteString.substring(1, 8);
            }
            // add all bits but the least significant to the binary string
            else {
                // Remove the least significant bit
                String byteString = String.format("%8s", Integer
                        .toBinaryString(b & 0xFF));
                binaryString = byteString.substring(0, 7) + binaryString.toString();
            }
        }
        return resultList;
    }

}
